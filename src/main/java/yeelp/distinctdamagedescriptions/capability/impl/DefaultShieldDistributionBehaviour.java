package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collector;

import com.google.common.base.Predicates;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDefaultDistribution;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps;
import yeelp.distinctdamagedescriptions.util.lib.InvariantViolationException;

public enum DefaultShieldDistributionBehaviour {
	NO_EFFECTIVENESS {
		@Override
		DDDBaseMap<Float> getBlockMap() {
			return DDDMaps.newResistMap();
		}
	},
	ALL_EFFECTIVENESS {
		@Override
		DDDBaseMap<Float> getBlockMap() {
			return DDDRegistries.damageTypes.getAll().stream().collect(DDD_TYPE_COLLECTOR);
		}
	},
	EFFECTIVE_TO_REGULAR_TYPES(DDDDamageType::isInternalType),
	ALLOW_BYPASS_DAMAGE_TYPE(DDDDamageType::isUnknownType);
	
	static final Collector<DDDDamageType, ?, DDDBaseMap<Float>> DDD_TYPE_COLLECTOR = DDDBaseMap.typesToDDDBaseMap(() -> 0.0f, (type) -> 1.0f);
	private ShieldDistribution dist;
	private final Predicate<DDDDamageType> p;
	
	private DefaultShieldDistributionBehaviour() {
		this(Predicates.alwaysTrue());
	}
	
	private DefaultShieldDistributionBehaviour(Predicate<DDDDamageType> p) {
		this.p = p;
	}
	
	public ShieldDistribution getShieldDistribution() {
		return this.dist == null ? this.dist = this.createShieldDistribution() : this.dist;
	}
	
	private ShieldDistribution createShieldDistribution() {
		return new DefaultShieldDistribution(this.getBlockMap());
	}
	
	DDDBaseMap<Float> getBlockMap() {
		return DDDRegistries.damageTypes.getAll().stream().filter(this.p.negate()).collect(DDD_TYPE_COLLECTOR);
	}
	
	private static class DefaultShieldDistribution extends ShieldDistribution implements IDefaultDistribution {
		
		public DefaultShieldDistribution(Map<DDDDamageType, Float> blockMap) {
			super(blockMap);
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return false;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return null;
		}

		@Override
		public ShieldDistribution copy() {
			return this;
		}

		@Override
		public ShieldDistribution update(ItemStack owner) {
			return this;
		}

		@Override
		public NBTTagCompound serializeSpecificNBT() {
			throw new UnsupportedOperationException("Default Shield distributions have no NBT!");
		}

		@Override
		public void deserializeSpecificNBT(NBTTagCompound lst) {
			throw new UnsupportedOperationException("Default Shield distributions have no NBT!");
		}

		@Override
		public void setWeight(DDDDamageType type, float amount) {
			throw new UnsupportedOperationException("Can not set new weights in default shield distribution.");
		}

		@Override
		public void setNewWeights(Map<DDDDamageType, Float> map) throws InvariantViolationException {
			throw new UnsupportedOperationException("Can not set new weights in default shield distribution.");
		}
	}

}
