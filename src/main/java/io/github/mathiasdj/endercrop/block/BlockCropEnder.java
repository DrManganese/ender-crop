package io.github.mathiasdj.endercrop.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.mathiasdj.endercrop.init.ModItems;
import io.github.mathiasdj.endercrop.reference.Names;
import io.github.mathiasdj.endercrop.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.Random;

public class BlockCropEnder extends BlockCrops
{
    @SideOnly(Side.CLIENT)
    private IIcon[] field_149867_a;

    public BlockCropEnder()
    {
        super();
        this.setBlockName(Names.Blocks.ENDER_CROP);
    }

    @Override
    public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        return player.getHeldItem().getItem().equals(Items.dye);
    }

    @Override
    public String getUnlocalizedName()
    {
        return String.format("tile.%s%s", Reference.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        if (p_149691_2_ < 0 || p_149691_2_ > 7)
        {
            p_149691_2_ = 7;
        }

        return this.field_149867_a[p_149691_2_];
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.field_149867_a = new IIcon[8];

        for (int i = 0; i < this.field_149867_a.length; ++i) {
            this.field_149867_a[i] = iconRegister.registerIcon(String.format("%s", getUnwrappedUnlocalizedName(this.getUnlocalizedName())) + "_stage" + i);
        }
    }


    @Override
    public int getRenderType()
    {
        return 1;
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    @Override
    public boolean func_149852_a(World p_149852_1_, Random p_149852_2_, int p_149852_3_, int p_149852_4_, int p_149852_5_)
    {
        return false;
    }

    //getSeed
    @Override
    protected Item func_149866_i()
    {
        return ModItems.item_seeds;
    }

    //getCrop
    @Override
    protected Item func_149865_P()
    {
        return Items.ender_pearl;
    }

    @Override
    protected boolean canPlaceBlockOn(Block ground)
    {
        return ground == Blocks.farmland || ground == Blocks.end_stone;
    }

    //canGrow
    @Override
    public boolean func_149851_a(World worldIn, int x, int y, int z, boolean isClient)
    {
        return worldIn.getBlockMetadata(x, y, z) < 7 && worldIn.getBlockLightValue(x, y + 1, z) <= 7;
    }

    @Override
    public boolean canBlockStay(World worldIn, int x, int y, int z)
    {
        return worldIn.getBlock(x, y - 1, z).canSustainPlant(worldIn, x, y - 1, z, ForgeDirection.UP, this);
    }

    @Override
    public void updateTick(World worldIn, int x, int y, int z, Random rand)
    {
        if (worldIn.getBlockLightValue(x, y + 1, z) <= 7)
        {
            int age = worldIn.getBlockMetadata(x, y, z);
            if (age < 7)
            {
                float chanceDivider = worldIn.getBlock(x, y - 1, z).isFertile(worldIn, x, y - 1, z) ? 2 : 1;

                if (rand.nextInt((int)(25.0F / chanceDivider) + 1) == 0)
                {
                    age++;
                    worldIn.setBlockMetadataWithNotify(x, y, z, age, 2);
                }
            }
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();

        Random rand = ((World)world).rand;

        int pearls = 0;
        int seeds = 1;

        //not fully grown
        if (metadata < 7)
        {
            //90% chance to get a seed back
            if (rand.nextInt(10) < 9) seeds++;
        }
        //fully grown
        else
        {
            //10% chance to get an extra seed
            if (rand.nextInt(10) == 9)
            {
                seeds++;
            }

            //10% chance to get a second pearl
            if (rand.nextInt(10) > 0)
            {
                pearls = 1;
            }
            else
            {
                pearls = 2;
            }
        }

        drops.add(new ItemStack(this.func_149866_i(), seeds, 0));
        if (pearls > 0) drops.add(new ItemStack(Items.ender_pearl, pearls, 0));
        return drops;
    }
}
