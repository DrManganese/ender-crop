package io.github.mathiasdj.endercrop.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import io.github.mathiasdj.endercrop.client.render.BlockRenderRegister;
import io.github.mathiasdj.endercrop.client.render.ItemRenderRegister;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        this.initRenderingAndTextures();
    }

    private void initRenderingAndTextures() {
        ItemRenderRegister.registerItemRenderers();
        BlockRenderRegister.registerBlockRenderers();
    }
}
