package com.takoy3466.infChestReborn.block.blockentity;

import com.takoy3466.infChestReborn.core.original.InfChestStack;
import com.takoy3466.infChestReborn.init.CompatBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockEntityInfChest extends BlockEntity implements MenuProvider {
    private InfChestStack chestStack = new InfChestStack();

    public BlockEntityInfChest(BlockPos pos, BlockState blockState) {
        super(CompatBlockEntities.INF_CHEST.get(), pos, blockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("test");
    }

    public InfChestStack getChestStack() {
        return chestStack;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains("item")) {
            chestStack.setStoredItem(ItemStack.parse(registries, tag.getCompound("item")).orElse(ItemStack.EMPTY));
        }

        chestStack.setCount(tag.getInt("count"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        ItemStack storedItem = chestStack.getStoredItem();

        if (!storedItem.isEmpty()) {
            tag.put("item", storedItem.save(registries));
        }

        tag.putInt("count", chestStack.getCount());
    }

    public void sync() {

        setChanged();

        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
    }
}
