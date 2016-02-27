package io.github.mathiasdj.endercrop.client.render;

import io.github.mathiasdj.endercrop.init.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class BlockRenderRegister
{
    public static void registerBlockRenderers()
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.blockCropEnder), 0, new ModelResourceLocation("endercrop:ender_crop"));
    }
}
