package com.takoy3466.infChestReborn.init;

import com.takoy3466.infChestReborn.core.registry.CompatRegistry;
import com.takoy3466.infChestReborn.core.registry.holder.CompatHolder;
import com.takoy3466.infChestReborn.dataComponent.InfChestContents;
import net.minecraft.core.component.DataComponentType;

public class CompatDataComponents {

    public static final CompatHolder<DataComponentType<InfChestContents>> INF_CHEST_CONTENTS = CompatRegistry.registerDataComponentType("inf_chest_contents", InfChestContents.CODEC, InfChestContents.STREAM_CODEC);

    public static void init() {
    }
}
