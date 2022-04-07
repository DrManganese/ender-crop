package io.github.drmanganese.endercrop.item;

import io.github.drmanganese.endercrop.init.ModBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class EnderSeedsItem extends ItemNameBlockItem {

    public EnderSeedsItem() {
        super(ModBlocks.ENDER_CROP.get(), (new Item.Properties()).tab(CreativeModeTab.TAB_MATERIALS));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, pLevel, tooltip, flag);
        tooltip.add(new TranslatableComponent("endercrop.tip.seed"));
    }
}
