package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.api.potions.IPotion;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

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
}
