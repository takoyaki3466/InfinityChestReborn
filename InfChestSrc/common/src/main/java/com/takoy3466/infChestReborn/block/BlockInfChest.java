package com.takoy3466.infChestReborn.block;

import com.mojang.serialization.MapCodec;
import com.takoy3466.infChestReborn.block.blockentity.BlockEntityInfChest;
import com.takoy3466.infChestReborn.core.original.InfChestNumberFormat;
import com.takoy3466.infChestReborn.dataComponent.InfChestContents;
import com.takoy3466.infChestReborn.init.CompatDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockInfChest extends BaseEntityBlock implements WorldlyContainerHolder {

    public static final MapCodec<BlockInfChest> CODEC = simpleCodec(BlockInfChest::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);
    private static final String TOOLTIP_INDENT = "  ";
    private static final double DROP_CENTER_OFFSET = 0.5;
    private static final double DROP_UPWARD_VELOCITY = 0.2;

    public BlockInfChest(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BlockEntityInfChest(pos, state);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (level.getBlockEntity(pos) instanceof BlockEntityInfChest chest) {
            player.openMenu(chest);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof BlockEntityInfChest chest) {
            dropChestWithContents(level, pos, chest);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        InfChestContents contents = stack.get(CompatDataComponents.INF_CHEST_CONTENTS.get());
        if (contents == null) {
            return;
        }
        ItemStack item = contents.item();
        tooltip.add(Component.literal(TOOLTIP_INDENT).append(item.getHoverName()));
        tooltip.add(Component.literal(TOOLTIP_INDENT + InfChestNumberFormat.formatStack(contents.count())));
        item.getItem().appendHoverText(item, context, tooltip, flag);
    }

    @Override
    public WorldlyContainer getContainer(@NotNull BlockState state, LevelAccessor level, @NotNull BlockPos pos) {
        return level.getBlockEntity(pos) instanceof BlockEntityInfChest chest ? chest.getChestStack() : null;
    }

    private void dropChestWithContents(Level level, BlockPos pos, BlockEntityInfChest chest) {
        ItemStack drop = new ItemStack(this);
        drop.applyComponents(chest.collectComponents());
        level.addFreshEntity(new ItemEntity(level, pos.getX() + DROP_CENTER_OFFSET, pos.getY() + DROP_CENTER_OFFSET, pos.getZ() + DROP_CENTER_OFFSET, drop, 0.0, DROP_UPWARD_VELOCITY, 0.0));
    }
}
