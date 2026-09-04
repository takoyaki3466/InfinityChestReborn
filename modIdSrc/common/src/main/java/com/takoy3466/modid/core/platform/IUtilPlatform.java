package com.takoy3466.modid.core.platform;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface IUtilPlatform {

    boolean hasCraftingRemainingItem(ItemStack stack);

    int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType);

    ItemStack getCraftingRemainingItem(ItemStack stack);

    int onArrowLoose(ItemStack stack, Level level, Player player, int charge, boolean hasAmmo);
}
