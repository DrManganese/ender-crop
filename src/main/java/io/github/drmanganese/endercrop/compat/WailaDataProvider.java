package io.github.drmanganese.endercrop.compat;

import io.github.drmanganese.endercrop.HoeHelper;
import io.github.drmanganese.endercrop.block.BlockCropEnder;
import io.github.drmanganese.endercrop.block.BlockTilledEndStone;
import io.github.drmanganese.endercrop.configuration.EnderCropConfiguration;
import io.github.drmanganese.endercrop.init.ModBlocks;

import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

import javax.annotation.Nonnull;
import java.util.List;

@SideOnly(Side.CLIENT)
public class WailaDataProvider implements IWailaDataProvider {

    public static void callbackRegister(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new WailaDataProvider(), BlockCropEnder.class);
        registrar.registerBodyProvider(new WailaDataProvider(), BlockTilledEndStone.class);

        if (EnderCropConfiguration.tilledEndStone) {
            registrar.registerBodyProvider(new WailaDataProvider(), Blocks.END_STONE.getClass());
        }
    }

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getBlock() == ModBlocks.CROP_ENDER) {
            if (accessor.getWorld().getBlockState(accessor.getPosition().down()).getBlock() == Blocks.FARMLAND) {
                if (accessor.getMetadata() / 7.0F < 1.0F) {
                    int light = accessor.getWorld().getLightFromNeighbors(accessor.getPosition().up());
                    if (light > 7) {
                        currenttip.clear();
                        currenttip.add(0, TextFormatting.RED + I18n.format("endercrop.wailatop.nogrowth") + " -> " + I18n.format("endercrop.wailatop.light") + " : " + light + TextFormatting.ITALIC + " (>7)");
                    }
                }
            }
        } else if (accessor.getBlock() == ModBlocks.TILLED_END_STONE) {
            currenttip.clear();
            if (accessor.getBlockState().getValue(BlockTilledEndStone.MOISTURE) == 7) {
                currenttip.add(TextFormatting.ITALIC + I18n.format("endercrop.wailatop.moist"));
            } else {
                currenttip.add(TextFormatting.ITALIC + I18n.format("endercrop.wailatop.dry"));
            }
        } else if (accessor.getBlock() == Blocks.END_STONE) {
            if (!HoeHelper.holdingHoeTool(accessor.getPlayer()).isEmpty()) {
                if (accessor.getPlayer().isCreative() || HoeHelper.canHoeEndstone(accessor.getPlayer())) {
                    currenttip.add(TextFormatting.DARK_GREEN + "\u2714" + I18n.format("endercrop.waila.hoeable"));
                } else {
                    currenttip.add(TextFormatting.RED + "\u2718" + I18n.format("endercrop.waila.nothoeable"));
                }
            }
        }

        return currenttip;
    }
}
