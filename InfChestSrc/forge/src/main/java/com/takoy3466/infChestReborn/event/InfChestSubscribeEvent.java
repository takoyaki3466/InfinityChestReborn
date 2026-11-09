package com.takoy3466.infChestReborn.event;

import com.takoy3466.infChestReborn.InfChestCommon;
import com.takoy3466.infChestReborn.block.blockentity.BlockEntityInfChest;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = InfChestCommon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class InfChestSubscribeEvent {

    private static final ResourceLocation ITEM_HANDLER_ID = ResourceLocation.fromNamespaceAndPath(InfChestCommon.MOD_ID, "item_handler");

    private InfChestSubscribeEvent() {
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        if (event.getObject() instanceof BlockEntityInfChest chest) {
            ItemHandlerProvider provider = new ItemHandlerProvider(chest);
            event.addCapability(ITEM_HANDLER_ID, provider);
            event.addListener(provider::invalidate);
        }
    }

    private static final class ItemHandlerProvider implements ICapabilityProvider {
        private final LazyOptional<IItemHandlerModifiable>[] handlers;

        private ItemHandlerProvider(BlockEntityInfChest chest) {
            this.handlers = SidedInvWrapper.create(chest.getChestStack(), Direction.values());
        }

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
            if (capability != ForgeCapabilities.ITEM_HANDLER) {
                return LazyOptional.empty();
            }

            Direction effectiveSide = side == null ? Direction.UP : side;
            return handlers[effectiveSide.ordinal()].cast();
        }

        private void invalidate() {
            for (LazyOptional<IItemHandlerModifiable> handler : handlers) {
                handler.invalidate();
            }
        }
    }
}
