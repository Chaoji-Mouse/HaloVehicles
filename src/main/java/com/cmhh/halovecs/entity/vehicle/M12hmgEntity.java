package com.cmhh.halovecs.entity.vehicle;

import com.atsuishio.superbwarfare.entity.vehicle.base.GeoVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.damage.DamageModifier;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;



public class M12hmgEntity extends GeoVehicleEntity {
    
    // 炮台旋转数据
    public static final EntityDataAccessor<Float> TURRET_Y_ROT = SynchedEntityData.defineId(M12hmgEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> TURRET_X_ROT = SynchedEntityData.defineId(M12hmgEntity.class, EntityDataSerializers.FLOAT);
    
    // 添加上一tick的角度用于插值
    public float turretYRotO;
    public float turretXRotO;

    public M12hmgEntity(EntityType<M12hmgEntity> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        // 只添加炮塔旋转数据
        builder.define(TURRET_Y_ROT, 0f);
        builder.define(TURRET_X_ROT, 0f);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        
        // 保存上一tick的角度用于插值
        turretYRotO = this.getTurretYRot();
        turretXRotO = this.getTurretXRot();
        
        // 炮台角度限制逻辑（如果需要特殊处理）
        // 基类 VehicleEntity 已经处理了基本的炮台角度调整
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

    // 添加插值方法
    public float getTurretYaw(float partialTick) {
        return net.minecraft.util.Mth.lerp(partialTick, turretYRotO, getTurretYRot());
    }
    
    public float getTurretPitch(float partialTick) {
        return net.minecraft.util.Mth.lerp(partialTick, turretXRotO, getTurretXRot());
    }

    @Override
    public DamageModifier getDamageModifier() {
        return super.getDamageModifier()
                .custom((source, damage) -> getSourceAngle(source, 0.25f) * damage);
    }
    private PlayState shootPredicate(AnimationState<M12hmgEntity> event) {
        // 添加调试输出
        System.out.println("[M12hmg DEBUG] shootPredicate called");
        
        // 检查主炮是否正在射击 - 尝试两种方式
        int timerBySeat = getShootAnimationTimer(2, 0);
        int timerByName = getShootAnimationTimer("MainGun");
        
        System.out.println("[M12hmg DEBUG] Shoot animation timer for seat 2, weapon 0: " + timerBySeat);
        System.out.println("[M12hmg DEBUG] Shoot animation timer for weapon 'MainGun': " + timerByName);
        
        // 使用较大的计时器值
        int timer = Math.max(timerBySeat, timerByName);
        System.out.println("[M12hmg DEBUG] Using timer value: " + timer);
        
        if (timer > 0) {
            System.out.println("[M12hmg DEBUG] Playing fire animation, timer = " + timer);
            // 使用动画文件中的实际动画名称："fire"
            // 使用thenLoop而不是thenPlay，使动画在射击期间持续循环播放
            return event.setAndContinue(RawAnimation.begin().thenLoop("fire"));
        }
        
        System.out.println("[M12hmg DEBUG] Playing idle animation");
        // 使用动画文件中的实际动画名称："idle"
        return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        // 添加调试输出
        System.out.println("[M12hmg DEBUG] registerControllers called");
        
        // 注册射击动画控制器
        data.add(new AnimationController<>(this, "shoot", 0, this::shootPredicate));
        
        System.out.println("[M12hmg DEBUG] Animation controller registered");
    }

    
}
