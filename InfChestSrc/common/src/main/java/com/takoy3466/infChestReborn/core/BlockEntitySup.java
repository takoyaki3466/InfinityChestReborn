package com.takoy3466.infChestReborn.core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface BlockEntitySup<T extends BlockEntity> {
    T create(BlockPos pos, BlockState state);
}
