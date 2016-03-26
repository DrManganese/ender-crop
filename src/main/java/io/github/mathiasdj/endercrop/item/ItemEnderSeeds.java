package io.github.mathiasdj.endercrop.item;

import net.minecraft.item.ItemSeeds;

import io.github.mathiasdj.endercrop.init.ModBlocks;
import io.github.mathiasdj.endercrop.reference.Names;

public class ItemEnderSeeds extends ItemSeeds {

    public ItemEnderSeeds() {
        super(ModBlocks.blockCropEnder, null);
        this.setUnlocalizedName(Names.Items.SEEDS);
    }
}
