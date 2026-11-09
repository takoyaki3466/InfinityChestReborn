package com.takoy3466.infChestReborn.block.blockentity;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicOps;
import com.takoy3466.infChestReborn.core.original.InfChestStack;
import com.takoy3466.infChestReborn.dataComponent.InfChestContents;
import com.takoy3466.infChestReborn.init.CompatBlockEntities;
import com.takoy3466.infChestReborn.init.CompatBlocks;
import com.takoy3466.infChestReborn.init.CompatDataComponents;
import com.takoy3466.infChestReborn.menu.MenuInfChest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class BlockEntityInfChest extends BlockEntity implements MenuProvider {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String TAG_CHEST_ITEM = "chestItem";

    private static final float OVERFLOW_POSITION_RANGE = 0.8F;
    private static final float OVERFLOW_POSITION_MARGIN = 0.1F;
    private static final double OVERFLOW_HEIGHT_OFFSET = 0.5;
    private static final double OVERFLOW_VELOCITY = 0.05;

    private final InfChestStack chestStack = new InfChestStack(new ChestListener());

    public BlockEntityInfChest(BlockPos pos, BlockState blockState) {
        super(CompatBlockEntities.INF_CHEST.get(), pos, blockState);
    }

    public InfChestStack getChestStack() {
        return chestStack;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return CompatBlocks.INF_CHEST.getBlock().getName();
    }

    @Override
    public @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new MenuInfChest(id, inventory, chestStack, ContainerLevelAccess.create(level, worldPosition));
    }

    // クライアントは空の更新タグを読み込まないため、取り出しで空になった状態も伝わるよう空でもキーを書く。
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(TAG_CHEST_ITEM, encodeContents(registries));
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        chestStack.restore(decodeContents(tag, registries));
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return saveCustomOnly(registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.@NotNull DataComponentInput input) {
        super.applyImplicitComponents(input);
        InfChestContents contents = input.get(CompatDataComponents.INF_CHEST_CONTENTS.get());
        if (contents != null) {
            chestStack.restore(contents.toStack());
        }
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder components) {
        super.collectImplicitComponents(components);
        InfChestContents contents = chestStack.toContents();
        if (contents != null) {
            components.set(CompatDataComponents.INF_CHEST_CONTENTS.get(), contents);
        }
    }

    @Override
    public void removeComponentsFromTag(@NotNull CompoundTag tag) {
        tag.remove(TAG_CHEST_ITEM);
    }

    private Tag encodeContents(HolderLookup.Provider registries) {
        InfChestContents contents = chestStack.toContents();
        if (contents == null) {
            return new CompoundTag();
        }
        DynamicOps<Tag> ops = registries.createSerializationContext(NbtOps.INSTANCE);
        return InfChestContents.CODEC.encodeStart(ops, contents)
                .resultOrPartial(error -> LOGGER.error("Failed to save chest contents at {}: {}", worldPosition, error))
                .orElseGet(CompoundTag::new);
    }

    private ItemStack decodeContents(CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag encoded = tag.getCompound(TAG_CHEST_ITEM);
        if (encoded.isEmpty()) {
            return ItemStack.EMPTY;
        }
        DynamicOps<Tag> ops = registries.createSerializationContext(NbtOps.INSTANCE);
        return InfChestContents.CODEC.parse(ops, encoded)
                .resultOrPartial(error -> LOGGER.error("Failed to load chest contents at {}: {}", worldPosition, error))
                .map(InfChestContents::toStack)
                .orElse(ItemStack.EMPTY);
    }

    private void popOverflow(ItemStack overflow) {
        if (level == null || level.isClientSide) {
            return;
        }
        RandomSource random = level.random;
        ItemEntity item = new ItemEntity(
                level,
                worldPosition.getX() + randomOffset(random),
                worldPosition.getY() + randomOffset(random) + OVERFLOW_HEIGHT_OFFSET,
                worldPosition.getZ() + randomOffset(random),
                overflow);
        item.setDeltaMovement(
                random.nextGaussian() * OVERFLOW_VELOCITY,
                random.nextGaussian() * OVERFLOW_VELOCITY,
                random.nextGaussian() * OVERFLOW_VELOCITY);
        level.addFreshEntity(item);
    }

    private static float randomOffset(RandomSource random) {
        return random.nextFloat() * OVERFLOW_POSITION_RANGE + OVERFLOW_POSITION_MARGIN;
    }

    private final class ChestListener implements InfChestStack.Listener {

        @Override
        public void onContentsChanged() {
            setChanged();
            Level currentLevel = getLevel();
            if (currentLevel != null && !currentLevel.isClientSide) {
                BlockState state = getBlockState();
                currentLevel.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_CLIENTS);
            }
        }

        @Override
        public void onOverflow(ItemStack overflow) {
            popOverflow(overflow);
        }

        @Override
        public boolean isUsableBy(Player player) {
            return Container.stillValidBlockEntity(BlockEntityInfChest.this, player);
        }
    }
}
