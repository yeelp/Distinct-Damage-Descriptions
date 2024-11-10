package yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.modifiers;

import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.Entity;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.CoTDDDModifierBuilder.IsModifierApplicableForEntity;

public final class CTEntityModifier extends CTDDDModifier<Entity> {

	private final IsModifierApplicableForEntity isModifierApplicable;

	public CTEntityModifier(String name, boolean shouldReallocate, IsModifierApplicableForEntity isModifierApplicable, int priority) {
		super(name, shouldReallocate, priority);
		this.isModifierApplicable = isModifierApplicable;
	}

	@Override
	public AppliesTo getAppliesToEnum() {
		return AppliesTo.PROJECTILE;
	}

	@Override
	public boolean applicable(Entity t) {
		return this.isModifierApplicable.handle(CraftTweakerMC.getIEntity(t));
	}

}
