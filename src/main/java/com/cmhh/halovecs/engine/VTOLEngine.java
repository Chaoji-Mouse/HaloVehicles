package com.cmhh.halovecs.engine;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

/**
 * VTOLEngine - 垂直起降引擎
 * <p>
 * 这是完全独立的 POJO 类，不继承自 EngineInfo 体系（EngineInfo 在 0.8.9 中改为 kotlinx.serialization）。
 * 通过 Gson 从 JsonObject 反序列化 JSON 数据，支持 vehicle JSON 配置中的所有字段。
 * <p>
 * 调用 work() 方法将执行 VTOL 物理逻辑。
 */
public class VTOLEngine {

    private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();

    // 独立 Gson 实例，不使用 DataLoader.GSON 以避免自定义 TypeAdapterFactory 导致的字段名冲突
    private static final Gson GSON = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
        .create();

    public VTOLEngine() {
        LOGGER.info("VTOLEngine constructor called - instance created!");
    }

    // ============================================================
    // 引擎基础参数（原 EngineInfo 基类）
    // ============================================================

    @SerializedName("EnergyCostRate")
    public double energyCostRate = 1.0;

    @SerializedName("Increment")
    public float increment = 0.001f;

    @SerializedName("Decrement")
    public float decrement = 0.001f;

    @SerializedName("EngineSoundVolume")
    public float engineSoundVolume = 0.4f;

    // ============================================================
    // 直升机引擎参数（原 EngineInfo.Helicopter）
    // ============================================================

    @SerializedName("PitchSpeed")
    public float pitchSpeed = 1f;

    @SerializedName("YawSpeed")
    public float yawSpeed = 1f;

    @SerializedName("RollSpeed")
    public float rollSpeed = 1f;

    @SerializedName("LiftSpeed")
    public float liftSpeed = 1f;

    @SerializedName("Speed")
    public float speed = 1f;

    // 引擎启动音效（存储为字符串，使用时解析为 SoundEvent）
    @SerializedName("EngineStartSound")
    public String engineStartSound = "";

    // ============================================================
    // VTOLEngine 特有字段（原 VTOLEngine 直接添加的字段）
    // ============================================================

    @SerializedName("SpeedRate")
    public float speedRate = 1;

    @SerializedName("GearRotateAngle")
    public float gearRotateAngle = 85;

    @SerializedName("HasGear")
    public boolean hasGear = true;

    // ============================================================
    // Getter 方法（供 VehicleEngineUtilsExtension 使用）
    // ============================================================

    public double getEnergyCostRate() { return energyCostRate; }
    public float getIncrement() { return increment; }
    public float getDecrement() { return decrement; }
    public float getPitchSpeed() { return pitchSpeed; }
    public float getYawSpeed() { return yawSpeed; }
    public float getRollSpeed() { return rollSpeed; }
    public float getLiftSpeed() { return liftSpeed; }
    public float getSpeed() { return speed; }
    public SoundEvent getEngineStartSound() {
        if (engineStartSound == null || engineStartSound.isEmpty()) return null;
        ResourceLocation loc = ResourceLocation.tryParse(engineStartSound);
        if (loc != null && BuiltInRegistries.SOUND_EVENT.containsKey(loc)) {
            return BuiltInRegistries.SOUND_EVENT.get(loc);
        }
        return null;
    }

    // ============================================================
    // 工厂方法：从 JsonObject 反序列化
    // ============================================================

    /**
     * 从 vehicle JSON 配置的 EngineInfo 部分反序列化 VTOLEngine。
     * 因为 0.8.9 的 engineInfo 是 SerializedGsonObject (JsonObject) 类型，
     * 不能用 DataLoader.GSON.fromJson(String, Class) 的方式直接反序列化，
     * 需要用 fromJson(JsonElement, Class) 的方式。
     */
    public static VTOLEngine fromJson(JsonObject json) {
        if (json == null) return null;
        try {
            return GSON.fromJson(json, VTOLEngine.class);
        } catch (Exception e) {
            LOGGER.error("Failed to deserialize VTOLEngine from JsonObject: {}", e.getMessage(), e);
            return null;
        }
    }

    // ============================================================
    // work() 方法
    // ============================================================

    public void work(VehicleEntity vehicle) {
        LOGGER.info("=== [VTOLEngine] work() called! vehicle: {}, energyCostRate={}, speedRate={}, hasGear={}, onGround={}",
                vehicle.getDisplayName().getString(), energyCostRate, speedRate, hasGear, vehicle.onGround());

        VehicleEngineUtilsExtension.vtolEngine(vehicle, this);
        
        LOGGER.info("=== [VTOLEngine] work() completed for vehicle: {}", vehicle.getDisplayName().getString());
    }
}
