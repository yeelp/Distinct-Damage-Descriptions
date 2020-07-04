package yeelp.distinctdamagedescriptions.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class MobResistancesProvider
{
	@CapabilityInject(IMobResistances.class)
	public static Capability<IMobResistances> mobResist = null;
	
	private IMobResistances instance = mobResist.getDefaultInstance();
	
	public static IMobResistances getMobResistances(EntityLivingBase entity)
	{
		return entity.getCapability(mobResist, null);
	}
}
