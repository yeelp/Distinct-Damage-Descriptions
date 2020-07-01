package yeelp.distinctdamagedescriptions.api.impl;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsAccessor;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsMutator;
import yeelp.distinctdamagedescriptions.util.DDDAttributes;

public enum DistinctDamageDescriptionsAPIImpl implements IDistinctDamageDescriptionsAccessor, IDistinctDamageDescriptionsMutator
{
	INSTANCE;
	
	private DistinctDamageDescriptionsAPIImpl()
	{
		DDDAPI.accessor = this;
		DDDAPI.mutator = this;
	}
	
	/************
	 * ACCESSOR *
	 ************/
	@Override
	public double getSlashingDamage(EntityLivingBase entity)
	{
		return entity.getAttributeMap().getAttributeInstance(DDDAttributes.SLASHING_DMG).getAttributeValue();
	}
	
	@Override
	public double getPiercingDamage(EntityLivingBase entity)
	{
		return entity.getAttributeMap().getAttributeInstance(DDDAttributes.PIERCING_DMG).getAttributeValue();
	}
	
	@Override
	public double getBludgeoningDamage(EntityLivingBase entity)
	{
		return entity.getAttributeMap().getAttributeInstance(DDDAttributes.BLUDGEONING_DMG).getAttributeValue();
	}

	@Override
	public double getSlashingResistance(EntityLivingBase entity)
	{
		return entity.getAttributeMap().getAttributeInstance(DDDAttributes.SLASHING).getAttributeValue();
	}
	
	@Override
	public double getPiercingResistance(EntityLivingBase entity)
	{
		return entity.getAttributeMap().getAttributeInstance(DDDAttributes.PIERCING).getAttributeValue();
	}

	@Override
	public double getBludgeoningResistance(EntityLivingBase entity)
	{
		return entity.getAttributeMap().getAttributeInstance(DDDAttributes.BLUDGEONING).getAttributeValue();
	}
}
