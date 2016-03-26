package io.github.mathiasdj.endercrop.init;

import net.minecraftforge.fml.common.registry.GameRegistry;

import io.github.mathiasdj.endercrop.block.BlockCropEnder;
import io.github.mathiasdj.endercrop.block.BlockTilledEndStone;
import io.github.mathiasdj.endercrop.configuration.EnderCropConfiguration;
import io.github.mathiasdj.endercrop.reference.Names;

public class ModBlocks {

    public static final BlockCropEnder blockCropEnder = new BlockCropEnder();
    public static final BlockTilledEndStone blockTilledEndStone = new BlockTilledEndStone();

    public static void init() {
        GameRegistry.registerBlock(blockCropEnder, Names.Blocks.ENDER_CROP);
        if (EnderCropConfiguration.tilledEndStone)
            GameRegistry.registerBlock(blockTilledEndStone, Names.Blocks.TILLED_END_STONE);
    }
}
