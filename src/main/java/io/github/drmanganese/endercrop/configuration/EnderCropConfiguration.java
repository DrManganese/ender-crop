package io.github.drmanganese.endercrop.configuration;

import io.github.drmanganese.endercrop.reference.Reference;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
@Config(modid = Reference.MOD_ID)
public class EnderCropConfiguration {

    @Comment({"Percent chance to get an ender pearl"})
    @RangeInt(min = 0, max = 100)
    public static int pearlChance = 100;

    @Comment({"Percent chance to get a second ender pearl"})
    @RangeInt(min = 0, max = 100)
    public static int secondPearlChance = 10;

    @Comment({"Percent chance to get a seed drop"})
    @RangeInt(min = 0, max = 100)
    public static int seedChance = 100;

    @Comment({"Percent chance to get a second seed drop"})
    @RangeInt(min = 0, max = 100)
    public static int secondSeedChance = 10;

    @Comment({"Crop growth multiplier on tilled soil, this value multiplies default vanilla growth rate", "e.g. 10.5 -> Ten and a half times the speed of vanilla crop"})
    @RangeDouble(min = 0)
    public static float tilledSoilMultiplier = 0.5F;

    @Comment({"Crop growth multiplier on tilled end stone, this value multiplies default vanilla growth rate", "e.g. 10.5 -> Ten and a half times the speed of vanilla crop"})
    @RangeDouble(min = 0)
    public static float tilledEndMultiplier = 1.0F;

    @Comment("Enable Tilled End Stone")
    @RequiresMcRestart
    public static boolean tilledEndStone = true;

    @Comment({"Chance to spawn endermite when harvesting an Ender Crop on Tilled End Stone (1 in ...)", "0 to disable"})
    @Name("Endermite chance")
    @RangeInt(min = 0)
    public static int miteChance = 50;

    @Comment({"Ender Seed dungeon spawn weight (e.g.: 1:golden apple, 100:bread)", "0 to disable"})
    @RangeInt(min = 0)
    public static int dungeonChance = 1;

    @Comment({"Is a hoe enchanted with Unbreaking (I) needed to till endstone?"})
    @Name("Tilling end stone needs unbreaking")
    public static boolean endstoneNeedsUnbreaking = true;

    @Comment({"Minimum mattock material level needed to till endstone"})
    @Name("Minimum mattock harvest level")
    @RangeInt(min = 0)
    public static int mattockHarvestLevelEndstone = 1;

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MOD_ID)) {
            ConfigManager.sync(Reference.MOD_ID, Type.INSTANCE);
        }
    }
}
