package com.takoy3466.infChestReborn.client;

import com.takoy3466.infChestReborn.core.original.InfChestNumberFormat;
import com.takoy3466.infChestReborn.dataComponent.InfChestContents;
import com.takoy3466.infChestReborn.init.CompatBlocks;
import com.takoy3466.infChestReborn.init.CompatDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class InfChestHudOverlay {

    private static final int TEXT_COLOR = 0xFFFFFF;
    private static final int TEXT_OFFSET_FROM_BOTTOM = 72;

    private InfChestHudOverlay() {
    }

    public static void render(GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.options.hideGui) {
            return;
        }

        ItemStack held = player.getMainHandItem();

        if (!held.is(CompatBlocks.INF_CHEST.getItem())) {
            return;
        }

        InfChestContents contents = held.get(CompatDataComponents.INF_CHEST_CONTENTS.get());
        if (contents != null) {
            drawContents(guiGraphics, minecraft.font, contents);
        }
    }

    private static void drawContents(GuiGraphics guiGraphics, Font font, InfChestContents contents) {
        Component text = contents.item().getHoverName().copy().append(" " + InfChestNumberFormat.formatStack(contents.count()));

        int x = guiGraphics.guiWidth() / 2 - font.width(text) / 2;
        int y = guiGraphics.guiHeight() - TEXT_OFFSET_FROM_BOTTOM;
        guiGraphics.drawString(font, text, x, y, TEXT_COLOR, false);
    }
}
