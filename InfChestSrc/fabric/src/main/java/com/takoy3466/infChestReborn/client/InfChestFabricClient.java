package com.takoy3466.infChestReborn.client;

import com.takoy3466.infChestReborn.init.CompatBlockEntities;
import com.takoy3466.infChestReborn.init.CompatBlocks;
import com.takoy3466.infChestReborn.init.CompatMenus;
import com.takoy3466.infChestReborn.screen.ScreenInfChest;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class InfChestFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(CompatMenus.INF_CHEST.get(), ScreenInfChest::new);
        BlockEntityRenderers.register(CompatBlockEntities.INF_CHEST.get(), InfChestBlockEntityRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(CompatBlocks.INF_CHEST.getBlock(), RenderType.translucent());
        HudRenderCallback.EVENT.register((guiGraphics, deltaTracker) -> InfChestHudOverlay.render(guiGraphics));
    }
}
