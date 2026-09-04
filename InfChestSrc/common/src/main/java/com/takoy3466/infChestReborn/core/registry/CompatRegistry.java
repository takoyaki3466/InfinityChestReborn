package com.takoy3466.infChestReborn.core.registry;

import com.mojang.serialization.Codec;
import com.takoy3466.infChestReborn.core.BlockEntitySup;
import com.takoy3466.infChestReborn.core.platform.Services;
import com.takoy3466.infChestReborn.core.registry.holder.CompatDoubleHolder;
import com.takoy3466.infChestReborn.core.registry.holder.CompatHolder;
import com.takoy3466.infChestReborn.init.CompatTabs;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class CompatRegistry {

    public static <T extends Item> CompatHolder<T> registerItem(String id, Supplier<T> supplier) {
        CompatHolder<T> compatHolder = CompatHolder.create(id);
        CompatTabs.ITEMS.add(compatHolder);
        Services.REGISTRY.registerItem(compatHolder, supplier);
        return compatHolder;
    }

    public static <B extends Block, U extends BlockItem> CompatDoubleHolder.BlockHolder<B> registerBlock(String id, Supplier<B> blockSup, Supplier<U> itemSup) {
        CompatHolder<B> blockHolder = CompatHolder.create(id);
        CompatHolder<BlockItem> itemHolder = CompatHolder.create(id);
        CompatTabs.ITEMS.add(itemHolder);
        CompatDoubleHolder.BlockHolder<B> doubleHolder = CompatDoubleHolder.BlockHolder.of(blockHolder, itemHolder);
        Services.REGISTRY.registerBlock(doubleHolder, blockSup, itemSup);
        return doubleHolder;
    }

    public static <B extends Block, U extends BlockItem> CompatDoubleHolder.BlockHolder<B> registerBlock(String id, Supplier<B> blockSup, Item.Properties properties) {
        return registerBlock(id, blockSup, () -> new BlockItem(blockSup.get(), properties));
    }

    public static <T extends BlockEntity> CompatHolder<BlockEntityType<T>> registerBlockEntityType(String id, BlockEntitySup<T> supplier, CompatDoubleHolder.BlockHolder<? extends Block> blockHolder) {
        CompatHolder<BlockEntityType<T>> compatHolder = CompatHolder.create(id);
        Services.REGISTRY.registerBlockEntityType(compatHolder, supplier, blockHolder);
        return compatHolder;
    }



    public static <T> CompatHolder<DataComponentType<T>> registerDataComponentType(String id, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        CompatHolder<DataComponentType<T>> compatHolder = CompatHolder.create(id);
        Services.REGISTRY.registerDataComponentType(compatHolder, codec, streamCodec);
        return compatHolder;
    }

    public static <T extends Recipe<?>> CompatHolder<RecipeSerializer<T>> registerRecipeSerializer(String id, Supplier<RecipeSerializer<T>> supplier) {
        CompatHolder<RecipeSerializer<T>> compatHolder = CompatHolder.create(id);
        Services.REGISTRY.registerRecipeSerializer(compatHolder, supplier);
        return compatHolder;
    }

    public static <T extends CreativeModeTab> CompatHolder<T> registerCreativeTab(String id, Supplier<T> supplier) {
        CompatHolder<T> compatHolder = CompatHolder.create(id);
        Services.REGISTRY.registerCreativeTab(compatHolder, supplier);
        return compatHolder;
    }

}
