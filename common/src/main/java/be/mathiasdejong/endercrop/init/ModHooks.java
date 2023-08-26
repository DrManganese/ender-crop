package be.mathiasdejong.endercrop.init;

import be.mathiasdejong.endercrop.HoeHelper;
import be.mathiasdejong.endercrop.config.EnderCropConfiguration;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.level.block.Blocks;

import dev.architectury.hooks.item.tool.HoeItemHooks;

import java.util.Objects;

public final class ModHooks {

    private static final Component UNTILLABLE_MESSAGE = Component.translatable("endercrop.alert.hoe")
        .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true));

    public static void addTillables() {
            HoeItemHooks.addTillable(
                Blocks.END_STONE,
                ctx -> {
                    if (EnderCropConfiguration.tilledEndStone.get()) {
                        final boolean canTill = HoeItem.onlyIfAirAbove(ctx)
                            && HoeHelper.canTillEndstone(ctx.getItemInHand(), ctx.getPlayer());
                        if (!canTill)
                            Objects.requireNonNull(ctx.getPlayer()).displayClientMessage(UNTILLABLE_MESSAGE, true);
                        return canTill;
                    } else
                        return false;
                },
                ctx -> {
                },
                ctx -> ModBlocks.TILLED_END_STONE.get().defaultBlockState());
    }
}
