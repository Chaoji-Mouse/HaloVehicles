package com.cmhh.halovecs.client.model.entity;

import com.atsuishio.superbwarfare.client.model.entity.VehicleModel;
import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.entity.vehicle.M12traEntity;

import net.minecraft.resources.ResourceLocation;

public class M12traModel extends VehicleModel<M12traEntity> {

    @Override
    public ResourceLocation getModelResource(M12traEntity animatable) {
        return new ResourceLocation(Halovecs.MODID, "geo/m12tra.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M12traEntity animatable) {
        return new ResourceLocation(Halovecs.MODID, "textures/entity/m12tra.png");
    }

    @Override
    public ResourceLocation getAnimationResource(M12traEntity animatable) {
        return null;
    }
}
