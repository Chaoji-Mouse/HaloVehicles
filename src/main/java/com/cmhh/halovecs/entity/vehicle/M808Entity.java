package com.cmhh.halovecs.entity.vehicle;


import com.atsuishio.superbwarfare.entity.vehicle.base.GeoVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.damage.DamageModifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animation.*;


public class M808Entity extends GeoVehicleEntity implements GeoEntity {

    //private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public M808Entity(EntityType<M808Entity> type, Level world) {
        super(type, world);
    }

    // 添加同轴机枪动画控制器
    private PlayState coaxFirePredicate(AnimationState<M808Entity> event) {
        if (getShootAnimationTimer(0, 1) > 0) { // 假设同轴机枪是武器索引1
            return event.setAndContinue(RawAnimation.begin().thenLoop("fire_coax"));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("idle_coax"));
    }

    // 添加武器站动画控制器  
    private PlayState weaponStationFirePredicate(AnimationState<M808Entity> event) {
        if (getShootAnimationTimer(1, 0) > 0) { // 假设武器站是座位1的武器0
            return event.setAndContinue(RawAnimation.begin().thenLoop("fire_weapon_station"));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("idle_weapon_station"));
    }

    @Override
    public DamageModifier getDamageModifier() {
        return super.getDamageModifier()
                .custom((source, damage) -> getSourceAngle(source, 0.25f) * damage);
    }

    private PlayState maingunFirePredicate(AnimationState<M808Entity> event) {
        if (getShootAnimationTimer(0, 0) > 0) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("fire"));
        }

        return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
    }

    // 更新 registerControllers 方法
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "MainGun", 0, this::maingunFirePredicate));
        data.add(new AnimationController<>(this, "Coax", 0, this::coaxFirePredicate));
        data.add(new AnimationController<>(this, "PassengerMachineGun", 0, this::weaponStationFirePredicate));
    }

    // 添加履带载具特定方法
    @Override
    public int getTrackAnimationLength() {
        return 80; // 与 Yx100 相同，或根据实际动画调整
    }

    @Override
    public float getTurretMaxHealth() {
        return 100; // 炮塔生命值
    }

    @Override
    public float getWheelMaxHealth() {
        return 100; // 车轮/履带生命值
    }

    @Override
    public float getEngineMaxHealth() {
        return 150; // 发动机生命值
    }
}
