package com.takoy3466.infChestReborn.init;

import com.takoy3466.infChestReborn.block.BlockInfChest;
import com.takoy3466.infChestReborn.core.registry.CompatRegistry;
import com.takoy3466.infChestReborn.core.registry.holder.CompatDoubleHolder;
import net.minecraft.world.item.Item;

public class CompatBlocks {

    public static void init() {
    }

    public static final CompatDoubleHolder.BlockHolder<BlockInfChest> INF_CHEST = CompatRegistry.registerBlock("inf_chest", BlockInfChest::new, new Item.Properties());
}
