package com.takoy3466.modid.core.platform;

import com.mojang.serialization.Codec;
import com.takoy3466.modid.core.BlockEntitySup;
import com.takoy3466.modid.core.registry.holder.CompatDoubleHolder;
import com.takoy3466.modid.core.registry.holder.CompatHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface IRegistryPlatform {

    <T extends Item> void registerItem(CompatHolder<T> compatHolder, Supplier<T> supplier);

    <T extends Block, U extends BlockItem> void registerBlock(CompatDoubleHolder.BlockHolder<T> doubleHolder, Supplier<T> blockSup, Supplier<U> itemSup);

    <T extends BlockEntity> void registerBlockEntityType(CompatHolder<BlockEntityType<T>> compatHolder, BlockEntitySup<T> supplier, CompatDoubleHolder.BlockHolder<? extends Block> blockHolder);

    <T> void registerDataComponentType(CompatHolder<DataComponentType<T>> compatHolder, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec);

    <T extends Recipe<?>> void registerRecipeSerializer(CompatHolder<RecipeSerializer<T>> compatHolder, Supplier<RecipeSerializer<T>> supplier);

    <T extends CreativeModeTab> void registerCreativeTab(CompatHolder<T> compatHolder, Supplier<T> supplier);
}
