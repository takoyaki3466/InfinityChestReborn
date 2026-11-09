package com.takoy3466.infChestReborn.init;

import com.takoy3466.infChestReborn.core.registry.CompatRegistry;
import com.takoy3466.infChestReborn.core.registry.holder.CompatHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CompatTabs {

    public static final List<CompatHolder<? extends Item>> ITEMS = new ArrayList<>();

    public static final CompatHolder<CreativeModeTab> INF_CHEST_TAB = CompatRegistry.registerCreativeTab("inf_chest_tab", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup.infchest_reborn"))
            .icon(() -> new ItemStack(CompatBlocks.INF_CHEST.getItem()))
            .displayItems((parameters, output) -> ITEMS.forEach(holder -> output.accept(holder.get())))
            .build());

    public static void init() {
    }
}
