package yeelp.distinctdamagedescriptions.handlers;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.impl.dists.DDDDaylightDist;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.DDDConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.GenericConfigReaderException;
import yeelp.distinctdamagedescriptions.util.ConfigReaderUtilities;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public class DaylightTracker extends AbstractTracker {

	protected static final Set<ResourceLocation> WHITELIST = Sets.newHashSet();
	protected static final Set<Class<? extends EntityLivingBase>> CLASS_WHITELIST = Sets.newHashSet();
	protected static final Set<Class<? extends EntityLivingBase>> CLASS_BLACKLIST = Sets.newHashSet();

	private static final class ConfigReader implements DDDConfigReader {
		private static final Set<DDDConfigReaderException> ERRORS = new HashSet<DDDConfigReaderException>();

		@Override
		public void read() {
			Arrays.stream(ModConfig.dmg.extraDamage.daylightWhitelist).filter(Predicates.not(ConfigReaderUtilities::isCommentEntry)).forEach((c) -> this.parse(c).ifPresent(WHITELIST::add));
		}

		@Override
		public String getName() {
			return "Daylight Susceptible Entities";
		}

		@Override
		public boolean shouldTime() {
			return false;
		}

		private final Optional<ResourceLocation> parse(String s) {
			return Optional.ofNullable(s).filter((str) -> {
				if(str.contains(":")) {
					return true;
				}
				ERRORS.add(new GenericConfigReaderException(this.getName(), str + " isn't a valid entity ID!"));
				return false;
			}).map(ResourceLocation::new);
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

	@SuppressWarnings("synthetic-access")
	public static final ConfigReader READER = new ConfigReader();

	@Override
	public boolean shouldStartTracking(EntityLivingBase entity) {
		Class<? extends EntityLivingBase> entityClass = entity.getClass();
		if(CLASS_BLACKLIST.contains(entityClass)) {
			return false;
		}
		if(CLASS_WHITELIST.contains(entityClass)) {
			return isInDaylight(entity);
		}
		return YResources.getEntityID(entity).filter(WHITELIST::contains).map((loc -> {
			CLASS_WHITELIST.add(entityClass);
			DistinctDamageDescriptions.debug(entityClass.getName());
			return isInDaylight(entity);
		})).orElseGet(() -> {
			CLASS_BLACKLIST.add(entityClass);
			DistinctDamageDescriptions.debug(entityClass.getName());
			return false;
		});
	}

	@Override
	public boolean shouldStopTracking(EntityLivingBase entity) {
		return !entity.isBurning();
	}
	
	protected static boolean isInDaylight(EntityLivingBase entity) {
		return entity.world.isDaytime() && entity.isBurning() && entity.getBrightness() > 0.5f && entity.world.canBlockSeeSky(new BlockPos(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ));
	}

	@Override
	public String getName() {
		return DDDDaylightDist.NAME;
	}

	public static void update() {
		if(ModConfig.dmg.extraDamage.enableDaylightBurningDamage) {
			READER.read();
		}
	}
}
