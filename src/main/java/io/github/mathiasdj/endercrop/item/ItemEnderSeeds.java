package io.github.mathiasdj.endercrop.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSeeds;

import io.github.mathiasdj.endercrop.reference.Names;

public class ItemEnderSeeds extends ItemSeeds {

    public ItemEnderSeeds(Block crops, Block soil) {
        super(crops, soil);
        this.setUnlocalizedName(Names.Items.SEEDS);
    }
}
