package be.mathiasdejong.endercrop.block;

import be.mathiasdejong.endercrop.ModExpectPlatform;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import static net.minecraft.world.level.block.Blocks.END_STONE;

public class TilledEndstoneBlock extends FarmBlock {

    private static final Properties PROPERTIES = Properties.of()
        .mapColor(MapColor.SAND)
        .randomTicks()
        .destroyTime(0.6F)
        .requiresCorrectToolForDrops()
        .strength(3.0F, 9.0F)
        .sound(SoundType.GRAVEL);

    public TilledEndstoneBlock() {
        super(PROPERTIES);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos())
            ? END_STONE.defaultBlockState()
            : super.getStateForPlacement(context);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource pRand) {
        if (!state.canSurvive(level, pos))
            turnToEndStone(state, level, pos);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        final int moisture = state.getValue(MOISTURE);
        if (!isNearWater(level, pos) && !level.isRainingAt(pos.above())) {
            if (moisture > 0) {
                level.setBlock(pos, state.setValue(MOISTURE, moisture - 1), 2);
            } else if (!shouldMaintainFarmland(level, pos)) {
                turnToEndStone(state, level, pos);
            }
        } else if (moisture < 7) {
            level.setBlock(pos, state.setValue(MOISTURE, 7), 2);
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        //noinspection ConstantValue
        if (!level.isClientSide && ModExpectPlatform.onFarmlandTrample(level, pos, state, fallDistance, entity))
            turnToEndStone(level.getBlockState(pos), level, pos);

        entity.causeFallDamage(fallDistance, 1.0F, entity.damageSources().fall());
    }

    public static void turnToEndStone(BlockState state, Level level, BlockPos pos) {
        level.setBlockAndUpdate(pos, pushEntitiesUp(state, END_STONE.defaultBlockState(), level, pos));
    }
}
