package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import java.util.function.Predicate;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import net.minecraft.entity.EntityLivingBase;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;
import yeelp.distinctdamagedescriptions.api.IDDDCapModifier;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.CTConsts;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.modifiers.CTEntityLivingBaseModifier;

@ZenClass(CTConsts.CTClasses.COTMOBDAMAGEMODIFIERBUILDER)
@ZenRegister
public final class CoTMobDamageModifierBuilder extends CoTDDDModifierBuilder<IEntityLivingBase, EntityLivingBase> {

	@ZenProperty
	public IsModifierApplicableForEntityLivingBase isModifierApplicable;
	
	private CoTMobDamageModifierBuilder(String name) {
		super(ModifierType.ENTITY_LIVING_BASE_DAMAGE, name);
	}

	@Override
	protected IDDDCapModifier<EntityLivingBase> makeModifierSpecific() {
		return new CTEntityLivingBaseModifier(this.name, this.shouldReallocate, this.isModifierApplicable, this.priority);
	}

	@Override
	protected CoTDDDModifierBuilder<IEntityLivingBase, EntityLivingBase> copySpecific() {
		CoTMobDamageModifierBuilder builder = new CoTMobDamageModifierBuilder(this.name);
		builder.isModifierApplicable = this.isModifierApplicable;
		return builder;
	}

	@Override
	protected Predicate<IEntityLivingBase> getPredicate() {
		return this.isModifierApplicable;
	}
	
	@ZenMethod
	public static CoTMobDamageModifierBuilder create(String name) {
		return new CoTMobDamageModifierBuilder(name);
	}

}
