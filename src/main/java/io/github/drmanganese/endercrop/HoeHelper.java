package io.github.drmanganese.endercrop;

import io.github.drmanganese.endercrop.configuration.EnderCropConfiguration;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public final class HoeHelper {

    public static Method tiCoGetHarvestLevelName = null;

    static {
        try {
            tiCoGetHarvestLevelName = Class.forName("slimeknights.tconstruct.library.utils.HarvestLevels").getDeclaredMethod("getHarvestLevelName", int.class);
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            //
        }
    }

    public static boolean canHoeEndstone(@Nonnull ItemStack itemStack, @Nullable PlayerEntity player, @Nullable BlockState blockState) {
        final Item item = itemStack.getItem();

        if (player != null && player.isCreative())
            return true;
        else if (item instanceof HoeItem)
            return EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, itemStack) > 0 || !EnderCropConfiguration.endstoneNeedsUnbreaking.get();
        else if (isHoe(itemStack)) {
            final int harvestLevel = item.getHarvestLevel(itemStack, ToolType.HOE, player, blockState);
            return harvestLevel >= EnderCropConfiguration.hoeToolHarvestLevelEndstone.get();
        } else
            return false;
    }

    public static boolean isHoe(@Nonnull ItemStack itemStack) {
        return itemStack.getToolTypes().contains(ToolType.HOE);
    }

    /**
     * Checks if the player is holding a tool that can till in any hand
     *
     * @param player The player being checked
     * @return the first {@link ItemStack} which is a hoe or {@link ItemStack#EMPTY} if no hoe was found
     */
    public static ItemStack holdingHoeTool(@Nonnull PlayerEntity player) {
        for (Hand enumHand : Hand.values()) {
            final ItemStack itemStack = player.getHeldItem(enumHand);
            if (isHoe(itemStack)) return itemStack;
        }

        return ItemStack.EMPTY;
    }

    // Info

    public static ITextComponent getHarvestLevelInfo(ItemStack itemStack) {
        final int requiredLevel = EnderCropConfiguration.hoeToolHarvestLevelEndstone.get();
        final ITextComponent defaultText = new StringTextComponent(String.valueOf(requiredLevel));

        if (itemStack.getItem().getRegistryName().getNamespace().equals("tconstruct"))
            return getHarvestLevelName(requiredLevel).orElse(defaultText);
        else
            return defaultText;
    }

    public static Optional<ITextComponent> getHarvestLevelName(int i) {
        if (tiCoGetHarvestLevelName != null) {
            try {
                return Optional.of((ITextComponent) tiCoGetHarvestLevelName.invoke(null, i));
            } catch (IllegalAccessException | InvocationTargetException e) {
                //
            }
        }

        return Optional.empty();
    }

    public static IFormattableTextComponent getToolErrorMessage(ItemStack itemStack) {
        if (itemStack.getItem() instanceof HoeItem)
            return new TranslationTextComponent("endercrop.alert.hoe");
        else
            return new TranslationTextComponent("endercrop.alert.hoetool", getHarvestLevelInfo(itemStack));
    }
}
