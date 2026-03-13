package com.cmhh.halovecs.client.model.entity;

import com.atsuishio.superbwarfare.client.model.entity.VehicleModel;
import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.entity.vehicle.M274mEntity;

import net.minecraft.resources.ResourceLocation;

public class M274mModel extends VehicleModel<M274mEntity> {

    @Override
    public ResourceLocation getModelResource(M274mEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "geo/m274m.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M274mEntity entity) {
        // 完全仿照TruckModel的getTextureResource方法模式
        int variant = entity.getTextureVariant();
        
        switch (variant) {
            case M274mEntity.VARIANT_CITY:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m274m_city.png");
            case M274mEntity.VARIANT_DUST:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m274m_dust.png");
            case M274mEntity.VARIANT_SNOW:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m274m_snow.png");
            case M274mEntity.VARIANT_COMP:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m274m_comp.png");
            case M274mEntity.VARIANT_DEFAULT:
            default:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m274m.png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(M274mEntity animatable) {
        return null;
    }
    
    // 暂时不重写collectTransform方法，使用父类的默认实现
    // VehicleModel基类可能已经能自动处理车轮动画
}
