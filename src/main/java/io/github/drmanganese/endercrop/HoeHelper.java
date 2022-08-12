package io.github.drmanganese.endercrop;

import io.github.drmanganese.endercrop.configuration.EnderCropConfiguration;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public final class HoeHelper {

    public static boolean canHoeEndstone(@Nonnull ItemStack itemStack, @Nullable Player player, @Nullable BlockState blockState) {
        final Item item = itemStack.getItem();

        if (player != null && player.isCreative())
            return true;
        else if (item instanceof HoeItem)
            return itemStack.getEnchantmentLevel(Enchantments.UNBREAKING) > 0 || !EnderCropConfiguration.endstoneNeedsUnbreaking.get();
        else
            return false;
    }

    public static boolean isHoe(@Nonnull ItemStack itemStack) {
        return itemStack.getItem() instanceof HoeItem;
    }

    /**
     * Checks if the player is holding a tool that can till in any hand
     *
     * @param player The player being checked
     * @return the first {@link ItemStack} which is a hoe or {@link ItemStack#EMPTY} if no hoe was found
     */
    public static ItemStack holdingHoeTool(@Nonnull Player player) {
        for (InteractionHand enumHand : InteractionHand.values()) {
            final ItemStack itemStack = player.getItemInHand(enumHand);
            if (isHoe(itemStack)) return itemStack;
        }

        return ItemStack.EMPTY;
    }

    // Info

    public static Component getHarvestLevelInfo(ItemStack itemStack) {
        final int requiredLevel = EnderCropConfiguration.hoeToolHarvestLevelEndstone.get();
        final Component defaultText = Component.literal(String.valueOf(requiredLevel));

        if (ForgeRegistries.ITEMS.getKey(itemStack.getItem()).getNamespace().equals("tconstruct"))
            return getHarvestLevelName(requiredLevel).orElse(defaultText);
        else
            return defaultText;
    }

    public static Optional<Component> getHarvestLevelName(int i) {
        return Optional.empty();
    }

    public static MutableComponent getToolErrorMessage(ItemStack itemStack) {
        if (itemStack.getItem() instanceof HoeItem)
            return Component.translatable("endercrop.alert.hoe");
        else
            return Component.translatable("endercrop.alert.hoetool", getHarvestLevelInfo(itemStack));
    }
}
