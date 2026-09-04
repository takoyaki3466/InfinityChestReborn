package com.takoy3466.infChestReborn.core.original;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public class InfChestSlot extends Slot {
    public InfChestSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public int getMaxStackSize() {
        return 2000000000;
    }
}
