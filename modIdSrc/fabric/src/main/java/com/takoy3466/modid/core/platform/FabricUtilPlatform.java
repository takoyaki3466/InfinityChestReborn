package com.takoy3466.modid.core.platform;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class FabricUtilPlatform implements IUtilPlatform {
    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return getCraftingRemainingItem(stack) != null;
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
        return FuelRegistry.INSTANCE.get(stack.getItem());
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return stack.getRecipeRemainder();
    }

    @Override
    public int onArrowLoose(ItemStack stack, Level level, Player player, int charge, boolean hasAmmo) {
        return charge;
    }
}
