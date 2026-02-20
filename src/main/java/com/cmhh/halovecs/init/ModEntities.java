package com.cmhh.halovecs.init;

import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.entity.vehicle.M12Entity;
import com.cmhh.halovecs.entity.vehicle.M12gauEntity;
import com.cmhh.halovecs.entity.vehicle.M12hmgEntity;
import com.cmhh.halovecs.entity.vehicle.M12rocEntity;
import com.cmhh.halovecs.entity.vehicle.M12traEntity;
import com.cmhh.halovecs.entity.vehicle.M274Entity;
import com.cmhh.halovecs.entity.vehicle.M274mEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = 
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Halovecs.MODID);

    public static final RegistryObject<EntityType<M12Entity>> M12 = 
        ENTITY_TYPES.register("m12", () -> EntityType.Builder.of(M12Entity::new, MobCategory.MISC)
                .setTrackingRange(512)
                .setUpdateInterval(1)
                .fireImmune()
                .sized(2.5f, 2.0f)
                .build(Halovecs.MODID + ":m12"));

    public static final RegistryObject<EntityType<M12hmgEntity>> M12HMG = 
        ENTITY_TYPES.register("m12hmg", () -> EntityType.Builder.of(M12hmgEntity::new, MobCategory.MISC)
                .setTrackingRange(512)
                .setUpdateInterval(1)
                .fireImmune()
                .sized(2.5f, 2.0f)
                .build(Halovecs.MODID + ":m12hmg"));

    public static final RegistryObject<EntityType<M12rocEntity>> M12ROC = 
        ENTITY_TYPES.register("m12roc", () -> EntityType.Builder.of(M12rocEntity::new, MobCategory.MISC)
                .setTrackingRange(512)
                .setUpdateInterval(1)
                .fireImmune()
                .sized(2.5f, 2.0f)
                .build(Halovecs.MODID + ":m12roc"));

    public static final RegistryObject<EntityType<M12gauEntity>> M12GAU = 
        ENTITY_TYPES.register("m12gau", () -> EntityType.Builder.of(M12gauEntity::new, MobCategory.MISC)
                .setTrackingRange(512)
                .setUpdateInterval(1)
                .fireImmune()
                .sized(2.5f, 2.0f)
                .build(Halovecs.MODID + ":m12gau"));

    public static final RegistryObject<EntityType<M12traEntity>> M12TRA = 
        ENTITY_TYPES.register("m12tra", () -> EntityType.Builder.of(M12traEntity::new, MobCategory.MISC)
                .setTrackingRange(512)
                .setUpdateInterval(1)
                .fireImmune()
                .sized(2.5f, 2.0f)
                .build(Halovecs.MODID + ":m12tra"));

    public static final RegistryObject<EntityType<M274Entity>> M274 = 
        ENTITY_TYPES.register("m274", () -> EntityType.Builder.of(M274Entity::new, MobCategory.MISC)
                .setTrackingRange(512)
                .setUpdateInterval(1)
                .fireImmune()
                .sized(1.5f, 1.5f)
                .build(Halovecs.MODID + ":m274"));

    public static final RegistryObject<EntityType<M274mEntity>> M274M = 
        ENTITY_TYPES.register("m274m", () -> EntityType.Builder.of(M274mEntity::new, MobCategory.MISC)
                .setTrackingRange(512)
                .setUpdateInterval(1)
                .fireImmune()
                .sized(1.5f, 1.5f)
                .build(Halovecs.MODID + ":m274m"));
}
