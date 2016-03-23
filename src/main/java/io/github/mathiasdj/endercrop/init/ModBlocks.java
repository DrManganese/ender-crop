package io.github.mathiasdj.endercrop.init;

import cpw.mods.fml.common.registry.GameRegistry;
import io.github.mathiasdj.endercrop.block.BlockCropEnder;
import io.github.mathiasdj.endercrop.block.BlockTilledEndStone;
import io.github.mathiasdj.endercrop.reference.Names;

public class ModBlocks
{
    public static BlockCropEnder blockCropEnder = new BlockCropEnder();
    public static BlockTilledEndStone blockTilledEndStone = new BlockTilledEndStone();

    public static void init()
    {
        GameRegistry.registerBlock(blockCropEnder, Names.Blocks.ENDER_CROP);
        GameRegistry.registerBlock(blockTilledEndStone, Names.Blocks.TILLED_END_STONE);
    }
}
