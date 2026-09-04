package com.takoy3466.infChestReborn.menu;

import com.takoy3466.infChestReborn.init.CompatMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class MenuInfChest extends AbstractContainerMenu {

    public MenuInfChest(int id, Inventory playerInv, FriendlyByteBuf buf) {
        super(CompatMenus.INF_CHEST.get(), id);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }
}
