package com.cmhh.halovecs.client.model.entity;

import com.atsuishio.superbwarfare.client.model.entity.VehicleModel;
import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.entity.vehicle.M12gauEntity;

import net.minecraft.resources.ResourceLocation;

public class M12gauModel extends VehicleModel<M12gauEntity> {

    @Override
    public ResourceLocation getModelResource(M12gauEntity animatable) {
        return new ResourceLocation(Halovecs.MODID, "geo/m12gau.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M12gauEntity animatable) {
        return new ResourceLocation(Halovecs.MODID, "textures/entity/m12gau.png");
    }

    @Override
    public ResourceLocation getAnimationResource(M12gauEntity animatable) {
        return new ResourceLocation(Halovecs.MODID, "animations/m12gau.animation.json");
    }
}
