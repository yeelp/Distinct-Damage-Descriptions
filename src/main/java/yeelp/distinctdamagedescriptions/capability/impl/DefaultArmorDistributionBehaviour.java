package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Set;
import java.util.function.Supplier;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDefaultDistribution;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.ArmorMap;

public enum DefaultArmorDistributionBehaviour implements IDefaultDistribution, IArmorDistribution {
	NO_EFFECTIVENESS(() -> 0.0f) {
		@Override
		public Set<DDDDamageType> getCategories() {
			return ImmutableSet.of();
		}

		@Override
		public final ArmorMap distributeArmor(float armor, float toughness) {
			return DDDMaps.newArmorMap();
		}
	},
	ALL_EFFECTIVENESS(() -> 1.0f) {
		private Set<DDDDamageType> types;
		@Override
		public Set<DDDDamageType> getCategories() {
			return this.types == null ? this.types = ImmutableSet.copyOf(DDDRegistries.damageTypes.getAll()) : this.types;
		}
	},
	EFFECTIVE_TO_REGULAR_TYPES(() -> 1.0f) {
		private Set<DDDDamageType> types;
		@Override
		public float getWeight(DDDDamageType type) {
			if(DDDDamageType.isInternalType(type)) {
				return 0.0f;				
			}
			return super.getWeight(type);
		}

		@Override
		public Set<DDDDamageType> getCategories() {
			return this.types == null ? this.types = DDDRegistries.damageTypes.getAll().stream().filter(Predicates.not(DDDDamageType::isInternalType)).collect(ImmutableSet.toImmutableSet()) : this.types;
		}
	},
	ALLOW_BYPASS_DAMAGE_TYPE(() -> 1.0f) {
		private Set<DDDDamageType> types;
		@Override
		public float getWeight(DDDDamageType type) {
			if(DDDDamageType.isUnknownType(type)) {
				return 0.0f;
			}
			return super.getWeight(type);
		}

		@Override
		public Set<DDDDamageType> getCategories() {
			return this.types == null ? DDDRegistries.damageTypes.getAll().stream().filter(Predicates.not(DDDDamageType::isUnknownType)).collect(ImmutableSet.toImmutableSet()) : this.types;
		}
	};

	private final DDDBaseMap<Float> dist;
	
	private DefaultArmorDistributionBehaviour(Supplier<Float> defaultVal) {
		this.dist = new DDDBaseMap<Float>(defaultVal);
	}
	
	@Override
	public float getWeight(DDDDamageType type) {
		return this.dist.getDefaultValue();
	}

	@Override
	public abstract Set<DDDDamageType> getCategories();

	@Override
	public ArmorMap distributeArmor(float armor, float toughness) {
		return this.getCategories().stream().collect(DDDMaps.ArmorMap.typesToArmorMap((type) -> new ArmorValues(armor, toughness)));
	}

	@Override
	public IArmorDistribution copy() {
		return this;
	}

	@Override
	public IArmorDistribution update(ItemStack owner) {
		return this;
	}

}
