package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
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
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.TiCConfigurations;
import yeelp.distinctdamagedescriptions.util.DistributionBias;

public abstract class TinkerToolDistribution<D extends IDistribution> extends AbstractTinkersDistribution<D> {

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
		return TiCConfigurations.weaponMaterialBias;
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
		
	}
	
	public static final class Shield extends TinkerToolDistribution<ShieldDistribution> {

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
		protected Optional<Map<DDDDamageType, Float>> calculateNewMap(ItemStack stack) {
			Optional<Map<DDDDamageType, Float>> map = super.calculateNewMap(stack);
			if(map.isPresent()) {
				float avgDurabilityRatio = this.getHeadMaterialIdentifiers(stack).stream().map(Functions.compose((m) -> ((HeadMaterialStats) m.getStats(this.getPartType())).durability, TinkerRegistry::getMaterial)).collect(Collectors.averagingInt(Integer::intValue)).floatValue()/1000;
				float bonus = avgDurabilityRatio * 0.3f;
				Map<DDDDamageType, Float> dist = map.get();
				ImmutableSet.<DDDDamageType>builder().addAll(Arrays.asList(DDDBuiltInDamageType.PHYSICAL_TYPES)).addAll(dist.keySet()).build().forEach((type) -> dist.compute(type, (t, f) -> Math.min(1.0f, f + bonus)));
			}
			return map;
		}		
	}
}
