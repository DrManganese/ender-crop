package io.github.mathiasdj.endercrop;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import io.github.mathiasdj.endercrop.handler.UseHoeEventHandler;
import io.github.mathiasdj.endercrop.init.ModBlocks;
import io.github.mathiasdj.endercrop.init.ModDungeonLoot;
import io.github.mathiasdj.endercrop.init.ModItems;
import io.github.mathiasdj.endercrop.init.Recipes;
import io.github.mathiasdj.endercrop.proxy.IProxy;
import io.github.mathiasdj.endercrop.reference.Reference;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class EnderCrop
{

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ModItems.init();
        ModBlocks.init();
        Recipes.init();
        ModDungeonLoot.init();
        FMLInterModComms.sendMessage("Waila", "register", "io.github.mathiasdj.endercrop.waila.WailaDataProvider.callbackRegister");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new UseHoeEventHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {

    }
}
