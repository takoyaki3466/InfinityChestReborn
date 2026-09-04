package com.takoy3466.infChestReborn;

import com.takoy3466.infChestReborn.init.CompatBlockEntities;
import com.takoy3466.infChestReborn.init.CompatBlocks;
import com.takoy3466.infChestReborn.init.CompatItems;
import com.takoy3466.infChestReborn.init.CompatTabs;

public class InfChestCommon {

    public static final String MOD_ID = "infchest_reborn";

    private InfChestCommon() {
    }

    public static void init() {
        CompatItems.init();
        CompatBlocks.init();
        CompatTabs.init();
        CompatBlockEntities.init();
    }
}
