package io.github.drmanganese.endercrop.event;

import io.github.drmanganese.endercrop.EnderCrop;
import io.github.drmanganese.endercrop.HoeHelper;
import io.github.drmanganese.endercrop.configuration.EnderCropConfiguration;
import io.github.drmanganese.endercrop.init.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnderCrop.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandlers {

    @SubscribeEvent
    public static void onBonemeal(BonemealEvent event) {
        if (event.getBlock().is(ModBlocks.ENDER_CROP.get()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onUseHoe(BlockEvent.BlockToolModificationEvent event) {
        if (event.isCanceled() || ! event.getToolAction().equals(ToolActions.HOE_TILL) || !EnderCropConfiguration.tilledEndStone.get())
            return;

        final UseOnContext ctx = event.getContext();
        final Level level = ctx.getLevel();
        final BlockState blockState = level.getBlockState(ctx.getClickedPos());
        final Player player = event.getPlayer();

        if (blockState.is(Blocks.END_STONE) && HoeItem.onlyIfAirAbove(ctx)) {
            if (HoeHelper.canHoeEndstone(ctx.getItemInHand(), player, blockState)) {
                event.setFinalState(ModBlocks.TILLED_END_STONE.get().defaultBlockState());
            } else if (!event.getContext().getLevel().isClientSide() && !EnderCrop.theOneProbeLoaded && player != null) {
                final MutableComponent message = HoeHelper.getToolErrorMessage(ctx.getItemInHand())
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true));
                player.displayClientMessage(message, true);
            }
        }
    }
}
