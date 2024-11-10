package yeelp.distinctdamagedescriptions.integration.baubles;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Lists;

import net.minecraftforge.fml.common.eventhandler.Event;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.DDDConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigInvalidException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigMalformedException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigParsingException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException;
import yeelp.distinctdamagedescriptions.integration.baubles.modifier.AbstractBaubleModifier;
import yeelp.distinctdamagedescriptions.integration.baubles.modifier.BaubleBruteForceModifier;
import yeelp.distinctdamagedescriptions.integration.baubles.modifier.BaubleDamageModifier;
import yeelp.distinctdamagedescriptions.integration.baubles.modifier.BaubleImmunityModifier;
import yeelp.distinctdamagedescriptions.integration.baubles.modifier.BaubleResistanceModifier;
import yeelp.distinctdamagedescriptions.integration.baubles.modifier.BaubleSlyStrikeModifier;
import yeelp.distinctdamagedescriptions.integration.baubles.util.BaubleModifierType;
import yeelp.distinctdamagedescriptions.util.ConfigReaderUtilities;

public class BaublesConfigReader implements DDDConfigReader {

	private static final String NEGATIVE_DIST_REGEX = ConfigReaderUtilities.buildListRegex(ConfigReaderUtilities.ALLOW_NEGATIVE_ENTRY_TUPLE_SUBREGEX);
	private static final Collection<DDDConfigReaderException> ERRORS = Lists.newArrayList();

	@SuppressWarnings("incomplete-switch")
	@Override
	public void read() {
		for(String s : ModConfig.compat.baubles.baubleMods) {
			if(ConfigReaderUtilities.isCommentEntry(s)) {
				continue;
			}
			if(s.endsWith(";")) {
				ERRORS.add(new ConfigMalformedException(this.getName(), s));
				continue;
			}
			String[] args = s.split(";");
			if(args[1].matches("\\d")) {
				int i = Integer.parseInt(args[1]);
				if(i < BaubleModifierType.values().length) {
					BaubleModifierType type = BaubleModifierType.values()[i];
					String regex = type.allowsNegative() ? NEGATIVE_DIST_REGEX : ConfigReaderUtilities.DIST_REGEX;
					if(args[2].matches(regex)) {
						Map<DDDDamageType, Float> map;
						AbstractBaubleModifier<? extends Event> mod = null;
						try {
							map = ConfigReaderUtilities.parseMap(this, args[2], ConfigReaderUtilities::parseDamageType, Float::parseFloat, () -> 0.0f);
						}
						catch(ConfigParsingException e) {
							ERRORS.add(e);
							continue;
						}
						switch(BaubleModifierType.values()[i]) {
							case BRUTE_FORCE:
								mod = new BaubleBruteForceModifier();
								break;
							case DAMAGE_MOD:
								mod = new BaubleDamageModifier();
								break;
							case IMMUNITY:
								mod = new BaubleImmunityModifier();
								break;
							case RESISTANCE_MOD:
								mod = new BaubleResistanceModifier();
								break;
							case SLY_STRIKE:
								mod = new BaubleSlyStrikeModifier();
								break;
						}
						if(mod != null) {
							mod.putAll(map);
							if(BaublesConfigurations.baubleModifiers.put(args[0], mod)) {
								DistinctDamageDescriptions.warn(String.format("%s overwrites existing bauble modifier of the same type!", mod.toString()));
							}
							continue;
						}
						throw new RuntimeException("Valid Bauble Modifier id detected, but no valid BaubleModifierType case!");
					}
				}
			}
			ERRORS.add(new ConfigInvalidException(this.getName(), s));
		}
	}

	@Override
	public String getName() {
		return "DDD Baubles Modifiers";
	}

	@Override
	public boolean shouldTime() {
		return true;
	}

	@Override
	public boolean doesSelfReportErrors() {
		return true;
	}

	@Override
	public Collection<DDDConfigReaderException> getSelfReportedErrors() {
		return ERRORS;
	}

}
