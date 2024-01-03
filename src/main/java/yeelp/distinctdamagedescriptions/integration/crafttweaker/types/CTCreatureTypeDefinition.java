package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.potions.IPotion;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.CreatureTypeData;

public class CTCreatureTypeDefinition implements ICTCreatureTypeDefinition {

	private final CreatureTypeData data;
	
	private static final Map<CreatureTypeData, ICTCreatureTypeDefinition> POOL = Maps.newHashMap();
	
	public static ICTCreatureTypeDefinition getFromString(String type) {
		CreatureTypeData data = DDDRegistries.creatureTypes.get(type);
		if(data == null || data == CreatureTypeData.UNKNOWN) {
			data = new CreatureTypeData(type, Sets.newHashSet(), false);
			DDDRegistries.creatureTypes.register(data);
		}
		return getFromCreatureTypeData(data);
	}
	
	public static ICTCreatureTypeDefinition getFromCreatureTypeData(CreatureTypeData data) {
		return POOL.computeIfAbsent(data, CTCreatureTypeDefinition::new);
	}
	
	public CTCreatureTypeDefinition(CreatureTypeData data) {
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
	
}
