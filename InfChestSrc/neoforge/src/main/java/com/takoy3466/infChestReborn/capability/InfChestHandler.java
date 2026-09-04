package com.takoy3466.infChestReborn.capability;

import com.takoy3466.infChestReborn.block.blockentity.BlockEntityInfChest;
import com.takoy3466.infChestReborn.core.original.InfChestStack;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class InfChestHandler implements IItemHandler {

    private final InfChestStack chestStack;
    private final BlockEntityInfChest blockEntity;

    public InfChestHandler(BlockEntityInfChest blockEntity) {
        this.blockEntity = blockEntity;
        this.chestStack = blockEntity.getChestStack();
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        if (slot != 0 || chestStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int amount = Math.min(
                chestStack.getCount(),
                chestStack.getStoredItem().getMaxStackSize()
        );

        return chestStack.getStoredItem().copyWithCount(amount);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (slot != 0 || stack.isEmpty()) {
            return stack;
        }

        int available = InfChestStack.MAX_COUNT - chestStack.getCount();

        if (available <= 0) {
            return stack;
        }

        int amount = Math.min(stack.getCount(), available);

        if (!simulate) {
            int inserted = chestStack.insert(stack, amount);

            if (inserted > 0) {
                blockEntity.sync();
            }
        }

        return stack.copyWithCount(stack.getCount() - amount);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot != 0 || amount <= 0) {
            return ItemStack.EMPTY;
        }

        int extracted = Math.min(amount, chestStack.getCount());

        if (simulate) {
            return chestStack.getStoredItem().copyWithCount(extracted);
        }

        ItemStack result = chestStack.extract(extracted);

        if (!result.isEmpty()) {
            blockEntity.setChanged();
        }

        return result;
    }

    @Override
    public int getSlotLimit(int i) {
        return chestStack.getStoredItem().isEmpty() ? 64 : chestStack.getStoredItem().getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (slot != 0 || stack.isEmpty()) {
            return false;
        }

        return chestStack.isEmpty() || ItemStack.isSameItemSameComponents(chestStack.getStoredItem(), stack);
    }
}
