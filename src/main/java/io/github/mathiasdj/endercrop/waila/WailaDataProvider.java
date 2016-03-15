package io.github.mathiasdj.endercrop.waila;

import io.github.mathiasdj.endercrop.EnderCrop;
import io.github.mathiasdj.endercrop.block.BlockCropEnder;
import io.github.mathiasdj.endercrop.block.BlockTilledEndStone;
import io.github.mathiasdj.endercrop.init.ModBlocks;
import mcp.mobius.waila.api.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

public class WailaDataProvider implements IWailaDataProvider
{
    public static void callbackRegister(IWailaRegistrar registrar)
    {
        registrar.registerStackProvider(new WailaDataProvider(), BlockCropEnder.class);
        registrar.registerStackProvider(new WailaDataProvider(), BlockTilledEndStone.class);
        registrar.registerBodyProvider(new WailaDataProvider(), BlockCropEnder.class);
        registrar.registerBodyProvider(new WailaDataProvider(), BlockTilledEndStone.class);
        registrar.registerBodyProvider(new WailaDataProvider(), Blocks.end_stone.getClass());
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return new ItemStack(accessor.getBlockState().getBlock());
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        if (accessor.getBlock() instanceof BlockCropEnder) {
            if (accessor.getWorld().getBlockState(accessor.getPosition().down()).getBlock() == Blocks.farmland) {
                int light = accessor.getWorld().getLightFromNeighbors(accessor.getPosition().up());
                if (light > 7)
                    currenttip.set(1, SpecialChars.RED + "Can't grow -> Light : " + light + SpecialChars.ITALIC + " (>7)");
            }
        }
        else if (accessor.getBlock() instanceof BlockTilledEndStone)
        {
            if (accessor.getBlockState().getValue(BlockTilledEndStone.MOISTURE) == 7) {
                currenttip.set(0, SpecialChars.ITALIC + "Moist");
            }
            else
            {
                currenttip.set(0, SpecialChars.ITALIC + "Dry");
            }
        }
        else if (accessor.getBlock() == Blocks.end_stone)
        {
            ItemStack heldItem = accessor.getPlayer().getHeldItem();
            if (heldItem != null && heldItem.getItem() instanceof ItemHoe)
            {
                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, heldItem) > 0 || accessor.getPlayer().capabilities.isCreativeMode)
                    currenttip.add(SpecialChars.DGREEN + "\u2714 Can hoe");
                else
                    currenttip.add(SpecialChars.RED + "\u2718 Can't hoe, enchant with Unbreaking");
            }
        }

        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos)
    {
        return tag;
    }
}
