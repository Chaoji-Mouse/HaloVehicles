package com.cmhh.halovecs.client.model.entity;

import com.atsuishio.superbwarfare.client.model.entity.VehicleModel;
import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.entity.vehicle.M808Entity;

import net.minecraft.resources.ResourceLocation;

public class M808Model extends VehicleModel<M808Entity> {

    @Override
    public ResourceLocation getModelResource(M808Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "geo/m808.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M808Entity entity) {
        // 完全仿照TruckModel的getTextureResource方法模式
        int variant = entity.getTextureVariant();
        
        switch (variant) {
            case M808Entity.VARIANT_CITY:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m808_city.png");
            case M808Entity.VARIANT_DUST:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m808_dust.png");
            case M808Entity.VARIANT_SNOW:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m808_snow.png");
            case M808Entity.VARIANT_COMP:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m808_comp.png");
            case M808Entity.VARIANT_DEFAULT:
            default:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m808.png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(M808Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "animations/m808.animation.json");
    }

}
