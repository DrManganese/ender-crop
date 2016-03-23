package io.github.mathiasdj.endercrop.block;

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

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.mathiasdj.endercrop.init.ModBlocks;
import io.github.mathiasdj.endercrop.init.ModItems;
import io.github.mathiasdj.endercrop.reference.Names;
import io.github.mathiasdj.endercrop.reference.Reference;

public class BlockCropEnder extends BlockCrops
{
    @SideOnly(Side.CLIENT)
    private IIcon[] field_149867_a;

    public BlockCropEnder()
    {
        super();
        this.setBlockName(Names.Blocks.ENDER_CROP);
    }

    private boolean isOnEndstone(World worldIn, int x, int y, int z) {
        return worldIn.getBlock(x, y - 1, z) == ModBlocks.blockTilledEndStone;
    }

    @Override
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        return player.getHeldItem() == null || player.getHeldItem().getItem().equals(Items.dye);
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
        return ground == Blocks.farmland || ground == ModBlocks.blockTilledEndStone;
    }

    //canGrow
    @Override
    public boolean func_149851_a(World worldIn, int x, int y, int z, boolean isClient)
    {
        return worldIn.getBlockMetadata(x, y, z) < 7 && (isOnEndstone(worldIn, x, y, z) || worldIn.getBlockLightValue(x, y + 1, z) <= 7);
    }

    @Override
    public boolean canBlockStay(World worldIn, int x, int y, int z)
    {
        return worldIn.getBlock(x, y - 1, z).canSustainPlant(worldIn, x, y - 1, z, ForgeDirection.UP, this);
    }

    private float func_149864_n(World p_149864_1_, int p_149864_2_, int p_149864_3_, int p_149864_4_)
    {
        float f = 1.0F;
        Block block = p_149864_1_.getBlock(p_149864_2_, p_149864_3_, p_149864_4_ - 1);
        Block block1 = p_149864_1_.getBlock(p_149864_2_, p_149864_3_, p_149864_4_ + 1);
        Block block2 = p_149864_1_.getBlock(p_149864_2_ - 1, p_149864_3_, p_149864_4_);
        Block block3 = p_149864_1_.getBlock(p_149864_2_ + 1, p_149864_3_, p_149864_4_);
        Block block4 = p_149864_1_.getBlock(p_149864_2_ - 1, p_149864_3_, p_149864_4_ - 1);
        Block block5 = p_149864_1_.getBlock(p_149864_2_ + 1, p_149864_3_, p_149864_4_ - 1);
        Block block6 = p_149864_1_.getBlock(p_149864_2_ + 1, p_149864_3_, p_149864_4_ + 1);
        Block block7 = p_149864_1_.getBlock(p_149864_2_ - 1, p_149864_3_, p_149864_4_ + 1);
        boolean flag = block2 == this || block3 == this;
        boolean flag1 = block == this || block1 == this;
        boolean flag2 = block4 == this || block5 == this || block6 == this || block7 == this;

        for (int l = p_149864_2_ - 1; l <= p_149864_2_ + 1; ++l)
        {
            for (int i1 = p_149864_4_ - 1; i1 <= p_149864_4_ + 1; ++i1)
            {
                float f1 = 0.0F;

                if (p_149864_1_.getBlock(l, p_149864_3_ - 1, i1).canSustainPlant(p_149864_1_, l, p_149864_3_ - 1, i1, ForgeDirection.UP, this))
                {
                    f1 = 1.0F;

                    if (p_149864_1_.getBlock(l, p_149864_3_ - 1, i1).isFertile(p_149864_1_, l, p_149864_3_ - 1, i1))
                    {
                        f1 = 3.0F;
                    }
                }

                if (l != p_149864_2_ || i1 != p_149864_4_)
                {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        if (flag2 || flag && flag1)
        {
            f /= 2.0F;
        }

        return f;
    }

    @Override
    public void updateTick(World worldIn, int x, int y, int z, Random rand)
    {
        float baseChance = (isOnEndstone(worldIn, x, y, z)) ? 25.0F : 50.0F;

        if (worldIn.getBlockLightValue(x, y+1, z) <= 7 || isOnEndstone(worldIn, x, y, z)) {
            if (worldIn.getBlockMetadata(x, y, z) < 7) {
                if (rand.nextInt((int) (baseChance / func_149864_n(worldIn, x, y, z)) + 1) == 0) {
                    worldIn.setBlockMetadataWithNotify(x, y, z, worldIn.getBlockMetadata(x, y, z) + 1, 2);
                }
            }
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();

        Random rand = world.rand;

        int pearls = 0;
        int seeds = 1;

        if (metadata == 7) {
            //10% chance to get an extra seed
            if (rand.nextInt(10) == 9) {
                seeds++;
            }

            //10% chance to get a second pearl
            if (rand.nextInt(10) == 0)
                pearls = 1;
            else
                pearls = 2;
        }

        drops.add(new ItemStack(this.func_149866_i(), seeds, 0));
        drops.add(new ItemStack(Items.ender_pearl, pearls, 0));
        return drops;
    }
}
