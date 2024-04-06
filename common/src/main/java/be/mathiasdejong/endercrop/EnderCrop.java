package be.mathiasdejong.endercrop;

import be.mathiasdejong.endercrop.init.ModBlocks;
import be.mathiasdejong.endercrop.init.ModHooks;
import be.mathiasdejong.endercrop.init.ModItems;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnderCrop {

    public static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        ModBlocks.BLOCKS.register();
        ModItems.ITEMS.register();
        ModHooks.addTillables();
        ModExpectPlatform.initConfig();
    }
}
