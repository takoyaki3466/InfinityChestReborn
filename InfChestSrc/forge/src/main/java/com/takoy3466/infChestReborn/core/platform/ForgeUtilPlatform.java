package com.takoy3466.infChestReborn.core.platform;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

public class ForgeUtilPlatform implements IUtilPlatform {
    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return stack.hasCraftingRemainingItem();
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
        return stack.getBurnTime(recipeType);
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return stack.getCraftingRemainingItem();
    }

    // 不安定らしい
    @Override
    public int onArrowLoose(ItemStack stack, Level level, Player player, int charge, boolean hasAmmo) {
        return ForgeEventFactory.onArrowLoose(stack, level, player, charge, hasAmmo);
    }
}
