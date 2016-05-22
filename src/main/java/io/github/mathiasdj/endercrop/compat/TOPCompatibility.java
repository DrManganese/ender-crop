package io.github.mathiasdj.endercrop.compat;

import com.google.common.base.Function;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import javax.annotation.Nullable;

import io.github.mathiasdj.endercrop.block.BlockCropEnder;
import io.github.mathiasdj.endercrop.block.BlockTilledEndStone;
import io.github.mathiasdj.endercrop.init.ModBlocks;
import io.github.mathiasdj.endercrop.reference.Reference;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;

public class TOPCompatibility {
    private static boolean registered;

    public static void register() {
        if (registered)
            return;
        registered = true;
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "io.github.mathiasdj.endercrop.compat.TOPCompatibility$GetTheOneProbe");
    }

    public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {
        public static ITheOneProbe probe;
        @Nullable
        @Override
        public Void apply(ITheOneProbe theOneProbe) {
            probe = theOneProbe;
            probe.registerProvider(new IProbeInfoProvider() {
                @Override
                public String getID() {
                    return Reference.MOD_ID;
                }

                @Override
                public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
                    if (blockState.getBlock() instanceof BlockTilledEndStone) {
                        if (mode == ProbeMode.EXTENDED) {
                            if (blockState.getValue(BlockTilledEndStone.MOISTURE) == 7) {
                                probeInfo.text(TextFormatting.DARK_GRAY + I18n.format("endercrop.waila.moist"));
                            } else {
                                probeInfo.text(TextFormatting.DARK_GRAY + I18n.format("endercrop.waila.dry"));
                            }
                        }

                        if (mode == ProbeMode.DEBUG) {
                            probeInfo.text("MOISTURE: " + blockState.getValue(BlockTilledEndStone.MOISTURE));
                        }
                    } else if (blockState.getBlock() instanceof BlockCropEnder) {
                        float age = blockState.getValue(BlockCropEnder.AGE) / 7.0F;

                        if (age == 1.0F) {
                            probeInfo.text(TextFormatting.GRAY + I18n.format("endercrop.top.growth") + " : " + I18n.format("endercrop.top.mature"));
                        } else {
                            if (world.getBlockState(data.getPos().down()).getBlock() == Blocks.FARMLAND && !ModBlocks.CROP_ENDER.canGrow(world, data.getPos(), blockState, world.isRemote)) {
                                probeInfo.text(TextFormatting.RED + I18n.format("endercrop.waila.nogrowth"));
                            } else {
                                probeInfo.text(TextFormatting.GRAY + I18n.format("endercrop.top.growth") + " : " + ((int) Math.floor(age*100.0)) + "%");
                            }
                        }
                        if (mode != ProbeMode.NORMAL) {
                            String text = TextFormatting.YELLOW + I18n.format("endercrop.waila.light") + ": " + world.getLightFromNeighbors(data.getPos().up());
                            if (world.getLightFromNeighbors(data.getPos().up()) >= 7)
                                text += TextFormatting.RED + "(>=7)";
                            probeInfo.text(text);
                        }
                    }
                }
            });
            return null;
        }
    }
}
