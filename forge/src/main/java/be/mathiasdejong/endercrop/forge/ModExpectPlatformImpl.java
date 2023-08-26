package be.mathiasdejong.endercrop.forge;

import be.mathiasdejong.endercrop.ModExpectPlatform;
import be.mathiasdejong.endercrop.Reference;
import be.mathiasdejong.endercrop.block.EnderCropBlock;
import be.mathiasdejong.endercrop.block.TilledEndstoneBlock;
import be.mathiasdejong.endercrop.config.EnderCropConfiguration;
import be.mathiasdejong.endercrop.init.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import dev.architectury.platform.forge.EventBuses;

import javax.annotation.ParametersAreNonnullByDefault;

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

            @Override
            public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
                if (!level.isClientSide
                        && ForgeHooks.onFarmlandTrample(
                                level, pos, Blocks.END_STONE.defaultBlockState(), fallDistance, entity))
                    turnToEndStone(level.getBlockState(pos), level, pos);

                entity.causeFallDamage(
                    fallDistance, 1.0F, level.damageSources().fall());
            }
        };
    }

    public static void initConfig() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EnderCropConfiguration.COMMON_CONFIG, Reference.CONFIG_FILE);
        EventBuses.getModEventBus(MOD_ID).ifPresent(bus -> {
            bus.addListener((ModConfigEvent.Loading event) -> EnderCropConfiguration.onLoad(event.getConfig()));
            bus.addListener((ModConfigEvent.Reloading event) -> EnderCropConfiguration.onLoad(event.getConfig()));
        });
    }

    public static boolean onCropsGrowPre(Level level, BlockPos pos, BlockState state, boolean doGrow) {
        return ForgeHooks.onCropsGrowPre(level, pos, state, doGrow);
    }

    public static void onCropsGrowPost(Level level, BlockPos pos, BlockState state) {
        ForgeHooks.onCropsGrowPost(level, pos, state);
    }

    public static boolean canSustainPlant(
        BlockState blockState, LevelReader level, BlockPos pos, Direction facing, EnderCropBlock crop) {
        return blockState.getBlock().canSustainPlant(blockState, level, pos, facing, crop);
    }

    public static boolean onFarmlandTrample(Level level, BlockPos pos, BlockState blockState, float fallDistance, Entity entity) {
        return ForgeHooks.onFarmlandTrample(level, pos, blockState, fallDistance, entity);
    }
}
