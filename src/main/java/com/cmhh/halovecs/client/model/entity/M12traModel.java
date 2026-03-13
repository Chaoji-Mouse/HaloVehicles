package com.cmhh.halovecs.client.model.entity;

import com.atsuishio.superbwarfare.client.model.entity.VehicleModel;
import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.entity.vehicle.M12traEntity;

import net.minecraft.resources.ResourceLocation;

public class M12traModel extends VehicleModel<M12traEntity> {

    @Override
    public ResourceLocation getModelResource(M12traEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "geo/m12tra.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M12traEntity entity) {
        // 完全仿照TruckModel的getTextureResource方法模式
        int variant = entity.getTextureVariant();
        
        switch (variant) {
            case M12traEntity.VARIANT_CITY:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m12tra_city.png");
            case M12traEntity.VARIANT_DUST:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m12tra_dust.png");
            case M12traEntity.VARIANT_SNOW:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m12tra_snow.png");
            case M12traEntity.VARIANT_COMP:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m12tra_comp.png");
            case M12traEntity.VARIANT_DEFAULT:
            default:
                return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m12tra.png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(M12traEntity animatable) {
        return null;
    }
    
    // 暂时不重写collectTransform方法，使用父类的默认实现
    // VehicleModel基类可能已经能自动处理车轮动画
}
