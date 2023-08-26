package be.mathiasdejong.endercrop.fabric;

import be.mathiasdejong.endercrop.ModExpectPlatform;
import be.mathiasdejong.endercrop.Reference;
import be.mathiasdejong.endercrop.block.EnderCropBlock;
import be.mathiasdejong.endercrop.block.TilledEndstoneBlock;
import be.mathiasdejong.endercrop.config.EnderCropConfiguration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.config.ModConfig;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import fuzs.forgeconfigapiport.api.config.v2.ModConfigEvents;

import static be.mathiasdejong.endercrop.Reference.MOD_ID;

public class ModExpectPlatformImpl extends ModExpectPlatform {

    public static Block getTillendEndstoneBlock() {
        return new TilledEndstoneBlock();
    }

    public static void initConfig() {
        ForgeConfigRegistry.INSTANCE.register(
            Reference.MOD_ID, ModConfig.Type.COMMON, EnderCropConfiguration.COMMON_CONFIG, Reference.CONFIG_FILE);
        ModConfigEvents.loading(MOD_ID).register(EnderCropConfiguration::onLoad);
        ModConfigEvents.reloading(MOD_ID).register(EnderCropConfiguration::onLoad);
    }

    public static boolean onCropsGrowPre(Level level, BlockPos pos, BlockState state, boolean doGrow) {
        return doGrow;
    }

    public static void onCropsGrowPost(Level level, BlockPos pos, BlockState state) {
        // NOOP
    }

    public static boolean canSustainPlant(
        BlockState blockState, LevelReader level, BlockPos pos, Direction facing, EnderCropBlock crop) {
        return crop.mayPlaceOn(level.getBlockState(pos), level, pos);
    }

    public static boolean onFarmlandTrample(Level level, BlockPos pos, BlockState blockState, float fallDistance,
                                            Entity entity) {
        return true;
    }
}
