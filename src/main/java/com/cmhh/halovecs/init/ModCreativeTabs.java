package com.cmhh.halovecs.init;

import com.atsuishio.superbwarfare.init.ModItems;
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
                addM12ContainerToCreativeTab(output);
                addM12hmgContainerToCreativeTab(output);
                addM12rocContainerToCreativeTab(output);
                addM12gauContainerToCreativeTab(output);
                addM12traContainerToCreativeTab(output);
            })
            .build());
    
    private static void addM12ContainerToCreativeTab(CreativeModeTab.Output output) {
        try {
            ItemStack m12Container = createM12Container();
            if (!m12Container.isEmpty()) {
                output.accept(m12Container);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addM12hmgContainerToCreativeTab(CreativeModeTab.Output output) {
        try {
            ItemStack m12hmgContainer = createM12hmgContainer();
            if (!m12hmgContainer.isEmpty()) {
                output.accept(m12hmgContainer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addM12rocContainerToCreativeTab(CreativeModeTab.Output output) {
        try {
            ItemStack m12rocContainer = createM12rocContainer();
            if (!m12rocContainer.isEmpty()) {
                output.accept(m12rocContainer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addM12gauContainerToCreativeTab(CreativeModeTab.Output output) {
        try {
            ItemStack m12gauContainer = createM12gauContainer();
            if (!m12gauContainer.isEmpty()) {
                output.accept(m12gauContainer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addM12traContainerToCreativeTab(CreativeModeTab.Output output) {
        try {
            ItemStack m12traContainer = createM12traContainer();
            if (!m12traContainer.isEmpty()) {
                output.accept(m12traContainer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ItemStack createM12Container() {
        try {
            // 获取container物品
            var containerItem = net.minecraft.core.registries.BuiltInRegistries.ITEM
                .get(ResourceLocation.fromNamespaceAndPath("superbwarfare", "container"));
            
            // 创建ItemStack
            ItemStack stack = new ItemStack(containerItem);
            
            // 创建完整的NBT数据
            var tag = new net.minecraft.nbt.CompoundTag();
            
            // 设置EntityType
            tag.putString("EntityType", "halovecs:m12");
            
            // 创建Entity子标签
            var entityTag = new net.minecraft.nbt.CompoundTag();
            
            
            tag.put("Entity", entityTag);
            
            // 尝试获取BlockEntity类型
            var containerBlockEntity = net.minecraft.core.registries.BuiltInRegistries.BLOCK_ENTITY_TYPE
                .get(ResourceLocation.fromNamespaceAndPath("superbwarfare", "container"));
            
            if (containerBlockEntity != null) {
                net.minecraft.world.item.BlockItem.setBlockEntityData(stack, containerBlockEntity, tag);
            } else {
                // 备选方案：直接设置DataComponents
                stack.set(net.minecraft.core.component.DataComponents.BLOCK_ENTITY_DATA, 
                    net.minecraft.world.item.component.CustomData.of(tag));
            }
            
            return stack;
        } catch (Exception e) {
            e.printStackTrace();
            return ItemStack.EMPTY;
        }
    }

    private static ItemStack createM12hmgContainer() {
        try {
            // 获取container物品
            var containerItem = net.minecraft.core.registries.BuiltInRegistries.ITEM
                .get(ResourceLocation.fromNamespaceAndPath("superbwarfare", "container"));
            
            // 创建ItemStack
            ItemStack stack = new ItemStack(containerItem);
            
            // 创建完整的NBT数据
            var tag = new net.minecraft.nbt.CompoundTag();
            
            // 设置EntityType
            tag.putString("EntityType", "halovecs:m12hmg");
            
            // 创建Entity子标签
            var entityTag = new net.minecraft.nbt.CompoundTag();
            
            
            tag.put("Entity", entityTag);
            
            // 尝试获取BlockEntity类型
            var containerBlockEntity = net.minecraft.core.registries.BuiltInRegistries.BLOCK_ENTITY_TYPE
                .get(ResourceLocation.fromNamespaceAndPath("superbwarfare", "container"));
            
            if (containerBlockEntity != null) {
                net.minecraft.world.item.BlockItem.setBlockEntityData(stack, containerBlockEntity, tag);
            } else {
                // 备选方案：直接设置DataComponents
                stack.set(net.minecraft.core.component.DataComponents.BLOCK_ENTITY_DATA, 
                    net.minecraft.world.item.component.CustomData.of(tag));
            }
            
            return stack;
        } catch (Exception e) {
            e.printStackTrace();
            return ItemStack.EMPTY;
        }
    }

    private static ItemStack createM12rocContainer() {
        try {
            // 获取container物品
            var containerItem = net.minecraft.core.registries.BuiltInRegistries.ITEM
                .get(ResourceLocation.fromNamespaceAndPath("superbwarfare", "container"));
            
            // 创建ItemStack
            ItemStack stack = new ItemStack(containerItem);
            
            // 创建完整的NBT数据
            var tag = new net.minecraft.nbt.CompoundTag();
            
            // 设置EntityType
            tag.putString("EntityType", "halovecs:m12roc");
            
            // 创建Entity子标签
            var entityTag = new net.minecraft.nbt.CompoundTag();
            
            
            tag.put("Entity", entityTag);
            
            // 尝试获取BlockEntity类型
            var containerBlockEntity = net.minecraft.core.registries.BuiltInRegistries.BLOCK_ENTITY_TYPE
                .get(ResourceLocation.fromNamespaceAndPath("superbwarfare", "container"));
            
            if (containerBlockEntity != null) {
                net.minecraft.world.item.BlockItem.setBlockEntityData(stack, containerBlockEntity, tag);
            } else {
                // 备选方案：直接设置DataComponents
                stack.set(net.minecraft.core.component.DataComponents.BLOCK_ENTITY_DATA, 
                    net.minecraft.world.item.component.CustomData.of(tag));
            }
            
            return stack;
        } catch (Exception e) {
            e.printStackTrace();
            return ItemStack.EMPTY;
        }
    }

    private static ItemStack createM12gauContainer() {
        try {
            // 获取container物品
            var containerItem = net.minecraft.core.registries.BuiltInRegistries.ITEM
                .get(ResourceLocation.fromNamespaceAndPath("superbwarfare", "container"));
            
            // 创建ItemStack
            ItemStack stack = new ItemStack(containerItem);
            
            // 创建完整的NBT数据
            var tag = new net.minecraft.nbt.CompoundTag();
            
            // 设置EntityType
            tag.putString("EntityType", "halovecs:m12gau");
            
            // 创建Entity子标签
            var entityTag = new net.minecraft.nbt.CompoundTag();
            
            
            tag.put("Entity", entityTag);
            
            // 尝试获取BlockEntity类型
            var containerBlockEntity = net.minecraft.core.registries.BuiltInRegistries.BLOCK_ENTITY_TYPE
                .get(ResourceLocation.fromNamespaceAndPath("superbwarfare", "container"));
            
            if (containerBlockEntity != null) {
                net.minecraft.world.item.BlockItem.setBlockEntityData(stack, containerBlockEntity, tag);
            } else {
                // 备选方案：直接设置DataComponents
                stack.set(net.minecraft.core.component.DataComponents.BLOCK_ENTITY_DATA, 
                    net.minecraft.world.item.component.CustomData.of(tag));
            }
            
            return stack;
        } catch (Exception e) {
            e.printStackTrace();
            return ItemStack.EMPTY;
        }
    }

    private static ItemStack createM12traContainer() {
        try {
            // 获取container物品
            var containerItem = net.minecraft.core.registries.BuiltInRegistries.ITEM
                .get(ResourceLocation.fromNamespaceAndPath("superbwarfare", "container"));
            
            // 创建ItemStack
            ItemStack stack = new ItemStack(containerItem);
            
            // 创建完整的NBT数据
            var tag = new net.minecraft.nbt.CompoundTag();
            
            // 设置EntityType
            tag.putString("EntityType", "halovecs:m12tra");
            
            // 创建Entity子标签
            var entityTag = new net.minecraft.nbt.CompoundTag();
            
            
            tag.put("Entity", entityTag);
            
            // 尝试获取BlockEntity类型
            var containerBlockEntity = net.minecraft.core.registries.BuiltInRegistries.BLOCK_ENTITY_TYPE
                .get(ResourceLocation.fromNamespaceAndPath("superbwarfare", "container"));
            
            if (containerBlockEntity != null) {
                net.minecraft.world.item.BlockItem.setBlockEntityData(stack, containerBlockEntity, tag);
            } else {
                // 备选方案：直接设置DataComponents
                stack.set(net.minecraft.core.component.DataComponents.BLOCK_ENTITY_DATA, 
                    net.minecraft.world.item.component.CustomData.of(tag));
            }
            
            return stack;
        } catch (Exception e) {
            e.printStackTrace();
            return ItemStack.EMPTY;
        }
    }
}
