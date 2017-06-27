package io.github.drmanganese.endercrop.init;


import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.github.drmanganese.endercrop.item.ItemEnderSeeds;

public class ModItems {

    public static final ItemEnderSeeds ENDER_SEEDS = new ItemEnderSeeds();

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        ModelLoader.setCustomModelResourceLocation(ENDER_SEEDS, 0, new ModelResourceLocation(ENDER_SEEDS.getRegistryName().toString()));
    }
}
