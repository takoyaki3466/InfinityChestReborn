package com.takoy3466.infChestReborn.core.platform;

import com.mojang.serialization.Codec;
import com.takoy3466.infChestReborn.InfChestCommon;
import com.takoy3466.infChestReborn.core.BlockEntitySup;
import com.takoy3466.infChestReborn.core.Identifier;
import com.takoy3466.infChestReborn.core.registry.holder.CompatDoubleHolder;
import com.takoy3466.infChestReborn.core.registry.holder.CompatHolder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
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

public class FabricRegistryPlatform implements IRegistryPlatform {
    @Override
    public <T extends Item> void registerItem(CompatHolder<T> compatHolder, Supplier<T> supplier) {
        Identifier identifier = new Identifier(InfChestCommon.MOD_ID, compatHolder.getId());
        T registered = Registry.register(BuiltInRegistries.ITEM, identifier.get(), supplier.get());
        compatHolder.set(() -> registered);
    }

    @Override
    public <T extends Block, U extends BlockItem> void registerBlock(CompatDoubleHolder.BlockHolder<T> doubleHolder, Supplier<T> blockSup, Supplier<U> itemSup) {
        Identifier identifier = new Identifier(InfChestCommon.MOD_ID, doubleHolder.getBlockHolder().getId());
        T registeredBlock = Registry.register(BuiltInRegistries.BLOCK, identifier.get(), blockSup.get());
        BlockItem registeredItem = Registry.register(BuiltInRegistries.ITEM, identifier.get(), itemSup.get());
        doubleHolder.getBlockHolder().set(() -> registeredBlock);
        doubleHolder.getItemHolder().set(() -> registeredItem);
    }

    @Override
    public <T extends BlockEntity> void registerBlockEntityType(CompatHolder<BlockEntityType<T>> compatHolder, BlockEntitySup<T> supplier, CompatDoubleHolder.BlockHolder<? extends Block> blockHolder) {
        Identifier identifier = new Identifier(InfChestCommon.MOD_ID, compatHolder.getId());
        BlockEntityType<T> registered = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, identifier.get(), BlockEntityType.Builder.of(supplier::create, blockHolder.getBlock()).build(null));
        compatHolder.set(() -> registered);
    }

    @Override
    public <T> void registerDataComponentType(CompatHolder<DataComponentType<T>> compatHolder, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        Identifier identifier = new Identifier(InfChestCommon.MOD_ID, compatHolder.getId());
        DataComponentType<T> registered = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, identifier.get(), DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build());
        compatHolder.set(() -> registered);
    }

    @Override
    public <T extends Recipe<?>> void registerRecipeSerializer(CompatHolder<RecipeSerializer<T>> compatHolder, Supplier<RecipeSerializer<T>> supplier) {
        Identifier identifier = new Identifier(InfChestCommon.MOD_ID, compatHolder.getId());
        RecipeSerializer<T> registered = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, identifier.get(), supplier.get());
        compatHolder.set(() -> registered);
    }

    @Override
    public <T extends CreativeModeTab> void registerCreativeTab(CompatHolder<T> compatHolder, Supplier<T> supplier) {
        Identifier identifier = new Identifier(InfChestCommon.MOD_ID, compatHolder.getId());
        T registered = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, identifier.get(), supplier.get());
        compatHolder.set(() -> registered);
    }
}
