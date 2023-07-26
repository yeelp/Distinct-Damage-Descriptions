package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.damage.IDamageSource;
import crafttweaker.api.entity.IEntityLivingBase;
import stanhebben.zenscript.ZenRuntimeException;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.CTDDDCustomDistribution;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.lib.YLib;

@ZenClass("mods.ddd.distributions.DistributionBuilder")
@ZenRegister
public final class CoTDDDDistributionBuilder {
	
	private static final Collection<CoTDDDDistributionBuilder> BUILDERS = new ArrayList<CoTDDDDistributionBuilder>();
	private static final Set<String> USED_NAMES = Sets.newHashSet();

	@ZenClass("mods.ddd.distributions.IsContextApplicable")
	@ZenRegister
	public interface IsContextApplicable {
		boolean handle(ICTDDDCustomDistribution thisDist, IDamageSource src, IEntityLivingBase target);
	}
	
	@ZenProperty 
	public String name;
	
	@ZenProperty
	public IsContextApplicable isContextApplicable;
	
	@ZenProperty
	public int priority = 0;
	
	private final DDDBaseMap<Float> weights;
	private final Map<String, Float> stringWeights;
	private boolean wasBuilt = false;
	
	@ZenMethod
	public static CoTDDDDistributionBuilder create(String name) {
		return new CoTDDDDistributionBuilder(name);
	}
	
	public CoTDDDDistributionBuilder(String name) {
		this.name = name;
		this.weights = new DDDBaseMap<Float>(() -> 0.0f);
		this.stringWeights = Maps.newHashMap();
	}
	
	@ZenMethod
	public void setWeight(ICTDDDDamageType type, float weight) {
		this.weights.put(type.asDDDDamageType(), weight);
	}
	
	@ZenMethod
	public void setWeight(String type, float weight) {
		this.stringWeights.put(type, weight);
	}
	
	@ZenMethod
	public void build() {
		if(this.wasBuilt) {
			return;
		}
		if(this.name == null) {
			throw new ZenRuntimeException("Distribution name can not be null!");
		}
		else if(USED_NAMES.contains(this.name)) {
			throw new ZenRuntimeException(String.format("%s is a name already used by a distribution created in another script!", this.name));
		}
		else if(DDDRegistries.distributions.get(this.name) != null) {
			throw new ZenRuntimeException(String.format("%s is a name already used by DDD!", this.name));
		}
		Map<String, Float> combined = Maps.newHashMap();
		combined.putAll(this.stringWeights);
		this.weights.forEach((t, f) -> combined.put(t.getTypeName().substring("ddd_".length()), f));
		String[] negWeights = combined.entrySet().stream().filter((e) -> e.getValue() < 0).map((e) -> e.getKey().concat(" with weight: ") + e.getValue()).toArray(String[]::new);
		if(negWeights.length > 0) {
			throw new ZenRuntimeException(String.format("Some weights for %s are negative! Negative weights are not allowed! Please fix the following weights: %s", this.name, YLib.joinNiceString(true, ",", negWeights)));
		}
		if(Math.abs(combined.values().stream().mapToDouble((f) -> f).sum() - 1) >= 0.01) {
			throw new ZenRuntimeException(String.format("%s does not have weights that add to 1!", this.name));
		}
		if(this.isContextApplicable == null) {
			throw new ZenRuntimeException(String.format("No applicable context function (isContextApplicable) defined for %s!", this.name));
		}
		BUILDERS.add(this);
		USED_NAMES.add(this.name);
		this.wasBuilt = true;
	}
	
	public static void registerDists() {
		BUILDERS.forEach((b) -> { 
			b.stringWeights.forEach((s, f) -> b.weights.put(DDDRegistries.damageTypes.get(s), f));
			DDDRegistries.distributions.register(new CTDDDCustomDistribution(b.name, b.weights, b.isContextApplicable, b.priority));
		});
		USED_NAMES.clear();
		BUILDERS.clear();
	}
}
