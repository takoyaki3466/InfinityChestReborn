package com.takoy3466.infChestReborn.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.takoy3466.infChestReborn.block.BlockInfChest;
import com.takoy3466.infChestReborn.block.blockentity.BlockEntityInfChest;
import com.takoy3466.infChestReborn.core.original.InfChestNumberFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;


public class InfChestBlockEntityRenderer implements BlockEntityRenderer<BlockEntityInfChest> {

    private static final float CENTER = 0.5F;
    private static final float BLOCK_ITEM_HEIGHT = 0.35F;
    private static final float BLOCK_ITEM_SCALE = 0.6F;
    private static final float FLAT_ITEM_HEIGHT = 0.32F;
    private static final float FLAT_ITEM_SCALE = 0.72F;
    private static final float BLOCK_MODEL_FRONT_ROTATION = 180.0F;

    private static final float COUNT_TEXT_SCALE = 0.01F;
    private static final int COUNT_TEXT_COLOR = 0xFFFFFF;
    private static final int NO_BACKGROUND = 0;

    private final ItemRenderer itemRenderer;
    private final Font font;

    public InfChestBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
        this.font = context.getFont();
    }

    @Override
    public void render(BlockEntityInfChest chest, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack stored = chest.getChestStack().getStoredItem();
        if (stored.isEmpty()) {
            return;
        }
        renderStoredItem(chest, stored, poseStack, bufferSource, packedLight, packedOverlay);
        Direction aimedFace = findAimedFace(chest.getBlockPos());
        if (aimedFace != null) {
            renderCount(stored.getCount(), aimedFace, poseStack, bufferSource);
        }
    }

    private void renderStoredItem(BlockEntityInfChest chest, ItemStack stored, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        boolean blockModel = itemRenderer.getModel(stored, chest.getLevel(), null, 0).isGui3d();

        float height = blockModel ? BLOCK_ITEM_HEIGHT : FLAT_ITEM_HEIGHT;
        float scale = blockModel ? BLOCK_ITEM_SCALE : FLAT_ITEM_SCALE;

        float frontRotation = blockModel ? BLOCK_MODEL_FRONT_ROTATION : 0.0F;
        float facingRotation = -chest.getBlockState().getValue(BlockInfChest.FACING).toYRot();

        poseStack.pushPose();
        poseStack.translate(CENTER, height, CENTER);
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(Axis.YP.rotationDegrees(facingRotation + frontRotation));
        itemRenderer.renderStatic(stored, ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, bufferSource, chest.getLevel(), 0);
        poseStack.popPose();
    }

    private void renderCount(int count, Direction face, PoseStack poseStack, MultiBufferSource bufferSource) {
        String text = InfChestNumberFormat.formatStack(count);

        poseStack.pushPose();
        poseStack.translate(CENTER + CENTER * face.getStepX(), CENTER + CENTER * face.getStepY(), CENTER + CENTER * face.getStepZ());
        poseStack.scale(-COUNT_TEXT_SCALE, -COUNT_TEXT_SCALE, COUNT_TEXT_SCALE);
        poseStack.mulPose(Axis.YP.rotationDegrees(textYaw(face)));
        poseStack.mulPose(Axis.XP.rotationDegrees(textPitch(face)));
        font.drawInBatch(text, -font.width(text) / 2.0F, 0.0F, COUNT_TEXT_COLOR, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, NO_BACKGROUND, LightTexture.FULL_BRIGHT);
        poseStack.popPose();
    }

    private static Direction findAimedFace(BlockPos chestPos) {
        Minecraft minecraft = Minecraft.getInstance();
        HitResult hit = minecraft.hitResult;
        if (minecraft.options.hideGui || !(hit instanceof BlockHitResult blockHit) || blockHit.getType() != HitResult.Type.BLOCK) {
            return null;
        }
        return blockHit.getBlockPos().equals(chestPos) ? blockHit.getDirection() : null;
    }

    private static float textYaw(Direction face) {
        return switch (face) {
            case SOUTH -> 180.0F;
            case WEST -> 270.0F;
            case EAST -> 90.0F;
            default -> 0.0F;
        };
    }

    private static float textPitch(Direction face) {
        return switch (face) {
            case UP -> -90.0F;
            case DOWN -> 90.0F;
            default -> 0.0F;
        };
    }
}
