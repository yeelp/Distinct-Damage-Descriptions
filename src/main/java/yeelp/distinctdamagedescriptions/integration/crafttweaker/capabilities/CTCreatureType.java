package yeelp.distinctdamagedescriptions.integration.crafttweaker.capabilities;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.util.ICreatureType;

@ZenClass("mods.ddd.CreatureType")
@ZenRegister
public class CTCreatureType
{
	private final IEntityLivingBase entityLiving;
	private final ICreatureType type;
	
	public CTCreatureType(IEntityLivingBase entityLiving)
	{
		this.entityLiving = entityLiving;
		this.type = DDDAPI.accessor.getMobCreatureType(CraftTweakerMC.getEntityLivingBase(this.entityLiving));
	}
	
	@ZenGetter("mainType")
	public String getMainType()
	{
		return this.type.getMainCreatureTypeData().getTypeName();
	}

	@ZenGetter("subType")
	public String getSubType()
	{
		return this.type.getSubCreatureTypeData().getTypeName();
	}

	@ZenMethod
	public float getModifierForDamageType(String damageType)
	{
		return this.type.getModifierForDamageType(damageType);
	}
}
