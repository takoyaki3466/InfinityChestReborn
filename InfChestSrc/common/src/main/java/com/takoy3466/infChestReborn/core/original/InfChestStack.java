package com.takoy3466.infChestReborn.core.original;

import net.minecraft.world.item.ItemStack;

public class InfChestStack {
    public static final int MAX_COUNT = 2000000000;

    private ItemStack storedItem = ItemStack.EMPTY;
    private int count = 0;

    public void setStoredItem(ItemStack storedItem) {
        this.storedItem = storedItem;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ItemStack getStoredItem() {
        return storedItem;
    }

    public int getCount() {
        return count;
    }

    public boolean isEmpty() {
        return storedItem.isEmpty() || count <= 0;
    }

    public int insert(ItemStack stack, int amount) {
        if (stack.isEmpty() || amount <= 0) {
            return 0;
        }

        if (isEmpty()) {
            storedItem = stack.copyWithCount(1);
        } else if (!ItemStack.isSameItemSameComponents(storedItem, stack)) {
            return 0;
        }

        int inserted = (int) Math.min(amount, MAX_COUNT - (long) count);

        count += inserted;

        return inserted;
    }

    public ItemStack extract(int amount) {
        if (isEmpty() || amount <= 0) {
            return ItemStack.EMPTY;
        }

        int extracted = Math.min(amount, count);

        ItemStack result = storedItem.copyWithCount(extracted);

        count -= extracted;

        if (count == 0) {
            storedItem = ItemStack.EMPTY;
        }

        return result;
    }

    public void clear() {
        storedItem = ItemStack.EMPTY;
        count = 0;
    }
}
