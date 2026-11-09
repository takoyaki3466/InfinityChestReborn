package com.takoy3466.infChestReborn.dataComponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.takoy3466.infChestReborn.core.original.InfChestStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

public record InfChestContents(ItemStack item, int count) {

    public static final Codec<InfChestContents> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.SINGLE_ITEM_CODEC.fieldOf("item").forGetter(InfChestContents::item),
            ExtraCodecs.intRange(1, InfChestStack.MAX_COUNT).fieldOf("Count").forGetter(InfChestContents::count)
    ).apply(instance, InfChestContents::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, InfChestContents> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, InfChestContents::item,
            ByteBufCodecs.VAR_INT, InfChestContents::count,
            InfChestContents::new
    );

    public InfChestContents {
        item = item.copyWithCount(1);
    }

    public static InfChestContents of(ItemStack stored) {
        return new InfChestContents(stored, stored.getCount());
    }

    public ItemStack toStack() {
        return item.copyWithCount(count);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof InfChestContents(ItemStack item1, int count1) && count == count1 && ItemStack.isSameItemSameComponents(item, item1);
    }

    @Override
    public int hashCode() {
        return 31 * ItemStack.hashItemAndComponents(item) + count;
    }
}
