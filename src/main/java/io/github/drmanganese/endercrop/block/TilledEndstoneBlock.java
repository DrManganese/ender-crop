package io.github.drmanganese.endercrop.block;

import io.github.drmanganese.endercrop.init.ModBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class TilledEndstoneBlock extends FarmlandBlock {

    private static final AbstractBlock.Properties PROPERTIES = AbstractBlock.Properties
            .create(Material.ROCK, MaterialColor.SAND)
            .tickRandomly()
            .hardnessAndResistance(0.6F)
            .setRequiresTool()
            .hardnessAndResistance(3.0F, 9.0F);


    public TilledEndstoneBlock() {
        super(PROPERTIES);
    }

    @Override
    public boolean isFertile(BlockState state, IBlockReader world, BlockPos pos) {
        return state.matchesBlock(this) && state.get(MOISTURE) > 0;
    }

    public static void turnToEndStone(BlockState state, World worldIn, BlockPos pos) {
        worldIn.setBlockState(pos, nudgeEntitiesWithNewState(state, Blocks.END_STONE.getDefaultState(), worldIn, pos));
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!state.isValidPosition(worldIn, pos)) {
            turnToEndStone(state, worldIn, pos);
        }
    }

    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        int i = state.get(MOISTURE);
        if (!hasWater(worldIn, pos) && !worldIn.isRainingAt(pos.up())) {
            if (i > 0) {
                worldIn.setBlockState(pos, state.with(MOISTURE, i - 1), 2);
            } else if (!hasCrops(worldIn, pos)) {
                turnToEndStone(state, worldIn, pos);
            }
        } else if (i < 7) {
            worldIn.setBlockState(pos, state.with(MOISTURE, 7), 2);
        }

    }

    private boolean hasCrops(IBlockReader worldIn, BlockPos pos) {
        BlockState plant = worldIn.getBlockState(pos.up());
        BlockState state = worldIn.getBlockState(pos);
        return plant.getBlock() instanceof net.minecraftforge.common.IPlantable && state.canSustainPlant(worldIn, pos, Direction.UP, (net.minecraftforge.common.IPlantable) plant.getBlock());
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
        final BlockState plant = plantable.getPlant(world, pos.offset(facing));
        return plant.getBlock() == ModBlocks.ENDER_CROP.get();
    }

    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (!worldIn.isRemote && ForgeHooks.onFarmlandTrample(worldIn, pos, this.getDefaultState(), fallDistance, entityIn)) {
            turnToEndStone(worldIn.getBlockState(pos), worldIn, pos);
        }

        entityIn.onLivingFall(fallDistance, 1.0F);
    }
}
