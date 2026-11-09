package com.takoy3466.infChestReborn.init;

import com.takoy3466.infChestReborn.block.BlockInfChest;
import com.takoy3466.infChestReborn.core.registry.CompatRegistry;
import com.takoy3466.infChestReborn.core.registry.holder.CompatDoubleHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class CompatBlocks {

    private static final float INF_CHEST_STRENGTH = 0.3F;

    public static final CompatDoubleHolder.BlockHolder<BlockInfChest> INF_CHEST = CompatRegistry.registerBlock("inf_chest", () -> new BlockInfChest(infChestProperties()), new Item.Properties());

    private static BlockBehaviour.Properties infChestProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.NONE)
                .strength(INF_CHEST_STRENGTH)
                .sound(SoundType.GLASS)
                .noOcclusion()
                .noLootTable();
    }

    public static void init() {
    }
}
