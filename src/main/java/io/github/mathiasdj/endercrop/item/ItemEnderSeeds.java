package io.github.mathiasdj.endercrop.item;

import io.github.mathiasdj.endercrop.reference.Names;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSeeds;

public class ItemEnderSeeds extends ItemSeeds
{
    public ItemEnderSeeds(Block crops)
    {
        super(crops, null);
        this.setUnlocalizedName(Names.Items.SEEDS);
    }
}
