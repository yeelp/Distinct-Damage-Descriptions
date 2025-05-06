package yeelp.distinctdamagedescriptions.integration.electroblobswizardry.client;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import electroblob.wizardry.client.gui.handbook.GuiWizardHandbook;
import electroblob.wizardry.util.MagicDamage.DamageType;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDDamageType.Type;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration.ConfigEntry;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.WizardryConfigurations;
import yeelp.distinctdamagedescriptions.util.Translations;
import yeelp.distinctdamagedescriptions.util.Translations.BasicTranslator;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.YLib;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDNumberFormatter;

public final class DDDWizardryBookAddon {

	private static final Set<String> BASE_DAMAGE_TYPES = Stream.of(DamageType.BLAST, DamageType.FIRE, DamageType.FORCE, DamageType.MAGIC, DamageType.FROST, DamageType.POISON, DamageType.RADIANT, DamageType.SHOCK, DamageType.WITHER).map((t) -> t.toString().toLowerCase()).collect(Collectors.toSet());
	private static final Set<ConfigEntry<IDamageDistribution>> ADDED_TYPES = Sets.newHashSet();
	private static final BasicTranslator TRANSLATOR = Translations.INSTANCE.getTranslator(IntegrationIds.WIZARDRY_ID);
	
	private DDDWizardryBookAddon() {
		//no instances
	}
	
	public static void init() {
		GuiWizardHandbook.registerAddonHandbookContent(ModConsts.MODID);
		initDistributionTags();
	}
	
	private static final void initDistributionTags() {
		WizardryConfigurations.spellTypeDist.forEach((entry) -> {
			if(BASE_DAMAGE_TYPES.contains(entry.getKey().toLowerCase())) {
				GuiWizardHandbook.addFormatTag(entry.getKey().toLowerCase()+"_dist", convertDistToGuidebookString(entry.getValue()));				
			}
			else {
				ADDED_TYPES.add(entry);
			}
		});
		List<String> others = Lists.newArrayList();
		ADDED_TYPES.forEach((entry) -> {
			String type = YLib.capitalize(entry.getKey().toLowerCase());
			others.add(type);
			others.add(TRANSLATOR.translate("newDist", type));
			others.add(convertDistToGuidebookString(entry.getValue()));
		});
		GuiWizardHandbook.addFormatTag("ddd_others", YLib.joinNiceString(false, "\n\n", others.stream()));
	}
	
	private static final String convertDistToGuidebookString(IDamageDistribution dist) {
		DamageMap map = dist.distributeDamage(1.0f);
		DDDMaps.adjustHiddenWeightsToUnknown(map);
		return YLib.joinNiceString(false, "\n", map.entrySet().stream().map((e) -> String.format("- %s %s %s", DDDNumberFormatter.PERCENT.format(e.getValue()), getFormattedTypeName(e.getKey()), TextFormatting.BLACK.toString())));
	}
	
	private static final String getFormattedTypeName(DDDDamageType type) {
		if(type.isCustomDamage() || type.getType().equals(Type.SPECIAL)) {
			return type.getFormattedDisplayName();
		}
		return type.getDisplayName();
	}
}
