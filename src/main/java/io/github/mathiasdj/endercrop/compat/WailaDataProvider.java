package io.github.mathiasdj.endercrop.compat;

import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

import io.github.mathiasdj.endercrop.block.BlockCropEnder;
import io.github.mathiasdj.endercrop.block.BlockTilledEndStone;
import io.github.mathiasdj.endercrop.configuration.EnderCropConfiguration;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.SpecialChars;

public class WailaDataProvider implements IWailaDataProvider {

    public static void callbackRegister(IWailaRegistrar registrar) {
        registrar.registerStackProvider(new WailaDataProvider(), BlockCropEnder.class);
        registrar.registerStackProvider(new WailaDataProvider(), BlockTilledEndStone.class);
        registrar.registerBodyProvider(new WailaDataProvider(), BlockCropEnder.class);
        registrar.registerBodyProvider(new WailaDataProvider(), BlockTilledEndStone.class);

        if (EnderCropConfiguration.tilledEndStone)
            registrar.registerBodyProvider(new WailaDataProvider(), Blocks.END_STONE.getClass());
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return new ItemStack(accessor.getBlockState().getBlock());
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getBlock() instanceof BlockCropEnder) {
            if (accessor.getWorld().getBlockState(accessor.getPosition().down()).getBlock() == Blocks.FARMLAND) {
                if (accessor.getMetadata() / 7.0F < 1.0F) {
                    int light = accessor.getWorld().getLightFromNeighbors(accessor.getPosition().up());
                    if (light > 7) {
                        currenttip.clear();
                        currenttip.add(0, SpecialChars.RED + I18n.format("endercrop.waila.nogrowth") + " -> " + I18n.format("endercrop.waila.light") + " : " + light + SpecialChars.ITALIC + " (>7)");
                    }
                }
            }
        } else if (accessor.getBlock() instanceof BlockTilledEndStone) {
            currenttip.clear();
            if (accessor.getBlockState().getValue(BlockTilledEndStone.MOISTURE) == 7) {
                currenttip.add(SpecialChars.ITALIC + I18n.format("endercrop.waila.moist"));
            } else {
                currenttip.add(SpecialChars.ITALIC + I18n.format("endercrop.waila.dry"));
            }
        } else if (accessor.getBlock() == Blocks.END_STONE) {
            boolean hoeInHand = false;
            boolean canHoe = false;

            ItemStack[] heldItems = {accessor.getPlayer().getHeldItem(EnumHand.MAIN_HAND), accessor.getPlayer().getHeldItem(EnumHand.OFF_HAND)};
            for (ItemStack heldItem : heldItems) {
                if (heldItem != null && heldItem.getItem() instanceof ItemHoe) {
                    hoeInHand = true;
                    canHoe = canHoe || (EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(34), heldItem) > 0 || accessor.getPlayer().capabilities.isCreativeMode);
                }
            }
            if (hoeInHand) {
                if (canHoe)
                    currenttip.add(SpecialChars.DGREEN + "\u2714" + I18n.format("endercrop.waila.hoeable"));
                else
                    currenttip.add(SpecialChars.RED + "\u2718" + I18n.format("endercrop.waila.nothoeable"));
            }
        }

        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        return tag;
    }
}
