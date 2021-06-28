package yeelp.distinctdamagedescriptions.integration.crafttweaker.capabilities;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenGetter;

@ZenExpansion("crafttweaker.entity.IEntityLivingBase")
@ZenRegister
public class CTIEntityLivingBaseExt {
	@ZenGetter("creatureType")
	public static CTCreatureType getCreatureType(IEntityLivingBase entity) {
		return new CTCreatureType(entity);
	}

	@ZenGetter("resistances")
	public static CTResistances getResistances(IEntityLivingBase entity) {
		return new CTResistances(entity);
	}
}
