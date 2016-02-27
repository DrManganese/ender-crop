package io.github.mathiasdj.endercrop.init;


import io.github.mathiasdj.endercrop.item.ItemEnderSeeds;
import io.github.mathiasdj.endercrop.reference.Names;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems
{
    public static ItemEnderSeeds item_seeds = new ItemEnderSeeds(ModBlocks.blockCropEnder, null);

    public static void init()
    {
        GameRegistry.registerItem(item_seeds, Names.Items.SEEDS);
    }
}
