package yeelp.distinctdamagedescriptions.integration.tic.tinkers.capability;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableSet;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.TinkerToolCore;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDDamageType.Type;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.TiCConfigurations;
import yeelp.distinctdamagedescriptions.integration.capability.IDistributionRequiresUpdate;
import yeelp.distinctdamagedescriptions.integration.tic.capability.AbstractTinkersDistribution;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.DistributionBias;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public abstract class TinkerToolDistribution<D extends IDistribution> extends AbstractTinkersDistribution<D, DistributionBias> {

	@Override
	protected String getPartType() {
		return MaterialTypes.HEAD;
	}

	@Override
	protected Iterator<PartMaterialType> getParts(ItemStack stack) {
		return ((TinkerToolCore) stack.getItem()).getRequiredComponents().iterator();
	}

	@Override
	protected IDDDConfiguration<DistributionBias> getConfiguration() {
		return TiCConfigurations.toolMaterialBias;
	}
	
	protected static final Map<DDDDamageType, Float> mergeMaps(Collection<Map<DDDDamageType, Float>> maps) {
		return mergeMaps(maps, maps.size());
	}
	
	private static final Map<DDDDamageType, Float> mergeMaps(Iterable<Map<DDDDamageType, Float>> maps, int size) {
		DDDBaseMap<Float> map = new DDDBaseMap<Float>(0.0f);
		maps.forEach((m) -> m.forEach((k, v) -> map.merge(k, v/size, Float::sum)));
		return map;
	}

	public static final class Tool extends TinkerToolDistribution<IDamageDistribution> {

		@CapabilityInject(Tool.class)
		public static Capability<Tool> cap;

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return cap == capability;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return this.hasCapability(capability, facing) ? cap.cast(this) : null;
		}

		@Override
		protected IDamageDistribution getDistributionCapabilityOnStack(ItemStack stack) {
			return DDDAPI.accessor.getDamageDistribution(stack);
		}
		
		@Override
		protected Optional<Map<DDDDamageType, Float>> determineNewMap(ItemStack stack, Collection<String> mats, IDDDConfiguration<DistributionBias> config) {
			float biasResistance = TiCConfigurations.toolBiasResistance.getOrFallbackToDefault(YResources.getRegistryString(stack));
			Set<Map<DDDDamageType, Float>> dists = mats.stream().map((m) -> config.getOrFallbackToDefault(m).getBiasedDistributionMap(this.baseDist, biasResistance).orElse(new HashMap<DDDDamageType, Float>())).filter((m) -> !m.isEmpty()).collect(Collectors.toSet());
			if(dists.size() == 0) {
				return Optional.empty();
			}
			return Optional.of(mergeMaps(dists));
		}

		public static void register() {
			IDistributionRequiresUpdate.register(Tool.class, Tool::new);
		}
		
		@CapabilityInject(Tool.class)
		public static void onRegister(Capability<Tool> cap) {
			IDistributionRequiresUpdate.PlayerHandler.allowCapabilityUpdates(cap);
		}

	}

	public static final class Shield extends TinkerToolDistribution<ShieldDistribution> {
		
		private static final Set<DDDDamageType> PHYSICAL_TYPES = DDDRegistries.damageTypes.getAll().stream().filter((t) -> t.getType()== Type.PHYSICAL).collect(Collectors.toSet());
		@CapabilityInject(Shield.class)
		public static Capability<Shield> cap;

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return cap == capability;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return this.hasCapability(capability, facing) ? cap.cast(this) : null;
		}

		@Override
		protected ShieldDistribution getDistributionCapabilityOnStack(ItemStack stack) {
			return DDDAPI.accessor.getShieldDistribution(stack);
		}
		
		@Override
		protected Optional<Map<DDDDamageType, Float>> determineNewMap(ItemStack stack, Collection<String> mats, IDDDConfiguration<DistributionBias> config) {
			Set<Map<DDDDamageType, Float>> dists = mats.stream().map((m) -> config.getOrFallbackToDefault(m).getPreferredMapCopy()).collect(Collectors.toSet());
			Map<DDDDamageType, Float> map = mergeMaps(dists);
			float avgDurabilityRatio = this.getKeyMaterialIdentifiers(stack).stream().map(Functions.compose((m) -> ((HeadMaterialStats) m.getStats(this.getPartType())).durability, TinkerRegistry::getMaterial)).collect(Collectors.averagingInt(Integer::intValue)).floatValue() / 1000;
			float bonus = (float) (avgDurabilityRatio * 0.3f + map.keySet().stream().filter(PHYSICAL_TYPES::contains).mapToDouble((t) -> 0.1f).sum());
			ImmutableSet.<DDDDamageType>builder().addAll(PHYSICAL_TYPES).addAll(map.keySet()).build().forEach((type) -> map.compute(type, (t, f) -> Math.min(1.0f, (f == null ? 0.0f : f) + bonus)));
			return Optional.of(map);
		}
		
		public static void register() {
			IDistributionRequiresUpdate.register(Shield.class, Shield::new);
		}
		
		@CapabilityInject(Shield.class)
		public static void onRegister(Capability<Shield> cap) {
			IDistributionRequiresUpdate.PlayerHandler.allowCapabilityUpdates(cap);
		}
	}
}
