package com.cmhh.halovecs.entity.vehicle;


import org.jetbrains.annotations.NotNull;

import com.atsuishio.superbwarfare.data.vehicle.subdata.EngineType;
import com.atsuishio.superbwarfare.entity.vehicle.base.GeoVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.damage.DamageModifier;
import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.engine.VTOLEngine;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;



public class T77hEntity extends GeoVehicleEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    
    // 本地跟踪引擎运行状态，用于触发引擎声音
    // 父类 VehicleEntity 的 wasEngineRunning 是 private 字段，无法直接访问
    private boolean localWasEngineRunning = false;

    // 纹理变体常量定义
    public static final int VARIANT_DEFAULT = 0;  // 默认纹理
    public static final int VARIANT_CITY = 1;     // 城市纹理
    public static final int VARIANT_DUST = 2;     // 沙漠纹理  
    public static final int VARIANT_SNOW = 3;     // 雪地纹理
    public static final int VARIANT_COMP = 4;     // 迷彩纹理

    // 实体数据访问器
    public static final EntityDataAccessor<Integer> TEXTURE_VARIANT = SynchedEntityData.defineId(T77hEntity.class, EntityDataSerializers.INT);

    public T77hEntity(EntityType<T77hEntity> type, Level world) {
        super(type, world);
        Halovecs.LOGGER.info("[T77hEntity] Constructor called");
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TEXTURE_VARIANT, VARIANT_DEFAULT); // 默认纹理
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("TextureVariant", this.entityData.get(TEXTURE_VARIANT));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(TEXTURE_VARIANT, compound.getInt("TextureVariant"));
    }

    @Override
    public @NotNull InteractionResult interact(Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        // 绿色染料 -> 默认纹理（与当前纹理不同时才切换）
        if (stack.getItem() == Items.GREEN_DYE && this.entityData.get(TEXTURE_VARIANT) != VARIANT_DEFAULT) {
            this.entityData.set(TEXTURE_VARIANT, VARIANT_DEFAULT);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            this.level().playSound(null, this, SoundEvents.BONE_MEAL_USE, this.getSoundSource(), 2, 1);
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        
        // 灰色染料 -> 城市纹理（与当前纹理不同时才切换）
        if (stack.getItem() == Items.GRAY_DYE && this.entityData.get(TEXTURE_VARIANT) != VARIANT_CITY) {
            this.entityData.set(TEXTURE_VARIANT, VARIANT_CITY);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            this.level().playSound(null, this, SoundEvents.BONE_MEAL_USE, this.getSoundSource(), 2, 1);
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        
        // 黄色染料 -> 沙漠纹理（与当前纹理不同时才切换）
        if (stack.getItem() == Items.YELLOW_DYE && this.entityData.get(TEXTURE_VARIANT) != VARIANT_DUST) {
            this.entityData.set(TEXTURE_VARIANT, VARIANT_DUST);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            this.level().playSound(null, this, SoundEvents.BONE_MEAL_USE, this.getSoundSource(), 2, 1);
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        
        // 白色染料 -> 雪地纹理（与当前纹理不同时才切换）
        if (stack.getItem() == Items.WHITE_DYE && this.entityData.get(TEXTURE_VARIANT) != VARIANT_SNOW) {
            this.entityData.set(TEXTURE_VARIANT, VARIANT_SNOW);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            this.level().playSound(null, this, SoundEvents.BONE_MEAL_USE, this.getSoundSource(), 2, 1);
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        
        // 橙色染料 -> 迷彩纹理（与当前纹理不同时才切换）
        if (stack.getItem() == Items.ORANGE_DYE && this.entityData.get(TEXTURE_VARIANT) != VARIANT_COMP) {
            this.entityData.set(TEXTURE_VARIANT, VARIANT_COMP);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            this.level().playSound(null, this, SoundEvents.BONE_MEAL_USE, this.getSoundSource(), 2, 1);
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        
        return super.interact(player, hand);
    }

    // 添加获取当前纹理变体的方法（供模型使用）
    public int getTextureVariant() {
        return this.entityData.get(TEXTURE_VARIANT);
    }

    @Override
    public DamageModifier getDamageModifier() {
        return super.getDamageModifier()
                .custom((source, damage) -> getSourceAngle(source, 0.25f) * damage);
    }

    private PlayState maingunFirePredicate(AnimationState<T77hEntity> event) {
        if (getShootAnimationTimer(2, 0) > 0) {
            // 开火时循环播放 fire（重复闪光效果）
            return event.setAndContinue(RawAnimation.begin().thenLoop("fire"));
        }

        // 不开火时循环播放 fire_idle（flare 缩放为 0）
        return event.setAndContinue(RawAnimation.begin().thenLoop("fire_idle"));
    }

    private boolean gearWasUp = true;  // 记录上一次完全状态：true=收起, false=放下

    private PlayState gearPredicate(AnimationState<T77hEntity> event) {
        float gearRot = this.entityData.get(SYNCHED_GEAR_ROT);

        if (gearRot < 0.1f) {
            // 起落架完全收起
            gearWasUp = true;
            return event.setAndContinue(RawAnimation.begin().thenLoop("idle2"));
        } else if (gearRot > 0.9f) {
            // 起落架完全放下
            gearWasUp = false;
            return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }

        // 在过渡区间，根据上一次的完全状态决定方向
        if (gearWasUp) {
            // 之前是收起状态，现在正在放下 → gearing（0→目标角度）
            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("gearing"));
        } else {
            // 之前是放下状态，现在正在收起 → gearing2（目标角度→0）
            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("gearing2"));
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "MainGun", 0, this::maingunFirePredicate));
        data.add(new AnimationController<>(this, "Gear", 0, this::gearPredicate));
    }

    /**
     * 完全重写 travel() 方法，使用 VTOLEngine 替代默认的 Helicopter 引擎。
     * <p>
     * 注意：不能调用 super.travel()，因为父类会缓存 engineCache（私有字段），
     * 首次调用时用 EngineInfo$Helicopter.class 反序列化 engineInfo，
     * 后续调用直接使用缓存的 Helicopter 实例，永远不会创建 VTOLEngine。
     * <p>
     * 这里完全接管 travel() 逻辑，手动创建 VTOLEngine 实例并调用其 work() 方法。
     * <p>
     * 注意：父类 travel() 中会在引擎启动时调用 playEngineSound.accept(this) 来播放引擎声音，
     * 由于我们跳过了 super.travel()，需要在这里手动触发引擎声音。
     */
    @Override
    public void travel() {
        var data = computed();
        Halovecs.LOGGER.info("[T77hEntity] travel() called, engineType={}, engineInfo={}", data.getEngineType(), data.getEngineInfo() != null ? "present" : "null");
        if (data.getEngineType() == EngineType.HELICOPTER && data.getEngineInfo() != null) {
            try {
                Halovecs.LOGGER.info("[T77hEntity] Deserializing VTOLEngine from engineInfo (JsonObject)...");
                
                // 0.8.9: engineInfo 是 SerializedGsonObject (JsonObject)，使用 fromJson(JsonObject, Class)
                VTOLEngine vtolEngine = VTOLEngine.fromJson(data.getEngineInfo());
                
                Halovecs.LOGGER.info("[T77hEntity] Deserialization result: {}", vtolEngine != null ? "success" : "null");
                if (vtolEngine != null) {
                    Halovecs.LOGGER.info("[T77hEntity] VTOLEngine fields: energyCostRate={}, increment={}, speedRate={}, hasGear={}", 
                            vtolEngine.getEnergyCostRate(), vtolEngine.getIncrement(), vtolEngine.speedRate, vtolEngine.hasGear);
                    Halovecs.LOGGER.info("[T77hEntity] Calling VTOLEngine.work()...");
                    vtolEngine.work(this);
                    Halovecs.LOGGER.info("[T77hEntity] VTOLEngine.work() completed");
                    
                    // 手动触发引擎声音：当引擎从关闭状态变为运行状态时，播放引擎声音
                    // 父类 travel() 中会在 !wasEngineRunning && engineRunning() 时调用 playEngineSound.accept(this)
                    // 由于我们跳过了 super.travel()，需要在这里手动处理
                    if (!this.localWasEngineRunning && this.engineRunning()) {
                        if (playEngineSound != null) {
                            playEngineSound.accept(this);
                        }
                    }
                    this.localWasEngineRunning = this.engineRunning();
                    
                    return;
                }
            } catch (Exception e) {
                Halovecs.LOGGER.error("[T77hEntity] Exception during VTOLEngine deserialization/work: {}", e.getMessage(), e);
            }
        }
        Halovecs.LOGGER.info("[T77hEntity] Falling back to super.travel()");
        super.travel();
    }

    /**
     * 重写 getEngineSoundVolume() 以修复引擎声音音量问题。
     * <p>
     * 父类的 getEngineSoundVolume() 依赖于私有字段 engineCache，
     * 该字段只在父类 travel() 中初始化。由于本类重写了 travel()
     * 且未调用 super.travel()，engineCache 始终为 null，导致音量返回 0。
     * <p>
     * 这里直接根据当前 POWER 和配置的 EngineSoundVolume 计算音量，
     * 不影响任何引擎逻辑。
     */
    @Override
    public float getEngineSoundVolume() {
        var data = this.computed();
        if (data.getEngineInfo() != null && data.getEngineInfo().has("EngineSoundVolume")) {
            return Math.abs(this.entityData.get(POWER)) * data.getEngineInfo().get("EngineSoundVolume").getAsFloat();
        }
        return Math.abs(this.entityData.get(POWER)) * 0.8f;
    }
}
