package com.takoy3466.infChestReborn.event;

import com.takoy3466.infChestReborn.InfChestCommon;
import com.takoy3466.infChestReborn.client.InfChestBlockEntityRenderer;
import com.takoy3466.infChestReborn.init.CompatBlockEntities;
import com.takoy3466.infChestReborn.init.CompatMenus;
import com.takoy3466.infChestReborn.screen.ScreenInfChest;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = InfChestCommon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEventBus {

    private ClientModEventBus() {
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(CompatMenus.INF_CHEST.get(), ScreenInfChest::new));
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CompatBlockEntities.INF_CHEST.get(), InfChestBlockEntityRenderer::new);
    }
}
