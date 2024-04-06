package be.mathiasdejong.endercrop;

import be.mathiasdejong.endercrop.block.EnderCropBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
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

    /* Neoforge-specific hooks */

    @ExpectPlatform
    public static boolean canSustainPlant(
        BlockState state, LevelReader level, BlockPos pos, Direction facing, EnderCropBlock crop) {
        throw new AssertionError();
    }

    // From CropsBlock, allows devs to intercept crop growth and possibly block it
    @ExpectPlatform
    public static boolean onCropsGrowPre(ServerLevel level, BlockPos pos, BlockState state, boolean doGrow) {
        throw new AssertionError();
    }

    // From CropsBlock, fires an event right after the crop block's age is incremented
    @ExpectPlatform
    public static void onCropsGrowPost(ServerLevel level, BlockPos pos, BlockState state) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean onFarmlandTrample(Level level, BlockPos pos, BlockState state, float fallDistance, Entity entity) {
       throw new AssertionError();
    }
}
