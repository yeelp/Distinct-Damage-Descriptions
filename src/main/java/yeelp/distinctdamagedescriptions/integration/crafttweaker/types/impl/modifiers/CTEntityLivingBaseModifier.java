package yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.modifiers;

import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.EntityLivingBase;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.CoTDDDModifierBuilder.IsModifierApplicableForEntityLivingBase;

public final class CTEntityLivingBaseModifier extends CTDDDModifier<EntityLivingBase> {

	private final IsModifierApplicableForEntityLivingBase isModifierApplicable;

	public CTEntityLivingBaseModifier(String name, boolean shouldReallocate, IsModifierApplicableForEntityLivingBase isModifierApplicable, int priority) {
		super(name, shouldReallocate, priority);
		this.isModifierApplicable = isModifierApplicable;
	}

	@Override
	public AppliesTo getAppliesToEnum() {
		return AppliesTo.MOB_DAMAGE;
	}

	@Override
	public boolean applicable(EntityLivingBase t) {
		return this.isModifierApplicable.handle(CraftTweakerMC.getIEntityLivingBase(t));
	}

}
