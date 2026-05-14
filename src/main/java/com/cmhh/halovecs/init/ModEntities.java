package com.cmhh.halovecs.init;

import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.entity.vehicle.M12Entity;
import com.cmhh.halovecs.entity.vehicle.M12gauEntity;
import com.cmhh.halovecs.entity.vehicle.M12hmgEntity;
import com.cmhh.halovecs.entity.vehicle.M12rocEntity;
import com.cmhh.halovecs.entity.vehicle.M12traEntity;
import com.cmhh.halovecs.entity.vehicle.M274Entity;
import com.cmhh.halovecs.entity.vehicle.M274mEntity;
import com.cmhh.halovecs.entity.vehicle.M808Entity;
import com.cmhh.halovecs.entity.vehicle.T77hEntity;

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
                .sized(2.5f, 2f)          // 碰撞箱大小（宽度, 高度）
        );
    
    // 注册 M12hmg 运输载具实体
    public static final DeferredHolder<EntityType<?>, EntityType<M12hmgEntity>> M12hmg = 
        register("m12hmg",
            EntityType.Builder.of(M12hmgEntity::new, MobCategory.MISC)
                .setTrackingRange(512)      // 追踪范围（区块）
                .setUpdateInterval(1)       // 更新间隔（tick）
                .fireImmune()               // 防火
                .sized(2.5f, 2f)          // 碰撞箱大小（宽度, 高度）
        );
    
    // 注册 M12roc 运输载具实体
    public static final DeferredHolder<EntityType<?>, EntityType<M12rocEntity>> M12roc = 
        register("m12roc",
            EntityType.Builder.of(M12rocEntity::new, MobCategory.MISC)
                .setTrackingRange(512)      // 追踪范围（区块）
                .setUpdateInterval(1)       // 更新间隔（tick）
                .fireImmune()               // 防火
                .sized(2.5f, 2f)          // 碰撞箱大小（宽度, 高度）
        );
    
    // 注册 M12gau 运输载具实体
    public static final DeferredHolder<EntityType<?>, EntityType<M12gauEntity>> M12gau = 
        register("m12gau",
            EntityType.Builder.of(M12gauEntity::new, MobCategory.MISC)
                .setTrackingRange(512)      // 追踪范围（区块）
                .setUpdateInterval(1)       // 更新间隔（tick）
                .fireImmune()               // 防火
                .sized(2.5f, 2f)          // 碰撞箱大小（宽度, 高度）
        );
    
    // 注册 M12tra 运输载具实体
    public static final DeferredHolder<EntityType<?>, EntityType<M12traEntity>> M12tra = 
        register("m12tra",
            EntityType.Builder.of(M12traEntity::new, MobCategory.MISC)
                .setTrackingRange(512)      // 追踪范围（区块）
                .setUpdateInterval(1)       // 更新间隔（tick）
                .fireImmune()               // 防火
                .sized(2.5f, 2f)          // 碰撞箱大小（宽度, 高度）
        );
    
    // 注册 M274 运输载具实体
    public static final DeferredHolder<EntityType<?>, EntityType<M274Entity>> M274 = 
        register("m274",
            EntityType.Builder.of(M274Entity::new, MobCategory.MISC)
                .setTrackingRange(512)      // 追踪范围（区块）
                .setUpdateInterval(1)       // 更新间隔（tick）
                .fireImmune()               // 防火
                .sized(1.5f, 1f)          // 碰撞箱大小（宽度, 高度）
        );
    
    // 注册 M274m 运输载具实体
    public static final DeferredHolder<EntityType<?>, EntityType<M274mEntity>> M274m = 
        register("m274m",
            EntityType.Builder.of(M274mEntity::new, MobCategory.MISC)
                .setTrackingRange(512)      // 追踪范围（区块）
                .setUpdateInterval(1)       // 更新间隔（tick）
                .fireImmune()               // 防火
                .sized(1.5f, 1f)          // 碰撞箱大小（宽度, 高度）
        );
    
    // 注册 M808 运输载具实体
    public static final DeferredHolder<EntityType<?>, EntityType<M808Entity>> M808 = 
        register("m808",
            EntityType.Builder.of(M808Entity::new, MobCategory.MISC)
                .setTrackingRange(512)      // 追踪范围（区块）
                .setUpdateInterval(1)       // 更新间隔（tick）
                .fireImmune()               // 防火
                .sized(7f, 3.5f)          // 碰撞箱大小（宽度, 高度）
        );
    
    // 注册 T77h 运输载具实体
    public static final DeferredHolder<EntityType<?>, EntityType<T77hEntity>> T77H = 
        register("t77h",
            EntityType.Builder.of(T77hEntity::new, MobCategory.MISC)
                .setTrackingRange(512)      // 追踪范围（区块）
                .setUpdateInterval(1)       // 更新间隔（tick）
                .fireImmune()               // 防火
                .sized(8f, 8f)          // 碰撞箱大小（宽度, 高度）
        );

    // 辅助注册方法
    private static <T extends net.minecraft.world.entity.Entity> 
    DeferredHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> entityTypeBuilder) {
        return REGISTRY.register(name, () -> entityTypeBuilder.build(name));
    }
}
