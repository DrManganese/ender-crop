package be.mathiasdejong.endercrop.neoforge;

import be.mathiasdejong.endercrop.ModExpectPlatform;
import be.mathiasdejong.endercrop.Reference;
import be.mathiasdejong.endercrop.block.EnderCropBlock;
import be.mathiasdejong.endercrop.block.TilledEndstoneBlock;
import be.mathiasdejong.endercrop.config.EnderCropConfiguration;
import be.mathiasdejong.endercrop.init.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import dev.architectury.platform.hooks.EventBusesHooks;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.IPlantable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

import static be.mathiasdejong.endercrop.Reference.MOD_ID;

@ParametersAreNonnullByDefault
public class ModExpectPlatformImpl extends ModExpectPlatform {
    public static Block getTillendEndstoneBlock() {
        return new TilledEndstoneBlock() {
            @Override
            public boolean isFertile(BlockState state, BlockGetter level, BlockPos pos) {
                return state.is(this) && state.getValue(MOISTURE) > 0;
            }

            @Override
            public boolean canSustainPlant(
                BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
                final BlockState plant = plantable.getPlant(world, pos.offset(facing.getNormal()));
                return plant.getBlock() == ModBlocks.ENDER_CROP.get();
            }
        };
    }

    public static void initConfig() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EnderCropConfiguration.COMMON_CONFIG, Reference.CONFIG_FILE);
        EventBusesHooks.whenAvailable(MOD_ID, bus -> {
            final Consumer<ModConfigEvent> onLoad = (event) ->
                EnderCropConfiguration.onLoad(
                    event.getConfig().getFileName(),
                    event.getConfig().getConfigData()
                );
            bus.addListener((ModConfigEvent.Loading event) -> onLoad.accept(event));
            bus.addListener((ModConfigEvent.Reloading event) -> onLoad.accept(event));
        });
    }

    /* Neoforge-specific hooks */

    public static boolean onCropsGrowPre(ServerLevel level, BlockPos pos, BlockState state, boolean doGrow) {
        return CommonHooks.onCropsGrowPre(level, pos, state, doGrow);
    }

    public static void onCropsGrowPost(ServerLevel level, BlockPos pos, BlockState state) {
        CommonHooks.onCropsGrowPost(level, pos, state);
    }

    public static boolean canSustainPlant(
        BlockState blockState, LevelReader level, BlockPos pos, Direction facing, EnderCropBlock crop) {
        return blockState.getBlock().canSustainPlant(blockState, level, pos, facing, crop);
    }

    public static boolean onFarmlandTrample(Level level, BlockPos pos, BlockState blockState, float fallDistance, Entity entity) {
        // This uses the entity.canTrample logic and allows interception
        return CommonHooks.onFarmlandTrample(level, pos, blockState, fallDistance, entity);
    }
}
