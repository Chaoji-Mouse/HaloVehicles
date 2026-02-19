package com.cmhh.halovecs.entity.vehicle;

import com.atsuishio.superbwarfare.entity.vehicle.base.GeoVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.damage.DamageModifier;
import com.cmhh.halovecs.entity.vehicle.utils.M274VehicleEngineUtils;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;



public class M274mEntity extends GeoVehicleEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final float CUSTOM_RUDDER_LIMIT = 2f; // 原值0.8f


    public M274mEntity(EntityType<M274mEntity> type, Level world) {
        super(type, world);
    }

    @Override
    public DamageModifier getDamageModifier() {
        return super.getDamageModifier()
                .custom((source, damage) -> getSourceAngle(source, 0.25f) * damage);
    }

    @Override
    public void setRudderRot(float rudderRot) {
        // 计算新的方向舵角度（复制SuperbWarfare逻辑）
        float newRudderRot = rudderRot - this.getEntityData().get(DELTA_ROT);
        
        // 关键修改：使用自定义的限制值
        // 注意：Mth.clamp参数顺序是(value, min, max)
        float clampedRudder = net.minecraft.util.Mth.clamp(
            newRudderRot, 
            -CUSTOM_RUDDER_LIMIT,  // 最小值（左转限制）
            CUSTOM_RUDDER_LIMIT     // 最大值（右转限制）
        );
        
        // 保持原版缩放因子0.75f
        float scaledRudder = clampedRudder * 0.75f;
        
        // 调用父类方法
        super.setRudderRot(scaledRudder);
    }


    

    
}
