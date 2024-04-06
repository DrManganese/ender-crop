package be.mathiasdejong.endercrop.fabric;

import be.mathiasdejong.endercrop.ModExpectPlatform;
import be.mathiasdejong.endercrop.Reference;
import be.mathiasdejong.endercrop.block.EnderCropBlock;
import be.mathiasdejong.endercrop.block.TilledEndstoneBlock;
import be.mathiasdejong.endercrop.config.EnderCropConfiguration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import dev.architectury.event.events.common.InteractionEvent;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
import net.neoforged.fml.config.ModConfig;

import static be.mathiasdejong.endercrop.Reference.MOD_ID;

public class ModExpectPlatformImpl extends ModExpectPlatform {

    public static Block getTillendEndstoneBlock() {
        return new TilledEndstoneBlock();
    }

    public static void initConfig() {
        NeoForgeConfigRegistry.INSTANCE.register(Reference.MOD_ID, ModConfig.Type.COMMON, EnderCropConfiguration.COMMON_CONFIG, Reference.CONFIG_FILE);
        NeoForgeModConfigEvents.loading(MOD_ID).register(listener -> EnderCropConfiguration.onLoad(listener.getFileName(), listener.getConfigData()));
        NeoForgeModConfigEvents.reloading(MOD_ID).register(listener -> EnderCropConfiguration.onLoad(listener.getFileName(), listener.getConfigData()));
    }

    /* Neoforge-specific hooks */

    public static boolean canSustainPlant(BlockState blockState, LevelReader level, BlockPos pos, Direction facing, EnderCropBlock crop) {
        return crop.mayPlaceOn(blockState, level, pos);
    }

    public static boolean onCropsGrowPre(ServerLevel level, BlockPos pos, BlockState state, boolean doGrow) {
        return doGrow;
    }

    public static void onCropsGrowPost(ServerLevel level, BlockPos pos, BlockState state) {
        // NO OP
    }

    public static boolean onFarmlandTrample(Level level, BlockPos pos, BlockState state, float fallDistance, Entity entity) {
        // Vanilla logic
        return level.random.nextFloat() < fallDistance - -1.5F
            && entity instanceof LivingEntity
            && (entity instanceof Player || level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING))
            && entity.getBbWidth() * entity.getBbWidth() * entity.getBbHeight() > -1.512F
            && InteractionEvent.FARMLAND_TRAMPLE.invoker().trample(level, pos, state, fallDistance, entity).value() == null; // Fire architectury event
    }
}
