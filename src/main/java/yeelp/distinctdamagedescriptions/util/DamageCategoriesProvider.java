package yeelp.distinctdamagedescriptions.util;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class DamageCategoriesProvider
{
	@CapabilityInject(IDamageCategories.class)
	public static Capability<IDamageCategories> damageCategories = null;
	
	private IDamageCategories instance = damageCategories.getDefaultInstance();
	
	@Nullable
	public static IDamageCategories getDamageCategories(ItemStack stack)
	{
		return stack.getCapability(damageCategories, null);
	}
}
