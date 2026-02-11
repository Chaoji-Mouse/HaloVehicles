package com.cmhh.halovecs.entity.vehicle;

import com.atsuishio.superbwarfare.entity.vehicle.base.GeoVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.damage.DamageModifier;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class M12Entity extends GeoVehicleEntity {
    
    // 只添加炮塔相关的同步数据，车轮旋转可能已经在父类中处理
    public static final EntityDataAccessor<Float> TURRET_Y_ROT = SynchedEntityData.defineId(M12Entity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> TURRET_X_ROT = SynchedEntityData.defineId(M12Entity.class, EntityDataSerializers.FLOAT);
    
    public M12Entity(EntityType<M12Entity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        // 只添加炮塔旋转数据
        builder.define(TURRET_Y_ROT, 0f);
        builder.define(TURRET_X_ROT, 0f);
    }
    
    // 只添加炮塔的getter和setter方法
    public float getTurretYRot() {
        return this.entityData.get(TURRET_Y_ROT);
    }
    
    public void setTurretYRot(float value) {
        this.entityData.set(TURRET_Y_ROT, value);
    }
    
    public float getTurretXRot() {
        return this.entityData.get(TURRET_X_ROT);
    }
    
    public void setTurretXRot(float value) {
        this.entityData.set(TURRET_X_ROT, value);
    }

    @Override
    public DamageModifier getDamageModifier() {
        return super.getDamageModifier()
                .custom((source, damage) -> getSourceAngle(source, 0.25f) * damage);
    }
    
}
