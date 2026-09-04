package com.takoy3466.modid.core.registry.holder;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

public class CompatHolder<T> implements IObjectHolder<T> {
    private final String id;
    private Supplier<? extends T> supplier;

    private CompatHolder(@NotNull String id) {
        this.id = id;
    }

    public static <B> CompatHolder<B> create(String id) {
        return new CompatHolder<>(id);
    }

    public String getId() {
        return id;
    }

    @Override
    public T get() {
        if (this.supplier == null) {
            throw new IllegalStateException("ModObject '" + id + "' has not been initialized.");
        }
        return supplier.get();
    }

    @Override
    public void set(Supplier<? extends T> supplier) {
        if (this.supplier != null) {
            throw new IllegalStateException("ModObject '" + id + "' has already been initialized.");
        }

        this.supplier = Objects.requireNonNull(supplier, "value cannot be null");
    }

    @Override
    public String toString() {
        return "ModObject{" + "id='" + id + '\'' + ", value=" + supplier + '}';
    }
}
