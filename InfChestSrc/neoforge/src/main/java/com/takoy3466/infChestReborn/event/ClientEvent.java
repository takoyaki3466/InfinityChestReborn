package com.takoy3466.infChestReborn.event;

import com.takoy3466.infChestReborn.InfChestCommon;
import com.takoy3466.infChestReborn.client.InfChestHudOverlay;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = InfChestCommon.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class ClientEvent {

    private ClientEvent() {
    }

    @SubscribeEvent
    public static void renderHud(RenderGuiEvent.Post event) {
        InfChestHudOverlay.render(event.getGuiGraphics());
    }
}
