# Halovecs 构建说明

## 项目概述
Halovecs 是一个 SuperbWarfare 的附属模组，添加了 M12 车辆实体。

## 构建要求
1. Java 21 JDK
2. Gradle (已包含在项目中)
3. **SuperbWarfare JAR 文件** (必需)

## 解决依赖问题

### 问题分析
Halovecs 依赖 SuperbWarfare 的 API，但 SuperbWarfare 只是参考源码，不是项目的一部分。

### 解决方案

#### 方案1：获取 SuperbWarfare JAR (推荐)
1. 从官方渠道下载 SuperbWarfare 模组 JAR 文件
2. 将 JAR 文件复制到 `halovecs/libs/` 目录
3. 重命名为 `superbwarfare-1.21.1.jar`
4. 取消 `build.gradle.kts` 中的注释：
   ```kotlin
   implementation(files("libs/superbwarfare-1.21.1.jar"))
   ```

#### 方案2：构建 SuperbWarfare 项目
1. 进入 SuperbWarfare 项目目录
2. 运行构建命令：
   ```bash
   cd ../SuperbWarfare-0.8.8-1.21
   .\gradlew.bat build
   ```
3. 将生成的 JAR 文件复制到 `halovecs/libs/`

#### 方案3：使用测试实体 (开发阶段)
项目已包含一个测试实体 `TestEntity`，不依赖 SuperbWarfare，可用于验证项目结构。

## 构建步骤

### 1. 清理项目
```bash
.\gradlew.bat clean
```

### 2. 生成开发环境
```bash
.\gradlew.bat genIntellijRuns
.\gradlew.bat genEclipseRuns
```

### 3. 构建项目
```bash
.\gradlew.bat build
```

### 4. 运行测试客户端
```bash
.\gradlew.bat runClient
```

## 项目结构说明

### 已修复的问题
1. **settings.gradle.kts** - 移除了对 SuperbWarfare 的错误包含
2. **build.gradle.kts** - 修正了依赖配置，添加了清晰的说明
3. **实体类** - 优化了代码结构

### 文件结构
```
halovecs/
├── src/main/java/com/cmhh/halovecs/
│   ├── Halovecs.java                    # 主类
│   ├── entity/
│   │   ├── vehicle/M12Entity.java       # M12 车辆实体 (需要 SuperbWarfare)
│   │   └── TestEntity.java              # 测试实体 (不依赖 SuperbWarfare)
│   ├── init/
│   │   ├── ModEntities.java             # 实体注册
│   │   └── ModEntityRenderers.java      # 渲染器注册
│   └── client/
│       ├── renderer/entity/M12Renderer.java
│       └── model/entity/M12Model.java
├── build.gradle.kts                     # 构建配置
├── settings.gradle.kts                  # 项目设置
└── BUILD_INSTRUCTIONS.md               # 本文件
```

## 常见问题

### Q: 构建时出现 "找不到符号" 错误
A: 需要 SuperbWarfare JAR 文件。按照 "方案1" 获取。

### Q: Gradle 配置缓存被锁定
A: 运行 `.\gradlew.bat --stop` 停止所有 Gradle 进程。

### Q: 如何测试项目结构？
A: 使用 `TestEntity` 类，它不依赖 SuperbWarfare。

## 下一步
1. 获取 SuperbWarfare JAR 文件
2. 取消 `build.gradle.kts` 中的 JAR 依赖注释
3. 构建项目
4. 在 Minecraft 中测试

## 联系方式
如有问题，请参考项目文档或联系开发者。
