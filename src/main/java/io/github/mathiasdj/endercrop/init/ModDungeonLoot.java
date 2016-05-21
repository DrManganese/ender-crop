package io.github.mathiasdj.endercrop.init;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import io.github.mathiasdj.endercrop.configuration.EnderCropConfiguration;

public class ModDungeonLoot {

    public static void init() {
        if (EnderCropConfiguration.dungeonChance > 0)
            ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ModItems.item_ender_seeds), 1, 2, EnderCropConfiguration.dungeonChance));
    }
}
