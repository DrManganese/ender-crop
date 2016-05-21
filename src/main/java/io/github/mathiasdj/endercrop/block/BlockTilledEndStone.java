package io.github.mathiasdj.endercrop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.Random;

public class BlockTilledEndStone extends BlockFarmland {

    public BlockTilledEndStone(String unlocalizedName) {
        super();
        this.setUnlocalizedName(unlocalizedName);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
        this.setLightOpacity(255);
        this.useNeighborBrightness = true;
        this.setHardness(0.6F);
        this.setHarvestLevel("pickaxe", 1);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int i = (Integer) state.getValue(MOISTURE);

        if (!this.hasWater(worldIn, pos) && !worldIn.canLightningStrike(pos.up())) {
            if (i > 0) {
                worldIn.setBlockState(pos, state.withProperty(MOISTURE, i - 1), 2);
            } else if (!this.hasCrops(worldIn, pos)) {
                worldIn.setBlockState(pos, Blocks.end_stone.getDefaultState());
            }
        } else if (i < 7) {
            worldIn.setBlockState(pos, state.withProperty(MOISTURE, 7), 2);
        }
    }

    private boolean hasCrops(World worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        return block instanceof net.minecraftforge.common.IPlantable && canSustainPlant(worldIn, pos, net.minecraft.util.EnumFacing.UP, (net.minecraftforge.common.IPlantable) block);
    }

    private boolean hasWater(World worldIn, BlockPos pos) {
        Iterator iterator = BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4)).iterator();
        BlockPos.MutableBlockPos mutableblockpos;

        do
        {
            if (!iterator.hasNext())
            {
                return false;
            }

            mutableblockpos = (BlockPos.MutableBlockPos)iterator.next();
        }
        while (worldIn.getBlockState(mutableblockpos).getBlock().getMaterial() != Material.water);

        return true;
    }

    @Override
    public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        IBlockState plant = plantable.getPlant(world, pos.offset(direction));
        return plant.getBlock() instanceof BlockCropEnder;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);

        if (worldIn.getBlockState(pos.up()).getBlock().getMaterial().isSolid()) {
            worldIn.setBlockState(pos, Blocks.end_stone.getDefaultState());
        }
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (entityIn instanceof EntityLivingBase) {
            if (!worldIn.isRemote && worldIn.rand.nextFloat() < fallDistance - 0.5F) {
                if (!(entityIn instanceof EntityPlayer) && !worldIn.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
                    return;
                }

                worldIn.setBlockState(pos, Blocks.end_stone.getDefaultState());
            }

            entityIn.fall(fallDistance, 1.0F);
        }
    }

    @Override
    public boolean isFertile(World world, BlockPos pos) {
        return ((Integer) world.getBlockState(pos).getValue(BlockTilledEndStone.MOISTURE)) > 0;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Blocks.end_stone.getItemDropped(Blocks.end_stone.getDefaultState(), rand, fortune);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock(Blocks.end_stone);
    }
}
