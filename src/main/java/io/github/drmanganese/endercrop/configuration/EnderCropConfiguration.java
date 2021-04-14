package io.github.drmanganese.endercrop.configuration;

import io.github.drmanganese.endercrop.EnderCrop;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnderCrop.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class EnderCropConfiguration {

    public static final ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.DoubleValue tilledSoilMultiplier;
    public static ForgeConfigSpec.DoubleValue tilledEndMultiplier;
    public static ForgeConfigSpec.BooleanValue tilledEndStone;
    public static ForgeConfigSpec.IntValue miteChance;
    public static ForgeConfigSpec.BooleanValue endstoneNeedsUnbreaking;
    public static ForgeConfigSpec.IntValue hoeToolHarvestLevelEndstone;

    static {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        buildConfig(builder);
        COMMON_CONFIG = builder.build();
    }

    public static void buildConfig(ForgeConfigSpec.Builder builder) {
        tilledSoilMultiplier = builder
                .comment("Crop growth multiplier on tilled soil, this value multiplies default vanilla growth rate", "e.g. 10.5 -> Ten and a half times the speed of vanilla crop.")
                .defineInRange("tilledSoilMultiplier", 0.5D, 0.0D, 25.0D);
        tilledEndMultiplier = builder
                .comment("Crop growth multiplier on tilled end stone, this value multiplies default vanilla growth rate", "e.g. 10.5 -> Ten and a half times the speed of vanilla crop")
                .defineInRange("tilledEndMultiplier", 1.0D, 0.0D, 25.0D);
        tilledEndStone = builder
                .comment("Enable Tilled End Stone")
                .define("tilledEndStone", true);
        miteChance = builder
                .comment("Chance to spawn endermite when harvesting an Ender Crop on Tilled End Stone (1 in ...)", "0 to disable")
                .defineInRange("miteChance", 50, 0, Integer.MAX_VALUE);
        endstoneNeedsUnbreaking = builder
                .comment("Is a hoe enchanted with Unbreaking (I) needed to till endstone?")
                .define("endstoneNeedsUnbreaking", true);
        hoeToolHarvestLevelEndstone = builder
                .comment("Minimum non-vanilla hoe tool (e.g. TiCo Kama) material level needed to till endstone")
                .defineInRange("hoeToolHarvestLevelEndstone", 1, 0, 20);
    }
}
