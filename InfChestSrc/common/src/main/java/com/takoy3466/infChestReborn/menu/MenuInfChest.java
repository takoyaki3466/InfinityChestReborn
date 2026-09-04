package com.takoy3466.infChestReborn.menu;

import com.takoy3466.infChestReborn.core.original.InfChestSlot;
import com.takoy3466.infChestReborn.init.CompatBlocks;
import com.takoy3466.infChestReborn.init.CompatMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MenuInfChest extends AbstractContainerMenu {
    private final Player player;
    private final ContainerLevelAccess access;

    public MenuInfChest(int id, Inventory inv) {
        this(id, inv, new SimpleContainer(2), ContainerLevelAccess.NULL);
    }

    public MenuInfChest(int id, Inventory inv, Container container, ContainerLevelAccess access) {
        super(CompatMenus.INF_CHEST.get(), id);
        this.player = inv.player;
        this.access = access;

        //飾り用
        this.addSlot(new InfChestSlot(container, 0, 12, 21) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public boolean mayPickup(Player player) {
                return false;
            }
        });

        // input
        this.addSlot(new InfChestSlot(container, 1, 80, 63));

        // output
        this.addSlot(new InfChestSlot(container, 1, 134, 63) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        for (int k = 0; k < 3; k++) {
            for (int i1 = 0; i1 < 9; i1++) {
                this.addSlot(new Slot(inv, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; l++) {
            this.addSlot(new Slot(inv, l, 8 + l * 18, 142));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, CompatBlocks.INF_CHEST.getBlock());
    }
}
