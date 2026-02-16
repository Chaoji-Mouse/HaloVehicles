package com.cmhh.halovecs;

import com.cmhh.halovecs.init.ModEntities;
import com.cmhh.halovecs.init.ModCreativeTabs;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Halovecs.MODID)
public class Halovecs {
    public static final String MODID = "halovecs";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Halovecs() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        LOGGER.info("HaloVehicles mod initialized for Forge 1.20.1");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HaloVehicles common setup");
    }
}
