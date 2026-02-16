package com.cmhh.halovecs.client.model.entity;

import com.atsuishio.superbwarfare.client.model.entity.VehicleModel;
import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.entity.vehicle.M12Entity;

import net.minecraft.resources.ResourceLocation;

public class M12Model extends VehicleModel<M12Entity> {

    @Override
    public ResourceLocation getModelResource(M12Entity animatable) {
        return new ResourceLocation(Halovecs.MODID, "geo/m12.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M12Entity animatable) {
        return new ResourceLocation(Halovecs.MODID, "textures/entity/m12.png");
    }

    @Override
    public ResourceLocation getAnimationResource(M12Entity animatable) {
        return null;
    }
}
