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
    public ResourceLocation getTextureResource(M12hmgEntity entity) {
        // 完全仿照TruckModel的getTextureResource方法模式
        int variant = entity.getTextureVariant();
        
        switch (variant) {
            case M12hmgEntity.VARIANT_CITY:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m12hmg_city.png");
            case M12hmgEntity.VARIANT_DUST:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m12hmg_dust.png");
            case M12hmgEntity.VARIANT_SNOW:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m12hmg_snow.png");
            case M12hmgEntity.VARIANT_COMP:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m12hmg_comp.png");
            case M12hmgEntity.VARIANT_DEFAULT:
            default:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m12hmg.png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(M12hmgEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "animations/m12hmg.animation.json");
    }

}
