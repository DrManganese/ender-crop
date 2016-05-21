package io.github.mathiasdj.endercrop.init;


import net.minecraftforge.fml.common.registry.GameRegistry;

import io.github.mathiasdj.endercrop.item.ItemEnderSeeds;
import io.github.mathiasdj.endercrop.reference.Names;

public class ModItems {

    public static ItemEnderSeeds item_ender_seeds = new ItemEnderSeeds(ModBlocks.blockCropEnder, null);

    public static void init() {
        GameRegistry.registerItem(item_ender_seeds, Names.Items.SEEDS);
    }
}
