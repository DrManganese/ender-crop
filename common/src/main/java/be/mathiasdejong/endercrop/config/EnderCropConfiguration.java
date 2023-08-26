package be.mathiasdejong.endercrop.config;

import be.mathiasdejong.endercrop.EnderCrop;

import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import org.jetbrains.annotations.Nullable;

public final class EnderCropConfiguration {

    public static final ForgeConfigSpec COMMON_CONFIG;
    public static ConfigValue<Double, ForgeConfigSpec.DoubleValue> tilledSoilMultiplier;
    public static ConfigValue<Double, ForgeConfigSpec.DoubleValue> tilledEndMultiplier;
    public static ConfigValue<Boolean, ForgeConfigSpec.BooleanValue> tilledEndStone;
    public static ConfigValue<Integer, ForgeConfigSpec.IntValue> miteChance;
    public static ConfigValue<Boolean, ForgeConfigSpec.BooleanValue> endstoneNeedsUnbreaking;

    static {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        buildConfig(builder);
        COMMON_CONFIG = builder.build();
    }

    public static void buildConfig(ForgeConfigSpec.Builder builder) {
        tilledSoilMultiplier = ConfigValue.defineDouble(
            builder,
            "tilledSoilMultiplier",
            0.5D, 0.0D, 25.0D,
            "Crop growth multiplier on farmland, this value multiplies the default vanilla growth rate.",
            "Example: 10.5 -> Ten and a half times the speed of vanilla crop growth.");
        tilledEndMultiplier = ConfigValue.defineDouble(
            builder,
            "tilledEndMultiplier",
            1.0D, 0.0D, 25.0D,
            "Crop growth multiplier on tilled end stone, this value multiplies the default vanilla growth rate.",
            "Example: 10.5 -> Ten and a half times the speed of vanilla crop growth.");
        tilledEndStone = ConfigValue.defineBoolean(
            builder,
            "tilledEndStone",
            true,
            "Enable tilling End stone.",
            "Disabling this does not remove the Tilled End Stone block, only the creation of it.");
        miteChance = ConfigValue.defineInt(
            builder,
            "miteChance",
            50, 0, Integer.MAX_VALUE,
            "Chance of spawning an Endermite when a fully grown Ender Crop grown on Tilled End Stone is harvested.",
            "Example: 10 -> 1 in 10 chance. 0 to disable.");
        endstoneNeedsUnbreaking = ConfigValue.defineBoolean(
            builder,
            "endstoneNeedsUnbreaking",
            true,
            "Require a hoe to be enchanted with Unbreaking (I) to till End stone.");
    }

    public static void onLoad(ModConfig modConfig) {
        EnderCrop.LOGGER.info("Reloading {} from disk", modConfig.getFileName());
        COMMON_CONFIG.acceptConfig(modConfig.getConfigData());
    }

    /**
     * Wrapper around the Forge config spec to make it easier to use when building the {@link ConfigScreen}.
     */
    public static class ConfigValue<T, V extends ForgeConfigSpec.ConfigValue<T>> {

        private final V configValue;
        private final String path;
        private final T min;
        private final T max;
        private final Component comment;

        private ConfigValue(V configValue, String path, @Nullable T min, @Nullable T max, String... comment) {
            this.configValue = configValue;
            this.path = path;
            this.min = min;
            this.max = max;
            this.comment = Component.literal(String.join("\n", comment));
        }

        public static ConfigValue<Double, ForgeConfigSpec.DoubleValue> defineDouble(ForgeConfigSpec.Builder builder,
                                                                                    String path, double defaultValue,
                                                                                    double min, double max,
                                                                                    String... comment) {
            return new ConfigValue<>(builder.comment(comment).defineInRange(path, defaultValue, min, max),
                path, min, max, comment);
        }

        public static ConfigValue<Integer, ForgeConfigSpec.IntValue> defineInt(ForgeConfigSpec.Builder builder,
                                                                               String path, int defaultValue, int min,
                                                                               int max, String... comment) {
            return new ConfigValue<>(
                builder.comment(comment).defineInRange(path, defaultValue, min, max), path, min, max, comment
            );
        }

        public static ConfigValue<Boolean, ForgeConfigSpec.BooleanValue> defineBoolean(ForgeConfigSpec.Builder builder,
                                                                                       String path,
                                                                                       boolean defaultValue,
                                                                                       String... comment) {
            return new ConfigValue<>(builder.comment(comment).define(path, defaultValue), path, null, null, comment);
        }

        public T get() {
            return configValue.get();
        }

        public V getConfigValue() {
            return configValue;
        }

        public T getMin() {
            return min;
        }

        public T getMax() {
            return max;
        }

        public Component getComment() {
            return comment;
        }

        public String getPath() {
            return path;
        }
    }
}