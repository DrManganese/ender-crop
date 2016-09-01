package io.github.drmanganese.endercrop.configuration;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class EnderCropConfiguration {

    public static boolean tilledEndStone = true;
    public static int miteChance = 50;
    public static int dungeonChance = 1;

    public static void init(File file) {
        Configuration config = new Configuration(file);
        config.load();

        tilledEndStone = config.getBoolean("tilledEndstone", Configuration.CATEGORY_GENERAL, true, "Enable Tilled End Stone");
        miteChance = config.getInt("miteChance", Configuration.CATEGORY_GENERAL, 50, 0, 50, "Chance to spawn endermite when harvesting an Ender Crop on Tilled End Stone (1 in ...)\n0 to disable");
        dungeonChance = config.getInt("dungeonChance", Configuration.CATEGORY_GENERAL, 1, 0, Integer.MAX_VALUE, "Ender Seed dungeon spawn weight (e.g.: 1:golden apple, 100:bread)\n0 to disable");

        if (config.hasChanged())
            config.save();
    }
}
