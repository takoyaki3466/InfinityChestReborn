package com.takoy3466.infChestReborn;

import com.takoy3466.infChestReborn.core.platform.NeoRegistryPlatform;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(InfChestCommon.MOD_ID)
public class InfChestNeo {

    public InfChestNeo(IEventBus bus) {

        NeoRegistryPlatform.register(bus);
        InfChestCommon.init();
    }
}
