package io.github.mathiasdj.endercrop.init;


import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import io.github.mathiasdj.endercrop.item.ItemEnderSeeds;

public class ModItems {

    public static final ItemEnderSeeds item_ender_seeds = new ItemEnderSeeds();

    public static void init() {
        GameRegistry.register(item_ender_seeds);
        initModel(item_ender_seeds);
    }

    private static void initModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString()));
    }
}
