package io.github.mathiasdj.endercrop.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Recipes
{
    public static void init()
    {
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.item_seeds), " p ", "psp", " p ", 'p', Items.ender_pearl, 's', Items.wheat_seeds);
    }
}
