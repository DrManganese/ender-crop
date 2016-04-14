package io.github.mathiasdj.endercrop.init;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import io.github.mathiasdj.endercrop.block.BlockCropEnder;
import io.github.mathiasdj.endercrop.block.BlockTilledEndStone;

public class ModBlocks {

    public static final List<Block> BLOCKS = new ArrayList<>();

    public static final BlockCropEnder CROP_ENDER = new BlockCropEnder();
    public static final BlockTilledEndStone TILLED_END_STONE = new BlockTilledEndStone();

    public static void register(){
        for (Block block : BLOCKS) {
            GameRegistry.register(block);
            GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        for (Block block : BLOCKS) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName().toString()));
        }
    }
}
