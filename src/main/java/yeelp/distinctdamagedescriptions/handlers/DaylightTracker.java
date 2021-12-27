package yeelp.distinctdamagedescriptions.handlers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.DDDConfigReader;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public final class DaylightTracker extends AbstractTracker {

	protected static final Set<ResourceLocation> WHITELIST = new HashSet<ResourceLocation>();

	private static final class ConfigReader implements DDDConfigReader {
		private static final Set<String> ERRORS = new HashSet<String>();

		@Override
		public void read() {
			Arrays.stream(ModConfig.dmg.extraDamage.daylightWhitelist).forEach((c) -> ConfigReader.parse(c).ifPresent(WHITELIST::add));
		}

		@Override
		public String getName() {
			return "Daylight Susceptible Entities";
		}

		@Override
		public boolean shouldTime() {
			return false;
		}

		private static final Optional<ResourceLocation> parse(String s) {
			return Optional.ofNullable(s).filter((str) -> {
				if(str.contains(":")) {
					return true;
				}
				ERRORS.add(str + "isn't a valid entity ID!");
				return false;
			}).map(ResourceLocation::new);
		}

	}

	@SuppressWarnings("synthetic-access")
	public static final ConfigReader READER = new ConfigReader();

	@Override
	public boolean shouldStartTracking(EntityLivingBase entity) {
		return entity.world.isDaytime() && YResources.getEntityID(entity).filter(WHITELIST::contains).isPresent() && entity.isBurning() && entity.world.canBlockSeeSky(entity.getPosition());
	}

	@Override
	public boolean shouldStopTracking(EntityLivingBase entity) {
		return !entity.isBurning();
	}

	@Override
	public String getName() {
		return "daylight";
	}

	public static void update() {
		if(ModConfig.dmg.extraDamage.enableDaylightBurningDamage) {
			READER.read();
		}
	}
}
