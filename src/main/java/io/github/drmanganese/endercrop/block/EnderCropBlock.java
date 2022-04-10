package io.github.drmanganese.endercrop.block;

import io.github.drmanganese.endercrop.configuration.EnderCropConfiguration;
import io.github.drmanganese.endercrop.init.ModBlocks;
import io.github.drmanganese.endercrop.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.Random;

public class EnderCropBlock extends CropBlock {

    private static final Properties PROPERTIES = BlockBehaviour.Properties
        .of(Material.PLANT)
        .noCollission()
        .randomTicks()
        .instabreak()
        .sound(SoundType.CROP);

    public EnderCropBlock() {
        super(PROPERTIES);
    }

    private static boolean isOnEndstone(LevelReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.below()).is(ModBlocks.TILLED_END_STONE.get());
    }

    // Vanilla growth chance is calculated as 1 in floor(25/points) + 1.
    // For the ender crop we multiply 'points' by the appropriate multiplier.
    protected static float getGrowthSpeed(Block blockIn, Level worldIn, BlockPos pos) {
        float baseChance = CropBlock.getGrowthSpeed(blockIn, worldIn, pos);
        if (isOnEndstone(worldIn, pos)) {
            baseChance *= EnderCropConfiguration.tilledEndMultiplier.get();
        } else {
            baseChance *= EnderCropConfiguration.tilledSoilMultiplier.get();
        }

        return baseChance;
    }

    @Override
    @Nonnull
    protected ItemLike getBaseSeedId() {
        return ModItems.ENDER_SEEDS.get();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.FARMLAND) || state.is(ModBlocks.TILLED_END_STONE.get());
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        boolean lightCheck;
        if (isOnEndstone(pLevel, pPos))
            lightCheck = pLevel.getRawBrightness(pPos, 0) >= 8 || pLevel.canSeeSky(pPos);
        else
            lightCheck = pLevel.getRawBrightness(pPos, 0) <= 7;

        final BlockPos below = pPos.below();
        if (pState.getBlock() == this) //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
            return lightCheck && pLevel.getBlockState(below).canSustainPlant(pLevel, below, Direction.UP, this);
        return lightCheck && this.mayPlaceOn(pLevel.getBlockState(below), pLevel, below);
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        return false;
    }

    @Override
    protected int getBonemealAgeIncrease(Level pLevel) {
        return 0;
    }

    public boolean isDarkEnough(Level worldIn, BlockPos pos) {
        return isOnEndstone(worldIn, pos) || worldIn.getRawBrightness(pos, 0) <= 7;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        if (isDarkEnough(level, pos)) {
            int age = this.getAge(state);
            if (!this.isMaxAge(state)) {
                final float growthChance = getGrowthSpeed(this, level, pos);
                if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / growthChance) + 1) == 0)) {
                    level.setBlock(pos, this.getStateForAge(age + 1), 2);
                    ForgeHooks.onCropsGrowPost(level, pos, state);
                }
            }
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        final boolean destroyed = super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);

        if (destroyed
            && EnderCropConfiguration.miteChance.get() > 0
            && isOnEndstone(level, pos)
            && this.isMaxAge(state)) {
            final int roll = level.random.nextInt(EnderCropConfiguration.miteChance.get());
            if (roll == 0) {
                final Endermite mite = EntityType.ENDERMITE.create(level);
                if (mite != null) {
                    mite.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
                    mite.setYBodyRot(Mth.wrapDegrees(level.random.nextFloat() * 360.0F));
                    mite.setTarget(player);
                    level.addFreshEntity(mite);
                }
            }
        }

        return destroyed;
    }
}
