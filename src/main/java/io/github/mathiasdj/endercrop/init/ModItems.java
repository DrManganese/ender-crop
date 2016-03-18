package io.github.mathiasdj.endercrop.init;


import io.github.mathiasdj.endercrop.item.ItemEnderSeeds;
import io.github.mathiasdj.endercrop.reference.Names;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems
{
    public static final ItemEnderSeeds item_ender_seeds = new ItemEnderSeeds(ModBlocks.blockCropEnder);

    public static void init()
    {
        GameRegistry.registerItem(item_ender_seeds, Names.Items.SEEDS);
    }
}
