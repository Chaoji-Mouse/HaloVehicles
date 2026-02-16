package com.cmhh.halovecs.client;

import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.client.renderer.entity.M12Renderer;
import com.cmhh.halovecs.client.renderer.entity.M12gauRenderer;
import com.cmhh.halovecs.client.renderer.entity.M12hmgRenderer;
import com.cmhh.halovecs.client.renderer.entity.M12rocRenderer;
import com.cmhh.halovecs.client.renderer.entity.M12traRenderer;
import com.cmhh.halovecs.init.ModEntities;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Halovecs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEntityRenderers {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.M12.get(), M12Renderer::new);
        event.registerEntityRenderer(ModEntities.M12HMG.get(), M12hmgRenderer::new);
        event.registerEntityRenderer(ModEntities.M12ROC.get(), M12rocRenderer::new);
        event.registerEntityRenderer(ModEntities.M12GAU.get(), M12gauRenderer::new);
        event.registerEntityRenderer(ModEntities.M12TRA.get(), M12traRenderer::new);
    }
}
