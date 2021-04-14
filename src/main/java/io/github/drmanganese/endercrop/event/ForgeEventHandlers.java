package io.github.drmanganese.endercrop.event;

import io.github.drmanganese.endercrop.EnderCrop;
import io.github.drmanganese.endercrop.HoeHelper;
import io.github.drmanganese.endercrop.configuration.EnderCropConfiguration;
import io.github.drmanganese.endercrop.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnderCrop.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandlers {

    @SubscribeEvent
    public static void onBonemeal(BonemealEvent event) {
        if (event.getBlock().matchesBlock(ModBlocks.ENDER_CROP.get()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onBlockToolInteract(BlockEvent.BlockToolInteractEvent event) {
        if (event.isCanceled() || !EnderCropConfiguration.tilledEndStone.get())
            return;

        final BlockState blockState = event.getFinalState();
        final PlayerEntity player = event.getPlayer();

        if (blockState.matchesBlock(Blocks.END_STONE) && event.getToolType() == ToolType.HOE) {
            if (HoeHelper.canHoeEndstone(event.getHeldItemStack(), player, blockState)) {
                event.setFinalState(ModBlocks.TILLED_END_STONE.get().getDefaultState());
            } else if (!event.getWorld().isRemote() && !EnderCrop.theOneProbeLoaded) {
                final IFormattableTextComponent message = HoeHelper.getToolErrorMessage(event.getHeldItemStack())
                    .setStyle(Style.EMPTY.setFormatting(TextFormatting.GRAY).setItalic(true));
                player.sendStatusMessage(message, true);
            }
        }
    }
}
