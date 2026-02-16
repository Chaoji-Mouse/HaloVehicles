package com.cmhh.halovecs.init;

import com.atsuishio.superbwarfare.item.common.container.ContainerBlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import com.cmhh.halovecs.Halovecs;
import com.cmhh.halovecs.init.ModEntities;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Halovecs.MODID);

    public static final RegistryObject<CreativeModeTab> HALOVECS_TAB = TABS.register("halovecs",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group.halovecs.vehicles"))
                    .icon(() -> ContainerBlockItem.createInstance(ModEntities.M12.get()))
                    .displayItems((params, output) -> { 
                        output.accept(ContainerBlockItem.createInstance(ModEntities.M12.get()));
                        output.accept(ContainerBlockItem.createInstance(ModEntities.M12HMG.get()));
                        output.accept(ContainerBlockItem.createInstance(ModEntities.M12ROC.get()));
                        output.accept(ContainerBlockItem.createInstance(ModEntities.M12GAU.get()));
                        output.accept(ContainerBlockItem.createInstance(ModEntities.M12TRA.get()));
                    })
                    .build()
    );
}
