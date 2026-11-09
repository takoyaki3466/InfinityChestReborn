package com.takoy3466.infChestReborn.menu;

import com.takoy3466.infChestReborn.core.original.InfChestSlot;
import com.takoy3466.infChestReborn.core.original.InfChestStack;
import com.takoy3466.infChestReborn.core.original.InfChestViewSlot;
import com.takoy3466.infChestReborn.init.CompatBlocks;
import com.takoy3466.infChestReborn.init.CompatMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MenuInfChest extends AbstractContainerMenu {

    public static final int DISPLAY_SLOT = 0;
    public static final int INPUT_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;

    private static final int PLAYER_SLOT_START = 3;
    private static final int PLAYER_MAIN_SLOT_COUNT = 27;
    private static final int PLAYER_HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_SLOT_COUNT = PLAYER_MAIN_SLOT_COUNT + PLAYER_HOTBAR_SLOT_COUNT;

    private static final int LEFT_BUTTON = 0;
    private static final int RIGHT_BUTTON = 1;

    private static final int DEBUG_STEP = 64;
    private static final int DEBUG_LARGE_STEP = 100_000_000;

    private final InfChestStack chestStack;
    private final ContainerLevelAccess access;

    public MenuInfChest(int id, Inventory inventory) {
        this(id, inventory, new InfChestStack(InfChestStack.Listener.NONE), ContainerLevelAccess.NULL);
    }

    public MenuInfChest(int id, Inventory inventory, InfChestStack chestStack, ContainerLevelAccess access) {
        super(CompatMenus.INF_CHEST.get(), id);
        this.chestStack = chestStack;
        this.access = access;
        addChestSlots();
        addPlayerSlots(inventory);
    }

    public InfChestStack getChestStack() {
        return chestStack;
    }

    @Override
    public void clicked(int slotId, int button, @NotNull ClickType clickType, @NotNull Player player) {
        if ((button != LEFT_BUTTON && button != RIGHT_BUTTON) || clickType == ClickType.QUICK_CRAFT) {
            return;
        }

        if (slotId == DISPLAY_SLOT) {
            applyDebugClick(button, clickType, player);

        } else if (getCarried().isEmpty()) {
            clickWithEmptyHand(slotId, button, clickType, player);

        } else {
            clickWithCarriedItem(slotId, button, clickType, player);
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        if (index >= PLAYER_SLOT_START) {
            storeFromSlot(slots.get(index));
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(access, player, CompatBlocks.INF_CHEST.getBlock());
    }

    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack stack, @NotNull Slot slot) {
        return !(slot instanceof InfChestSlot) && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public boolean canDragTo(@NotNull Slot slot) {
        return false;
    }

    private void addChestSlots() {
        addSlot(new InfChestSlot(chestStack, InfChestStack.STORED_SLOT, 12, 21));
        addSlot(new InfChestViewSlot(chestStack, InfChestStack.INPUT_SLOT, 80, 63, () -> chestStack.getItem(InfChestStack.INPUT_SLOT)));
        addSlot(new InfChestViewSlot(chestStack, InfChestStack.STORED_SLOT, 134, 63, chestStack::createOutputPreview));
    }

    private void addPlayerSlots(Inventory inventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < PLAYER_HOTBAR_SLOT_COUNT; column++) {
                int slot = column + row * PLAYER_HOTBAR_SLOT_COUNT + PLAYER_HOTBAR_SLOT_COUNT;
                addSlot(new Slot(inventory, slot, 8 + column * 18, 84 + row * 18));
            }
        }
        for (int column = 0; column < PLAYER_HOTBAR_SLOT_COUNT; column++) {
            addSlot(new Slot(inventory, column, 8 + column * 18, 142));
        }
    }

    private void applyDebugClick(int button, ClickType clickType, Player player) {
        if (!player.getAbilities().instabuild) {
            return;
        }
        int amount = clickType == ClickType.QUICK_MOVE ? DEBUG_LARGE_STEP : DEBUG_STEP;
        if (button == LEFT_BUTTON) {
            chestStack.addCount(amount);
        } else {
            chestStack.removeItem(InfChestStack.STORED_SLOT, amount);
        }
    }

    private void clickWithEmptyHand(int slotId, int button, ClickType clickType, Player player) {
        boolean quickMove = clickType == ClickType.QUICK_MOVE;
        if (slotId == INPUT_SLOT) {
            return;
        }
        if (slotId == OUTPUT_SLOT) {
            if (quickMove) {
                moveStoredToInventory(chestStack.createOutputPreview().getCount());
            } else {
                takeOutputToCarried(button);
            }
        } else if (quickMove && slotId >= 0) {
            quickMoveStack(player, slotId);
        } else {
            super.clicked(slotId, button, clickType, player);
        }
    }

    private void clickWithCarriedItem(int slotId, int button, ClickType clickType, Player player) {
        boolean quickMove = clickType == ClickType.QUICK_MOVE;
        if (slotId == OUTPUT_SLOT) {
            return;
        }
        if (slotId == INPUT_SLOT) {
            if (quickMove) {
                storeCarriedAndMatchingInventory();
            } else {
                storeCarried(button);
            }
        } else if (quickMove && slotId >= 0) {
            storeCarriedAndFillInventory();
        } else {
            super.clicked(slotId, button, clickType, player);
        }
    }

    private void takeOutputToCarried(int button) {
        int size = chestStack.createOutputPreview().getCount();
        if (button == RIGHT_BUTTON) {
            size = (size & 1) + (size >> 1);
        }
        setCarried(chestStack.removeItem(InfChestStack.STORED_SLOT, size));
    }

    private void storeCarried(int button) {
        ItemStack carried = getCarried();
        ItemStack offered = button == RIGHT_BUTTON ? carried.copyWithCount(1) : carried;
        carried.shrink(chestStack.store(offered));
    }

    private void storeCarriedAndMatchingInventory() {
        ItemStack carried = getCarried();
        carried.shrink(chestStack.store(carried));
        for (int i = 0; i < PLAYER_SLOT_COUNT; i++) {
            Slot slot = slots.get(PLAYER_SLOT_START + i);
            if (chestStack.isSameContents(slot.getItem()) && storeFromSlot(slot)) {
                return;
            }
        }
    }

    private void storeCarriedAndFillInventory() {
        ItemStack carried = getCarried();
        if (!chestStack.isSameContents(carried)) {
            return;
        }
        carried.shrink(chestStack.store(carried));
        moveStoredToInventory(chestStack.getStoredCount());
    }

    private boolean storeFromSlot(Slot slot) {
        ItemStack stack = slot.getItem();
        int stored = chestStack.store(stack);
        if (stored > 0) {
            stack.shrink(stored);
            slot.set(stack.isEmpty() ? ItemStack.EMPTY : stack);
        }
        return !stack.isEmpty();
    }

    private void moveStoredToInventory(int limit) {
        int remaining = limit;
        for (int i = 0; i < PLAYER_SLOT_COUNT && remaining > 0; i++) {
            remaining -= fillSlotFromStored(slots.get(PLAYER_SLOT_START + i), remaining);
        }
        chestStack.removeItem(InfChestStack.STORED_SLOT, limit - remaining);
    }

    private int fillSlotFromStored(Slot slot, int available) {
        ItemStack present = slot.getItem();
        if (!present.isEmpty() && !chestStack.isSameContents(present)) {
            return 0;
        }
        ItemStack stored = chestStack.getStoredItem();
        int moved = Math.min(stored.getMaxStackSize() - present.getCount(), available);
        if (moved <= 0) {
            return 0;
        }
        slot.set(stored.copyWithCount(present.getCount() + moved));
        return moved;
    }
}
