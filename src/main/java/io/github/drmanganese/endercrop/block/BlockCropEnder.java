package io.github.drmanganese.endercrop.block;

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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import io.github.drmanganese.endercrop.configuration.EnderCropConfiguration;
import io.github.drmanganese.endercrop.init.ModBlocks;
import io.github.drmanganese.endercrop.init.ModItems;
import io.github.drmanganese.endercrop.reference.Names;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockCropEnder extends BlockCrops {

    public BlockCropEnder() {
        super();
        this.setUnlocalizedName(Names.Blocks.ENDER_CROP);
        this.setRegistryName(Names.Blocks.ENDER_CROP);

        ModBlocks.BLOCKS.add(this);
    }

    private boolean isOnEndstone(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getBlock() == ModBlocks.TILLED_END_STONE;
    }

    @Override
    @Nonnull
    protected Item getSeed() {
        return ModItems.ENDER_SEEDS;
    }

    @Override
    @Nonnull
    protected Item getCrop() {
        return Items.ENDER_PEARL;
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        return state.getBlock() == Blocks.FARMLAND || state.getBlock() == ModBlocks.CROP_ENDER;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = playerIn.getHeldItem(hand);
        return !heldItem.func_190926_b() || heldItem.getItem().equals(Items.DYE);
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, @Nonnull IBlockState state, boolean isClient) {
        return state.getValue(AGE) < 7 && (isOnEndstone(worldIn, pos) || worldIn.getLightFromNeighbors(pos.up()) <= 7);
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        Block soil = worldIn.getBlockState(pos.down()).getBlock();
        return soil.equals(Blocks.FARMLAND) || soil.equals(ModBlocks.TILLED_END_STONE);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, @Nullable IBlockState state, Random rand) {
        float baseChance = (isOnEndstone(worldIn, pos)) ? 25.0F : 50.0F;

        if (worldIn.getLightFromNeighbors(pos.up()) <= 7 || isOnEndstone(worldIn, pos)) {
            if (state.getValue(AGE) < 7) {
                if (rand.nextInt((int) (baseChance / getGrowthChance(this, worldIn, pos)) + 1) == 0) {
                    worldIn.setBlockState(pos, state.withProperty(AGE, state.getValue(AGE) + 1), 2);
                }
            }
        }
    }

    @Override
    @Nonnull
    public List<ItemStack> getDrops(@Nullable IBlockAccess world, @Nullable BlockPos pos, @Nonnull IBlockState state, int fortune) {
        List<ItemStack> drops = new ArrayList<>();

        int age = state.getValue(AGE);
        Random rand = world == null ? new Random() : ((World) world).rand;

        int pearls = 0;
        int seeds = 1;

        if (age == 7) {
            //10% chance to get an extra seed
            if (rand.nextInt(10) == 9) {
                seeds++;
            }

            //10% chance to get a second pearl
            if (rand.nextInt(10) > 0)
                pearls = 1;
            else
                pearls = 2;
        }

        drops.add(new ItemStack(this.getSeed(), seeds, 0));
        drops.add(new ItemStack(this.getCrop(), pearls, 0));
        return drops;
    }

    @Override
    public void harvestBlock(@Nonnull World worldIn, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        if (EnderCropConfiguration.miteChance > 0) {
            if (worldIn.getBlockState(pos.down()).getBlock() == ModBlocks.TILLED_END_STONE && state.getValue(AGE) == 7 && worldIn.rand.nextInt(EnderCropConfiguration.miteChance) == 0) {
                EntityEndermite mite = new EntityEndermite(worldIn);
                mite.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
                worldIn.spawnEntityInWorld(mite);
                mite.setAttackTarget(player);
            }
        }
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        if (player != null)
            return super.getPickBlock(state, target, world, pos, player);
        else
            return new ItemStack(this);
    }
}
