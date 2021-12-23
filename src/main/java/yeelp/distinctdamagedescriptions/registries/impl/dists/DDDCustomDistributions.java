package yeelp.distinctdamagedescriptions.registries.impl.dists;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Maps;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.DDDPredefinedDistribution;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.util.DamageTypeData;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public final class DDDCustomDistributions implements DDDPredefinedDistribution {
	private final Map<String, DDDDamageType> includeAllMap;
	private final Map<String, SourceMap> srcMap;

	private final class SourceMap {
		private final Map<String, DDDDamageType> direct, indirect;

		SourceMap() {
			this(DDDBuiltInDamageType.NORMAL);
		}

		SourceMap(DDDDamageType defaultVal) {
			this.direct = new NonNullMap<String, DDDDamageType>(() -> defaultVal);
			this.indirect = new NonNullMap<String, DDDDamageType>(() -> defaultVal);
		}

		void update(DDDDamageType type, DamageTypeData data) {
			for(String s : data.getDirectSources()) {
				this.direct.put(s, type);
			}
			for(String s : data.getIndirectSources()) {
				this.indirect.put(s, type);
			}
		}

		Map<String, DDDDamageType> getDirect() {
			return this.direct;
		}

		Map<String, DDDDamageType> getIndirect() {
			return this.indirect;
		}
		
		
	}

	public DDDCustomDistributions() {
		this.includeAllMap = Maps.newHashMap();
		this.srcMap = Maps.newHashMap();
	}

	@Override
	public boolean enabled() {
		return ModConfig.core.useCustomDamageTypes;
	}

	@Override
	public Set<DDDDamageType> getTypes(DamageSource src, EntityLivingBase target) {
		HashSet<DDDDamageType> set = new HashSet<DDDDamageType>();
		if(this.includeAllMap.containsKey(src.getDamageType())) {			
			set.add(this.includeAllMap.get(src.getDamageType()));
		}
		if(this.srcMap.containsKey(src.getDamageType())) {
			Optional<String> direct = YResources.getEntityIDString(src.getImmediateSource());
			Optional<String> indirect = YResources.getEntityIDString(src.getTrueSource());
			DistinctDamageDescriptions.debug(direct.orElse("") + ", " + indirect.orElse(""));
			SourceMap sMap = this.srcMap.get(src.getDamageType());
			DDDDamageType directType = sMap.getDirect().get(direct.orElse(""));
			DDDDamageType indirectType = sMap.getIndirect().get(indirect.orElse(""));
			DistinctDamageDescriptions.debug(directType.getTypeName() + ", " + indirectType.getTypeName());
			set.add(directType);
			set.add(indirectType);
			set.remove(DDDBuiltInDamageType.NORMAL);
		}
		return set;
	}

	@Override
	public String getName() {
		return "json";
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(DamageSource src, EntityLivingBase target) {
		Set<DDDDamageType> types = getTypes(src, target);
		if(types.size() == 0) {
			return Optional.empty();
		}
		if(types.size() == 1) {
			return Optional.of(types.iterator().next().getBaseDistribution());
		}
		Map<DDDDamageType, Float> map = new NonNullMap<DDDDamageType, Float>(() -> 0.0f);
		float weight = 1.0f / types.size();
		for(DDDDamageType type : types) {
			map.put(type, weight);
		}
		return Optional.of(new DamageDistribution(map));
	}

	public void registerDamageTypeData(DDDDamageType type, DamageTypeData[] datas) {
		for(DamageTypeData d : datas) {
			update(type, d);
		}
		DistinctDamageDescriptions.info(String.format("Registered damage type info for type %s!", type.getDisplayName()));
	}

	private void update(DDDDamageType type, DamageTypeData data) {
		if(data.includeAll()) {
			this.includeAllMap.put(data.getOriginalSource(), type);
		}
		if(!this.srcMap.containsKey(data.getOriginalSource())) {
			// this.srcMap.putIfAbsent would still require the
			// computing of a new SourceMap every time, so it's less
			// efficient
			this.srcMap.put(data.getOriginalSource(), new SourceMap());
		}
		this.srcMap.get(data.getOriginalSource()).update(type, data);
	}
}
