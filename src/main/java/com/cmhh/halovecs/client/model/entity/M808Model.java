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
    public ResourceLocation getTextureResource(M808Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m808.png");
    }

    @Override
    public ResourceLocation getAnimationResource(M808Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "animations/m808.animation.json");
    }

}
