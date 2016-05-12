package io.github.mathiasdj.endercrop.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import io.github.mathiasdj.endercrop.configuration.EnderCropConfiguration;
import io.github.mathiasdj.endercrop.handler.LootHandler;
import io.github.mathiasdj.endercrop.handler.UseHoeEventHandler;
import io.github.mathiasdj.endercrop.init.ModBlocks;
import io.github.mathiasdj.endercrop.init.ModItems;
import io.github.mathiasdj.endercrop.init.Recipes;

public abstract class CommonProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        EnderCropConfiguration.init(event.getSuggestedConfigurationFile());

        ModBlocks.register();
        ModItems.init();
        Recipes.init();

        FMLInterModComms.sendMessage("Waila", "register", "io.github.mathiasdj.endercrop.waila.WailaDataProvider.callbackRegister");
    }

    @Override
    public void init(FMLInitializationEvent event) {
        System.out.println(EnderCropConfiguration.tilledEndStone);

        MinecraftForge.EVENT_BUS.register(new LootHandler());
        if (EnderCropConfiguration.tilledEndStone)
            MinecraftForge.EVENT_BUS.register(new UseHoeEventHandler());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }
}
