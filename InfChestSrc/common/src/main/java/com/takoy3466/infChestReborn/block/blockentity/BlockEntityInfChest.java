package com.takoy3466.infChestReborn.block.blockentity;

import com.takoy3466.infChestReborn.core.original.InfChestStack;
import com.takoy3466.infChestReborn.init.CompatBlockEntities;
import com.takoy3466.infChestReborn.menu.MenuInfChest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BlockEntityInfChest extends BaseContainerBlockEntity implements MenuProvider {
    private final InfChestStack chestStack = new InfChestStack();

    public BlockEntityInfChest(BlockPos pos, BlockState blockState) {
        super(CompatBlockEntities.INF_CHEST.get(), pos, blockState);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.literal("test");
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return NonNullList.of(chestStack.getStoredItem());
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        chestStack.insert(nonNullList.getFirst());
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
    protected @NotNull AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new MenuInfChest(id, inv, this, ContainerLevelAccess.create(Objects.requireNonNull(level), getBlockPos()));
    }

    @Override
    public int getContainerSize() {
        return 1;
    }
}
