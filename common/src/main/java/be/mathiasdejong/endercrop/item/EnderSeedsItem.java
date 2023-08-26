package be.mathiasdejong.endercrop.item;

import be.mathiasdejong.endercrop.init.ModBlocks;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class EnderSeedsItem extends ItemNameBlockItem {

    public EnderSeedsItem() {
        super(ModBlocks.ENDER_CROP.get(), new Properties().arch$tab(CreativeModeTabs.NATURAL_BLOCKS));
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("endercrop.tip.seed"));
    }
}
