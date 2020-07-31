package yeelp.distinctdamagedescriptions.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CreatureTypeProvider
{
	@CapabilityInject(ICreatureType.class)
	public static Capability<ICreatureType> creatureType = null;
	
	private ICreatureType instance = creatureType.getDefaultInstance();
	
	public static ICreatureType getCreatureType(EntityLivingBase entity)
	{
		return entity.getCapability(creatureType, null);
	}
}
