package io.github.mathiasdj.endercrop.init;

import io.github.mathiasdj.endercrop.block.BlockCropEnder;
import io.github.mathiasdj.endercrop.reference.Names;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks
{
    public static BlockCropEnder blockCropEnder = new BlockCropEnder(Names.Blocks.ENDER_CROP);

    public static void init()
    {
        GameRegistry.registerBlock(blockCropEnder, "ender_crop");
    }
}
