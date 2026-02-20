# HaloVehicles-Forge-1.20.1 项目规则

## 项目概述

这是一个基于 SuperbWarfare 模组的 Halo 载具扩展模组，使用 Forge 1.20.1 和 GeckoLib 4.x。

## 开发工作流程

### 标准构建流程

```powershell
# 1. 构建模组
.\gradlew.bat build --no-daemon

# 2. 复制到测试目录
Copy-Item "build\libs\halovecs-1.0.0.jar" "D:\mc\.minecraft\versions\1.20.1-Forge_47.4.16\mods\" -Force

# 3. 启动游戏测试
# 4. 查看日志排查问题
```

### 文件路径

| 类型 | 路径 |
|------|------|
| 构建输出 | `build/libs/halovecs-1.0.0.jar` |
| 测试目录 | `D:\mc\.minecraft\versions\1.20.1-Forge_47.4.16\mods\` |
| 日志文件 | `D:\mc\.minecraft\versions\1.20.1-Forge_47.4.16\logs\latest.log` |

---

## 添加新载具完整步骤

### 1. 创建实体类

**路径**: `src/main/java/com/cmhh/halovecs/entity/vehicle/{Name}Entity.java`

```java
package com.cmhh.halovecs.entity.vehicle;

import com.atsuishio.superbwarfare.entity.vehicle.base.GeoVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.damage.DamageModifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class M274Entity extends GeoVehicleEntity {

    public M274Entity(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    public DamageModifier getDamageModifier() {
        return super.getDamageModifier()
                .custom((source, damage) -> getSourceAngle(source, 0.25f) * damage);
    }
}
```

### 2. 创建模型类

**路径**: `src/main/java/com/cmhh/halovecs/client/model/entity/{Name}Model.java`

```java
package com.cmhh.halovecs.client.model.entity;

import com.atsuishio.superbwarfare.client.model.entity.VehicleModel;
import com.cmhh.halovecs.Halovecs;
import net.minecraft.resources.ResourceLocation;

public class M274Model extends VehicleModel<M274Entity> {

    @Override
    public ResourceLocation getModelResource(M274Entity animatable) {
        return new ResourceLocation(Halovecs.MODID, "geo/m274.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(M274Entity animatable) {
        return new ResourceLocation(Halovecs.MODID, "textures/entity/m274.png");
    }

    @Override
    public ResourceLocation getAnimationResource(M274Entity animatable) {
        return null;  // 无动画文件时返回 null
    }
}
```

### 3. 创建渲染器类

**路径**: `src/main/java/com/cmhh/halovecs/client/renderer/entity/{Name}Renderer.java`

```java
package com.cmhh.halovecs.client.renderer.entity;

import com.atsuishio.superbwarfare.client.renderer.entity.VehicleRenderer;
import com.cmhh.halovecs.client.model.entity.M274Model;
import com.cmhh.halovecs.entity.vehicle.M274Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class M274Renderer extends VehicleRenderer<M274Entity> {

    public M274Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new M274Model());
        this.shadowRadius = 0.8f;
    }
}
```

### 4. 注册实体

**文件**: `src/main/java/com/cmhh/halovecs/init/ModEntities.java`

```java
// 添加实体注册
public static final RegistryObject<EntityType<M274Entity>> M274 = ENTITIES.register("m274",
    () -> EntityType.Builder.<M274Entity>of(M274Entity::new, MobCategory.MISC)
        .sized(2.0f, 1.5f)  // 根据模型调整碰撞箱
        .build("m274"));

public static final RegistryObject<EntityType<M274mEntity>> M274M = ENTITIES.register("m274m",
    () -> EntityType.Builder.<M274mEntity>of(M274mEntity::new, MobCategory.MISC)
        .sized(2.0f, 1.5f)
        .build("m274m"));
```

### 5. 注册渲染器 ⚠️ 必须步骤

**文件**: `src/main/java/com/cmhh/halovecs/client/ModEntityRenderers.java`

```java
// 添加导入
import com.cmhh.halovecs.client.renderer.entity.M274Renderer;
import com.cmhh.halovecs.client.renderer.entity.M274mRenderer;

// 在 registerEntityRenderers 方法中添加
event.registerEntityRenderer(ModEntities.M274.get(), M274Renderer::new);
event.registerEntityRenderer(ModEntities.M274M.get(), M274mRenderer::new);
```

### 6. 添加创造模式标签

**文件**: `src/main/java/com/cmhh/halovecs/init/ModCreativeTabs.java`

```java
// 在 tab 方法中添加
.output(ModItems.M274.get())
.output(ModItems.M274M.get())
```

### 7. 添加资源文件

| 类型 | 路径 |
|------|------|
| 模型文件 | `assets/halovecs/geo/{name}.geo.json` |
| 材质文件 | `assets/halovecs/textures/entity/{name}.png` |
| 图标文件 | `assets/halovecs/textures/vehicle_icon/{name}_icon.png` |
| 动画文件 | `assets/halovecs/animations/{name}.animation.json` (可选) |
| 数据文件 | `data/halovecs/sbw/vehicles/{name}.json` |
| 配方文件 | `data/halovecs/recipe/{name}.json` |

### 8. 添加语言文件

**文件**: `assets/halovecs/lang/en_us.json`
```json
"item.halovecs.m274": "M274 Mongoose",
"item.halovecs.m274m": "M274M Mongoose"
```

**文件**: `assets/halovecs/lang/zh_cn.json`
```json
"item.halovecs.m274": "M274 猫鼬号",
"item.halovecs.m274m": "M274M 榴弹猫鼬号"
```

---

## ⚠️ 重要注意事项

### GeckoLib 动画规则

| 情况 | 模型 getAnimationResource() | 实体类 |
|------|---------------------------|--------|
| **无动画文件** | 返回 `null` | **不要**添加 `registerControllers` 方法 |
| **有动画文件** | 返回动画资源路径 | 需要添加 `registerControllers` 方法 |

**错误示例** - 会导致崩溃：
```java
// 模型返回 null
@Override
public ResourceLocation getAnimationResource(M274Entity animatable) {
    return null;
}

// 但实体类尝试播放动画 -> 崩溃！
@Override
public void registerControllers(AnimatableManager.ControllerRegistrar data) {
    data.add(new AnimationController<>(this, "MainGun", 0, this::predicate));
}
```

### 渲染器注册检查清单

- [ ] 实体类已创建
- [ ] 模型类已创建
- [ ] 渲染器类已创建
- [ ] **ModEntities.java 已注册实体**
- [ ] **ModEntityRenderers.java 已注册渲染器** ⚠️ 容易遗漏
- [ ] ModCreativeTabs.java 已添加物品
- [ ] 资源文件已复制
- [ ] 语言文件已更新

### 常见崩溃原因

| 错误信息 | 原因 | 解决方案 |
|---------|------|---------|
| `"entityrenderer" is null` | 渲染器未注册 | 在 `ModEntityRenderers.java` 添加注册 |
| `"location" is null` in `getAnimation` | 动画控制器调用不存在的动画 | 移除 `registerControllers` 或添加动画文件 |
| 找不到模型/材质 | 资源文件路径错误 | 检查 `geo/` 和 `textures/entity/` 目录 |

---

## 调试流程

### 1. 查看崩溃日志

```powershell
# 查看最近的错误
Get-Content "D:\mc\.minecraft\versions\1.20.1-Forge_47.4.16\logs\latest.log" | Select-String "ERROR|Exception|FATAL" -Context 2,5
```

### 2. 检查 JAR 文件时间戳

```powershell
Get-Item "D:\mc\.minecraft\versions\1.20.1-Forge_47.4.16\mods\halovecs-1.0.0.jar" | Select-Object LastWriteTime
```

### 3. 验证构建是否成功

```powershell
.\gradlew.bat build --no-daemon
# 检查输出是否有 "BUILD SUCCESSFUL"
```

---

## 依赖模组

测试环境需要以下依赖模组：
- SuperbWarfare 0.8.8
- GeckoLib 4.x (forge-1.20.1-4.8.3)
- Curios API
- Patchouli (可选)
- Cloth Config

---

## 版本差异说明

从 1.21.1 (NeoForge) 迁移到 1.20.1 (Forge) 时注意：

| 项目 | 1.20.1 (Forge) | 1.21.1 (NeoForge) |
|------|----------------|-------------------|
| ResourceLocation | `new ResourceLocation(modid, path)` | `ResourceLocation.fromNamespaceAndPath(modid, path)` |
| GeckoLib 包路径 | `software.bernie.geckolib.core.animation.*` | `software.bernie.geckolib.animation.*` |
| AnimatableInstanceCache | 不需要手动创建 | 需要手动创建 |
