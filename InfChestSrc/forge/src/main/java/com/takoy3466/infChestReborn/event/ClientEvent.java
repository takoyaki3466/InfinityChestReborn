package com.takoy3466.infChestReborn.event;

import com.takoy3466.infChestReborn.InfChestCommon;
import com.takoy3466.infChestReborn.client.InfChestHudOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = InfChestCommon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ClientEvent {

    private ClientEvent() {
    }

    @SubscribeEvent
    public static void renderHud(CustomizeGuiOverlayEvent.Chat event) {
        InfChestHudOverlay.render(event.getGuiGraphics());
    }
}
