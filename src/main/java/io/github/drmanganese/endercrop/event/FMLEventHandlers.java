package io.github.drmanganese.endercrop.event;

import io.github.drmanganese.endercrop.EnderCrop;
import io.github.drmanganese.endercrop.compat.TOPCompatibility;
import io.github.drmanganese.endercrop.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

@Mod.EventBusSubscriber(modid = EnderCrop.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class FMLEventHandlers {

    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {
        final Block tilledEndStone = ModBlocks.TILLED_END_STONE.get();
        final BlockItem tilledEndStoneBlockItem = new BlockItem(tilledEndStone, new Item.Properties().group(ItemGroup.DECORATIONS));
        tilledEndStoneBlockItem.setRegistryName(tilledEndStone.getRegistryName());
        event.getRegistry().register(tilledEndStoneBlockItem);
    }

    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(ModBlocks.ENDER_CROP.get(), RenderType.getCutout());
    }

    @SubscribeEvent
    public static void onEnqueueIMC(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe"))
            TOPCompatibility.register();
    }
}
