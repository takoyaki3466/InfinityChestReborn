package com.takoy3466.modid;

import com.takoy3466.modid.init.CompatBlocks;
import com.takoy3466.modid.init.CompatItems;
import com.takoy3466.modid.init.CompatTabs;

public class ModIdCommon {

    public static final String MOD_ID = "modid";

    private ModIdCommon() {
    }

    public static void init() {
        CompatItems.init();
        CompatBlocks.init();
        CompatTabs.init();
    }
}
