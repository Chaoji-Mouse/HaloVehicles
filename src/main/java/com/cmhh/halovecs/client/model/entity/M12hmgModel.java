package com.cmhh.halovecs.client.model.entity;

import com.atsuishio.superbwarfare.client.model.entity.VehicleModel;
import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.entity.vehicle.M12hmgEntity;

import net.minecraft.resources.ResourceLocation;

public class M12hmgModel extends VehicleModel<M12hmgEntity> {

    @Override
    public ResourceLocation getModelResource(M12hmgEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "geo/m12hmg.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M12hmgEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m12hmg.png");
    }

    @Override
    public ResourceLocation getAnimationResource(M12hmgEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "animations/m12hmg.animation.json");
    }

}
