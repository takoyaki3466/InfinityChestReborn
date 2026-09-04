package com.takoy3466.infChestReborn.core.interfaces;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

@FunctionalInterface
public interface CompatMenuSupplier<T extends AbstractContainerMenu> {
    T create(int id, Inventory inventory);
}