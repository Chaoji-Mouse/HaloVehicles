package com.cmhh.halovecs.client.model.entity;

import com.atsuishio.superbwarfare.client.model.entity.VehicleModel;
import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.entity.vehicle.M12rocEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class M12rocModel extends VehicleModel<M12rocEntity> {

    @Override
    public ResourceLocation getModelResource(M12rocEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "geo/m12roc.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M12rocEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m12roc.png");
    }

    @Override
    public ResourceLocation getAnimationResource(M12rocEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "animations/m12roc.animation.json");
    }
    
    // 重写 collectTransform 方法以处理炮台和武器骨骼
    @Override
    public TransformContext<M12rocEntity> collectTransform(String boneName) {
        // 先调用父类方法
        var parentTransform = super.collectTransform(boneName);
        if (parentTransform != null) {
            return parentTransform;
        }
        
        // 处理炮台和武器骨骼
        switch (boneName) {
            case "turret":  // 炮台旋转（Y轴）
                return (bone, vehicle, state) -> {
                    float turretYRot = vehicle.getTurretYaw(state.getPartialTick());
                    bone.setRotY(turretYRot * Mth.DEG_TO_RAD);
                };
                
            case "gun":  // 炮管俯仰（X轴）
                return (bone, vehicle, state) -> {
                    float turretXRot = vehicle.getTurretPitch(state.getPartialTick());
                    bone.setRotX(-turretXRot * Mth.DEG_TO_RAD);
                };
                
            // 移除flare骨骼的脚本控制，完全使用动画文件中的动画
            // flare骨骼的显示/隐藏和动画由动画文件控制
        }
        
        return null;
    }
}
