package yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.modifiers;

import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.CoTDDDModifierBuilder.IsModifierApplicableForItemStack;

public final class CTItemModifier extends CTDDDModifier<ItemStack> {

	private final IsModifierApplicableForItemStack isModifierApplicable;
	private final AppliesTo appliesTo;
	public CTItemModifier(String name, boolean shouldReallocate, AppliesTo appliesTo, IsModifierApplicableForItemStack isModifierApplicable, int priority) {
		super(name, shouldReallocate, priority);
		this.isModifierApplicable = isModifierApplicable;
		this.appliesTo = appliesTo;
	}

	@Override
	public AppliesTo getAppliesToEnum() {
		return this.appliesTo;
	}

	@Override
	public boolean applicable(ItemStack t) {
		return this.isModifierApplicable.handle(CraftTweakerMC.getIItemStackForMatching(t));
	}

}
