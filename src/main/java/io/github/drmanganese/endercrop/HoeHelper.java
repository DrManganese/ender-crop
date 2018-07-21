package io.github.drmanganese.endercrop;

import io.github.drmanganese.endercrop.configuration.EnderCropConfiguration;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class HoeHelper {

    public static Method tiCoGetHarvestLevelName = null;

    static {
        try {
            tiCoGetHarvestLevelName = Class.forName("slimeknights.tconstruct.library.utils.HarvestLevels").getDeclaredMethod("getHarvestLevelName", int.class);
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            //
        }
    }

    /**
     * Checks if the tool in the itemstack can till end stone
     *
     * @param itemStack The held itemstack
     * @return <code>true</code> if the tool can till end stone
     */
    public static boolean canHoeEndstone(@Nonnull ItemStack itemStack) {
        final Item item = itemStack.getItem();

        if (item.getToolClasses(itemStack).contains("mattock")) {
            // If the tool is a Tinkers' Construct mattock, check if its harvest level is greater than or equal to the
            // configurated value
            return item.getHarvestLevel(itemStack, "mattock", null, null) >= EnderCropConfiguration.mattockHarvestLevelEndstone;
        } else if (item instanceof ItemHoe) {
            // If the tool is a hoe, check if it has been enchanted with unbreaking
            // Always true when configurated to ignore
            return EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, itemStack) > 0 || !EnderCropConfiguration.endstoneNeedsUnbreaking;
        } else {
            return false;
        }
    }

    public static boolean canHoeEndstone(@Nonnull EntityPlayer player) {
        for (EnumHand enumHand : EnumHand.values()) {
            if (canHoeEndstone(player.getHeldItem(enumHand))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the player is holding a tool that can till in any hand
     *
     * @param player The player being checked
     * @return the first {@link ItemStack} which is a hoe or {@link ItemStack#EMPTY} if no hoe was found
     */
    public static ItemStack holdingHoeTool(@Nonnull EntityPlayer player) {
        for (EnumHand enumHand : EnumHand.values()) {
            final ItemStack itemStack = player.getHeldItem(enumHand);
            if (itemStack.isEmpty()) continue;
            if (itemStack.getItem() instanceof ItemHoe || itemStack.getItem().getToolClasses(itemStack).contains("mattock")) {
                return itemStack.copy();
            }
        }

        return ItemStack.EMPTY;
    }

    public static String getHarvestLevelName(int i) {
        if (tiCoGetHarvestLevelName != null) {
            try {
                return (String) tiCoGetHarvestLevelName.invoke(null, i);
            } catch (IllegalAccessException | InvocationTargetException e) {
                //
            }
        }

        return new String[]{"Stone", "Iron", "Diamond", "Obsidian", "Cobalt"}[i];

    }
}
