package yeelp.distinctdamagedescriptions.integration.crafttweaker.capabilities;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import stanhebben.zenscript.annotations.*;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.IMobCreatureType;
import yeelp.distinctdamagedescriptions.capability.impl.MobCreatureType;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.CTConsts;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTCreatureTypeDefinition;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.CTCreatureTypeDefinition;

import javax.annotation.Nonnull;
import java.util.Iterator;

@ZenClass(CTConsts.CTClasses.CTCREATURETYPE)
@IterableSimple(CTConsts.CTClasses.CTCREATURETYPEDEF)
@ZenRegister
public class CTCreatureType implements Iterable<ICTCreatureTypeDefinition> {
	private final IMobCreatureType type;

	public CTCreatureType(IEntityLivingBase entityLiving) {
		this.type = DDDAPI.accessor.getMobCreatureType(CraftTweakerMC.getEntityLivingBase(entityLiving)).orElse(MobCreatureType.UNKNOWN);
	}
	
	@ZenOperator(OperatorType.CONTAINS)
	@ZenMethod
	public boolean contains(ICTCreatureTypeDefinition def) {
		return this.type.isCreatureType(def.getName());
	}

	@Nonnull
	@Override
	public Iterator<ICTCreatureTypeDefinition> iterator() {
		return this.type.getCreatureTypeNames().stream().map(CTCreatureTypeDefinition::getFromString).iterator();
	}
}
