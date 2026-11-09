package com.takoy3466.infChestReborn.event;

import com.takoy3466.infChestReborn.init.CompatBlockEntities;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;

public final class InfChestSubscribeEvent {

    private InfChestSubscribeEvent() {
    }

    public static void registerEvent() {
        ItemStorage.SIDED.registerForBlockEntity((chest, side) -> InventoryStorage.of(chest.getChestStack(), side), CompatBlockEntities.INF_CHEST.get());
    }
}
