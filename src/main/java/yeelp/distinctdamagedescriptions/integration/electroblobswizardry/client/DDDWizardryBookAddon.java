package yeelp.distinctdamagedescriptions.integration.electroblobswizardry.client;

import electroblob.wizardry.client.gui.handbook.GuiWizardHandbook;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDDamageType.Type;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.integration.electroblobswizardry.WizardryConfigurations;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.YLib;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.DDDNumberFormatter;

public final class DDDWizardryBookAddon {

	private DDDWizardryBookAddon() {
		//no instances
	}
	
	public static void init() {
		GuiWizardHandbook.registerAddonHandbookContent(ModConsts.MODID);
		initDistributionTags();
	}
	
	private static final void initDistributionTags() {
		WizardryConfigurations.spellTypeDist.forEach((entry) -> {
			GuiWizardHandbook.addFormatTag(entry.getKey()+"_dist", convertDistToGuidebookString(entry.getValue()));
		});
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
