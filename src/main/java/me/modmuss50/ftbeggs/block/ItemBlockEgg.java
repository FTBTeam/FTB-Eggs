package me.modmuss50.ftbeggs.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import reborncore.common.itemblock.ItemBlockBase;

public class ItemBlockEgg extends ItemBlockBase {
	public ItemBlockEgg(Block block) {
		super(block, block, new String[] {});
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return block.getUnlocalizedName() + "." + stack.getItemDamage();
	}

}
