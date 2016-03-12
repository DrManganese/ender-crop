package io.github.mathiasdj.endercrop.client.render;

import io.github.mathiasdj.endercrop.init.ModBlocks;
import io.github.mathiasdj.endercrop.init.ModItems;
import io.github.mathiasdj.endercrop.reference.Models;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;

public class ItemRenderRegister
{
    public static void registerItemRenderers()
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ModItems.item_ender_seeds, 0, Models.Item.SEEDS);

        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.blockTilledEndStone), 0, Models.Block.TILLED_END_STONE);
    }
}
