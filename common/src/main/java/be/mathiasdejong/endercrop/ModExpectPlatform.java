package be.mathiasdejong.endercrop;

import be.mathiasdejong.endercrop.block.EnderCropBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class ModExpectPlatform {

    @ExpectPlatform
    public static Block getTillendEndstoneBlock() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void initConfig() {
        throw new AssertionError();
    }

    /* Forge-specific hooks */

    @ExpectPlatform
    public static boolean canSustainPlant(
        BlockState state, LevelReader level, BlockPos pos, Direction facing, EnderCropBlock crop) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean onCropsGrowPre(Level level, BlockPos pos, BlockState state, boolean doGrow) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void onCropsGrowPost(Level level, BlockPos pos, BlockState state) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean onFarmlandTrample(Level level, BlockPos pos, BlockState blockState, float fallDistance,
                                            Entity entity) {
        throw new AssertionError();
    }
}
