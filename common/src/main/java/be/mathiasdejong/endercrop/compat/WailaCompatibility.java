package be.mathiasdejong.endercrop.compat;

import be.mathiasdejong.endercrop.HoeHelper;
import be.mathiasdejong.endercrop.Reference;
import be.mathiasdejong.endercrop.block.EnderCropBlock;
import be.mathiasdejong.endercrop.config.EnderCropConfiguration;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec2;

import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.SubTextElement;

import static net.minecraft.ChatFormatting.*;

@WailaPlugin
public class WailaCompatibility implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(EnderCropGrowthProvider.INSTANCE, EnderCropBlock.class);
        registration.registerBlockComponent(EndStoneTillingProvider.INSTANCE, Block.class);
    }

    public enum EnderCropGrowthProvider implements IBlockComponentProvider {
        INSTANCE;

        private static final Component NO_GROWTH =
            Component.translatable("endercrop.wailatop.nogrowth").withStyle(RED);
        private static final Component LIGHT_LEVEL =
            Component.translatable("endercrop.wailatop.light").append(": ").withStyle(YELLOW);

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig pluginConfig) {
            final EnderCropBlock enderCrop = (EnderCropBlock) blockAccessor.getBlock();

            if (!enderCrop.isMaxAge(blockAccessor.getBlockState())) {
                if (!enderCrop.isDarkEnough(blockAccessor.getLevel(), blockAccessor.getPosition())) {
                    tooltip.add(NO_GROWTH);
                    if (blockAccessor.getPlayer().isCrouching()) {
                        final int lightLevel = blockAccessor.getLevel().getRawBrightness(blockAccessor.getPosition(),
                            0);
                        tooltip.add(LIGHT_LEVEL.copy().append(Component.literal(lightLevel + " (>7)").withStyle(RED)));
                    }
                }
            }
        }

        @Override
        public ResourceLocation getUid() {
            return new ResourceLocation(Reference.MOD_ID, Reference.Blocks.ENDER_CROP);
        }
    }

    public enum EndStoneTillingProvider implements IBlockComponentProvider {
        INSTANCE;

        private static final Component CHECK = Component.literal("✔").withStyle(GREEN);
        private static final Component X = Component.literal("✕").withStyle(RED);
        private static final Component TILL = Component.translatable("endercrop.waila:till");
        private static final Component UNBREAKING_HINT = Component.literal(" (")
            .append(
                Component.translatable("enchantment.minecraft.unbreaking")
                    .append(" I+")
                    .withStyle(RED)
            )
            .append(")");

        private static final ItemStack HOE = new ItemStack(Items.WOODEN_HOE);
        private static final ItemStack ENCHANTED_HOE = HOE.copy();

        static {
            ENCHANTED_HOE.enchant(Enchantments.UNBREAKING, 1);
        }

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig pluginConfig) {
            final IElementHelper elements = tooltip.getElementHelper();

            if (blockAccessor.getBlock().equals(Blocks.END_STONE) && EnderCropConfiguration.tilledEndStone.get()) {
                final ItemStack hoeStack = HoeHelper.holdingHoeTool(blockAccessor.getPlayer());
                if (!hoeStack.isEmpty()) {
                    final boolean canTill = HoeHelper.canTillEndstone(hoeStack, blockAccessor.getPlayer());
                    tooltip.add(elements.item(EnderCropConfiguration.endstoneNeedsUnbreaking.get() ? ENCHANTED_HOE :
                        HOE, 0.75F).size(new Vec2(10, 13)).translate(new Vec2(-2, -2)));
                    tooltip.append(new SubTextElement(canTill ? CHECK : X).translate(new Vec2(-3.5f, 6)));
                    tooltip.append(elements.spacer(4, 0));
                    tooltip.append(elements.text(TILL.copy().append(unbreakingHint(canTill))).translate(new Vec2(0, 2)));
                }
            }
        }

        private Component unbreakingHint(boolean canTill) {
            if (EnderCropConfiguration.endstoneNeedsUnbreaking.get() && !canTill)
                return UNBREAKING_HINT;
            else return Component.empty();
        }

        @Override
        public ResourceLocation getUid() {
            return new ResourceLocation(Reference.MOD_ID, Reference.Blocks.TILLED_END_STONE);
        }
    }
}