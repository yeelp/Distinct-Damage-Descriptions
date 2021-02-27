package yeelp.distinctdamagedescriptions.capability.providers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;

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
	
	public static IDamageDistribution getDamageDistribution(IProjectile projectile)
	{
		if(projectile instanceof Entity)
		{
			return ((Entity) projectile).getCapability(damageDist, null);
		}
		else
		{
			return null;
		}
	}
}
