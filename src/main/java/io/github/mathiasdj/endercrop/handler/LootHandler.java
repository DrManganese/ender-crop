package io.github.mathiasdj.endercrop.handler;

import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import io.github.mathiasdj.endercrop.configuration.EnderCropConfiguration;
import io.github.mathiasdj.endercrop.init.ModItems;
import io.github.mathiasdj.endercrop.reference.Reference;

public class LootHandler {

    @SubscribeEvent
    public void lootTableLoadEvent(LootTableLoadEvent event) {
        if (event.getName().equals(LootTableList.CHESTS_ABANDONED_MINESHAFT) ||
                event.getName().equals(LootTableList.CHESTS_IGLOO_CHEST) ||
                event.getName().equals(LootTableList.CHESTS_DESERT_PYRAMID) ||
                event.getName().equals(LootTableList.CHESTS_JUNGLE_TEMPLE) ||
                event.getName().equals(LootTableList.CHESTS_SIMPLE_DUNGEON)) {
            LootPool main = event.getTable().getPool("main");
            if (main != null)
                main.addEntry(new LootEntryItem(ModItems.ENDER_SEEDS, EnderCropConfiguration.dungeonChance, 0, new LootFunction[0], new LootCondition[0], Reference.MOD_ID + ":seeds"));
        }
    }
}
