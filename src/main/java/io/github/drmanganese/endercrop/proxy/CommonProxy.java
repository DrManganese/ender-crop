package io.github.drmanganese.endercrop.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import io.github.drmanganese.endercrop.compat.TOPCompatibility;
import io.github.drmanganese.endercrop.configuration.EnderCropConfiguration;
import io.github.drmanganese.endercrop.handler.LootHandler;
import io.github.drmanganese.endercrop.handler.UseHoeEventHandler;
import io.github.drmanganese.endercrop.init.ModBlocks;
import io.github.drmanganese.endercrop.init.ModItems;

public abstract class CommonProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        FMLInterModComms.sendMessage("Waila", "register", "io.github.mathiasdj.endercrop.compat.WailaDataProvider.callbackRegister");
        if (Loader.isModLoaded("theoneprobe"))
            TOPCompatibility.register();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new LootHandler());
        if (EnderCropConfiguration.tilledEndStone)
            MinecraftForge.EVENT_BUS.register(new UseHoeEventHandler());
    }

    @SubscribeEvent
    public void onBlockRegistry(RegistryEvent.Register<Block> event) {
        ModBlocks.BLOCKS.forEach(event.getRegistry()::register);
    }

    @SubscribeEvent
    public void onItemRegistry(RegistryEvent.Register<Item> event) {
        ModBlocks.BLOCKS.forEach(b -> event.getRegistry().register(new ItemBlock(b).setRegistryName(b.getRegistryName())));
        event.getRegistry().register(ModItems.ENDER_SEEDS);
    }
}
