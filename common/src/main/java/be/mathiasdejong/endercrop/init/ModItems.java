package be.mathiasdejong.endercrop.init;

import be.mathiasdejong.endercrop.Reference;
import be.mathiasdejong.endercrop.item.EnderSeedsItem;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

import static be.mathiasdejong.endercrop.Reference.MOD_ID;

public final class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> ENDER_SEEDS = ITEMS.register(Reference.Items.SEEDS, EnderSeedsItem::new);

    public static final RegistrySupplier<BlockItem> TILLED_END_STONE = ITEMS.register(
        Reference.Blocks.TILLED_END_STONE,
        () -> new BlockItem(ModBlocks.TILLED_END_STONE.get(),
            new Item.Properties().arch$tab(CreativeModeTabs.NATURAL_BLOCKS))
    );
}
