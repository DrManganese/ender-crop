package io.github.mathiasdj.endercrop.block;

import io.github.mathiasdj.endercrop.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
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

    @Override
    protected Item getSeed()
    {
        return ModItems.item_seeds;
    }

    @Override
    protected Item getCrop()
    {
        return Items.ender_pearl;
    }

    @Override
    protected boolean canPlaceBlockOn(Block ground)
    {
        return ground == Blocks.farmland || ground == Blocks.end_stone;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return false;
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        return worldIn.getBlockState(pos.down()).getBlock().canSustainPlant(worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (worldIn.getLightFromNeighbors(pos.up()) <= 7)
        {
            if (state.getValue(AGE) < 7)
            {
                float chanceDivider = worldIn.getBlockState(pos.down()).getBlock().isFertile(worldIn, pos.down()) ? 2 : 1;

                if (rand.nextInt((int)(25.0F / chanceDivider) + 1) == 0)
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
}
