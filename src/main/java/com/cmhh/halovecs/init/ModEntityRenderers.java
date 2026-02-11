package com.cmhh.halovecs.init;

import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.client.renderer.entity.M12Renderer;
import com.cmhh.halovecs.client.renderer.entity.M12hmgRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Halovecs.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEntityRenderers {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // 注册 M12 实体渲染器
        event.registerEntityRenderer(ModEntities.M12.get(), M12Renderer::new);
        // 注册 M12hmg 实体渲染器
        event.registerEntityRenderer(ModEntities.M12hmg.get(), M12hmgRenderer::new);
    }
}
