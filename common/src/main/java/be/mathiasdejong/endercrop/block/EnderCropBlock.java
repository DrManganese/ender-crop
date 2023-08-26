package be.mathiasdejong.endercrop.block;

import be.mathiasdejong.endercrop.ModExpectPlatform;
import be.mathiasdejong.endercrop.config.EnderCropConfiguration;
import be.mathiasdejong.endercrop.init.ModBlocks;
import be.mathiasdejong.endercrop.init.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnderCropBlock extends CropBlock {

    private static final Properties PROPERTIES = Properties.of()
        .mapColor(MapColor.PLANT)
        .noCollission()
        .noOcclusion()
        .randomTicks()
        .instabreak()
        .sound(SoundType.CROP);

    public EnderCropBlock() {
        super(PROPERTIES);
    }

    @Override
    public void playerDestroy(
        Level level,
        Player player,
        BlockPos pos,
        BlockState state,
        @Nullable BlockEntity blockEntity,
        ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (EnderCropConfiguration.miteChance.get() > 0 && isOnEndstone(level, pos) && this.isMaxAge(state)) {
            final int roll = level.random.nextInt(EnderCropConfiguration.miteChance.get());
            if (roll == 0) {
                final Endermite mite = EntityType.ENDERMITE.create(level);
                if (mite != null) {
                    mite.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
                    mite.lookAt(player, 360.0F, 360.0F);
                    mite.setTarget(player);
                    level.addFreshEntity(mite);
                }
            }
        }
    }

    @Override
    public boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.FARMLAND) || state.is(ModBlocks.TILLED_END_STONE.get());
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (isDarkEnough(level, pos)) {
            final int age = this.getAge(state);
            if (!this.isMaxAge(state)) {
                final float growthChance = getGrowthSpeed(this, level, pos);
                final boolean doGrow = growthChance > 0 && random.nextInt((int) (25.0F / growthChance) + 1) == 0;
                if (ModExpectPlatform.onCropsGrowPre(level, pos, state, doGrow)) {
                    level.setBlock(pos, this.getStateForAge(age + 1), 2);
                    ModExpectPlatform.onCropsGrowPost(level, pos, state);
                }
            }
        }
    }

    public boolean isDarkEnough(Level worldIn, BlockPos pos) {
        return isOnEndstone(worldIn, pos) || worldIn.getRawBrightness(pos, 0) <= 7;
    }

    // Reimplementing the whole thing because it's static, and we don't want to penalize tilled end stone
    protected static float getGrowthSpeed(Block block, Level level, BlockPos pos) {
        float f = 1.0F;
        BlockPos blockPos = pos.below();

        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                float g = 0.0F;
                BlockState blockState = level.getBlockState(blockPos.offset(i, 0, j));
                if (blockState.is(Blocks.FARMLAND) || blockState.is(ModBlocks.TILLED_END_STONE.get())) {
                    g = 1.0F;
                    if (blockState.getValue(FarmBlock.MOISTURE) > 0) {
                        g = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    g /= 4.0F;
                }

                f += g;
            }
        }

        BlockPos blockPos2 = pos.north();
        BlockPos blockPos3 = pos.south();
        BlockPos blockPos4 = pos.west();
        BlockPos blockPos5 = pos.east();
        boolean bl = level.getBlockState(blockPos4).is(block) || level.getBlockState(blockPos5).is(block);
        boolean bl2 = level.getBlockState(blockPos2).is(block) || level.getBlockState(blockPos3).is(block);
        if (bl && bl2) {
            f /= 2.0F;
        } else {
            boolean bl3 = level.getBlockState(blockPos4.north()).is(block) || level.getBlockState(blockPos5.north()).is(block) || level.getBlockState(blockPos5.south()).is(block) || level.getBlockState(blockPos4.south()).is(block);
            if (bl3) {
                f /= 2.0F;
            }
        }

        if (isOnEndstone(level, pos))
            f *= EnderCropConfiguration.tilledEndMultiplier.get();
        else
            f *= EnderCropConfiguration.tilledSoilMultiplier.get();

        return f;
    }

    @Override
    protected int getBonemealAgeIncrease(Level level) {
        return 0;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        boolean lightCheck;
        if (isOnEndstone(level, pos)) lightCheck = level.getRawBrightness(pos, 0) >= 8 || level.canSeeSky(pos);
        else lightCheck = level.getRawBrightness(pos, 0) <= 7;

        final BlockPos below = pos.below();
        //noinspection ConstantValue
        return lightCheck
            && ModExpectPlatform.canSustainPlant(level.getBlockState(below), level, below, Direction.UP, this);
    }

    private static boolean isOnEndstone(LevelReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.below()).is(ModBlocks.TILLED_END_STONE.get());
    }

    @Override
    @NotNull
    protected ItemLike getBaseSeedId() {
        return ModItems.ENDER_SEEDS.get();
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        // NOOP
    }
}
