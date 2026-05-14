package com.cmhh.halovecs.engine;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.utils.VehicleEngineUtils;
import com.atsuishio.superbwarfare.entity.vehicle.utils.VehicleVecUtils;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.tools.VectorTool;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Math;

import static com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity.*;

public class VehicleEngineUtilsExtension {

    /**
     * 注意：此方法是 static 方法，由 VTOLEngine.work() 调用。
     * 该方法不包含引擎声音触发逻辑——引擎声音由调用者（T77hEntity.travel()）负责。
     */
    public static void vtolEngine(VehicleEntity vehicle, VTOLEngine engineInfo) {
    // ==================== 参数提取 ====================
    int energyCost = (int) engineInfo.getEnergyCostRate();      // 基础能量消耗率（固定值，与功率无关）
    float powerAdd = engineInfo.getIncrement();                 // 功率增加速度（上升加速度系数）
    float powerReduce = engineInfo.getDecrement();              // 功率减少速度（下降加速度系数）
    float pitchSpeed = engineInfo.getPitchSpeed();              // 俯仰控制灵敏度（鼠标Y轴响应度）
    float yawSpeed = engineInfo.getYawSpeed();                  // 偏航控制灵敏度（A/D键转向响应度）
    float rollSpeed = engineInfo.getRollSpeed();                // 滚转控制灵敏度（鼠标X轴响应度）
    float lift = engineInfo.getLiftSpeed();                     // 升力系数（垂直推力强度，值越大升力越强）
    float speedRate = engineInfo.speedRate;                     // 速度系数（基础推力强度）
    float gearRotateAngle = engineInfo.gearRotateAngle;         // 起落架旋转角度（默认85度）

    if (vehicle.getEntityData().get(SYNCHED_GEAR_ROT) == 0) {
        // ==================== 地面/空中阻力处理 ====================
        if (vehicle.onGround()) {
            // 地面状态：施加较强水平阻力（0.8倍衰减），保持垂直速度不变
            vehicle.setDeltaMovement(vehicle.getDeltaMovement().multiply(0.8, 1, 0.8));
        } else {
            // 空中状态：根据后退输入调整滚转阻尼
            vehicle.setZRot(vehicle.getRoll() * (vehicle.backInputDown() ? 0.9f : 0.99f));

            // 计算综合阻力系数f（范围0.01-0.99）
            float f = (float) Mth.clamp(
                0.95f - 0.015 * vehicle.getDeltaMovement().length() + 
                0.02f * Mth.abs(90 - (float) VehicleVecUtils.calculateAngle(
                    vehicle.getDeltaMovement(), vehicle.getViewVector(1))) / 90, 
                0.01, 0.99
            );
            
            // 根据俯仰角度施加前后推力
            vehicle.setDeltaMovement(vehicle.getDeltaMovement().add(
                vehicle.getViewVector(1).scale(
                    (vehicle.getXRot() < 0 ? -0.035 : (vehicle.getXRot() > 0 ? 0.035 : 0)) * 
                    vehicle.getDeltaMovement().length()
                )
            ));
            
            // 应用阻力
            vehicle.setDeltaMovement(vehicle.getDeltaMovement().multiply(f, 0.95, f));
        }

        // ==================== 水中物理处理 ====================
        if (vehicle.isInFluidType() && vehicle.tickCount % 4 == 0 && 
            VehicleVecUtils.getSubmergedHeight(vehicle) > 0.5 * vehicle.getBbHeight()) {
            
            vehicle.setDeltaMovement(vehicle.getDeltaMovement().multiply(0.6, 0.6, 0.6));
            
            if (vehicle.getLastTickSpeed() > 0.4) {
                vehicle.hurt(
                    ModDamageTypes.causeVehicleStrikeDamage(
                        vehicle.level().registryAccess(), 
                        vehicle, 
                        vehicle.getFirstPassenger() == null ? vehicle : vehicle.getFirstPassenger()
                    ), 
                    6 + (float) (20 * ((vehicle.getLastTickSpeed() - 0.4) * (vehicle.getLastTickSpeed() - 0.4)))
                );
            }
        }

        // ==================== 乘客状态检查 ====================
        Entity pilot = vehicle.getFirstPassenger();
        boolean hasPassenger = false;

        for (int i = 0; i < vehicle.getMaxPassengers() - 1; i++) {
            if (vehicle.getNthEntity(i) != null) {
                hasPassenger = true;
            }
        }

        float diffX;
        float diffZ;

        // ==================== 载具健康状态检查 ====================
        if (vehicle.getHealth() > 0.1f * vehicle.getMaxHealth()) {
            
            var landingPos = VehicleEngineUtils.INSTANCE.findNearestLandingPos(vehicle, 30);
            
            // ========== 无人驾驶状态处理 ==========
            if (pilot == null) {
                vehicle.setLeftInputDown(false);
                vehicle.setRightInputDown(false);
                vehicle.setForwardInputDown(false);
                vehicle.setBackInputDown(true);
                vehicle.setUpInputDown(false);
                vehicle.setDownInputDown(false);
                
                vehicle.setZRot(vehicle.getRoll() * 0.98f);
                vehicle.setXRot(vehicle.getXRot() * 0.98f);
                
                vehicle.getDeltaMovement().multiply(0.96, 0.98, 0.96);
                
                if (hasPassenger) {
                    vehicle.getEntityData().set(POWER, vehicle.getEntityData().get(POWER) * 0.99f);
                }
            } 
            // ========== 玩家驾驶状态处理 ==========
            else {
                // ----- 转向控制（A/D键）-----
                if (!vehicle.backInputDown() || landingPos == null) {
                    if (vehicle.rightInputDown()) {
                        vehicle.setHoldTick(vehicle.getHoldTick() + 1);
                        vehicle.getEntityData().set(DELTA_ROT, 
                            vehicle.getEntityData().get(DELTA_ROT) - 
                            2f * Math.min(vehicle.getHoldTick(), 7) * vehicle.getEntityData().get(POWER)
                        );
                    } else if (vehicle.leftInputDown()) {
                        vehicle.setHoldTick(vehicle.getHoldTick() + 1);
                        vehicle.getEntityData().set(DELTA_ROT, 
                            vehicle.getEntityData().get(DELTA_ROT) + 
                            2f * Math.min(vehicle.getHoldTick(), 7) * vehicle.getEntityData().get(POWER)
                        );
                    } else {
                        vehicle.setHoldTick(0);
                    }
                    
                    // ----- 姿态控制（鼠标）-----
                    vehicle.setXRot(vehicle.getXRot() + 
                        ((vehicle.onGround()) ? 0 : 1.5f) * pitchSpeed * 
                        vehicle.getMouseMoveSpeedY() * vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT)
                    );
                    
                    vehicle.setZRot(vehicle.getRoll() - rollSpeed * 
                        (vehicle.getEntityData().get(DELTA_ROT) + 
                        (vehicle.onGround() ? 0 : 0.25f) * vehicle.getMouseMoveSpeedX() * 
                        vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT))
                    );
                }

                // ----- 偏航控制（鼠标X轴）-----
                vehicle.setYRot(vehicle.getYRot() + yawSpeed * Mth.clamp(
                    (vehicle.onGround() ? 0.1f : 2f) * vehicle.getMouseMoveSpeedX() * 
                    vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT) + 
                    (vehicle.getEntityData().get(SUB_ENGINE_DAMAGED) ? 25 : 0) * 
                    vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT), 
                    -10f, 10f
                ));
                
                // ----- 自动降落系统 -----
                if (landingPos != null && !vehicle.onGround() && vehicle.backInputDown()) {
                    VehicleEngineUtils.INSTANCE.updateAutoLanding(vehicle, landingPos);
                }

                // ----- 降落提示 -----
                if (pilot instanceof Player player && vehicle.level().isClientSide && 
                    landingPos != null && !vehicle.onGround()) {
                    player.displayClientMessage(Component.translatable("tips.superbwarfare.press_s_to_landing"), true);
                }

                // ----- 地面稳定 -----
                if (vehicle.onGround()) {
                    vehicle.setZRot(vehicle.getRoll() * 0.98f);
                    vehicle.setXRot(vehicle.getXRot() * 0.98f);
                }
            }

            // ========== 能量和引擎状态管理 ==========
            if (vehicle.getEnergy() < energyCost || (vehicle.getMaxEnergy() > 0 && vehicle.getEnergy() <= 0)) {
                vehicle.getEntityData().set(POWER, vehicle.getEntityData().get(POWER) * 0.995f);
                vehicle.setForwardInputDown(false);
                vehicle.setBackInputDown(false);
                vehicle.setEngineStart(false);
                vehicle.setEngineStartOver(false);
            } else {
                boolean up = vehicle.forwardInputDown();
                boolean down = vehicle.downInputDown();

                // ----- 引擎启动逻辑 -----
                // 第一次按下上升键时启动引擎，播放启动音效
                if (!vehicle.getEngineStart() && up) {
                    vehicle.setEngineStart(true);
                    // 引擎启动音效：engineInfo.getEngineStartSound() 是 SoundEvent 类型
                    if (engineInfo.getEngineStartSound() != null) {
                        vehicle.level().playSound(null, vehicle.getX(), vehicle.getY(), vehicle.getZ(), 
                            engineInfo.getEngineStartSound(), 
                            vehicle.getSoundSource(), 3.0f, 1.0f);
                    }
                }

                // ----- 上升控制（W键/上键）-----
                if (up && vehicle.getEngineStartOver()) {
                    vehicle.setHoldPowerTick(vehicle.getHoldPowerTick() + 1);
                    vehicle.getEntityData().set(POWER, Math.min(
                        vehicle.getEntityData().get(POWER) + 
                        0.0007f * powerAdd * Math.min(vehicle.getHoldPowerTick(), 10), 
                        0.12f
                    ));
                }

                // ----- 下降控制（S键/下键）-----
                if (vehicle.getEngineStartOver()) {
                    if (down) {
                        vehicle.setHoldPowerTick(vehicle.getHoldPowerTick() + 1);
                        vehicle.getEntityData().set(POWER, Math.max(
                            vehicle.getEntityData().get(POWER) - 
                            0.001f * powerReduce * Math.min(vehicle.getHoldPowerTick(), 5), 
                            vehicle.onGround() ? 0 : 0.025f / lift
                        ));
                    } else if (vehicle.backInputDown()) {
                        vehicle.setHoldPowerTick(vehicle.getHoldPowerTick() + 1);
                        vehicle.getEntityData().set(POWER, Math.max(
                            vehicle.getEntityData().get(POWER) - 
                            0.001f * powerReduce * Math.min(vehicle.getHoldPowerTick(), 5), 
                            vehicle.onGround() ? 0 : 0.058f / lift
                        ));
                    }
                }

                // ----- 引擎启动阶段功率增加 -----
                if (vehicle.getEngineStart() && !vehicle.getEngineStartOver()) {
                    vehicle.getEntityData().set(POWER, Math.min(
                        vehicle.getEntityData().get(POWER) + 0.0012f * powerAdd, 
                        0.045f
                    ));
                }

                // ----- 无输入时的自动功率调整 -----
                if (!(up || down || vehicle.backInputDown()) && vehicle.getEngineStartOver()) {
                    if (vehicle.getDeltaMovement().y() < 0) {
                        vehicle.getEntityData().set(POWER, Math.min(
                            vehicle.getEntityData().get(POWER) + 0.0002f, 
                            0.12f
                        ));
                    } else {
                        vehicle.getEntityData().set(POWER, Math.max(
                            vehicle.getEntityData().get(POWER) - 
                            (vehicle.onGround() ? 0.00005f : 0.0002f), 
                            0
                        ));
                    }
                    vehicle.setHoldPowerTick(0);
                }
            }

            // ==================== 起落架系统 ====================
            if (engineInfo.hasGear) {
                if (vehicle.upInputDown()) {
                    vehicle.setUpInputDown(false);
                    if (vehicle.getEntityData().get(SYNCHED_GEAR_ROT) < 0.1f && !vehicle.onGround()) {
                        // 收起起落架，提升功率到飞行水平
                        vehicle.getEntityData().set(GEAR_UP, true);
                        vehicle.getEntityData().set(POWER, 0.2f);
                        vehicle.getEntityData().set(SYNCHED_PROPELLER_ROT, 0.2f);
                    } else if (vehicle.getEntityData().get(SYNCHED_GEAR_ROT) > 0.9f) {
                        vehicle.getEntityData().set(GEAR_UP, false);
                    }
                }

                if (vehicle.onGround()) {
                    vehicle.getEntityData().set(GEAR_UP, false);
                }

                if (vehicle.getEntityData().get(GEAR_UP)) {
                    vehicle.getEntityData().set(SYNCHED_GEAR_ROT, Math.min(vehicle.getEntityData().get(SYNCHED_GEAR_ROT) + 0.05f, 1));
                } else {
                    vehicle.getEntityData().set(SYNCHED_GEAR_ROT, Math.max(vehicle.getEntityData().get(SYNCHED_GEAR_ROT) - 0.05f, 0));
                }

                vehicle.setGearRot(vehicle.getEntityData().get(SYNCHED_GEAR_ROT) * gearRotateAngle);
            }
        }
        // ==================== 载具损坏状态处理 ====================
        else if (!vehicle.onGround() && vehicle.getEngineStartOver()) {
            vehicle.getEntityData().set(POWER, Math.max(vehicle.getEntityData().get(POWER) - 0.0003f, 0.01f));
            vehicle.setDestroyRot(vehicle.getDestroyRot() + 0.08f);

            diffX = 45 - vehicle.getXRot();
            diffZ = -20 - vehicle.getRoll();

            vehicle.setXRot(vehicle.getXRot() + diffX * 0.05f * vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT));
            vehicle.setYRot(vehicle.getYRot() + vehicle.getDestroyRot());
            vehicle.setZRot(vehicle.getRoll() + diffZ * 0.1f * vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT));

            vehicle.setDeltaMovement(vehicle.getDeltaMovement().add(0, -vehicle.getDestroyRot() * 0.004, 0));
        }

        // ==================== 引擎损坏影响 ====================
        if (vehicle.getEntityData().get(MAIN_ENGINE_DAMAGED)) {
            vehicle.getEntityData().set(POWER, vehicle.getEntityData().get(POWER) * 0.98f);
        }

        // ==================== 状态衰减和更新 ====================
        vehicle.getEntityData().set(DELTA_ROT, vehicle.getEntityData().get(DELTA_ROT) * 0.9f);

        vehicle.getEntityData().set(SYNCHED_PROPELLER_ROT, 
            Mth.lerp(0.18f, vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT), vehicle.getEntityData().get(POWER))
        );

        vehicle.setPropellerRot(vehicle.getPropellerRot() + 30 * vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT));

        vehicle.getEntityData().set(SYNCHED_PROPELLER_ROT, vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT) * 0.9995f);

        // ==================== 能量消耗 ====================
        if (vehicle.getEngineStart()) {
            vehicle.consumeEnergy((int) (energyCost * 8.3333f * Mth.abs(vehicle.getEntityData().get(POWER))));
        }

        // ==================== 升力计算和应用 ====================
        Vec3 force = vehicle.getUpVec(1);

        vehicle.setDeltaMovement(vehicle.getDeltaMovement().add(
            force.scale(vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT) * lift)
        ));

        // ==================== 引擎状态阈值检查 ====================
        if (vehicle.getEntityData().get(POWER) > 0.04f) {
            vehicle.setEngineStartOver(true);
        }

        if (vehicle.getEntityData().get(POWER) < 0.0004f) {
            vehicle.setEngineStart(false);
            vehicle.setEngineStartOver(false);
        }

    } else if (vehicle.getEntityData().get(SYNCHED_GEAR_ROT) == 1) {
        // ==================== 固定翼飞机模式 ====================
        // 能量消耗 = 能量消耗率 × 当前功率绝对值
        int planeEnergyCost = (int) (engineInfo.getEnergyCostRate() * Mth.abs(vehicle.getEntityData().get(POWER)));

        // ==================== 空气阻力计算 ====================
        float f = (float) Mth.clamp(
            Math.max((vehicle.onGround() ? 0.819f : 0.82f) - 0.005 * vehicle.getDeltaMovement().length(), 0.5) + 
            0.001f * Mth.abs(90 - (float) VehicleVecUtils.calculateAngle(
                vehicle.getDeltaMovement(), vehicle.getViewVector(1))) / 90, 
            0.01, 0.99
        );

        // 根据飞行方向施加推力
        boolean forward = vehicle.getDeltaMovement().dot(vehicle.getViewVector(1)) > 0;
        vehicle.setDeltaMovement(vehicle.getDeltaMovement().add(
            vehicle.getViewVector(1).scale((forward ? 0.227 : 0.1) * vehicle.getDeltaMovement().dot(vehicle.getViewVector(1))))
        );
        
        // 应用阻力
        vehicle.setDeltaMovement(vehicle.getDeltaMovement().multiply(f, f, f));

        // ==================== 水中物理处理 ====================
        if (vehicle.isInFluidType() && vehicle.tickCount % 4 == 0) {
            vehicle.setDeltaMovement(vehicle.getDeltaMovement().multiply(0.6, 0.6, 0.6));
            
            if (vehicle.getLastTickSpeed() > 0.4) {
                vehicle.hurt(
                    ModDamageTypes.causeVehicleStrikeDamage(
                        vehicle.level().registryAccess(), 
                        vehicle, 
                        vehicle.getFirstPassenger() == null ? vehicle : vehicle.getFirstPassenger()
                    ), 
                    (float) (20 * ((vehicle.getLastTickSpeed() - 0.4) * (vehicle.getLastTickSpeed() - 0.4)))
                );
            }
        }

        // ==================== 乘客状态检查 ====================
        Entity passenger = vehicle.getFirstPassenger();

        // ==================== 能量管理 ====================
        if (vehicle.getEnergy() < energyCost || (vehicle.getMaxEnergy() > 0 && vehicle.getEnergy() <= 0)) {
            vehicle.setForwardInputDown(false);
            vehicle.setBackInputDown(false);
            vehicle.setEngineStart(false);
            vehicle.setEngineStartOver(false);
            vehicle.getEntityData().set(POWER, vehicle.getEntityData().get(POWER) * 0.95f);
        } else {
            vehicle.consumeEnergy(energyCost);
        }

        // ==================== 载具健康状态检查 ====================
        if (vehicle.getHealth() > 0.1f * vehicle.getMaxHealth()) {
            // ========== 无人驾驶状态处理 ==========
            if (passenger == null || vehicle.isInFluidType()) {
                vehicle.setLeftInputDown(false);
                vehicle.setRightInputDown(false);
                vehicle.setForwardInputDown(false);
                vehicle.setBackInputDown(false);
                
                vehicle.getEntityData().set(POWER, vehicle.getEntityData().get(POWER) * 0.95f);
                
                if (vehicle.onGround()) {
                    vehicle.setDeltaMovement(vehicle.getDeltaMovement().multiply(0.94, 1, 0.94));
                } else {
                    vehicle.setXRot(Mth.clamp(vehicle.getXRot() + 0.1f, -89, 89));
                }
            } 
            // ========== 玩家驾驶状态处理 ==========
            else if (passenger instanceof Player) {
                // ----- 引擎启动 -----
                if (!vehicle.getEngineStart() && vehicle.forwardInputDown() && vehicle.getEntityData().get(POWER) > 0.01) {
                    vehicle.setEngineStart(true);
                    // 引擎启动音效
                    if (engineInfo.getEngineStartSound() != null) {
                        vehicle.level().playSound(null, vehicle.getX(), vehicle.getY(), vehicle.getZ(), 
                            engineInfo.getEngineStartSound(), 
                            vehicle.getSoundSource(), 3.0f, 1.0f);
                    }
                }

                // ----- 推力控制（W/S键）-----
                if (vehicle.getEnergy() >= energyCost) {
                    if (vehicle.forwardInputDown()) {
                        vehicle.getEntityData().set(POWER, (float) Mth.clamp(
                            vehicle.getEntityData().get(POWER) + 0.0045f * powerAdd, -0.1, 1
                        ));
                    }

                    if (vehicle.backInputDown()) {
                        vehicle.getEntityData().set(POWER, Math.max(
                            vehicle.getEntityData().get(POWER) - 0.006f * powerReduce, 
                            vehicle.onGround() ? -0.2f : 0.4f
                        ));
                    }
                }

                if (!vehicle.forwardInputDown() && !vehicle.backInputDown()) {
                    vehicle.getEntityData().set(POWER, vehicle.getEntityData().get(POWER) * 0.995f);
                }

                // ----- 滚转控制（A/D键，仅在空中有效）-----
                if (!vehicle.onGround()) {
                    if (vehicle.rightInputDown()) {
                        vehicle.getEntityData().set(DELTA_ROT, vehicle.getEntityData().get(DELTA_ROT) - 0.6f);
                    } else if (vehicle.leftInputDown()) {
                        vehicle.getEntityData().set(DELTA_ROT, vehicle.getEntityData().get(DELTA_ROT) + 0.6f);
                    }
                }

                // ----- 刹车系统（下键）-----
                if (vehicle.downInputDown()) {
                    if (vehicle.onGround()) {
                        vehicle.getEntityData().set(POWER, vehicle.getEntityData().get(POWER) * 0.92f);
                        vehicle.setDeltaMovement(vehicle.getDeltaMovement().multiply(0.97, 1, 0.97));
                    } else {
                        vehicle.getEntityData().set(POWER, vehicle.getEntityData().get(POWER) * 0.97f);
                        vehicle.setDeltaMovement(vehicle.getDeltaMovement().multiply(0.994, 1, 0.994));
                    }
                    
                    vehicle.getEntityData().set(PLANE_BREAK, Math.min(vehicle.getEntityData().get(PLANE_BREAK) + 10, 60f));
                }
            }

            // ==================== 飞行姿态控制 ====================
            float rotSpeed = 1.5f + 1.2f * Mth.abs(VectorTool.calculateY(vehicle.getRoll()));

            float addY = Mth.clamp(
                Math.max((vehicle.onGround() ? 0.6f : 0.2f) * (float) vehicle.getDeltaMovement().length(), 0f) * 
                vehicle.getMouseMoveSpeedX(), 
                -rotSpeed, rotSpeed
            );
            
            float addX = Mth.clamp(
                Math.min((float) Math.max(vehicle.getDeltaMovement().dot(vehicle.getViewVector(1)) - 0.24, 0.15), 0.4f) * 
                vehicle.getMouseMoveSpeedY(), 
                -3.5f, 3.5f
            );
            
            float addZ = vehicle.getEntityData().get(DELTA_ROT) - 
                (vehicle.onGround() ? 0 : 0.004f) * vehicle.getMouseMoveSpeedX() * 
                (float) vehicle.getDeltaMovement().dot(vehicle.getViewVector(1));

            vehicle.setYRot(vehicle.getYRot() + yawSpeed * addY);
            
            if (!vehicle.onGround()) {
                vehicle.setXRot(vehicle.getXRot() + pitchSpeed * addX);
                vehicle.setZRot(vehicle.getRoll() - rollSpeed * addZ);
            }

            // ==================== 自动回正系统 ====================
            if (!vehicle.onGround()) {
                float xSpeed = 1 + 20 * Mth.abs(vehicle.getXRot() / 180);
                float speed = Mth.clamp(Mth.abs(vehicle.getRoll()) / (90 / xSpeed), 0, 1);

                if (vehicle.getRoll() > 0) {
                    vehicle.setZRot(vehicle.getRoll() - Math.min(speed, vehicle.getRoll()));
                } else if (vehicle.getRoll() < 0) {
                    vehicle.setZRot(vehicle.getRoll() + Math.min(speed, -vehicle.getRoll()));
                }
            }

            vehicle.setPropellerRot(vehicle.getPropellerRot() + 30 * vehicle.getEntityData().get(POWER));

            // ==================== 起落架系统 ====================
            if (engineInfo.hasGear) {
                if (vehicle.upInputDown()) {
                    vehicle.setUpInputDown(false);
                    if (vehicle.getEntityData().get(SYNCHED_GEAR_ROT) < 0.1f && !vehicle.onGround()) {
                        vehicle.getEntityData().set(GEAR_UP, true);
                    } else if (vehicle.getEntityData().get(SYNCHED_GEAR_ROT) > 0.9f) {
                        Vec3 lookVec = vehicle.getViewVector(1);
                        double speed = 25.0 / 20.0;
                        vehicle.setDeltaMovement(lookVec.scale(speed));
                        vehicle.getEntityData().set(GEAR_UP, false);
                        // 立即降低功率到直升机水平，防止过渡期间升力过大
                        vehicle.getEntityData().set(POWER, 0.04f);
                        vehicle.getEntityData().set(SYNCHED_PROPELLER_ROT, 0.04f);
                    }
                }

                if (vehicle.onGround()) {
                    vehicle.getEntityData().set(GEAR_UP, false);
                }

                if (vehicle.getEntityData().get(GEAR_UP)) {
                    vehicle.getEntityData().set(SYNCHED_GEAR_ROT, Math.min(vehicle.getEntityData().get(SYNCHED_GEAR_ROT) + 0.05f, 1));
                } else {
                    vehicle.getEntityData().set(SYNCHED_GEAR_ROT, Math.max(vehicle.getEntityData().get(SYNCHED_GEAR_ROT) - 0.05f, 0));
                }

                vehicle.setGearRot(vehicle.getEntityData().get(SYNCHED_GEAR_ROT) * gearRotateAngle);
            }

            // ==================== 襟翼系统 ====================
            float flapX = (1 - (Mth.abs(vehicle.getRoll())) / 90) * 
                Mth.clamp(vehicle.getMouseMoveSpeedY(), -22.5f, 22.5f) - 
                VectorTool.calculateY(vehicle.getRoll()) * 
                Mth.clamp(vehicle.getMouseMoveSpeedX(), -22.5f, 22.5f);

            vehicle.setFlap1LRot(Mth.clamp(-flapX - 4 * addZ - vehicle.getEntityData().get(PLANE_BREAK), -22.5f, 22.5f));
            vehicle.setFlap1RRot(Mth.clamp(-flapX + 4 * addZ - vehicle.getEntityData().get(PLANE_BREAK), -22.5f, 22.5f));
            vehicle.setFlap1L2Rot(Mth.clamp(-flapX - 4 * addZ + vehicle.getEntityData().get(PLANE_BREAK), -22.5f, 22.5f));
            vehicle.setFlap1R2Rot(Mth.clamp(-flapX + 4 * addZ + vehicle.getEntityData().get(PLANE_BREAK), -22.5f, 22.5f));

            vehicle.setFlap2LRot(Mth.clamp(flapX - 4 * addZ, -22.5f, 22.5f));
            vehicle.setFlap2RRot(Mth.clamp(flapX + 4 * addZ, -22.5f, 22.5f));

            float flapY = (1 - (Mth.abs(vehicle.getRoll())) / 90) * 
                Mth.clamp(vehicle.getMouseMoveSpeedX(), -22.5f, 22.5f) + 
                VectorTool.calculateY(vehicle.getRoll()) * 
                Mth.clamp(vehicle.getMouseMoveSpeedY(), -22.5f, 22.5f);
            vehicle.setFlap3Rot(flapY * 5);
        } 
        // ==================== 载具损坏状态处理 ====================
        else if (!vehicle.onGround()) {
            float diffX2;
            vehicle.getEntityData().set(POWER, Math.max(vehicle.getEntityData().get(POWER) - 0.0003f, 0.02f));
            vehicle.setDestroyRot(vehicle.getDestroyRot() + 0.1f);
            
            diffX2 = 90 - vehicle.getXRot();
            
            vehicle.setXRot(vehicle.getXRot() + diffX2 * 0.001f * vehicle.getDestroyRot());
            vehicle.setZRot(vehicle.getRoll() - vehicle.getDestroyRot());
            
            vehicle.setDeltaMovement(vehicle.getDeltaMovement().add(0, -0.03, 0));
            vehicle.setDeltaMovement(vehicle.getDeltaMovement().add(0, -vehicle.getDestroyRot() * 0.005, 0));
        }

        // ==================== 状态衰减 ====================
        vehicle.getEntityData().set(DELTA_ROT, vehicle.getEntityData().get(DELTA_ROT) * 0.85f);
        vehicle.getEntityData().set(PLANE_BREAK, vehicle.getEntityData().get(PLANE_BREAK) * 0.8f);

        if (vehicle.onGround()) {
            vehicle.getEntityData().set(POWER, vehicle.getEntityData().get(POWER) * 0.995f);
        }

        // ==================== 引擎损坏影响 ====================
        if (vehicle.getEntityData().get(MAIN_ENGINE_DAMAGED)) {
            vehicle.getEntityData().set(POWER, vehicle.getEntityData().get(POWER) * 0.96f);
        }
        if (vehicle.getEntityData().get(SUB_ENGINE_DAMAGED)) {
            vehicle.getEntityData().set(POWER, vehicle.getEntityData().get(POWER) * 0.96f);
        }

        // ==================== 升力和推力计算 ====================
        double flapAngle = (vehicle.getFlap1LRot() + vehicle.getFlap1RRot() + vehicle.getFlap1L2Rot() + vehicle.getFlap1R2Rot()) / 4;

        vehicle.setDeltaMovement(vehicle.getDeltaMovement().add(
            vehicle.getUpVec(1).scale(
                vehicle.getDeltaMovement().dot(vehicle.getViewVector(1)) * 0.022 * lift * 
                (1 + Math.sin((vehicle.onGround() ? 25 : flapAngle + 25) * Mth.DEG_TO_RAD))
            )
        ));

        vehicle.setDeltaMovement(vehicle.getDeltaMovement().add(
            vehicle.getViewVector(1).scale(
                0.03 * speedRate * vehicle.getEntityData().get(POWER) * (vehicle.sprintInputDown() ? 2.2 : 1)
            )
        ));

        // ==================== 引擎状态阈值检查 ====================
        if (vehicle.getEntityData().get(POWER) > 0.2f) {
            vehicle.setEngineStartOver(true);
        }
        if (vehicle.getEntityData().get(POWER) < 0.0004f) {
            vehicle.setEngineStart(false);
            vehicle.setEngineStartOver(false);
        }
    } else {
        // ==================== 通用模式（SYNCHED_GEAR_ROT 过渡状态）====================
        if (vehicle.onGround()) {
            vehicle.setDeltaMovement(vehicle.getDeltaMovement().multiply(0.8, 1, 0.8));
        } else {
            vehicle.setZRot(vehicle.getRoll() * (vehicle.backInputDown() ? 0.9f : 0.99f));

            boolean inGearTransition = engineInfo.hasGear &&
                vehicle.getEntityData().get(SYNCHED_GEAR_ROT) > 0.05f &&
                vehicle.getEntityData().get(SYNCHED_GEAR_ROT) < 0.95f;

            float f = (float) Mth.clamp(
                0.95f - 0.015 * vehicle.getDeltaMovement().length() +
                0.02f * Mth.abs(90 - (float) VehicleVecUtils.calculateAngle(
                    vehicle.getDeltaMovement(), vehicle.getViewVector(1))) / 90,
                0.01, 0.99
            );

            vehicle.setDeltaMovement(vehicle.getDeltaMovement().add(
                vehicle.getViewVector(1).scale(
                    (vehicle.getXRot() < 0 ? -0.035 : (vehicle.getXRot() > 0 ? 0.035 : 0)) *
                    vehicle.getDeltaMovement().length()
                )
            ));

            // 起落架过渡期间跳过阻尼，保持按钮按下时的覆写速度
            if (!inGearTransition) {
                vehicle.setDeltaMovement(vehicle.getDeltaMovement().multiply(f, 0.95, f));
            }
        }

        if (vehicle.isInFluidType() && vehicle.tickCount % 4 == 0 && 
            VehicleVecUtils.getSubmergedHeight(vehicle) > 0.5 * vehicle.getBbHeight()) {
            
            vehicle.setDeltaMovement(vehicle.getDeltaMovement().multiply(0.6, 0.6, 0.6));
            
            if (vehicle.getLastTickSpeed() > 0.4) {
                vehicle.hurt(
                    ModDamageTypes.causeVehicleStrikeDamage(
                        vehicle.level().registryAccess(), 
                        vehicle, 
                        vehicle.getFirstPassenger() == null ? vehicle : vehicle.getFirstPassenger()
                    ), 
                    6 + (float) (20 * ((vehicle.getLastTickSpeed() - 0.4) * (vehicle.getLastTickSpeed() - 0.4)))
                );
            }
        }

        Entity pilot2 = vehicle.getFirstPassenger();
        boolean hasPassenger2 = false;

        for (int i = 0; i < vehicle.getMaxPassengers() - 1; i++) {
            if (vehicle.getNthEntity(i) != null) {
                hasPassenger2 = true;
            }
        }

        float diffX3;
        float diffZ3;

        if (vehicle.getHealth() > 0.1f * vehicle.getMaxHealth()) {
            
            var landingPos2 = VehicleEngineUtils.INSTANCE.findNearestLandingPos(vehicle, 30);
            
            if (pilot2 == null) {
                vehicle.setLeftInputDown(false);
                vehicle.setRightInputDown(false);
                vehicle.setForwardInputDown(false);
                vehicle.setBackInputDown(true);
                vehicle.setUpInputDown(false);
                vehicle.setDownInputDown(false);
                
                vehicle.setZRot(vehicle.getRoll() * 0.98f);
                vehicle.setXRot(vehicle.getXRot() * 0.98f);
                
                vehicle.getDeltaMovement().multiply(0.96, 0.98, 0.96);
                
                if (hasPassenger2) {
                    vehicle.getEntityData().set(POWER, vehicle.getEntityData().get(POWER) * 0.99f);
                }
            } 
            else {
                if (!vehicle.backInputDown() || landingPos2 == null) {
                    if (vehicle.rightInputDown()) {
                        vehicle.setHoldTick(vehicle.getHoldTick() + 1);
                        vehicle.getEntityData().set(DELTA_ROT, 
                            vehicle.getEntityData().get(DELTA_ROT) - 
                            2f * Math.min(vehicle.getHoldTick(), 7) * vehicle.getEntityData().get(POWER)
                        );
                    } else if (vehicle.leftInputDown()) {
                        vehicle.setHoldTick(vehicle.getHoldTick() + 1);
                        vehicle.getEntityData().set(DELTA_ROT, 
                            vehicle.getEntityData().get(DELTA_ROT) + 
                            2f * Math.min(vehicle.getHoldTick(), 7) * vehicle.getEntityData().get(POWER)
                        );
                    } else {
                        vehicle.setHoldTick(0);
                    }
                    
                    vehicle.setXRot(vehicle.getXRot() + 
                        ((vehicle.onGround()) ? 0 : 1.5f) * pitchSpeed * 
                        vehicle.getMouseMoveSpeedY() * vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT)
                    );
                    
                    vehicle.setZRot(vehicle.getRoll() - rollSpeed * 
                        (vehicle.getEntityData().get(DELTA_ROT) + 
                        (vehicle.onGround() ? 0 : 0.25f) * vehicle.getMouseMoveSpeedX() * 
                        vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT))
                    );
                }

                vehicle.setYRot(vehicle.getYRot() + yawSpeed * Mth.clamp(
                    (vehicle.onGround() ? 0.1f : 2f) * vehicle.getMouseMoveSpeedX() * 
                    vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT) + 
                    (vehicle.getEntityData().get(SUB_ENGINE_DAMAGED) ? 25 : 0) * 
                    vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT), 
                    -10f, 10f
                ));
                
                if (landingPos2 != null && !vehicle.onGround() && vehicle.backInputDown()) {
                    VehicleEngineUtils.INSTANCE.updateAutoLanding(vehicle, landingPos2);
                }

                if (pilot2 instanceof Player player2 && vehicle.level().isClientSide && 
                    landingPos2 != null && !vehicle.onGround()) {
                    player2.displayClientMessage(Component.translatable("tips.superbwarfare.press_s_to_landing"), true);
                }

                if (vehicle.onGround()) {
                    vehicle.setZRot(vehicle.getRoll() * 0.98f);
                    vehicle.setXRot(vehicle.getXRot() * 0.98f);
                }
            }

            if (vehicle.getEnergy() < energyCost || (vehicle.getMaxEnergy() > 0 && vehicle.getEnergy() <= 0)) {
                vehicle.getEntityData().set(POWER, vehicle.getEntityData().get(POWER) * 0.995f);
                vehicle.setForwardInputDown(false);
                vehicle.setBackInputDown(false);
                vehicle.setEngineStart(false);
                vehicle.setEngineStartOver(false);
            } else {
                boolean up2 = vehicle.forwardInputDown();
                boolean down2 = vehicle.downInputDown();

                if (!vehicle.getEngineStart() && up2) {
                    vehicle.setEngineStart(true);
                    // 引擎启动音效
                    if (engineInfo.getEngineStartSound() != null) {
                        vehicle.level().playSound(null, vehicle.getX(), vehicle.getY(), vehicle.getZ(), 
                            engineInfo.getEngineStartSound(), 
                            vehicle.getSoundSource(), 3.0f, 1.0f);
                    }
                }

                if (up2 && vehicle.getEngineStartOver()) {
                    vehicle.setHoldPowerTick(vehicle.getHoldPowerTick() + 1);
                    vehicle.getEntityData().set(POWER, Math.min(
                        vehicle.getEntityData().get(POWER) + 
                        0.0007f * powerAdd * Math.min(vehicle.getHoldPowerTick(), 10), 
                        0.12f
                    ));
                }

                if (vehicle.getEngineStartOver()) {
                    if (down2) {
                        vehicle.setHoldPowerTick(vehicle.getHoldPowerTick() + 1);
                        vehicle.getEntityData().set(POWER, Math.max(
                            vehicle.getEntityData().get(POWER) - 
                            0.001f * powerReduce * Math.min(vehicle.getHoldPowerTick(), 5), 
                            vehicle.onGround() ? 0 : 0.025f / lift
                        ));
                    } else if (vehicle.backInputDown()) {
                        vehicle.setHoldPowerTick(vehicle.getHoldPowerTick() + 1);
                        vehicle.getEntityData().set(POWER, Math.max(
                            vehicle.getEntityData().get(POWER) - 
                            0.001f * powerReduce * Math.min(vehicle.getHoldPowerTick(), 5), 
                            vehicle.onGround() ? 0 : 0.058f / lift
                        ));
                    }
                }

                if (vehicle.getEngineStart() && !vehicle.getEngineStartOver()) {
                    vehicle.getEntityData().set(POWER, Math.min(
                        vehicle.getEntityData().get(POWER) + 0.0012f * powerAdd, 
                        0.045f
                    ));
                }

                if (!(up2 || down2 || vehicle.backInputDown()) && vehicle.getEngineStartOver()) {
                    if (vehicle.getDeltaMovement().y() < 0) {
                        vehicle.getEntityData().set(POWER, Math.min(
                            vehicle.getEntityData().get(POWER) + 0.0002f, 
                            0.12f
                        ));
                    } else {
                        vehicle.getEntityData().set(POWER, Math.max(
                            vehicle.getEntityData().get(POWER) - 
                            (vehicle.onGround() ? 0.00005f : 0.0002f), 
                            0
                        ));
                    }
                    vehicle.setHoldPowerTick(0);
                }
            }

            if (engineInfo.hasGear) {
                if (vehicle.upInputDown()) {
                    vehicle.setUpInputDown(false);
                    if (vehicle.getEntityData().get(SYNCHED_GEAR_ROT) < 0.1f && !vehicle.onGround()) {
                        Vec3 lookVec = vehicle.getViewVector(1);
                        double speed = 33.0 / 20.0;
                        vehicle.setDeltaMovement(lookVec.scale(speed));
                        vehicle.getEntityData().set(GEAR_UP, true);
                        vehicle.getEntityData().set(POWER, 0.2f);
                        vehicle.getEntityData().set(SYNCHED_PROPELLER_ROT, 0.2f);
                    } else if (vehicle.getEntityData().get(SYNCHED_GEAR_ROT) > 0.9f) {
                        vehicle.getEntityData().set(GEAR_UP, false);
                        vehicle.getEntityData().set(POWER, 0.04f);
                        vehicle.getEntityData().set(SYNCHED_PROPELLER_ROT, 0.04f);
                    }
                }

                if (vehicle.onGround()) {
                    vehicle.getEntityData().set(GEAR_UP, false);
                }

                if (vehicle.getEntityData().get(GEAR_UP)) {
                    vehicle.getEntityData().set(SYNCHED_GEAR_ROT, Math.min(vehicle.getEntityData().get(SYNCHED_GEAR_ROT) + 0.05f, 1));
                } else {
                    vehicle.getEntityData().set(SYNCHED_GEAR_ROT, Math.max(vehicle.getEntityData().get(SYNCHED_GEAR_ROT) - 0.05f, 0));
                }

                vehicle.setGearRot(vehicle.getEntityData().get(SYNCHED_GEAR_ROT) * gearRotateAngle);
            }
        }
        else if (!vehicle.onGround() && vehicle.getEngineStartOver()) {
            vehicle.getEntityData().set(POWER, Math.max(vehicle.getEntityData().get(POWER) - 0.0003f, 0.01f));
            vehicle.setDestroyRot(vehicle.getDestroyRot() + 0.08f);

            diffX3 = 45 - vehicle.getXRot();
            diffZ3 = -20 - vehicle.getRoll();

            vehicle.setXRot(vehicle.getXRot() + diffX3 * 0.05f * vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT));
            vehicle.setYRot(vehicle.getYRot() + vehicle.getDestroyRot());
            vehicle.setZRot(vehicle.getRoll() + diffZ3 * 0.1f * vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT));

            vehicle.setDeltaMovement(vehicle.getDeltaMovement().add(0, -vehicle.getDestroyRot() * 0.004, 0));
        }

        if (vehicle.getEntityData().get(MAIN_ENGINE_DAMAGED)) {
            vehicle.getEntityData().set(POWER, vehicle.getEntityData().get(POWER) * 0.98f);
        }

        vehicle.getEntityData().set(DELTA_ROT, vehicle.getEntityData().get(DELTA_ROT) * 0.9f);

        vehicle.getEntityData().set(SYNCHED_PROPELLER_ROT, 
            Mth.lerp(0.18f, vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT), vehicle.getEntityData().get(POWER))
        );

        vehicle.setPropellerRot(vehicle.getPropellerRot() + 30 * vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT));

        vehicle.getEntityData().set(SYNCHED_PROPELLER_ROT, vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT) * 0.9995f);

        if (vehicle.getEngineStart()) {
            vehicle.consumeEnergy((int) (energyCost * 8.3333f * Mth.abs(vehicle.getEntityData().get(POWER))));
        }

        Vec3 force2 = vehicle.getUpVec(1);

        vehicle.setDeltaMovement(vehicle.getDeltaMovement().add(
            force2.scale(vehicle.getEntityData().get(SYNCHED_PROPELLER_ROT) * lift)
        ));

        if (vehicle.getEntityData().get(POWER) > 0.04f) {
            vehicle.setEngineStartOver(true);
        }

        if (vehicle.getEntityData().get(POWER) < 0.0004f) {
            vehicle.setEngineStart(false);
            vehicle.setEngineStartOver(false);
        }
    }
}
}
