package io.github.drmanganese.endercrop.block;

import io.github.drmanganese.endercrop.configuration.EnderCropConfiguration;
import io.github.drmanganese.endercrop.init.ModBlocks;
import io.github.drmanganese.endercrop.init.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.Random;

public class EnderCropBlock extends CropsBlock {

    private static final AbstractBlock.Properties PROPERTIES = AbstractBlock.Properties
            .create(Material.PLANTS)
            .doesNotBlockMovement()
            .tickRandomly()
            .zeroHardnessAndResistance()
            .sound(SoundType.CROP);

    public EnderCropBlock() {
        super(PROPERTIES);
    }

    private static boolean isOnEndstone(IBlockReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getBlock() == ModBlocks.TILLED_END_STONE.get();
    }

    // Vanilla growth chance is calculated as 1 in floor(25/points) + 1.
    // For the ender crop we multiply 'points' by the appropriate multiplier.
    protected static float getGrowthChance(Block blockIn, IBlockReader worldIn, BlockPos pos) {
        float baseChance = CropsBlock.getGrowthChance(blockIn, worldIn, pos);
        if (isOnEndstone(worldIn, pos)) {
            baseChance *= EnderCropConfiguration.tilledEndMultiplier.get();
        } else {
            baseChance *= EnderCropConfiguration.tilledSoilMultiplier.get();
        }

        return baseChance;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        final BlockPos blockPos = pos.down();
        if (state.getBlock() == this)
            return worldIn.getBlockState(blockPos).canSustainPlant(worldIn, blockPos, Direction.UP, this);
        return this.isValidGround(worldIn.getBlockState(blockPos), worldIn, blockPos);
    }

    @Override
    @Nonnull
    protected IItemProvider getSeedsItem() {
        return ModItems.ENDER_SEEDS.get();
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.matchesBlock(Blocks.FARMLAND) || state.matchesBlock(ModBlocks.TILLED_END_STONE.get());
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    protected int getBonemealAgeIncrease(World worldIn) {
        return 0;
    }

    public boolean isDarkEnough(World worldIn, BlockPos pos) {
        return isOnEndstone(worldIn, pos) || worldIn.getLightSubtracted(pos, 0) <= 7;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (isDarkEnough(worldIn, pos)) {
            int age = this.getAge(state);
            if (!this.isMaxAge(state)) {
                final float growthChance = getGrowthChance(this, worldIn, pos);
                if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt((int) (25.0F / growthChance) + 1) == 0)) {
                    worldIn.setBlockState(pos, this.withAge(age + 1), 2);
                    ForgeHooks.onCropsGrowPost(worldIn, pos, state);
                }
            }
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBlockHarvested(worldIn, pos, state, player);
        if (worldIn.isRemote()) return;

        if (EnderCropConfiguration.miteChance.get() > 0 && worldIn.getBlockState(pos.down()).getBlock() == ModBlocks.TILLED_END_STONE.get() && this.isMaxAge(state)) {
            final int roll = worldIn.rand.nextInt(EnderCropConfiguration.miteChance.get());
            if (roll == 0) {
                EndermiteEntity mite = new EndermiteEntity(EntityType.ENDERMITE, worldIn);
                mite.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
                worldIn.addEntity(mite);
                mite.setAttackTarget(player);
            }
        }
    }
}
