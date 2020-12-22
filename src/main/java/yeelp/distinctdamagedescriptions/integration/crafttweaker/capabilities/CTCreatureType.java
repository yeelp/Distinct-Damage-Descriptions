package yeelp.distinctdamagedescriptions.integration.crafttweaker.capabilities;

import java.util.LinkedList;
import java.util.List;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
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
	
	@ZenGetter("types")
	public List<String> getType()
	{
		return new LinkedList<String>(this.type.getCreatureTypeNames());
	}
}
