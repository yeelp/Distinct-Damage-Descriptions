package yeelp.distinctdamagedescriptions.api.impl;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsAccessor;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsMutator;
import yeelp.distinctdamagedescriptions.util.DamageCategoriesProvider;
import yeelp.distinctdamagedescriptions.util.IDamageCategories;
import yeelp.distinctdamagedescriptions.util.ResistancesAttributes;

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
	@Nullable
	public IDamageCategories getDamageCategories(ItemStack stack)
	{
		return DamageCategoriesProvider.getDamageCategories(stack);
	}

	@Override
	public double getSlashingResistance(EntityLivingBase entity)
	{
		return entity.getAttributeMap().getAttributeInstance(ResistancesAttributes.SLASHING).getAttributeValue();
	}
	
	@Override
	public double getPiercingResistance(EntityLivingBase entity)
	{
		return entity.getAttributeMap().getAttributeInstance(ResistancesAttributes.PIERCING).getAttributeValue();
	}

	@Override
	public double getBludgeoningResistance(EntityLivingBase entity)
	{
		return entity.getAttributeMap().getAttributeInstance(ResistancesAttributes.BLUDGEONING).getAttributeValue();
	}
}
