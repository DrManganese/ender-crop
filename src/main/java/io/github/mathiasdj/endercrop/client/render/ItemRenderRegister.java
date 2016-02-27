package io.github.mathiasdj.endercrop.client.render;

import io.github.mathiasdj.endercrop.init.ModItems;
import io.github.mathiasdj.endercrop.reference.Models;
import net.minecraft.client.Minecraft;

public class ItemRenderRegister
{
    public static void registerItemRenderers()
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ModItems.item_seeds, 0, Models.Item.SEEDS);
    }
}
