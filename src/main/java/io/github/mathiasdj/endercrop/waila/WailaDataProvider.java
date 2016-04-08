package io.github.mathiasdj.endercrop.waila;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

import io.github.mathiasdj.endercrop.block.BlockCropEnder;
import io.github.mathiasdj.endercrop.block.BlockTilledEndStone;
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

        registrar.registerBodyProvider(new WailaDataProvider(), Blocks.end_stone.getClass());
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return new ItemStack(accessor.getBlock());
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        Block block = accessor.getBlock();

        int blockX = accessor.getPosition().blockX;
        int blockY = accessor.getPosition().blockY;
        int blockZ = accessor.getPosition().blockZ;

        if (block instanceof BlockCropEnder) {
            if (accessor.getWorld().getBlock(blockX, blockY - 1, blockZ) == Blocks.farmland) {
                int light = accessor.getWorld().getBlockLightValue(blockX, blockY + 1, blockZ);
                if (light > 7)
                    currenttip.set(1, SpecialChars.RED + "Can't grow -> Light : " + light + SpecialChars.ITALIC + " (>7)");
            }
        } else if (block instanceof BlockTilledEndStone) {
            if (accessor.getWorld().getBlockMetadata(blockX, blockY, blockZ) > 0)
                currenttip.set(0, SpecialChars.ITALIC + "Moist");
            else
                currenttip.set(0, SpecialChars.ITALIC + "Dry");
        } else if (block == Blocks.end_stone) {
            ItemStack heldItem = accessor.getPlayer().getHeldItem();
            if (heldItem != null && heldItem.getItem() instanceof ItemHoe) {
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, heldItem) > 0 || accessor.getPlayer().capabilities.isCreativeMode) {
                    currenttip.add(SpecialChars.DGREEN + "\u2714 Can hoe");
                } else {
                    currenttip.add(SpecialChars.RED + "\u2718 Can't hoe, enchant with Unbreaking");
                }
            }
        }

        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
        return tag;
    }
}
