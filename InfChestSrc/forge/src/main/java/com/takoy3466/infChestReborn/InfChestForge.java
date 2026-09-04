package com.takoy3466.infChestReborn;

import com.takoy3466.infChestReborn.core.platform.ForgeRegistryPlatform;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(InfChestCommon.MOD_ID)
public class InfChestForge {

    public InfChestForge(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();

        ForgeRegistryPlatform.register(bus);
        InfChestCommon.init();
    }
}
