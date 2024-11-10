package yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.modifiers;

import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.EntityLivingBase;
import yeelp.distinctdamagedescriptions.api.impl.AbstractDDDResistanceModifier;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.CoTDDDModifierBuilder.IsModifierApplicableForEntityLivingBase;

public final class CTEntityLivingBaseResistanceModifier extends AbstractDDDResistanceModifier {

	private final IsModifierApplicableForEntityLivingBase isModifierApplicable;
	private final String name;
	
	public CTEntityLivingBaseResistanceModifier(String name, boolean shouldReallocate, float adaptabilityMod, IsModifierApplicableForEntityLivingBase isModifierApplicable) {
		super(() -> 0.0f, Source.CoT, shouldReallocate, adaptabilityMod);
		this.isModifierApplicable = isModifierApplicable;
		this.name = name;
	}
	
	public CTEntityLivingBaseResistanceModifier(String name, boolean shouldReallocate, float adaptabilityMod, IsModifierApplicableForEntityLivingBase isModifierApplicable, boolean adaptabilityToggle) {
		super(() -> 0.0f, Source.CoT, shouldReallocate, adaptabilityMod, adaptabilityToggle);
		this.isModifierApplicable = isModifierApplicable;
		this.name = name;
	}

	@Override
	public boolean applicable(EntityLivingBase t) {
		return this.isModifierApplicable.handle(CraftTweakerMC.getIEntityLivingBase(t));
	}

	@Override
	public String getName() {
		return this.name;
	}

}
