package yeelp.distinctdamagedescriptions.integration.crafttweaker.capabilities;

import java.util.Iterator;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import stanhebben.zenscript.annotations.IterableSimple;
import stanhebben.zenscript.annotations.OperatorType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenOperator;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.IMobCreatureType;
import yeelp.distinctdamagedescriptions.capability.impl.MobCreatureType;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.CTConsts;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTCreatureTypeDefinition;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.CTCreatureTypeDefinition;

@ZenClass(CTConsts.CTClasses.CTCREATURETYPE)
@IterableSimple(CTConsts.CTClasses.CTCREATURETYPEDEF)
@ZenRegister
public class CTCreatureType implements Iterable<ICTCreatureTypeDefinition> {
	private final IEntityLivingBase entityLiving;
	private final IMobCreatureType type;

	public CTCreatureType(IEntityLivingBase entityLiving) {
		this.entityLiving = entityLiving;
		this.type = DDDAPI.accessor.getMobCreatureType(CraftTweakerMC.getEntityLivingBase(this.entityLiving)).orElse(MobCreatureType.UNKNOWN);
	}
	
	@ZenOperator(OperatorType.CONTAINS)
	@ZenMethod
	public boolean contains(ICTCreatureTypeDefinition def) {
		return this.type.isCreatureType(def.getName());
	}

	@Override
	public Iterator<ICTCreatureTypeDefinition> iterator() {
		return this.type.getCreatureTypeNames().stream().map(CTCreatureTypeDefinition::getFromString).iterator();
	}
}
