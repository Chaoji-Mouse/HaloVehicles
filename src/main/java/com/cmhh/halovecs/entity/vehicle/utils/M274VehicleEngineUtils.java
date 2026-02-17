package com.cmhh.halovecs.entity.vehicle.utils;

import com.atsuishio.superbwarfare.data.vehicle.subdata.EngineInfo;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import net.minecraft.util.Mth;
import org.joml.Math;
import net.minecraft.world.entity.Entity;

/**
 * M274专用转向计算工具
 * 完美复制SuperbWarfare逻辑，只修改方向舵角度限制
 */
public class M274VehicleEngineUtils {
    
    // 唯一修改的参数：方向舵角度限制
    private static final float CUSTOM_RUDDER_LIMIT = 1.5f;    // 原值0.8f → +87.5%
    
    /**
     * M274专用wheelEngine方法
     * 完美复制SuperbWarfare逻辑，只修改方向舵角度限制
     */
    public static void wheelEngineForM274(VehicleEntity vehicle, EngineInfo.Wheel engineInfo) {
        // 1. 获取原版参数（从engineInfo读取，保持原值）
        double buoyancy = engineInfo.buoyancy;
        int energyCost = (int) (engineInfo.energyCostRate * Mth.abs(vehicle.getEntityData().get(VehicleEntity.POWER)));
        double wheelRotSpeed = engineInfo.wheelRotSpeed;
        double wheelDifferential = engineInfo.wheelDifferential; // 使用原版值
        float maxForwardSpeedRate = engineInfo.maxForwardSpeedRate;
        float maxBackwardSpeedRate = engineInfo.maxBackwardSpeedRate;
        float powerAdd = engineInfo.increment;
        float powerReduce = engineInfo.decrement;
        float steeringSpeed = engineInfo.steeringSpeed; // 使用原版值
        
        // 2. 浮力计算（原版逻辑）
        if (buoyancy != 0) {
            double fluidFloat = buoyancy * com.atsuishio.superbwarfare.entity.vehicle.utils.VehicleVecUtils.getSubmergedHeight(vehicle);
            vehicle.setDeltaMovement(vehicle.getDeltaMovement().add(0, fluidFloat, 0));
        }
        
        // 3. 地面/流体阻力计算（原版逻辑）
        if (vehicle.onGround()) {
            float f0 = 0.54f + 0.25f * Mth.abs(90 - (float) com.atsuishio.superbwarfare.entity.vehicle.utils.VehicleVecUtils.calculateAngle(vehicle.getDeltaMovement(), vehicle.getViewVector(1))) / 90;
            vehicle.setDeltaMovement(vehicle.getDeltaMovement().add(vehicle.getViewVector(1).normalize().scale(0.05 * vehicle.getDeltaMovement().dot(vehicle.getViewVector(1)))));
            vehicle.setDeltaMovement(vehicle.getDeltaMovement().multiply(f0, 0.99, f0));
        } else if (vehicle.isInFluidType()) {
            float f1 = 0.74f + 0.09f * Mth.abs(90 - (float) com.atsuishio.superbwarfare.entity.vehicle.utils.VehicleVecUtils.calculateAngle(vehicle.getDeltaMovement(), vehicle.getViewVector(1))) / 90;
            vehicle.setDeltaMovement(vehicle.getDeltaMovement().add(vehicle.getViewVector(1).normalize().scale(0.04 * vehicle.getDeltaMovement().dot(vehicle.getViewVector(1)))));
            vehicle.setDeltaMovement(vehicle.getDeltaMovement().multiply(f1, 0.85, f1));
        } else {
            vehicle.setDeltaMovement(vehicle.getDeltaMovement().multiply(0.99, 0.99, 0.99));
        }
        
        // 4. 能量检查（原版逻辑）
        Entity passenger0 = vehicle.getFirstPassenger();
        
        if (vehicle.getEnergy() < energyCost || (vehicle.getMaxEnergy() > 0 && vehicle.getEnergy() <= 0)) {
            vehicle.setForwardInputDown(false);
            vehicle.setBackInputDown(false);
            vehicle.setLeftInputDown(false);
            vehicle.setRightInputDown(false);
            vehicle.getEntityData().set(VehicleEntity.POWER, vehicle.getEntityData().get(VehicleEntity.POWER) * 0.95f);
            vehicle.getEntityData().set(VehicleEntity.DELTA_ROT, vehicle.getEntityData().get(VehicleEntity.DELTA_ROT) * 0.5f);
        }
        
        // 5. 无乘客时重置输入（原版逻辑）
        if (passenger0 == null) {
            vehicle.setLeftInputDown(false);
            vehicle.setRightInputDown(false);
            vehicle.setForwardInputDown(false);
            vehicle.setBackInputDown(false);
            vehicle.getEntityData().set(VehicleEntity.POWER, 0f);
        }
        
        // 6. 前进/后退输入处理（原版逻辑）
        if (vehicle.forwardInputDown()) {
            vehicle.getEntityData().set(VehicleEntity.POWER, Math.min(vehicle.getEntityData().get(VehicleEntity.POWER) 
                + (vehicle.getEntityData().get(VehicleEntity.POWER) < 0 ? powerAdd * 2f : powerAdd), 1));
        }
        
        if (vehicle.backInputDown()) {
            vehicle.getEntityData().set(VehicleEntity.POWER, Math.max(vehicle.getEntityData().get(VehicleEntity.POWER) 
                - (vehicle.getEntityData().get(VehicleEntity.POWER) > 0 ? powerReduce * 2f : powerReduce), -1));
        }
        
        // 7. 转向输入处理（原版逻辑，使用原版steeringSpeed）
        if (vehicle.leftInputDown()) {
            vehicle.getEntityData().set(VehicleEntity.DELTA_ROT, 
                vehicle.getEntityData().get(VehicleEntity.DELTA_ROT) + steeringSpeed);
        }
        if (vehicle.rightInputDown()) {
            vehicle.getEntityData().set(VehicleEntity.DELTA_ROT, 
                vehicle.getEntityData().get(VehicleEntity.DELTA_ROT) - steeringSpeed);
        }
        
        // 8. DELTA_ROT衰减（原版逻辑）
        vehicle.getEntityData().set(VehicleEntity.DELTA_ROT, vehicle.getEntityData().get(VehicleEntity.DELTA_ROT) 
            * (float) Math.max(0.78f - 0.25f * vehicle.getDeltaMovement().horizontalDistance(), 0.1));
        
        // 9. 方向舵角度计算 - 唯一修改的地方！
        float newRudderRot = vehicle.getRudderRot() - vehicle.getEntityData().get(VehicleEntity.DELTA_ROT);
        
        // 关键修改：使用自定义的方向舵角度限制
        float clampedRudder = Mth.clamp(newRudderRot, CUSTOM_RUDDER_LIMIT, -CUSTOM_RUDDER_LIMIT);
        float scaledRudder = clampedRudder * 0.75f; // 保持原版缩放因子0.75f
        
        vehicle.setRudderRot(scaledRudder);
        
        // 10. 车轮差速计算（原版逻辑，使用原版wheelDifferential）
        float wheelDiff = (float) (wheelDifferential * vehicle.getEntityData().get(VehicleEntity.DELTA_ROT));
        wheelDiff = Mth.clamp(wheelDiff, -5f, 5f); // 保持原限制
        
        // 11. 应用车轮旋转（原版逻辑）
        vehicle.setLeftWheelRot((float) ((vehicle.getLeftWheelRot() - wheelRotSpeed) 
            + wheelDiff * vehicle.getDeltaMovement().length()));
        vehicle.setRightWheelRot((float) ((vehicle.getRightWheelRot() - wheelRotSpeed) 
            - wheelDiff * vehicle.getDeltaMovement().length()));
        
        // 12. Y轴旋转计算（原版逻辑）
        vehicle.setYRot((float) (vehicle.getYRot() - Math.max((vehicle.isInFluidType() && !vehicle.onGround() ? 6 : 12) 
            * vehicle.getDeltaMovement().horizontalDistance(), 0) * vehicle.getRudderRot() 
            * (vehicle.getEntityData().get(VehicleEntity.POWER) > 0 ? 1 : -1)));
    }
}
