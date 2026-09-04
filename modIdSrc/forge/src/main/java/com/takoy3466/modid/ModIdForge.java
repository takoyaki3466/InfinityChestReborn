package com.takoy3466.modid;

import com.takoy3466.modid.core.platform.ForgeRegistryPlatform;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModIdCommon.MOD_ID)
public class ModIdForge {

    public ModIdForge(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();

        ForgeRegistryPlatform.register(bus);
        ModIdCommon.init();
    }
}
