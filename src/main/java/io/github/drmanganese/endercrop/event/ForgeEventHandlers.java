package io.github.drmanganese.endercrop.event;

import io.github.drmanganese.endercrop.EnderCrop;
import io.github.drmanganese.endercrop.HoeHelper;
import io.github.drmanganese.endercrop.configuration.EnderCropConfiguration;
import io.github.drmanganese.endercrop.init.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnderCrop.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandlers {

    @SubscribeEvent
    public static void onBonemeal(BonemealEvent event) {
        if (event.getBlock().matchesBlock(ModBlocks.ENDER_CROP.get()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onUseHoe(UseHoeEvent event) {
        if (event.getResult() != Event.Result.DEFAULT || event.isCanceled() || !EnderCropConfiguration.tilledEndStone.get()) {
            return;
        }

        final PlayerEntity player = event.getPlayer();
        final World world = event.getContext().getWorld();
        final BlockPos pos = event.getContext().getPos();

        if (world.getBlockState(pos).getBlock() == Blocks.END_STONE) {
            if (HoeHelper.canHoeEndstone(event.getContext().getItem()) || player.isCreative()) {
                world.setBlockState(pos, ModBlocks.TILLED_END_STONE.get().getDefaultState());
                world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                event.setResult(Event.Result.ALLOW);
            } else if (!world.isRemote && !ModList.get().isLoaded("waila") && !ModList.get().isLoaded("theoneprobe")) {
                final ITextComponent message = new TranslationTextComponent("endercrop.alert.hoe")
                        .setStyle(Style.EMPTY.setFormatting(TextFormatting.GRAY).setItalic(true));
                player.sendStatusMessage(message, true);
            }
        }
    }
}
