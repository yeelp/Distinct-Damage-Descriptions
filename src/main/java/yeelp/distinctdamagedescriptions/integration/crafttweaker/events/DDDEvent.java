package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import yeelp.distinctdamagedescriptions.event.DamageDescriptionEvent;

public abstract class DDDEvent implements IDDDEvent
{
	private final DamageDescriptionEvent internal;
	
	DDDEvent(DamageDescriptionEvent evt)
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
	public float getDamage()
	{
		return this.internal.getDamage();
	}

	@Override
	public void setDamage(float damage)
	{
		this.internal.setDamage(damage);
	}

	@Override
	public float getResistance()
	{
		return this.internal.getResistance();
	}

	@Override
	public void setResiatance(float resistance)
	{
		this.internal.setResistance(resistance);
	}
	
}
