package com.cmhh.halovecs;

import org.slf4j.Logger;

import com.cmhh.halovecs.init.ModEntities;
import com.cmhh.halovecs.init.ModCreativeTabs;  // 修复导入路径
import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Halovecs.MODID)
public class Halovecs {
    public static final String MODID = "halovecs";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Halovecs(IEventBus modEventBus) {
        // 注册实体
        ModEntities.REGISTRY.register(modEventBus);
        
        // 注册创造标签
        ModCreativeTabs.TABS.register(modEventBus);

        // 注册 mod 生命周期事件
        modEventBus.addListener(this::commonSetup);

        LOGGER.info("Halovecs mod initialized");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Halovecs common setup");
    }
}
