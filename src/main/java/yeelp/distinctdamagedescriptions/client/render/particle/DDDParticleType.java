package yeelp.distinctdamagedescriptions.client.render.particle;

import net.minecraft.util.ResourceLocation;
import yeelp.distinctdamagedescriptions.ModConsts;

public enum DDDParticleType
{
	WEAKNESS("weakness_shield"),
	RESISTANCE("resist_shield"),
	IMMUNITY("immunity_shield");
	
	private ResourceLocation loc;
	private DDDParticleType(String particleName)
	{
		this.loc = new ResourceLocation(ModConsts.MODID, "particle/"+particleName);
	}
	public ResourceLocation getResourceLocation()
	{
		return this.loc;
	}
}
