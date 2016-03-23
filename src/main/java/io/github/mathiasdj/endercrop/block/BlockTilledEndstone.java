package io.github.mathiasdj.endercrop.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.mathiasdj.endercrop.reference.Names;
import io.github.mathiasdj.endercrop.reference.Reference;

public class BlockTilledEndStone extends BlockFarmland {

    @SideOnly(Side.CLIENT)
    private IIcon field_149824_a;
    @SideOnly(Side.CLIENT)
    private IIcon field_149823_b;

    public BlockTilledEndStone() {
        super();
        this.setBlockName(Names.Blocks.TILLED_END_STONE);
        this.setLightOpacity(255);
        this.useNeighborBrightness = true;
        this.setHardness(0.6F);
        this.setHarvestLevel("pickaxe", 1);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 1 ? (p_149691_2_ > 0 ? this.field_149824_a : this.field_149823_b) : Blocks.end_stone.getBlockTextureFromSide(p_149691_1_);
    }

    @Override
    public String getUnlocalizedName()
    {
        return String.format("tile.%s%s", Reference.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.field_149824_a = p_149651_1_.registerIcon(String.format("%s", getUnwrappedUnlocalizedName(this.getUnlocalizedName())) + "_wet");
        this.field_149823_b = p_149651_1_.registerIcon(String.format("%s", getUnwrappedUnlocalizedName(this.getUnlocalizedName())) + "_dry");
    }

    @Override
    public void updateTick(World worldIn, int x, int y, int z, Random rand)
    {
        if (!this.hasWater(worldIn, x, y, z) && !worldIn.canLightningStrikeAt(x, y + 1, z))
        {
            int l = worldIn.getBlockMetadata(x, y, z);

            if (l > 0)
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, l - 1, 2);
            }
            else if (!this.hasCrops(worldIn, x, y, z))
            {
                worldIn.setBlock(x, y, z, Blocks.end_stone);
            }
        }
        else
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, 7, 2);
        }
    }

    private boolean hasCrops(World worldIn, int x, int y, int z)
    {
        byte b0 = 0;

        for (int l = x - b0; l <= x + b0; ++l)
        {
            for (int i1 = z - b0; i1 <= z + b0; ++i1)
            {
                Block block = worldIn.getBlock(l, y + 1, i1);

                if (block instanceof IPlantable && canSustainPlant(worldIn, x, y, z, ForgeDirection.UP, (IPlantable)block))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean hasWater(World worldIn, int x, int y, int z)
    {
        for (int l = x - 4; l <= x + 4; ++l)
        {
            for (int i1 = y; i1 <= y + 1; ++i1)
            {
                for (int j1 = z - 4; j1 <= z + 4; ++j1)
                {
                    if (worldIn.getBlock(l, i1, j1).getMaterial() == Material.water)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean canSustainPlant(IBlockAccess worldIn, int x, int y, int z, ForgeDirection direction, IPlantable plantable)
    {
        Block plant = plantable.getPlant(worldIn, x, y+1, z);
        return plant instanceof BlockCropEnder;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block block)
    {
        super.onNeighborBlockChange(worldIn, x, y, z, block);
        Material material = worldIn.getBlock(x, y + 1, z).getMaterial();

        if (material.isSolid())
        {
            worldIn.setBlock(x, y, z, Blocks.end_stone);
        }
    }

    @Override
    public void onFallenUpon(World worldIn, int x, int y, int z, Entity entity, float fallDistance)
    {
        if (!worldIn.isRemote && worldIn.rand.nextFloat() < fallDistance - 0.5F)
        {
            if (!(entity instanceof EntityPlayer) && !worldIn.getGameRules().getGameRuleBooleanValue("mobGriefing"))
            {
                return;
            }

            worldIn.setBlock(x, y, z, Blocks.end_stone);
        }
    }

    @Override
    public boolean isFertile(World world, int x, int y, int z) {
        return (world.getBlockMetadata(x, y, z)) > 0;
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random rand, int fortune) {
        return Blocks.end_stone.getItemDropped(p_149650_1_, rand, fortune);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, int x, int y, int z) {
        return Item.getItemFromBlock(Blocks.end_stone);
    }
}
