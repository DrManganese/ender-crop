package io.github.mathiasdj.endercrop.handler;

import io.github.mathiasdj.endercrop.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class UseHoeEventHandler
{
    @SubscribeEvent
    public void useHoe (UseHoeEvent event)
    {
        if (event.getResult() != Event.Result.DEFAULT || event.isCanceled())
        {
            return;
        }

        World world = event.world;
        BlockPos pos = event.pos;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block == Blocks.end_stone) {
            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, event.current) > 0 || event.entityPlayer.capabilities.isCreativeMode) {
                world.setBlockState(pos, ModBlocks.blockTilledEndStone.getDefaultState());
                event.setResult(Event.Result.ALLOW);
            } else {
                if (!world.isRemote)
                    event.entityPlayer.addChatComponentMessage(new ChatComponentText("\u00A77\u00A7o" + "This block can only be tilled by a hoe enchanted with Unbreaking I+"));
            }
        }
    }
}
