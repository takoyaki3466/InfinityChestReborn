package com.takoy3466.modid;

import net.fabricmc.api.ModInitializer;

public class ModIdFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        ModIdCommon.init();
    }
}
