package com.cmhh.halovecs.entity.vehicle;


import com.atsuishio.superbwarfare.entity.vehicle.base.GeoVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.damage.DamageModifier;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;

import software.bernie.geckolib.util.GeckoLibUtil;



public class M12traEntity extends GeoVehicleEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public M12traEntity(EntityType<M12traEntity> type, Level world) {
        super(type, world);
    }

    @Override
    public DamageModifier getDamageModifier() {
        return super.getDamageModifier()
                .custom((source, damage) -> getSourceAngle(source, 0.25f) * damage);
    }

    private PlayState maingunFirePredicate(AnimationState<M12traEntity> event) {
        if (getShootAnimationTimer(2, 0) > 0) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("fire"));
        }

        return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "MainGun", 0, this::maingunFirePredicate));
    }
}
