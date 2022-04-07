package io.github.drmanganese.endercrop.compat;

import com.google.common.base.Function;
import io.github.drmanganese.endercrop.EnderCrop;
import io.github.drmanganese.endercrop.HoeHelper;
import io.github.drmanganese.endercrop.block.EnderCropBlock;
import io.github.drmanganese.endercrop.block.TilledEndstoneBlock;
import io.github.drmanganese.endercrop.configuration.EnderCropConfiguration;
import io.github.drmanganese.endercrop.init.ModBlocks;
import mcjty.theoneprobe.api.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.InterModComms;

import javax.annotation.Nullable;

public final class TOPCompatibility implements Function<ITheOneProbe, Void> {
    public static ITheOneProbe probe;

    public static void register() {
        InterModComms.sendTo("theoneprobe", "GetTheOneProbe", TOPCompatibility::new);
    }

    @Nullable
    @Override
    public Void apply(ITheOneProbe theOneProbe) {
        probe = theOneProbe;
        probe.registerProvider(new IProbeInfoProvider() {
            @Override
            public ResourceLocation getID() {
                return new ResourceLocation(EnderCrop.MOD_ID);
            }

            @Override
            public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world, BlockState blockState, IProbeHitData data) {
                if (blockState.is(ModBlocks.TILLED_END_STONE.get())) {
                    if (mode == ProbeMode.EXTENDED) {
                        if (blockState.getValue(TilledEndstoneBlock.MOISTURE) == 7) {
                            probeInfo.text(CompoundText.create().label("{*endercrop.wailatop.moist*}"));
                        } else {
                            probeInfo.text(CompoundText.create().label("{*endercrop.wailatop.dry*}"));
                        }
                    }
                    if (mode == ProbeMode.DEBUG) {
                        probeInfo.text(CompoundText.create().labelInfo("MOISTURE: ", blockState.getValue(TilledEndstoneBlock.MOISTURE)));
                    }
                } else if (blockState.is(ModBlocks.ENDER_CROP.get())) {
                    final EnderCropBlock enderCrop = (EnderCropBlock) blockState.getBlock();

                    if (!enderCrop.isMaxAge(blockState)) {
                        if (!enderCrop.isDarkEnough(world, data.getPos())) {
                            probeInfo.text(CompoundText.create().error("{*endercrop.wailatop.nogrowth*}"));
                            if (mode == ProbeMode.EXTENDED) {
                                final int lightLevel = world.getRawBrightness(data.getPos(), 0);
                                probeInfo.text(CompoundText.create().label("{*endercrop.wailatop.light*}: ").info(String.valueOf(lightLevel)).error(" (>7)"));
                            }
                        }
                    }
                } else if (blockState.is(Blocks.END_STONE) && EnderCropConfiguration.tilledEndStone.get()) {
                    final ItemStack hoeStack = HoeHelper.holdingHoeTool(player);
                    if (!hoeStack.isEmpty()) {
                        final IProbeInfo hori = probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER));
                        if (HoeHelper.canHoeEndstone(hoeStack, player, blockState)) {
                            hori.icon(new ResourceLocation("theoneprobe", "textures/gui/icons.png"), 0, 16, 13, 13, probeInfo.defaultIconStyle().width(18).height(14).textureWidth(32).textureHeight(32));
                            hori.text(CompoundText.create().ok("{*endercrop.top.hoe*}"));
                        } else {
                            hori.icon(new ResourceLocation("theoneprobe", "textures/gui/icons.png"), 16, 16, 13, 13, probeInfo.defaultIconStyle().width(18).height(14).textureWidth(32).textureHeight(32));
                            if (hoeStack.getItem() instanceof HoeItem) {
                                hori.text(CompoundText.create().warning("{*endercrop.top.hoe*}" + (EnderCropConfiguration.endstoneNeedsUnbreaking.get() ? " ({*enchantment.minecraft.unbreaking*} I+)" : "")));
                            } else {
                                hori.text(
                                    CompoundText.create()
                                        .warning("{*endercrop.top.hoe*}")
                                        .warning(" (")
                                        .text(HoeHelper.getHarvestLevelInfo(hoeStack))
                                        .warning(")")
                                );
                            }
                        }
                    }
                }
            }
        });
        return null;
    }
}
