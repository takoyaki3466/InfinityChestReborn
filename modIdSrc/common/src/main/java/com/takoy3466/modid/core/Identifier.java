package com.takoy3466.modid.core;

import net.minecraft.resources.ResourceLocation;

public class Identifier {
    private final ResourceLocation rl;

    public Identifier(ResourceLocation rl) {
        this.rl = rl;
    }

    public Identifier(String namespace, String path) {
        this.rl = ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public Identifier(String path) {
        this.rl = ResourceLocation.withDefaultNamespace(path);
    }

    public ResourceLocation get() {
        return rl;
    }
}
