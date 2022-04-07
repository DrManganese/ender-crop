package io.github.drmanganese.endercrop.init;

import io.github.drmanganese.endercrop.EnderCrop;
import io.github.drmanganese.endercrop.block.EnderCropBlock;
import io.github.drmanganese.endercrop.block.TilledEndstoneBlock;
import io.github.drmanganese.endercrop.reference.Names;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, EnderCrop.MOD_ID);

    public static final RegistryObject<Block> ENDER_CROP = BLOCKS.register(Names.Blocks.ENDER_CROP, EnderCropBlock::new);
    public static final RegistryObject<Block> TILLED_END_STONE = BLOCKS.register(Names.Blocks.TILLED_END_STONE, TilledEndstoneBlock::new);
}
