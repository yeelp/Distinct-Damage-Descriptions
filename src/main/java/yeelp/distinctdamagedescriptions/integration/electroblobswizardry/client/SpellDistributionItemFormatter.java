package yeelp.distinctdamagedescriptions.integration.electroblobswizardry.client;

import electroblob.wizardry.Wizardry;
import electroblob.wizardry.item.ISpellCastingItem;
import electroblob.wizardry.spell.Spell;
import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.WizardryConfigurations;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.*;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.IconAggregator;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.iconaggregation.ItemDamageDistributionIconAggregator;

import java.util.Objects;
import java.util.Optional;

public final class SpellDistributionItemFormatter extends ItemDistributionFormatter implements IModCompatTooltipFormatter<ItemStack> {
	
	private static SpellDistributionItemFormatter instance;
	
	private SpellDistributionItemFormatter() {
		super(KeyTooltip.CTRL, DDDDamageFormatter.COLOURED, SpellDistributionItemFormatter::getDist, "ebwizardry.spelldistribution");
	}
	
	static Optional<IDamageDistribution> getDist(ItemStack stack) {
		//noinspection DataFlowIssue
		return Optional.ofNullable(WizardryConfigurations.spellTypeDist.get(WizardryConfigurations.spellTypeDamage.get(((ISpellCastingItem) stack.getItem()).getCurrentSpell(stack).getRegistryName().toString())));
	}

	@Override
	public boolean applicable(ItemStack t) {
		if(!ModConfig.compat.ebwizardry.showSpellDistributions) {
			return false;
		}
		Spell spell;
		if(t.getItem() instanceof ISpellCastingItem && Wizardry.proxy.shouldDisplayDiscovered(spell = ((ISpellCastingItem) t.getItem()).getCurrentSpell(t), t)) {
			String regName = Objects.requireNonNull(spell.getRegistryName()).toString();
			return WizardryConfigurations.spellTypeDamage.configured(regName);
		}
		return false;
	}

	@Override
	public IconAggregator getIconAggregator() {
		return SpellDistributionIconAggregator.getInstance();
	}
	
	public static SpellDistributionItemFormatter getInstance() {
		return instance == null ? instance = new SpellDistributionItemFormatter() : instance;
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

	private static final class SpellDistributionIconAggregator extends ItemDamageDistributionIconAggregator {

		private static SpellDistributionIconAggregator instance;
		
		private SpellDistributionIconAggregator() {
			super(SpellDistributionItemFormatter.getInstance(), SpellDistributionItemFormatter::getDist);
		}
		
		public static SpellDistributionIconAggregator getInstance() {
			return instance == null ? instance = new SpellDistributionIconAggregator() : instance;
		}
		
	}

}
