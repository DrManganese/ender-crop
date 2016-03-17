package io.github.mathiasdj.endercrop.init;

import io.github.mathiasdj.endercrop.block.BlockCropEnder;
import io.github.mathiasdj.endercrop.block.BlockTilledEndStone;
import io.github.mathiasdj.endercrop.configuration.EnderCropConfiguration;
import io.github.mathiasdj.endercrop.reference.Names;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks
{
    public static BlockCropEnder blockCropEnder = new BlockCropEnder(Names.Blocks.ENDER_CROP);
    public static BlockTilledEndStone blockTilledEndStone = new BlockTilledEndStone(Names.Blocks.TILLED_END_STONE);

    public static void init()
    {
        GameRegistry.registerBlock(blockCropEnder, Names.Blocks.ENDER_CROP);
        if (EnderCropConfiguration.tilledEndStone)
            GameRegistry.registerBlock(blockTilledEndStone, Names.Blocks.TILLED_END_STONE);
    }
}
