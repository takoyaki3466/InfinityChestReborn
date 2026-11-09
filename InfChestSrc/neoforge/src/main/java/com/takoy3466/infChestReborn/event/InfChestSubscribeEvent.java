package com.takoy3466.infChestReborn.event;

import com.takoy3466.infChestReborn.InfChestCommon;
import com.takoy3466.infChestReborn.init.CompatBlockEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

@EventBusSubscriber(modid = InfChestCommon.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class InfChestSubscribeEvent {

    private InfChestSubscribeEvent() {
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                CompatBlockEntities.INF_CHEST.get(),
                (chest, side) -> new SidedInvWrapper(chest.getChestStack(), side));
    }
}
