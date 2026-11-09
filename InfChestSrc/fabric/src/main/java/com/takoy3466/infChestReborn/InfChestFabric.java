package com.takoy3466.infChestReborn;

import com.takoy3466.infChestReborn.event.InfChestSubscribeEvent;
import net.fabricmc.api.ModInitializer;

public class InfChestFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        InfChestCommon.init();
        InfChestSubscribeEvent.registerEvent();
    }
}
