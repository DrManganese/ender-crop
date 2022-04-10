package io.github.drmanganese.endercrop.init;


import io.github.drmanganese.endercrop.EnderCrop;
import io.github.drmanganese.endercrop.item.EnderSeedsItem;
import io.github.drmanganese.endercrop.reference.Names;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EnderCrop.MOD_ID);

    public static final RegistryObject<Item> ENDER_SEEDS = ITEMS.register(Names.Items.SEEDS, EnderSeedsItem::new);
}
