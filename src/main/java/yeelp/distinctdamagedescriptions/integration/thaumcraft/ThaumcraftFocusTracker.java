package yeelp.distinctdamagedescriptions.integration.thaumcraft;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusPackage;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;
import yeelp.distinctdamagedescriptions.util.lib.YLib;

public final class ThaumcraftFocusTracker {

	private static ThaumcraftFocusTracker instance;
	private static final Predicate<Entity> VALID_TARGET = Predicates.and(Predicates.notNull(), EntityLivingBase.class::isInstance);
	
	
	public static ThaumcraftFocusTracker getInstance() {
		return instance == null ? instance = new ThaumcraftFocusTracker() : instance;
	}
	
	private static final class FocusPackageTimestamp {
		private final int tickTimestamp;
		private final UUID packageUUID;
		
		FocusPackageTimestamp(int ticks, UUID uuid) {
			this.packageUUID = uuid;
			this.tickTimestamp = ticks;
		}

		public int getTickTimestamp() {
			return this.tickTimestamp;
		}

		public UUID getPackageUUID() {
			return this.packageUUID;
		}
	}
	
	private Multimap<UUID, Aspect> castedAspects = MultimapBuilder.hashKeys().hashSetValues().build();
	private Map<UUID, FocusPackageTimestamp> hitByPackageMap = Maps.newHashMap();
	
	public synchronized void trackAspects(FocusPackage focusPackage, RayTraceResult[] targets, Iterable<Aspect> aspects) {
		UUID packageUUID = focusPackage.getUniqueID();
		Arrays.stream(targets).map((t) -> t.entityHit).filter(VALID_TARGET).forEach((entity) -> {
			UUID uuid = entity.getUniqueID();
			FocusPackageTimestamp prevTimestamp = this.hitByPackageMap.get(uuid);	
			if(!packageUUID.equals(new UUID(0, 0)) && (prevTimestamp == null  || !prevTimestamp.getPackageUUID().equals(packageUUID))) {
				this.castedAspects.removeAll(uuid);
				this.hitByPackageMap.put(uuid, new FocusPackageTimestamp(entity.ticksExisted, packageUUID));
			}			
			DebugLib.outputFormattedDebug("UUID: %s, Package UUID: %s", uuid.toString(), packageUUID.toString());
			this.castedAspects.putAll(uuid, aspects);
		});
		DebugLib.doDebug(() -> {
			Builder<String> builder = Stream.builder();
			aspects.forEach((aspect) -> builder.accept(aspect.getTag()));
			DistinctDamageDescriptions.debug("Aspects: " + YLib.joinNiceString(true, ",", builder.build()));
		});
	}
	
	public boolean hasTrackedAspectsThisTick(EntityLivingBase target) {
		return this.castedAspects.containsKey(target.getUniqueID()) && this.hitByPackageMap.get(target.getUniqueID()).getTickTimestamp() == target.ticksExisted;
	}
	
	public Collection<Aspect> getAspects(EntityLivingBase target) {
		return this.castedAspects.get(target.getUniqueID());
	}
}
