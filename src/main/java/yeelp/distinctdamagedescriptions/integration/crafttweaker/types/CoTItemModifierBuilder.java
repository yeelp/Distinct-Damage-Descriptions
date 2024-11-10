package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import java.util.function.Predicate;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;
import yeelp.distinctdamagedescriptions.api.IDDDCapModifier;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.CTConsts;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.modifiers.CTItemModifier;

@ZenClass(CTConsts.CTClasses.COTITEMMODIFIERBUILDER)
@ZenRegister
public final class CoTItemModifierBuilder extends CoTDDDModifierBuilder<IItemStack, ItemStack> {

	@ZenProperty
	public IsModifierApplicableForItemStack isModifierApplicable;
	
	private CoTItemModifierBuilder(ModifierType type, String name) {
		super(type, name);
	}

	@Override
	protected CoTDDDModifierBuilder<IItemStack, ItemStack> copySpecific() {
		CoTItemModifierBuilder builder = new CoTItemModifierBuilder(this.type, this.name);
		builder.isModifierApplicable = this.isModifierApplicable;
		return builder;
	}
	
	@Override
	protected IDDDCapModifier<ItemStack> makeModifierSpecific() {
		return new CTItemModifier(this.name, this.shouldReallocate, this.type.appliesTo(), this.isModifierApplicable, this.priority);
	}

	@Override
	protected Predicate<IItemStack> getPredicate() {
		return this.isModifierApplicable;
	}
	
	@ZenMethod
	public static CoTItemModifierBuilder createItemDamageModifier(String name) {
		return new CoTItemModifierBuilder(ModifierType.ITEM_DAMAGE, name);
	}

	@ZenMethod
	public static CoTItemModifierBuilder createArmorModifier(String name) {
		return new CoTItemModifierBuilder(ModifierType.ITEM_ARMOR, name);
	}
	
	@ZenMethod
	public static CoTItemModifierBuilder createShieldModifier(String name) {
		return new CoTItemModifierBuilder(ModifierType.ITEM_SHIELD, name);
	}
}
