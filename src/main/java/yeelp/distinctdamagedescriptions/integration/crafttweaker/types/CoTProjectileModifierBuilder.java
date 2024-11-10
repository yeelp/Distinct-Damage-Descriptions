package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import java.util.function.Predicate;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import net.minecraft.entity.Entity;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;
import yeelp.distinctdamagedescriptions.api.IDDDCapModifier;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.CTConsts;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.modifiers.CTEntityModifier;

@ZenClass(CTConsts.CTClasses.COTPROJDAMAGEMODIFIERBUILDER)
@ZenRegister
public final class CoTProjectileModifierBuilder extends CoTDDDModifierBuilder<IEntity, Entity> {

	private CoTProjectileModifierBuilder(String name) {
		super(ModifierType.ENTITY, name);
	}

	@ZenProperty
	public IsModifierApplicableForEntity isModifierApplicable;
	
	@Override
	protected IDDDCapModifier<Entity> makeModifierSpecific() {
		return new CTEntityModifier(this.name, this.shouldReallocate, this.isModifierApplicable, this.priority);
	}

	@Override
	protected CoTDDDModifierBuilder<IEntity, Entity> copySpecific() {
		CoTProjectileModifierBuilder builder = new CoTProjectileModifierBuilder(this.name);
		builder.isModifierApplicable = this.isModifierApplicable;
		return builder;
	}

	@Override
	protected Predicate<IEntity> getPredicate() {
		return this.isModifierApplicable;
	}
	
	@ZenMethod
	public static CoTProjectileModifierBuilder create(String name) {
		return new CoTProjectileModifierBuilder(name);
	}

}
