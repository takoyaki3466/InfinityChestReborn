package com.takoy3466.infChestReborn.event;

import com.takoy3466.infChestReborn.InfChestCommon;
import com.takoy3466.infChestReborn.client.InfChestBlockEntityRenderer;
import com.takoy3466.infChestReborn.init.CompatBlockEntities;
import com.takoy3466.infChestReborn.init.CompatMenus;
import com.takoy3466.infChestReborn.screen.ScreenInfChest;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = InfChestCommon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEventBus {

    private ClientModEventBus() {
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(CompatMenus.INF_CHEST.get(), ScreenInfChest::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CompatBlockEntities.INF_CHEST.get(), InfChestBlockEntityRenderer::new);
    }
}
