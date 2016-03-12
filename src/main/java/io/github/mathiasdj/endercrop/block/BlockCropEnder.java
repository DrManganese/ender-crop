package io.github.mathiasdj.endercrop.block;

import io.github.mathiasdj.endercrop.init.ModBlocks;
import io.github.mathiasdj.endercrop.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockCropEnder extends BlockCrops
{
    public BlockCropEnder(String unlocalizedName)
    {
        super();
        this.setUnlocalizedName(unlocalizedName);
    }

    public boolean isOnEndstone(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).getBlock() == ModBlocks.blockTilledEndStone;
    }

    @Override
    protected Item getSeed()
    {
        return ModItems.item_ender_seeds;
    }

    @Override
    protected Item getCrop()
    {
        return Items.ender_pearl;
    }

    @Override
    protected boolean canPlaceBlockOn(Block ground)
    {
        return ground == Blocks.farmland || ground == ModBlocks.blockCropEnder;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return playerIn.getHeldItem().getItem().equals(Items.dye);
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return state.getValue(AGE) < 7 && (isOnEndstone(worldIn, pos) || worldIn.getLightFromNeighbors(pos.up()) <= 7);
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        return worldIn.getBlockState(pos.down()).getBlock().canSustainPlant(worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        float baseChance = (isOnEndstone(worldIn, pos)) ? 25.0F : 50.0F;

        if (worldIn.getLightFromNeighbors(pos.up()) <= 7 || isOnEndstone(worldIn, pos))
        {
            if (state.getValue(AGE) < 7)
            {
                if (rand.nextInt((int)(baseChance / getGrowthChance(this, worldIn, pos)) + 1) == 0)
                {
                    worldIn.setBlockState(pos, state.withProperty(AGE, state.getValue(AGE) + 1), 2);
                }
            }
        }
    }


    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        List<ItemStack> drops = new ArrayList<ItemStack>();

        int age = state.getValue(AGE);
        Random rand = ((World)world).rand;

        int pearls = 0;
        int seeds = 1;

        //not fully grown
        if (age < 7)
        {
            //90% chance to get a seed back
            if (rand.nextInt(10) < 9) seeds++;
        }
        //fully grown
        else
        {
            //10% chance to get an extra seed
            if (rand.nextInt(10) == 9)
            {
                seeds++;
            }

            //10% chance to get a second pearl
            if (rand.nextInt(10) > 0)
            {
                pearls = 1;
            }
            else
            {
                pearls = 2;
            }
        }

        drops.add(new ItemStack(this.getSeed(), seeds, 0));
        if (pearls > 0) drops.add(new ItemStack(this.getCrop(), pearls, 0));
        return drops;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)
    {
        super.harvestBlock(worldIn, player, pos, state, te);

        if (worldIn.getBlockState(pos.down()).getBlock() == ModBlocks.blockTilledEndStone && state.getValue(AGE) == 7 && worldIn.rand.nextInt(50) == 0)
        {
            EntityEndermite mite = new EntityEndermite(worldIn);
            mite.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), MathHelper.wrapAngleTo180_float(worldIn.rand.nextFloat() * 360.0F), 0.0F);
            worldIn.spawnEntityInWorld(mite);
            mite.setAttackTarget(player);
        }
    }
}
