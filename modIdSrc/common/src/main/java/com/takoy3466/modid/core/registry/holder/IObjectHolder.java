package com.takoy3466.modid.core.registry.holder;

import java.util.function.Supplier;

public interface IObjectHolder<T> {

    T get();

    void set(Supplier<? extends T> supplier);
}
