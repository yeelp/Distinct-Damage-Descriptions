package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import yeelp.distinctdamagedescriptions.event.DamageDescriptionEvent;

public abstract class CTDDDEvent implements IDDDEvent
{
	private final DamageDescriptionEvent internal;
	
	CTDDDEvent(DamageDescriptionEvent evt)
	{
		this.internal = evt;
	}
	
	@Override
	public IEntity getEntity()
	{
		return CraftTweakerMC.getIEntity(this.internal.getAttacker());
	}

	@Override
	public IEntityLivingBase getDefender()
	{
		return CraftTweakerMC.getIEntityLivingBase(this.internal.getDefender());
	}

	@Override
	public float getDamage(String type)
	{
		return this.internal.getDamage(type);
	}

	@Override
	public void setDamage(String type, float damage)
	{
		this.internal.setDamage(type, damage);
	}

	@Override
	public float getResistance(String type)
	{
		return this.internal.getResistance(type);
	}

	@Override
	public void setResistance(String type, float resistance)
	{
		this.internal.setResistance(type, resistance);
	}
}
