package com.takoy3466.infChestReborn.core.original;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * チェスト側のスロット。
 *
 * <p>実際の操作は {@code MenuInfChest#clicked} が独自に処理するため、
 * バニラのスロット操作経路から在庫へ触れられないよう閉じている。
 */
public class InfChestSlot extends Slot {

    public InfChestSlot(InfChestStack chestStack, int containerSlot, int x, int y) {
        super(chestStack, containerSlot, x, y);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean mayPickup(@NotNull Player player) {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return InfChestStack.MAX_COUNT;
    }
}
