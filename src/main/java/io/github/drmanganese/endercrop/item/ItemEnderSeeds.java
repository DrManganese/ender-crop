package io.github.drmanganese.endercrop.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.github.drmanganese.endercrop.init.ModBlocks;
import io.github.drmanganese.endercrop.reference.Names;

import java.util.List;

import javax.annotation.Nullable;

public class ItemEnderSeeds extends ItemSeeds {

    public ItemEnderSeeds() {
        super(ModBlocks.CROP_ENDER, null);
        this.setUnlocalizedName(Names.Items.SEEDS);
        this.setRegistryName(Names.Items.SEEDS);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (stack.getItem() instanceof ItemSeeds) {
            tooltip.add(I18n.format("endercrop.tip.seed"));
        }
    }
}
