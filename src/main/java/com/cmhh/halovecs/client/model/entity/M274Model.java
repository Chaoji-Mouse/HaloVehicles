package com.cmhh.halovecs.client.model.entity;

import com.atsuishio.superbwarfare.client.model.entity.VehicleModel;
import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.entity.vehicle.M274Entity;

import net.minecraft.resources.ResourceLocation;

public class M274Model extends VehicleModel<M274Entity> {

    @Override
    public ResourceLocation getModelResource(M274Entity animatable) {
        return new ResourceLocation(Halovecs.MODID, "geo/m274.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M274Entity animatable) {
        return new ResourceLocation(Halovecs.MODID, "textures/entity/m274.png");
    }

    @Override
    public ResourceLocation getAnimationResource(M274Entity animatable) {
        return null;
    }
}
