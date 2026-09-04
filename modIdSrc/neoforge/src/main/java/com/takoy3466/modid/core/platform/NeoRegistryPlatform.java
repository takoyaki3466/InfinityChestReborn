package com.takoy3466.modid.core.platform;

import com.mojang.serialization.Codec;
import com.takoy3466.modid.ModIdCommon;
import com.takoy3466.modid.core.BlockEntitySup;
import com.takoy3466.modid.core.registry.holder.CompatDoubleHolder;
import com.takoy3466.modid.core.registry.holder.CompatHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
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
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class NeoRegistryPlatform implements IRegistryPlatform {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ModIdCommon.MOD_ID);
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ModIdCommon.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ModIdCommon.MOD_ID);
    private static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ModIdCommon.MOD_ID);
    private    static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ModIdCommon.MOD_ID);
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModIdCommon.MOD_ID);


    public static void register(IEventBus bus) {
        DATA_COMPONENTS.register(bus);
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
        SERIALIZERS.register(bus);
        TABS.register(bus);
    }

    @Override
    public <T extends Item> void registerItem(CompatHolder<T> compatHolder, Supplier<T> supplier) {
        DeferredItem<T> deferredItem = ITEMS.register(compatHolder.getId(), supplier);
        compatHolder.set(deferredItem);
    }

    @Override
    public <T extends Block, U extends BlockItem> void registerBlock(CompatDoubleHolder.BlockHolder<T> doubleHolder, Supplier<T> blockSup, Supplier<U> itemSup) {
        DeferredBlock<T> deferredBlock = BLOCKS.register(doubleHolder.getBlockHolder().getId(), blockSup);
        DeferredItem<BlockItem> deferredItem = ITEMS.register(doubleHolder.getItemHolder().getId(), itemSup);
        doubleHolder.getBlockHolder().set(deferredBlock);
        doubleHolder.getItemHolder().set(deferredItem);
    }

    @Override
    public <T extends BlockEntity> void registerBlockEntityType(CompatHolder<BlockEntityType<T>> compatHolder, BlockEntitySup<T> supplier, CompatDoubleHolder.BlockHolder<? extends Block> blockHolder) {
        DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> deferredHolder = BLOCK_ENTITIES.register(compatHolder.getId(), () -> BlockEntityType.Builder.of(supplier::create, blockHolder.getBlock()).build(null));
        compatHolder.set(deferredHolder);
    }

    @Override
    public <T> void registerDataComponentType(CompatHolder<DataComponentType<T>> compatHolder, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        DeferredHolder<DataComponentType<?>, DataComponentType<T>> deferredHolder = DATA_COMPONENTS.register(compatHolder.getId(), () -> DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build());
        compatHolder.set(deferredHolder);
    }

    @Override
    public <T extends Recipe<?>> void registerRecipeSerializer(CompatHolder<RecipeSerializer<T>> compatHolder, Supplier<RecipeSerializer<T>> supplier) {
        DeferredHolder<RecipeSerializer<?>, RecipeSerializer<T>> deferredHolder = SERIALIZERS.register(compatHolder.getId(), supplier);
        compatHolder.set(deferredHolder);
    }

    @Override
    public <T extends CreativeModeTab> void registerCreativeTab(CompatHolder<T> compatHolder, Supplier<T> supplier) {
        DeferredHolder<CreativeModeTab, T> deferredHolder = TABS.register(compatHolder.getId(), supplier);
        compatHolder.set(deferredHolder);
    }
}
