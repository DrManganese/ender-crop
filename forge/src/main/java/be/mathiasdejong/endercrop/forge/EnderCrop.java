package be.mathiasdejong.endercrop.forge;

import be.mathiasdejong.endercrop.forge.compat.TOPCompatibility;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import dev.architectury.platform.forge.EventBuses;

import static be.mathiasdejong.endercrop.Reference.MOD_ID;

@Mod(MOD_ID)
public class EnderCrop {

    public EnderCrop() {
        EventBuses.registerModEventBus(MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        be.mathiasdejong.endercrop.EnderCrop.init();
        EventBuses.getModEventBus(MOD_ID).ifPresent(eventBus -> eventBus.addListener(EnderCrop::onEnqueueIMC));
    }

    public static void onEnqueueIMC(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe"))
            TOPCompatibility.register();
    }
}
