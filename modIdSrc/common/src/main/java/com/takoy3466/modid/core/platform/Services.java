package com.takoy3466.modid.core.platform;

import java.util.ServiceLoader;

public class Services {

    public static final IUtilPlatform UTIL = load(IUtilPlatform.class);
    public static final IRegistryPlatform REGISTRY = load(IRegistryPlatform.class);

    private static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }
}
