package zmaster587.libVulpes.block;

import java.util.Random;

import zmaster587.libVulpes.LibVulpes;
import zmaster587.libVulpes.inventory.GuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RotatableMachineBlock extends RotatableBlock {
	protected final Random random = new Random();
	
	static  PropertyBool STATE = PropertyBool.create("state");

	public RotatableMachineBlock(Material par2Material) {
		super(par2Material);
		this.isBlockContainer = true;
	}
	
	public int getMetaFromState(IBlockState state) {
		int flag = state.getValue(STATE) ? 8 : 0;
		return state.getValue(FACING).getIndex() | flag;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		boolean state = meta >= 8;
		if(meta > 1)
			return this.getDefaultState().withProperty(FACING, EnumFacing.values()[meta & 7]).withProperty(STATE, state);
		else
			return this.getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(STATE, state);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[]{FACING, STATE});
	}

    /**
     * Called on server worlds only when the block has been replaced by a different block ID, or the same block with a
     * different metadata value, but before the new metadata value is set. Args: World, x, y, z, old block ID, old
     * metadata
     */
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		IInventory tileentitychest = (IInventory)world.getTileEntity(pos);

		if (tileentitychest != null)
		{
			for (int j1 = 0; j1 < tileentitychest.getSizeInventory(); ++j1)
			{
				ItemStack itemstack = tileentitychest.getStackInSlot(j1);

				if (itemstack != null)
				{
					float f = this.random.nextFloat() * 0.8F + 0.1F;
					float f1 = this.random.nextFloat() * 0.8F + 0.1F;
					EntityItem entityitem;

					for (float f2 = this.random.nextFloat() * 0.8F + 0.1F; itemstack.getCount() > 0; world.spawnEntity(entityitem))
					{
						int k1 = this.random.nextInt(21) + 10;

						if (k1 > itemstack.getCount())
						{
							k1 = itemstack.getCount();
						}

						itemstack.setCount(itemstack.getCount() - k1);

						entityitem = new EntityItem(world, (double)((float)pos.getX() + f), (double)((float)pos.getY() + f1), (double)((float)pos.getZ() + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
						float f3 = 0.05F;
						entityitem.motionX = (double)((float)this.random.nextGaussian() * f3);
						entityitem.motionY = (double)((float)this.random.nextGaussian() * f3 + 0.2F);
						entityitem.motionZ = (double)((float)this.random.nextGaussian() * f3);

						if (itemstack.hasTagCompound())
						{
							entityitem.getItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
						}
					}
				}
			}
		}

		super.breakBlock(world,pos, state);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos,
			IBlockState state, EntityPlayer playerIn, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		//Handlue gui through modular system
		if(!worldIn.isRemote )
			playerIn.openGui(LibVulpes.instance, GuiHandler.guiId.MODULAR.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}
	
	/**
	 * Called when the block receives a BlockEvent - see World.addBlockEvent. By default, passes it on to the tile
	 * entity at this location. Args: world, x, y, z, blockID, EventID, event parameter
	 */
	/*public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6)
	{
		super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
		TileEntity tileentity = par1World.getTileEntity(par2, par3, par4);
		return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
	}*/
}
