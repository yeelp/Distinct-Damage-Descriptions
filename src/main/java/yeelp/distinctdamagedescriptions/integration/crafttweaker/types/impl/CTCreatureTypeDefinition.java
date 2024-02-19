package yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.potions.IPotion;
import yeelp.distinctdamagedescriptions.api.IHasCreationSource.Source;
import yeelp.distinctdamagedescriptions.api.impl.CreatureType;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTCreatureTypeDefinition;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

public class CTCreatureTypeDefinition implements ICTCreatureTypeDefinition {

	private final CreatureType data;
	
	private static final Map<CreatureType, ICTCreatureTypeDefinition> POOL = Maps.newHashMap();
	
	public static ICTCreatureTypeDefinition getFromString(String type) {
		CreatureType data = DDDRegistries.creatureTypes.get(type);
		if(data == null || data == CreatureType.UNKNOWN) {
			data = new CreatureType(type, Sets.newHashSet(), false, Source.CT);
			DDDRegistries.creatureTypes.register(data);
		}
		return getFromCreatureType(data);
	}
	
	public static ICTCreatureTypeDefinition getFromCreatureType(CreatureType data) {
		return POOL.computeIfAbsent(data, CTCreatureTypeDefinition::new);
	}
	
	public CTCreatureTypeDefinition(CreatureType data) {
		this.data = data;
	}
	
	@Override
	public ICTCreatureTypeDefinition setCritImmunity(boolean status) {
		this.data.criticalImmunity = status;
		return this;
	}

	@Override
	public ICTCreatureTypeDefinition setPotionImmunity(IPotion potion, boolean status) {
		if(status) {
			this.data.addPotionImmunity(CraftTweakerMC.getPotion(potion));
		}
		else {
			this.data.removePotionImmunity(CraftTweakerMC.getPotion(potion));
		}
		return this;
	}

	@Override
	public String getName() {
		return this.data.getTypeName();
	}

	@Override
	public ICTCreatureTypeDefinition addEntityToType(IEntityDefinition def) {
		DDDRegistries.creatureTypes.addTypeToEntity(def.getId(), this.data);
		return this;
	}

	@Override
	public ICTCreatureTypeDefinition removeEntityFromType(IEntityDefinition def) {
		DDDRegistries.creatureTypes.removeTypeFromEntity(def.getId(), this.data);
		return this;
	}
	
	
	
}
