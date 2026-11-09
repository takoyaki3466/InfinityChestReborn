package com.takoy3466.infChestReborn;

import com.takoy3466.infChestReborn.core.InfChestConfig;
import com.takoy3466.infChestReborn.init.*;

public class InfChestCommon {

    public static final String MOD_ID = "infchest_reborn";

    private InfChestCommon() {
    }

    public static void init() {
        InfChestConfig.load();

        CompatDataComponents.init();
        CompatBlocks.init();
        CompatBlockEntities.init();
        CompatMenus.init();
        CompatTabs.init();
    }
}
