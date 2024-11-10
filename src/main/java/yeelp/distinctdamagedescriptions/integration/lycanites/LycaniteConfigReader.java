package yeelp.distinctdamagedescriptions.integration.lycanites;

import java.util.Arrays;
import java.util.Collection;

import com.google.common.collect.Lists;

import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.DDDConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigInvalidException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException;
import yeelp.distinctdamagedescriptions.util.ConfigReaderUtilities;

public final class LycaniteConfigReader implements DDDConfigReader {

	private static final String REGEX = "^[a-zA-Z]+;[a-zA-Z]+(,(\\s)?[a-zA-Z]+)*$";
	private final Collection<DDDConfigReaderException> errors = Lists.newArrayList();

	@Override
	public void read() {
		for(String s : ModConfig.compat.lycanites.creatureProjectiles) {
			if(ConfigReaderUtilities.isCommentEntry(s)) {
				continue;
			}
			if(s.matches(REGEX)) {
				String[] arr = s.split(";");
				Arrays.stream(arr[1].split(",")).map(String::trim).forEach((str) -> LycanitesConfigurations.creatureProjectiles.put(str, arr[0]));				
			}
			else {
				this.errors.add(new ConfigInvalidException(this.getName(), s));
			}
		}
	}
	
	@Override
	public boolean doesSelfReportErrors() {
		return true;
	}
	
	@Override
	public Collection<DDDConfigReaderException> getSelfReportedErrors() {
		return this.errors;
	}

	@Override
	public boolean shouldTime() {
		return true;
	}

	@Override
	public String getName() {
		return "Lycanite Creature Projectiles";
	}

}
