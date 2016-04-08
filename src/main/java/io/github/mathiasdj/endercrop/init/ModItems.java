package io.github.mathiasdj.endercrop.init;


import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.github.mathiasdj.endercrop.item.ItemEnderSeeds;

public class ModItems {

    public static final ItemEnderSeeds item_ender_seeds = new ItemEnderSeeds();

    public static void init() {
        GameRegistry.register(item_ender_seeds);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        ModelLoader.setCustomModelResourceLocation(item_ender_seeds, 0, new ModelResourceLocation(item_ender_seeds.getRegistryName().toString()));
    }
}
