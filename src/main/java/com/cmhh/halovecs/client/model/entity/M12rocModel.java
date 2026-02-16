package com.cmhh.halovecs.client.model.entity;

import com.atsuishio.superbwarfare.client.model.entity.VehicleModel;
import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.entity.vehicle.M12rocEntity;

import net.minecraft.resources.ResourceLocation;

public class M12rocModel extends VehicleModel<M12rocEntity> {

    @Override
    public ResourceLocation getModelResource(M12rocEntity animatable) {
        return new ResourceLocation(Halovecs.MODID, "geo/m12roc.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M12rocEntity animatable) {
        return new ResourceLocation(Halovecs.MODID, "textures/entity/m12roc.png");
    }

    @Override
    public ResourceLocation getAnimationResource(M12rocEntity animatable) {
        return new ResourceLocation(Halovecs.MODID, "animations/m12roc.animation.json");
    }
}
