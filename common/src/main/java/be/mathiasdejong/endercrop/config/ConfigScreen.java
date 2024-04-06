package be.mathiasdejong.endercrop.config;

import be.mathiasdejong.endercrop.Reference;
import be.mathiasdejong.endercrop.config.EnderCropConfiguration.ConfigValue;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.google.common.collect.Lists;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.DoubleListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

public class ConfigScreen {

    private static final ResourceLocation BACKGROUND =
        new ResourceLocation(Reference.MOD_ID, "textures/block/tilled_end_stone_dry.png");

    public static Screen getConfigScreen(Screen parent) {
        final ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setDefaultBackgroundTexture(BACKGROUND)
            .setTitle(Component.literal("Ender Crop").withStyle(ChatFormatting.AQUA));
        final ConfigCategory defaultCategory = builder.getOrCreateCategory(Component.literal(Reference.MOD_ID));

        Lists.newArrayList(
            ConfigValueToClothEntry.fromDouble(builder, EnderCropConfiguration.tilledSoilMultiplier),
            ConfigValueToClothEntry.fromDouble(builder, EnderCropConfiguration.tilledEndMultiplier),
            ConfigValueToClothEntry.fromBoolean(builder, EnderCropConfiguration.tilledEndStone),
            ConfigValueToClothEntry.fromInt(builder, EnderCropConfiguration.miteChance),
            ConfigValueToClothEntry.fromBoolean(builder, EnderCropConfiguration.endstoneNeedsUnbreaking)
        ).forEach(defaultCategory::addEntry);

        return builder.build();
    }

    private static final class ConfigValueToClothEntry {

        private static DoubleListEntry fromDouble(ConfigBuilder builder, ConfigValue<Double, DoubleValue> configValue) {
            return builder
                .entryBuilder()
                .startDoubleField(Component.literal(configValue.getPath()), configValue.get())
                .setSaveConsumer(configValue.getConfigValue()::set)
                .setTooltip(configValue.getComment())
                .setMin(configValue.getMin())
                .setMax(configValue.getMax())
                .setDefaultValue(configValue.getConfigValue().getDefault())
                .build();
        }

        private static IntegerListEntry fromInt(ConfigBuilder builder, ConfigValue<Integer, IntValue> configValue) {
            return builder
                .entryBuilder()
                .startIntField(Component.literal(configValue.getPath()), configValue.get())
                .setSaveConsumer(configValue.getConfigValue()::set)
                .setTooltip(configValue.getComment())
                .setMin(configValue.getMin())
                .setMax(configValue.getMax())
                .setDefaultValue(configValue.getConfigValue().getDefault())
                .build();
        }

        private static BooleanListEntry fromBoolean(ConfigBuilder builder,
                                                    ConfigValue<Boolean, BooleanValue> configValue) {
            return builder
                .entryBuilder()
                .startBooleanToggle(Component.literal(configValue.getPath()), configValue.get())
                .setSaveConsumer(configValue.getConfigValue()::set)
                .setTooltip(configValue.getComment())
                .setDefaultValue(configValue.getConfigValue().getDefault())
                .build();
        }
    }
}
