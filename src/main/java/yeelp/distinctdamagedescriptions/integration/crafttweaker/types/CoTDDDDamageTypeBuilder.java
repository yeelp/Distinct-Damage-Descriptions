package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.ZenRuntimeException;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDDamageType.Type;
import yeelp.distinctdamagedescriptions.api.IHasCreationSource.Source;
import yeelp.distinctdamagedescriptions.api.impl.DDDCustomDamageType;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

@ZenClass("mods.ddd.DamageTypeBuilder")
@ZenRegister
public final class CoTDDDDamageTypeBuilder {

	private static final Collection<CoTDDDDamageTypeBuilder> BUILDERS = new ArrayList<CoTDDDDamageTypeBuilder>();
	private static final Set<String> USED_NAMES = new HashSet<String>();
	
	private boolean wasBuilt = false;
	
	@ZenProperty
	public String name;
	
	@ZenProperty
	public String displayName;
	
	@ZenProperty
	public String deathMessageNoAttacker;
	
	@ZenProperty
	public String deathMessageHasAttacker;
	
	@ZenProperty
	public int color = 0xFFFFFF;
	
	private Type type = Type.SPECIAL;
	
	@ZenMethod
	public void setPhysical() {
		this.type = Type.PHYSICAL;
	}
	
	@ZenMethod
	public void setSpecial() {
		this.type = Type.SPECIAL;
	}
	
	@ZenMethod
	public static CoTDDDDamageTypeBuilder create(String name) {
		return new CoTDDDDamageTypeBuilder(name);
	}
	
	public CoTDDDDamageTypeBuilder(String name) {
		this.name = name;
	}
	
	@ZenMethod
	public void register() {
		if(this.wasBuilt) {
			return;
		}
		if(USED_NAMES.contains(this.name)) {
			throw new ZenRuntimeException(String.format("A Custom Damage Type with name %s was created in another script!", this.name));
		}
		if(this.name == null) {
			throw new ZenRuntimeException("internal damage type name can not be null!");
		}
		if(this.displayName == null) {
			CraftTweakerAPI.logWarning(String.format("%s doesn't have a display name set! Will use internal name as display name, but it is recommended to set a display name with ZenProperty displayName!", this.name));
			this.displayName = this.name;
		}
		BUILDERS.add(this);
		USED_NAMES.add(this.name);
	}
	
	private DDDDamageType createDamageType() {
		return new DDDCustomDamageType(this.name, this.displayName, this.type == Type.PHYSICAL, this.deathMessageHasAttacker, this.deathMessageNoAttacker, this.color, Source.CoT);
	}
	
	public static void registerTypes() {
		BUILDERS.stream().map(CoTDDDDamageTypeBuilder::createDamageType).forEach((t) -> {
			if(DDDRegistries.damageTypes.isRegistered(t)) {
				throw new ZenRuntimeException(String.format("%s was already registered! Original registration source: %s!", t.getTypeName(), DDDRegistries.damageTypes.get(t.getTypeName()).getCreationSourceString()));
			}
			DDDRegistries.damageTypes.register(t);
		});
	}
}
