package com.takoy3466.infChestReborn.screen;

import com.takoy3466.infChestReborn.InfChestCommon;
import com.takoy3466.infChestReborn.core.original.InfChestNumberFormat;
import com.takoy3466.infChestReborn.core.original.InfChestStack;
import com.takoy3466.infChestReborn.menu.MenuInfChest;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ScreenInfChest extends AbstractContainerScreen<MenuInfChest> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(InfChestCommon.MOD_ID, "textures/gui/inf_chest_gui.png");
    private static final String CONTAINER_TITLE_KEY = "container." + InfChestCommon.MOD_ID + ".inf_chest";

    private static final int LABEL_COLOR = 0x404040;
    private static final int TITLE_X = 8;
    private static final int TITLE_Y = 5;
    private static final int INVENTORY_TITLE_X = 8;
    private static final int INVENTORY_TITLE_Y = 72;
    private static final int ITEM_NAME_X = 35;
    private static final int ITEM_NAME_Y = 17;
    private static final int ITEM_COUNT_X = 47;
    private static final int ITEM_COUNT_Y = 29;
    private static final int LARGE_CHEST_COUNT_RIGHT_X = 164;
    private static final int LARGE_CHEST_COUNT_Y = 40;
    private static final int ITEM_INFO_X = 7;
    private static final int ITEM_INFO_Y = 42;
    private static final int ITEM_INFO_LINE_HEIGHT = 10;

    public ScreenInfChest(MenuInfChest menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Component chestTitle = Component.translatable(CONTAINER_TITLE_KEY, InfChestNumberFormat.formatMax(InfChestStack.MAX_COUNT));
        drawLabel(guiGraphics, chestTitle, TITLE_X, TITLE_Y);
        drawLabel(guiGraphics, playerInventoryTitle, INVENTORY_TITLE_X, INVENTORY_TITLE_Y);
        InfChestStack chestStack = menu.getChestStack();
        if (chestStack.hasStack()) {
            renderStoredItemInfo(guiGraphics, chestStack.getStoredItem());
        }
    }

    @Override
    protected void renderSlot(@NotNull GuiGraphics guiGraphics, Slot slot) {
        if (slot.index == MenuInfChest.DISPLAY_SLOT) {
            guiGraphics.renderItem(slot.getItem(), slot.x, slot.y);
        } else {
            super.renderSlot(guiGraphics, slot);
        }
    }

    private void renderStoredItemInfo(GuiGraphics guiGraphics, ItemStack stored) {
        if (minecraft == null) {
            return;
        }

        drawLabel(guiGraphics, stored.getHoverName(), ITEM_NAME_X, ITEM_NAME_Y);
        drawLabel(guiGraphics, Component.literal(InfChestNumberFormat.formatStack(stored.getCount())), ITEM_COUNT_X, ITEM_COUNT_Y);

        Component largeChests = Component.literal(InfChestNumberFormat.formatLargeChests(stored.getCount()));
        drawLabel(guiGraphics, largeChests, LARGE_CHEST_COUNT_RIGHT_X - font.width(largeChests), LARGE_CHEST_COUNT_Y);

        List<Component> itemInfo = new ArrayList<>();
        stored.getItem().appendHoverText(stored, Item.TooltipContext.of(minecraft.level), itemInfo, TooltipFlag.NORMAL);
        for (int i = 0; i < itemInfo.size(); i++) {
            drawLabel(guiGraphics, itemInfo.get(i), ITEM_INFO_X, ITEM_INFO_Y + i * ITEM_INFO_LINE_HEIGHT);
        }
    }

    private void drawLabel(GuiGraphics guiGraphics, Component text, int x, int y) {
        guiGraphics.drawString(font, text, x, y, LABEL_COLOR, false);
    }
}
