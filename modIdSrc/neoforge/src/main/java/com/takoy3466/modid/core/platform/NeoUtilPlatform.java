package com.takoy3466.modid.core.platform;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;

public class NeoUtilPlatform implements IUtilPlatform {
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

    @Override
    public int onArrowLoose(ItemStack stack, Level level, Player player, int charge, boolean hasAmmo) {
        return EventHooks.onArrowLoose(stack, level, player, charge, hasAmmo);
    }
}
