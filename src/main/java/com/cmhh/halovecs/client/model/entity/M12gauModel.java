package com.cmhh.halovecs.client.model.entity;

import com.atsuishio.superbwarfare.client.model.entity.VehicleModel;
import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.entity.vehicle.M12gauEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class M12gauModel extends VehicleModel<M12gauEntity> {

    @Override
    public ResourceLocation getModelResource(M12gauEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "geo/m12gau.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M12gauEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "textures/entity/m12gau.png");
    }

    @Override
    public ResourceLocation getAnimationResource(M12gauEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Halovecs.MODID, "animations/m12gau.animation.json");
    }
    
    // 重写 collectTransform 方法以处理炮台和武器骨骼
    @Override
    public TransformContext<M12gauEntity> collectTransform(String boneName) {
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
            case "laser":  // 激光骨骼缩放控制
                return (bone, vehicle, state) -> {
                    // 从 VehicleEntity 基类获取激光数据
                    float length = vehicle.getEntityData().get(
                        com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity.LASER_LENGTH
                    );
                    float scale = Math.min(net.minecraft.util.Mth.lerp(
                        state.getPartialTick(),
                        vehicle.getEntityData().get(
                            com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity.LASER_SCALE_O
                        ),
                        vehicle.getEntityData().get(
                            com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity.LASER_SCALE
                        )
                    ), 1.2f);
                    
                    // 设置激光缩放：Z轴为长度方向，X和Y轴为粗细
                    // 注意：激光骨骼在模型中的初始位置是炮管末端
                    bone.setScaleZ(10 * length);  // 长度缩放
                    bone.setScaleX(scale);        // 宽度缩放
                    bone.setScaleY(scale);        // 高度缩放
                };
        }
        
        return null;
    }
}
