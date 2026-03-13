package com.cmhh.halovecs.entity.vehicle;

import com.atsuishio.superbwarfare.entity.vehicle.base.GeoVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.damage.DamageModifier;
import com.cmhh.halovecs.entity.vehicle.utils.M274VehicleEngineUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;



public class M274Entity extends GeoVehicleEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final float CUSTOM_RUDDER_LIMIT = 2f; // 原值0.8f

    // 纹理变体常量定义
    public static final int VARIANT_DEFAULT = 0;  // 默认纹理
    public static final int VARIANT_CITY = 1;     // 城市纹理
    public static final int VARIANT_DUST = 2;     // 沙漠纹理  
    public static final int VARIANT_SNOW = 3;     // 雪地纹理
    public static final int VARIANT_COMP = 4;     // 迷彩纹理

    // 实体数据访问器（完全仿照TruckEntity的模式）
    public static final EntityDataAccessor<Integer> TEXTURE_VARIANT = SynchedEntityData.defineId(M274Entity.class, EntityDataSerializers.INT);


    public M274Entity(EntityType<M274Entity> type, Level world) {
        super(type, world);
    }

    // 添加同步数据定义（完全仿照TruckEntity的defineSynchedData方法）
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TEXTURE_VARIANT, VARIANT_DEFAULT); // 默认纹理
    }

    // 添加NBT数据保存/读取（完全仿照TruckEntity的addAdditionalSaveData/readAdditionalSaveData方法）
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

    // 添加交互逻辑（完全仿照TruckEntity的interact方法模式）
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

    @Override
    public void setRudderRot(float rudderRot) {
        // 计算新的方向舵角度（复制SuperbWarfare逻辑）
        float newRudderRot = rudderRot - this.getEntityData().get(DELTA_ROT);
        
        // 关键修改：使用自定义的限制值
        // 注意：Mth.clamp参数顺序是(value, min, max)
        float clampedRudder = net.minecraft.util.Mth.clamp(
            newRudderRot, 
            -CUSTOM_RUDDER_LIMIT,  // 最小值（左转限制）
            CUSTOM_RUDDER_LIMIT     // 最大值（右转限制）
        );
        
        // 保持原版缩放因子0.75f
        float scaledRudder = clampedRudder * 0.75f;
        
        // 调用父类方法
        super.setRudderRot(scaledRudder);
    }

    
    
}
