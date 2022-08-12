package io.github.drmanganese.endercrop.compat;

import io.github.drmanganese.endercrop.HoeHelper;
import io.github.drmanganese.endercrop.block.EnderCropBlock;
import io.github.drmanganese.endercrop.configuration.EnderCropConfiguration;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.SubTextElement;

@WailaPlugin
public class WailaCompatibility implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(EnderCropGrowthProvider.INSTANCE, EnderCropBlock.class);
        registration.registerBlockComponent(EndStoneTillingProvider.INSTANCE, Block.class);
    }

    public enum EnderCropGrowthProvider implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig pluginConfig) {
            final EnderCropBlock enderCrop = (EnderCropBlock) blockAccessor.getBlock();

            if (!enderCrop.isMaxAge(blockAccessor.getBlockState())) {
                if (!enderCrop.isDarkEnough(blockAccessor.getLevel(), blockAccessor.getPosition())) {
                    tooltip.add(
                        Component
                            .translatable("endercrop.wailatop.nogrowth")
                            .withStyle(ChatFormatting.RED)
                    );
                    if (blockAccessor.getPlayer().isCrouching()) {
                        final int lightLevel = blockAccessor.getLevel().getRawBrightness(blockAccessor.getPosition(), 0);
                        tooltip.add(
                            Component
                                .translatable("endercrop.wailatop.light")
                                .append(": ")
                                .withStyle(ChatFormatting.YELLOW)
                                .append(Component.literal(lightLevel + " (>7)")
                                    .withStyle(ChatFormatting.RED))
                        );
                    }
                }
            }
        }

        @Override
        public ResourceLocation getUid() {
            return new ResourceLocation("endercrop:endercrop");
        }
    }

    public enum EndStoneTillingProvider implements IBlockComponentProvider {
        INSTANCE;

        private static final Component CHECK = Component.literal("\u2714").withStyle(ChatFormatting.GREEN);
        private static final Component X = Component.literal("\u2715").withStyle(ChatFormatting.RED);
        private static final ItemStack HOE = new ItemStack(Items.WOODEN_HOE);

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig pluginConfig) {
            final IElementHelper elements = tooltip.getElementHelper();

            if (blockAccessor.getBlock().equals(Blocks.END_STONE) && EnderCropConfiguration.tilledEndStone.get()) {
                final ItemStack hoeStack = HoeHelper.holdingHoeTool(blockAccessor.getPlayer());
                if (!hoeStack.isEmpty()) {
                    final boolean canTill = HoeHelper.canHoeEndstone(hoeStack, blockAccessor.getPlayer(), blockAccessor.getBlockState());
                    tooltip.add(
                        elements
                            .item(HOE, 0.75F)
                            .size(new Vec2(10, 13))
                            .translate(new Vec2(0, -2))
                    );
                    tooltip.append(new SubTextElement(canTill ? CHECK : X).translate(new Vec2(-3.5f, 6)));
                    tooltip.append(elements.spacer(4, 0));
                    tooltip.append(
                        elements
                            .text(Component.literal("Till").append(unbreakingHint(canTill)))
                            .translate(new Vec2(0, 2))
                    );
                }
            }
        }

        private Component unbreakingHint(boolean canTill) {
            if (EnderCropConfiguration.endstoneNeedsUnbreaking.get() && !canTill)
                return Component
                    .literal(" (")
                    .append(Component.translatable("enchantment.minecraft.unbreaking").append(" I+").withStyle(ChatFormatting.RED))
                    .append(")");
            else
                return Component.empty();
        }

        @Override
        public ResourceLocation getUid() {
            return new ResourceLocation("endercrop", "endstone");
        }
    }
}
