package yeelp.distinctdamagedescriptions.integration.electroblobswizardry.client;

import java.util.Optional;

import electroblob.wizardry.Wizardry;
import electroblob.wizardry.item.ISpellCastingItem;
import electroblob.wizardry.spell.Spell;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.WizardryConfigurations;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDDamageFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDNumberFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ItemDistributionFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.KeyTooltip;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.ObjectFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.IconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ItemDamageDistributionIconAggregator;

public final class SpellDistributionItemFormatter extends ItemDistributionFormatter implements IModCompatTooltipFormatter<ItemStack> {
	
	private static SpellDistributionItemFormatter instance;
	
	private SpellDistributionItemFormatter() {
		super(KeyTooltip.CTRL, DDDDamageFormatter.COLOURED, SpellDistributionItemFormatter::getDist, "ebwizardry.spelldistribution");
	}
	
	static Optional<IDamageDistribution> getDist(ItemStack stack) {
		return Optional.ofNullable(WizardryConfigurations.spellTypeDist.get(WizardryConfigurations.spellTypeDamage.get(((ISpellCastingItem) stack.getItem()).getCurrentSpell(stack).getRegistryName().toString())));
	}

	@Override
	public boolean applicable(ItemStack t) {
		if(!ModConfig.compat.ebwizardry.showSpellDistributions) {
			return false;
		}
		Spell spell;
		if(t.getItem() instanceof ISpellCastingItem && Wizardry.proxy.shouldDisplayDiscovered(spell = ((ISpellCastingItem) t.getItem()).getCurrentSpell(t), t)) {
			String regName = spell.getRegistryName().toString();
			return WizardryConfigurations.spellTypeDamage.configured(regName);
		}
		return false;
	}

	@Override
	public IconAggregator getIconAggregator() {
		return SpellDistrbutionIconAggregator.getInstance();
	}
	
	public static SpellDistributionItemFormatter getInstance() {
		return instance == null ? new SpellDistributionItemFormatter() : instance;
	}
	
	@Override
	public TooltipOrder getType() {
		//so we appear below the item distribution. Spells are just fancy projectiles half the time anyway.
		return TooltipOrder.PROJECTILE;
	}
	
	@Override
	public ObjectFormatter<Float> getNumberFormattingStrategy() {
		return DDDNumberFormatter.PERCENT;
	}
	
	@Override
	protected boolean shouldShowDist(ItemStack stack) {
		return true;
	}
	
	private static final class SpellDistrbutionIconAggregator extends ItemDamageDistributionIconAggregator {

		private static SpellDistrbutionIconAggregator instance;
		
		private SpellDistrbutionIconAggregator() {
			super(SpellDistributionItemFormatter.getInstance(), SpellDistributionItemFormatter::getDist);
		}
		
		public static SpellDistrbutionIconAggregator getInstance() {
			return instance == null ? new SpellDistrbutionIconAggregator() : instance;
		}
		
	}

}
