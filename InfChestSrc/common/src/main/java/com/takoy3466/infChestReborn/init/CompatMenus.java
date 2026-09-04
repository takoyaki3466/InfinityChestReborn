package com.takoy3466.infChestReborn.init;

import com.takoy3466.infChestReborn.core.registry.CompatRegistry;
import com.takoy3466.infChestReborn.core.registry.holder.CompatHolder;
import com.takoy3466.infChestReborn.menu.MenuInfChest;
import net.minecraft.world.inventory.MenuType;

public class CompatMenus {

    public static void init() {
    }

    public static final CompatHolder<MenuType<MenuInfChest>> INF_CHEST = CompatRegistry.registerMenuType();
}
