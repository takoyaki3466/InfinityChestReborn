package com.takoy3466.infChestReborn.core.registry;

import net.minecraft.client.KeyMapping;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class KeyMappingRegister {
    private final String MOD_ID;
    private final String KEY_CATEGORIES;

    public final List<KeyMapping> entries = new ArrayList<>();

    public KeyMappingRegister(String modId) {
        this.MOD_ID = modId;
        this.KEY_CATEGORIES = "key." +  "categories." + modId;
    }

    public static KeyMappingRegister create(String modId) {
        return new KeyMappingRegister(modId);
    }

    public KeyMapping register(@NotNull String keyName, int key) {
        String KEY_NAME = "key." + MOD_ID + "." + keyName;
        KeyMapping keyMapping = new KeyMapping(KEY_NAME, key, KEY_CATEGORIES);
        entries.add(keyMapping);
        return keyMapping;

    }

    public String getCategories() {
        return KEY_CATEGORIES;
    }
}