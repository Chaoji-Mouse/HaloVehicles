package com.cmhh.halovecs.client.model.entity;

import com.atsuishio.superbwarfare.client.model.entity.VehicleModel;
import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.entity.vehicle.M274Entity;

import net.minecraft.resources.ResourceLocation;

public class M274Model extends VehicleModel<M274Entity> {

    @Override
    public ResourceLocation getModelResource(M274Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "geo/m274.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M274Entity entity) {
        // 完全仿照TruckModel的getTextureResource方法模式
        int variant = entity.getTextureVariant();
        
        switch (variant) {
            case M274Entity.VARIANT_CITY:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m274_city.png");
            case M274Entity.VARIANT_DUST:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m274_dust.png");
            case M274Entity.VARIANT_SNOW:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m274_snow.png");
            case M274Entity.VARIANT_COMP:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m274_comp.png");
            case M274Entity.VARIANT_DEFAULT:
            default:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m274.png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(M274Entity animatable) {
        return null;
    }
    
    // 暂时不重写collectTransform方法，使用父类的默认实现
    // VehicleModel基类可能已经能自动处理车轮动画
}
