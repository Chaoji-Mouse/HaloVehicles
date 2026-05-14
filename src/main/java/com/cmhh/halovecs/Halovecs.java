package com.cmhh.halovecs;

import org.slf4j.Logger;

import com.cmhh.halovecs.init.ModEntities;
import com.cmhh.halovecs.init.ModCreativeTabs;
import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(Halovecs.MODID)
public class Halovecs {
    public static final String MODID = "halovecs";
    public static final Logger LOGGER = LogUtils.getLogger();

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

        // 诊断：检查载具数据是否被加载（此时数据包可能尚未加载完成）
        try {
            var data = com.atsuishio.superbwarfare.data.CustomData.VEHICLE_DATA;
            LOGGER.info("[Diagnostic] VEHICLE_DATA keys count: {}", data.keySet().size());
            LOGGER.info("[Diagnostic] Contains 'halovecs:m12': {}", data.containsKey("halovecs:m12"));
            LOGGER.info("[Diagnostic] Contains 'halovecs:m808': {}", data.containsKey("halovecs:m808"));
            LOGGER.info("[Diagnostic] Contains 'halovecs:t77h': {}", data.containsKey("halovecs:t77h"));
        } catch (Exception e) {
            LOGGER.error("[Diagnostic] Failed to check VEHICLE_DATA: {}", e.toString());
        }

        // 注册服务端启动事件，数据包在此时已完全加载
        NeoForge.EVENT_BUS.addListener((ServerStartingEvent serverEvent) -> {
            try {
                var data = com.atsuishio.superbwarfare.data.CustomData.VEHICLE_DATA;
                LOGGER.info("[Diagnostic-Server] VEHICLE_DATA keys: {}", data.keySet());
                for (String key : data.keySet()) {
                    if (key.contains("halovecs")) {
                        LOGGER.info("[Diagnostic-Server] Found halovecs vehicle: {}", key);
                    }
                }
            } catch (Exception ex) {
                LOGGER.error("[Diagnostic-Server] Failed to check VEHICLE_DATA: {}", ex.toString());
            }
        });
    }
}
