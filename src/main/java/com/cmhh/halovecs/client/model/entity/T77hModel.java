package com.cmhh.halovecs.client.model.entity;

import com.atsuishio.superbwarfare.client.model.entity.VehicleModel;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.entity.vehicle.T77hEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;

public class T77hModel extends VehicleModel<T77hEntity> {

    @Override
    public ResourceLocation getModelResource(T77hEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "geo/t77h.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T77hEntity entity) {
        int variant = entity.getTextureVariant();
        
        switch (variant) {
            case T77hEntity.VARIANT_CITY:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/t77h_city.png");
            case T77hEntity.VARIANT_DUST:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/t77h_dust.png");
            case T77hEntity.VARIANT_SNOW:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/t77h_snow.png");
            case T77hEntity.VARIANT_COMP:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/t77h_comp.png");
            case T77hEntity.VARIANT_DEFAULT:
            default:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/t77h.png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(T77hEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "animations/t77h.animation.json");
    }

    @Override
    public @Nullable TransformContext<T77hEntity> collectTransform(String boneName) {
        return switch (boneName) {
            // wingRR（右外侧副翼）← A10 wingLR2（左外侧副翼值, flap1L2Rot）
            case "wingRR" -> (bone, vehicle, state) ->
                    bone.setRotX(1.5f * Mth.lerp(state.getPartialTick(), vehicle.getFlap1L2RotO(), vehicle.getFlap1L2Rot()) * Mth.DEG_TO_RAD);

            // wingLL（左外侧副翼）← A10 wingRR2（右外侧副翼值, flap1R2Rot）
            case "wingLL" -> (bone, vehicle, state) ->
                    bone.setRotX(1.5f * Mth.lerp(state.getPartialTick(), vehicle.getFlap1R2RotO(), vehicle.getFlap1R2Rot()) * Mth.DEG_TO_RAD);

            // wingLB（左襟翼）← A10 wingLB（左襟翼值, flap2LRot）
            case "wingLB" -> (bone, vehicle, state) ->
                    bone.setRotX(Mth.lerp(state.getPartialTick(), vehicle.getFlap2LRotO(), vehicle.getFlap2LRot()) * Mth.DEG_TO_RAD);

            // wingRB（右襟翼）← A10 wingRB（右襟翼值, flap2RRot）
            case "wingRB" -> (bone, vehicle, state) ->
                    bone.setRotX(Mth.lerp(state.getPartialTick(), vehicle.getFlap2RRotO(), vehicle.getFlap2RRot()) * Mth.DEG_TO_RAD);

            // 内翼副翼动画
            case "wingLa" -> (bone, vehicle, state) ->
                    bone.setRotX(1.5f * Mth.lerp(state.getPartialTick(), vehicle.getFlap1LRotO(), vehicle.getFlap1LRot()) * Mth.DEG_TO_RAD);

            case "wingRa" -> (bone, vehicle, state) ->
                    bone.setRotX(1.5f * Mth.lerp(state.getPartialTick(), vehicle.getFlap1RRotO(), vehicle.getFlap1RRot()) * Mth.DEG_TO_RAD);

            // 导弹可见性 - 使用 0.8.9 标准做法（直接从 GunData 读取）
            case "missile" -> (bone, vehicle, state) ->
                    bone.setHidden(shouldHideMissile(vehicle, 1));
            case "missile2" -> (bone, vehicle, state) ->
                    bone.setHidden(shouldHideMissile(vehicle, 2));
            case "missile3" -> (bone, vehicle, state) ->
                    bone.setHidden(shouldHideMissile(vehicle, 3));
            case "missile4" -> (bone, vehicle, state) ->
                    bone.setHidden(shouldHideMissile(vehicle, 4));
            case "missile5" -> (bone, vehicle, state) ->
                    bone.setHidden(shouldHideMissile(vehicle, 5));
            case "missile6" -> (bone, vehicle, state) ->
                    bone.setHidden(shouldHideMissile(vehicle, 6));
            case "missile7" -> (bone, vehicle, state) ->
                    bone.setHidden(shouldHideMissile(vehicle, 7));
            case "missile8" -> (bone, vehicle, state) ->
                    bone.setHidden(shouldHideMissile(vehicle, 8));

            // 炸弹可见性 - 使用 0.8.9 标准做法
            case "bomb" -> (bone, vehicle, state) ->
                    bone.setHidden(shouldHideBomb(vehicle, 1));
            case "bomb2" -> (bone, vehicle, state) ->
                    bone.setHidden(shouldHideBomb(vehicle, 2));
            case "bomb3" -> (bone, vehicle, state) ->
                    bone.setHidden(shouldHideBomb(vehicle, 3));
            case "bomb4" -> (bone, vehicle, state) ->
                    bone.setHidden(shouldHideBomb(vehicle, 4));
            case "bomb5" -> (bone, vehicle, state) ->
                    bone.setHidden(shouldHideBomb(vehicle, 5));

            default -> null;
        };
    }

    /**
     * 判断该数量的导弹是否应隐藏。
     * 完全仿照 A10Model.shouldHideMissile 的 0.8.9 标准做法。
     */
    private boolean shouldHideMissile(VehicleEntity vehicle, int ammo) {
        var gunData = vehicle.getGunData("Missile");
        return gunData == null || gunData.ammo.get() < ammo;
    }

    /**
     * 判断该数量的炸弹是否应隐藏。
     * 完全仿照 A10Model.shouldHideBomb 的 0.8.9 标准做法。
     */
    private boolean shouldHideBomb(VehicleEntity vehicle, int ammo) {
        var gunData = vehicle.getGunData("Bomb");
        return gunData == null || gunData.ammo.get() < ammo;
    }
}
