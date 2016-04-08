package io.github.mathiasdj.endercrop.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import io.github.mathiasdj.endercrop.init.ModBlocks;
import io.github.mathiasdj.endercrop.init.ModItems;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ModBlocks.initModels();
        ModItems.initModels();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }
}
