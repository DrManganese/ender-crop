package io.github.drmanganese.endercrop.item;

import io.github.drmanganese.endercrop.init.ModBlocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class EnderSeedsItem extends BlockNamedItem {

    public EnderSeedsItem() {
        super(ModBlocks.ENDER_CROP.get(), (new Item.Properties()).group(ItemGroup.MATERIALS));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("endercrop.tip.seed"));
    }
}
