package com.cmhh.halovecs.init;

import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.item.common.container.ContainerBlockItem;
import com.atsuishio.superbwarfare.item.common.container.ContainerBlockItem;
import com.cmhh.halovecs.Halovecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = 
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Halovecs.MODID);
    
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> HALOVECS_TAB = 
        TABS.register("halovecs_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group.halovecs.main"))
            .icon(() -> new ItemStack(ModItems.CONTAINER.get()))
            .displayItems((params, output) -> {
                output.accept(ContainerBlockItem.createInstance(ModEntities.M12.get()));
                output.accept(ContainerBlockItem.createInstance(ModEntities.M12hmg.get()));
                output.accept(ContainerBlockItem.createInstance(ModEntities.M12roc.get()));
                output.accept(ContainerBlockItem.createInstance(ModEntities.M12gau.get()));
                output.accept(ContainerBlockItem.createInstance(ModEntities.M12tra.get()));
                output.accept(ContainerBlockItem.createInstance(ModEntities.M274.get()));
                output.accept(ContainerBlockItem.createInstance(ModEntities.M274m.get()));
            })
            .build());
    
}