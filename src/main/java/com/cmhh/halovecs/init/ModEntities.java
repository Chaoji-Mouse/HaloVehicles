package com.cmhh.halovecs.init;

import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.entity.vehicle.M12Entity;
import com.cmhh.halovecs.entity.vehicle.M12hmgEntity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {

    // 创建实体注册器
    public static final DeferredRegister<EntityType<?>> REGISTRY = 
        DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Halovecs.MODID);

    // 注册 M12 运输载具实体
    public static final DeferredHolder<EntityType<?>, EntityType<M12Entity>> M12 = 
        register("m12",
            EntityType.Builder.of(M12Entity::new, MobCategory.MISC)
                .setTrackingRange(512)      // 追踪范围（区块）
                .setUpdateInterval(1)       // 更新间隔（tick）
                .fireImmune()               // 防火
                .sized(2.6f, 2.0f)          // 碰撞箱大小（宽度, 高度）
        );
    
    // 注册 M12hmg 运输载具实体
    public static final DeferredHolder<EntityType<?>, EntityType<M12hmgEntity>> M12hmg = 
        register("m12hmg",
            EntityType.Builder.of(M12hmgEntity::new, MobCategory.MISC)
                .setTrackingRange(512)      // 追踪范围（区块）
                .setUpdateInterval(1)       // 更新间隔（tick）
                .fireImmune()               // 防火
                .sized(2.6f, 2.0f)          // 碰撞箱大小（宽度, 高度）
        );

    // 辅助注册方法
    private static <T extends net.minecraft.world.entity.Entity> 
    DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> entityTypeBuilder) {
        return REGISTRY.register(name, () -> entityTypeBuilder.build(name));
    }
}