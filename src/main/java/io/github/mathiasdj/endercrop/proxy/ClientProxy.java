package io.github.mathiasdj.endercrop.proxy;

import io.github.mathiasdj.endercrop.client.render.BlockRenderRegister;
import io.github.mathiasdj.endercrop.client.render.ItemRenderRegister;

public class ClientProxy extends CommonProxy {

    @Override
    public void initRenderingAndTextures() {
        ItemRenderRegister.registerItemRenderers();
        BlockRenderRegister.registerBlockRenderers();
    }
}
