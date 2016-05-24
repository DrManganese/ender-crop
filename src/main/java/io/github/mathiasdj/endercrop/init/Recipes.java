package io.github.mathiasdj.endercrop.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Recipes {

    public static void init() {
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.ENDER_SEEDS), " p ", "psp", " p ", 'p', Items.ENDER_PEARL, 's', Items.WHEAT_SEEDS);
    }
}
