package com.takoy3466.infChestReborn.core.registry.holder;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CompatDoubleHolder<R, S> {
    private final CompatHolder<R> front;
    private final CompatHolder<S> behind;

    private CompatDoubleHolder(CompatHolder<R> front, CompatHolder<S> behind) {
        this.front = front;
        this.behind = behind;
    }
    
    public static <R, S> CompatDoubleHolder<R, S> of(CompatHolder<R> front, CompatHolder<S> behind) {
        return new CompatDoubleHolder<>(front, behind);
    }

    public CompatHolder<R> getFront() {
        return front;
    }

    public CompatHolder<S> getBehind() {
        return behind;
    }

    public static class BlockHolder<BLOCK extends Block> {
        private final CompatDoubleHolder<BLOCK, BlockItem> HOLDER;

        public BlockHolder(CompatDoubleHolder<BLOCK, BlockItem> holder) {
            this.HOLDER = holder;
        }

        private BlockHolder(CompatHolder<BLOCK> front, CompatHolder<BlockItem> behind) {
            this(CompatDoubleHolder.of(front, behind));
        }

        public static <B extends Block> BlockHolder<B> of(CompatHolder<B> front, CompatHolder<BlockItem> behind) {
            return new BlockHolder<>(front, behind);
        }

        public CompatHolder<BLOCK> getBlockHolder() {
            return HOLDER.getFront();
        }

        public CompatHolder<BlockItem> getItemHolder() {
            return HOLDER.getBehind();
        }

        public BLOCK getBlock() {
            return getBlockHolder().get();
        }

        public Item getItem() {
            return getItemHolder().get();
        }
    }
}
