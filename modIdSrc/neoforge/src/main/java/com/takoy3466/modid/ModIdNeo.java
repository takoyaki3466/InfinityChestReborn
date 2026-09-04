package com.takoy3466.modid;

import com.takoy3466.modid.core.platform.NeoRegistryPlatform;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(ModIdCommon.MOD_ID)
public class ModIdNeo {

    public ModIdNeo(IEventBus bus) {

        NeoRegistryPlatform.register(bus);
        ModIdCommon.init();
    }
}
