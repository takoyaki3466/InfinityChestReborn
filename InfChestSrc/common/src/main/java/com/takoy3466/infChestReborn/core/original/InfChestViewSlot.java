package com.takoy3466.infChestReborn.core.original;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * 在庫から導出した内容を表示するスロット。
 *
 * <p>表示内容は在庫だけで決まるので、サーバーからの同期は反映しない。
 * 反映すると搬入口の同期がクライアント側の在庫へ「搬入」として加算されてしまう。
 */
public class InfChestViewSlot extends InfChestSlot {

    private final Supplier<ItemStack> view;

    public InfChestViewSlot(InfChestStack chestStack, int containerSlot, int x, int y, Supplier<ItemStack> view) {
        super(chestStack, containerSlot, x, y);
        this.view = view;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return view.get();
    }

    @Override
    public void set(@NotNull ItemStack stack) {
    }
}
