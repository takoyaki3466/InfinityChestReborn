package com.takoy3466.infChestReborn.init;

import com.takoy3466.infChestReborn.block.blockentity.BlockEntityInfChest;
import com.takoy3466.infChestReborn.core.registry.CompatRegistry;
import com.takoy3466.infChestReborn.core.registry.holder.CompatHolder;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CompatBlockEntities {

    public static void init() {
    }

    public static final CompatHolder<BlockEntityType<BlockEntityInfChest>> INF_CHEST = CompatRegistry.registerBlockEntityType("inf_chest", BlockEntityInfChest::new, CompatBlocks.INF_CHEST);
}
