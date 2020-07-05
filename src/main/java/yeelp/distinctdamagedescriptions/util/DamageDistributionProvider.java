package yeelp.distinctdamagedescriptions.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class DamageDistributionProvider
{
	@CapabilityInject(IDamageDistribution.class)
	public static Capability<IDamageDistribution> damageDist = null;
	
	private IDamageDistribution instance = damageDist.getDefaultInstance();
	
	public static IDamageDistribution getDamageDistribution(ItemStack stack)
	{
		return stack.getCapability(damageDist, null);
	}
	
	public static IDamageDistribution getDamageDistribution(EntityLivingBase entity)
	{
		return entity.getCapability(damageDist, null);
	}
}
