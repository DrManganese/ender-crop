package io.github.mathiasdj.endercrop.handler;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.UseHoeEvent;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import io.github.mathiasdj.endercrop.init.ModBlocks;

public class UseHoeEventHandler {

    @SubscribeEvent
    public void useHoe(UseHoeEvent event) {
        if (event.getResult() != Event.Result.DEFAULT || event.isCanceled()) {
            return;
        }

        World world = event.world;
        Block block = world.getBlock(event.x, event.y, event.z);

        if (block == Blocks.end_stone) {
            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, event.current) > 0 || event.entityPlayer.capabilities.isCreativeMode) {
                world.setBlock(event.x, event.y, event.z, ModBlocks.blockTilledEndStone);
                event.setResult(Event.Result.ALLOW);
            } else {
                if (!world.isRemote)
                    event.entityPlayer.addChatComponentMessage(new ChatComponentText("\u00A77\u00A7oThis block can only be tilled by a hoe enchanted with \u00A77\u00A7oUnbreaking I+"));
            }
        }
    }
}
