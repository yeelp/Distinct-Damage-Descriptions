package yeelp.distinctdamagedescriptions.util.development;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.util.lib.DDDAttributeModifierCollections;

public final class LivingHurtEventInfo extends AbstractLivingEventDeveloperModeInfo<LivingHurtEvent> {

	public LivingHurtEventInfo() {
		super(() -> ModConfig.dev.showHurtInfo);
	}
	
	@Override
	public boolean shouldFire(LivingHurtEvent evt) {
		return true;
	}
	
	@Override
	protected StringBuilder getInfo(LivingHurtEvent evt, StringBuilder sb) {
		DDDAPI.accessor.getDDDCombatTracker(evt.getEntityLiving()).ifPresent((tracker) -> {
			AbstractAttributeMap attributes = tracker.getFighter().getAttributeMap();
			float armor = mapIfNonNullElseGetDefault(attributes.getAttributeInstance(SharedMonsterAttributes.ARMOR).getModifier(DDDAttributeModifierCollections.ArmorModifiers.ARMOR.getUUID()), AttributeModifier::getAmount, 0).floatValue();
			float toughness = mapIfNonNullElseGetDefault(attributes.getAttributeInstance(SharedMonsterAttributes.ARMOR_TOUGHNESS).getModifier(DDDAttributeModifierCollections.ArmorModifiers.TOUGHNESS.getUUID()), AttributeModifier::getAmount, 0).floatValue();
			if(armor != 0 || toughness != 0) {
				sb.append(String.format("HURT: Defender %s got a %+.2f armor and %+.2f toughness modification from armor effectiveness.", tracker.getFighter().getName(), armor, toughness));
				sb.append(NEW_LINE);
			}
			sb.append(String.format("Current damage: %.2f", evt.getAmount()));
		});
		return sb;
	}

}
