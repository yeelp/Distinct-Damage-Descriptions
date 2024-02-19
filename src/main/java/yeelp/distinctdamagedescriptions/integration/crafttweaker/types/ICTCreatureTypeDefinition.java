package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import java.util.List;
import java.util.stream.Collectors;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.api.potions.IPotion;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.CTCreatureTypeDefinition;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

@ZenClass("mods.ddd.CreatureTypeDefinition")
@ZenRegister
public interface ICTCreatureTypeDefinition {
	
	@ZenMethod("setCritImmunity")
	ICTCreatureTypeDefinition setCritImmunity(boolean status);
	
	@ZenMethod("setPotionImmunity")
	ICTCreatureTypeDefinition setPotionImmunity(IPotion potion, boolean status);
	
	@ZenGetter("name")
	String getName();
	
	@ZenMethod("addEntityToType")
	ICTCreatureTypeDefinition addEntityToType(IEntityDefinition def);
	
	@ZenMethod("removeEntityFromType")
	ICTCreatureTypeDefinition removeEntityFromType(IEntityDefinition def);
	
	@ZenMethod
	static List<ICTCreatureTypeDefinition> getAllTypes() {
		return DDDRegistries.creatureTypes.getAll().stream().map(CTCreatureTypeDefinition::getFromCreatureType).collect(Collectors.toList());
	}
}
