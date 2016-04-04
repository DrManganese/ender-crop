package io.github.mathiasdj.endercrop.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;

import java.util.List;

import io.github.mathiasdj.endercrop.init.ModBlocks;
import io.github.mathiasdj.endercrop.reference.Names;

public class ItemEnderSeeds extends ItemSeeds {

    public ItemEnderSeeds() {
        super(ModBlocks.blockCropEnder, null);
        this.setUnlocalizedName(Names.Items.SEEDS);
        this.setRegistryName(Names.Items.SEEDS);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        if (stack.getItem() instanceof ItemSeeds) {
            tooltip.add("Plant on farmland or tilled end stone");
        }
    }
}
