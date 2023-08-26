package be.mathiasdejong.endercrop;

import be.mathiasdejong.endercrop.config.EnderCropConfiguration;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.item.enchantment.Enchantments.UNBREAKING;

public final class HoeHelper {

    public static boolean canTillEndstone(ItemStack itemStack, Player player) {
        final Item item = itemStack.getItem();

        if (player != null && player.isCreative()) return true;
        else if (item instanceof HoeItem)
            return EnchantmentHelper.getItemEnchantmentLevel(UNBREAKING, itemStack) > 0
                || !EnderCropConfiguration.endstoneNeedsUnbreaking.get();
        else return false;
    }

    public static ItemStack holdingHoeTool(@NotNull Player player) {
        for (InteractionHand enumHand : InteractionHand.values()) {
            final ItemStack itemStack = player.getItemInHand(enumHand);
            if (itemStack.getItem() instanceof HoeItem) return itemStack;
        }

        return ItemStack.EMPTY;
    }
}
