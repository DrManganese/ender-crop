package io.github.drmanganese.endercrop.event;

import io.github.drmanganese.endercrop.EnderCrop;
import io.github.drmanganese.endercrop.compat.TOPCompatibility;
import io.github.drmanganese.endercrop.init.ModBlocks;
import io.github.drmanganese.endercrop.reference.Names;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = EnderCrop.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class FMLEventHandlers {

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ITEMS, helper -> {
            final Block tilledEndStone = ModBlocks.TILLED_END_STONE.get();
            final BlockItem tilledEndStoneBlockItem = new BlockItem(tilledEndStone, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS));

            helper.register(new ResourceLocation("endercrop", Names.Blocks.TILLED_END_STONE), tilledEndStoneBlockItem);
        });
    }

    @SubscribeEvent
    public static void onEnqueueIMC(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe"))
            TOPCompatibility.register();
    }
}
