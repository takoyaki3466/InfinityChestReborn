package com.takoy3466.infChestReborn.core.original;

import com.takoy3466.infChestReborn.dataComponent.InfChestContents;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 1 種類のアイテムを最大 {@value #MAX_COUNT} 個まで保持するチェスト本体の中身。
 *
 * <p>元 Mod の {@code InfinityChestTile} が持っていた在庫管理と {@code ISidedInventory} の仕様を移したもの。
 * ブロックエンティティとクライアント側メニューの双方で使うため、ワールドへの副作用は {@link Listener} に委譲する。
 *
 * <p>スロット {@value #STORED_SLOT} は在庫そのもの（搬出専用）、スロット {@value #INPUT_SLOT} は搬入口である。
 * {@link #getItem(int)} が実数を持つ生の参照を返すのは意図的で、バニラのホッパーは搬送失敗時に
 * 受け取った参照へ元の個数を書き戻して巻き戻す。コピーを返すとアイテムが消失する。
 */
public class InfChestStack implements WorldlyContainer {

    public static final int MAX_COUNT = 2_000_000_000;
    public static final int STORED_SLOT = 0;
    public static final int INPUT_SLOT = 1;

    private static final int[] SLOTS_FOR_ALL_FACES = {STORED_SLOT, INPUT_SLOT};

    /** 在庫の所有者へ通知するためのコールバック。 */
    public interface Listener {

        /** クライアント側メニューのように、ワールドと結び付かない在庫で使う実装。 */
        Listener NONE = new Listener() {
            @Override
            public void onContentsChanged() {
            }

            @Override
            public void onOverflow(ItemStack overflow) {
            }

            @Override
            public boolean isUsableBy(Player player) {
                return true;
            }
        };

        void onContentsChanged();

        /** 搬入しきれなかったアイテムを受け取る。元 Mod ではその場にアイテムとして排出していた。 */
        void onOverflow(ItemStack overflow);

        boolean isUsableBy(Player player);
    }

    private final Listener listener;
    private ItemStack chestItem = ItemStack.EMPTY;

    public InfChestStack(Listener listener) {
        this.listener = listener;
    }

    /** 保持中のスタックを返す。個数の変更は必ずこのクラスのメソッドを通すこと。 */
    public ItemStack getStoredItem() {
        return chestItem;
    }

    public int getStoredCount() {
        return chestItem.getCount();
    }

    public boolean hasStack() {
        return !chestItem.isEmpty();
    }

    public boolean isMax() {
        return chestItem.getCount() >= MAX_COUNT;
    }

    /** {@code stack} がプレイヤー操作で現在の在庫へ合流できるかを返す。 */
    public boolean isSameContents(ItemStack stack) {
        return hasStack() && InfChestItemRules.isStorable(stack) && InfChestItemRules.isSameContents(chestItem, stack);
    }

    /**
     * プレイヤー操作による格納を行う。{@code stack} 自体は変更しない。
     *
     * @return 受け入れた個数
     */
    public int store(ItemStack stack) {
        if (!InfChestItemRules.isStorable(stack)) {
            return 0;
        }
        return accept(stack);
    }

    /** デバッグ操作用に在庫の個数だけを増やす。空のチェストには何もしない。 */
    public void addCount(int amount) {
        if (!hasStack()) {
            return;
        }
        chestItem.setCount((int) Math.min((long) chestItem.getCount() + amount, MAX_COUNT));
        setChanged();
    }

    /** 搬出スロットに表示する、1 スタック分以下の在庫のコピーを返す。 */
    public ItemStack createOutputPreview() {
        if (!hasStack()) {
            return ItemStack.EMPTY;
        }
        return chestItem.copyWithCount(Math.min(chestItem.getCount(), chestItem.getMaxStackSize()));
    }

    @Nullable
    public InfChestContents toContents() {
        return hasStack() ? InfChestContents.of(chestItem) : null;
    }

    /** 保存データや同期データから在庫を復元する。読み込みなので通知は行わない。 */
    public void restore(ItemStack stack) {
        replaceStored(stack);
    }

    @Override
    public int getContainerSize() {
        return SLOTS_FOR_ALL_FACES.length;
    }

    @Override
    public boolean isEmpty() {
        return !hasStack();
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return slot == INPUT_SLOT ? createInputView() : chestItem;
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        if (slot != STORED_SLOT || amount <= 0 || !hasStack()) {
            return ItemStack.EMPTY;
        }
        ItemStack removed;
        if (chestItem.getCount() <= amount) {
            removed = chestItem;
            chestItem = ItemStack.EMPTY;
        } else {
            removed = chestItem.split(amount);
        }
        setChanged();
        return removed;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        if (slot != STORED_SLOT) {
            return ItemStack.EMPTY;
        }
        ItemStack removed = chestItem;
        chestItem = ItemStack.EMPTY;
        return removed;
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        if (slot == INPUT_SLOT) {
            insertFromAutomation(stack);
        } else {
            replaceStored(stack);
        }
        setChanged();
    }

    @Override
    public int getMaxStackSize() {
        return MAX_COUNT;
    }

    @Override
    public void setChanged() {
        listener.onContentsChanged();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return listener.isUsableBy(player);
    }

    @Override
    public void clearContent() {
        chestItem = ItemStack.EMPTY;
    }

    @Override
    public boolean canPlaceItem(int slot, @NotNull ItemStack stack) {
        return slot == INPUT_SLOT && (!hasStack() || ItemStack.isSameItemSameComponents(chestItem, stack));
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction side) {
        return SLOTS_FOR_ALL_FACES;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, @NotNull ItemStack stack, @Nullable Direction direction) {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, @NotNull ItemStack stack, @NotNull Direction direction) {
        return slot == STORED_SLOT;
    }

    /**
     * 搬入口の見かけの中身を返す。満杯時だけ塞がって見せることでホッパーの搬入を止める。
     */
    private ItemStack createInputView() {
        return isMax() ? chestItem.copyWithCount(chestItem.getMaxStackSize()) : ItemStack.EMPTY;
    }

    private void insertFromAutomation(ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        int overflow = stack.getCount() - accept(stack);
        if (overflow > 0) {
            listener.onOverflow(stack.copyWithCount(overflow));
        }
    }

    /** 容量と同一性だけを判定して格納する。格納可否の方針は呼び出し側が決める。 */
    private int accept(ItemStack stack) {
        if (!hasStack()) {
            replaceStored(stack);
            setChanged();
            return chestItem.getCount();
        }
        if (!isSameContents(stack)) {
            return 0;
        }
        int accepted = Math.min(stack.getCount(), MAX_COUNT - chestItem.getCount());
        if (accepted <= 0) {
            return 0;
        }
        chestItem.grow(accepted);
        setChanged();
        return accepted;
    }

    private void replaceStored(ItemStack stack) {
        chestItem = stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(Math.min(stack.getCount(), MAX_COUNT));
    }
}
