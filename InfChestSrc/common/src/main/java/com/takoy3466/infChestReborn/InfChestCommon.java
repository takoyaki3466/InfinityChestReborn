package com.takoy3466.infChestReborn;

import com.takoy3466.infChestReborn.init.*;

public class InfChestCommon {

    public static final String MOD_ID = "infchest_reborn";

    private InfChestCommon() {
    }

    public static void init() {
        CompatItems.init();
        CompatBlocks.init();
        CompatBlockEntities.init();
        CompatTabs.init();
    }
}
