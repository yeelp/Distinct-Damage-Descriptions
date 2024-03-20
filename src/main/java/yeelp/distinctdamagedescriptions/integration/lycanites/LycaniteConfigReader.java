package yeelp.distinctdamagedescriptions.integration.lycanites;

import java.util.Arrays;

import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.DDDConfigReader;
import yeelp.distinctdamagedescriptions.util.ConfigReaderUtilities;

public final class LycaniteConfigReader implements DDDConfigReader {

	@Override
	public void read() {
		for(String s : ModConfig.compat.lycanites.creatureProjectiles) {
			if(ConfigReaderUtilities.isCommentEntry(s)) {
				continue;
			}
			String[] arr = s.split(";");
			Arrays.stream(arr[1].split(",")).map(String::trim).forEach((str) -> LycanitesConfigurations.creatureProjectiles.put(str, arr[0]));
		}
	}

	@Override
	public String getName() {
		return "Lycanite Creature Projectiles";
	}

	@Override
	public boolean shouldTime() {
		return true;
	}

}
