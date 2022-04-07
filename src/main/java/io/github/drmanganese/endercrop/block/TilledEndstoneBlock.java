package io.github.drmanganese.endercrop.block;

import io.github.drmanganese.endercrop.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class TilledEndstoneBlock extends FarmBlock {

    private static final Properties PROPERTIES = BlockBehaviour.Properties
        .of(Material.STONE, MaterialColor.SAND)
        .randomTicks()
        .destroyTime(0.6F)
        .requiresCorrectToolForDrops()
        .strength(3.0F, 9.0F);


    public TilledEndstoneBlock() {
        super(PROPERTIES);
    }

    @Override
    public boolean isFertile(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(this) && state.getValue(MOISTURE) > 0;
    }

    public static void turnToEndStone(BlockState pState, Level pLevel, BlockPos pPos) {
        pLevel.setBlockAndUpdate(pPos, pushEntitiesUp(pState, Blocks.END_STONE.defaultBlockState(), pLevel, pPos));
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        int i = pState.getValue(MOISTURE);
        if (!isNearWater(pLevel, pPos) && !pLevel.isRainingAt(pPos.above())) {
            if (i > 0) {
                pLevel.setBlock(pPos, pState.setValue(MOISTURE, i - 1), 2);
            } else if (!isUnderCrops(pLevel, pPos)) {
                turnToEndStone(pState, pLevel, pPos);
            }
        } else if (i < 7) {
            pLevel.setBlock(pPos, pState.setValue(MOISTURE, 7), 2);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return !this.defaultBlockState().canSurvive(pContext.getLevel(), pContext.getClickedPos()) ? Blocks.END_STONE.defaultBlockState() : super.getStateForPlacement(pContext);
    }
    
    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRand) {
        if (!pState.canSurvive(pLevel, pPos)) {
            turnToEndStone(pState, pLevel, pPos);
        }
    }
    
    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        final BlockState plant = plantable.getPlant(world, pos.offset(facing.getNormal()));
        return plant.getBlock() == ModBlocks.ENDER_CROP.get();
    }

    @Override
    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
        if (!pLevel.isClientSide && ForgeHooks.onFarmlandTrample(pLevel, pPos, Blocks.END_STONE.defaultBlockState(), pFallDistance, pEntity))
            turnToEndStone(pLevel.getBlockState(pPos), pLevel, pPos);

        pEntity.causeFallDamage(pFallDistance, 1.0F, DamageSource.FALL);
    }
}
