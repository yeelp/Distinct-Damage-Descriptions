package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
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
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.CTConsts;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.CTDDDCustomDistribution;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.lib.YLib;

@ZenClass(CTConsts.CTClasses.COTDISTBUILDER)
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
	
	private CoTDDDDistributionBuilder(CoTDDDDistributionBuilder builder) {
		this.name = builder.name;
		this.weights = builder.weights;
		this.stringWeights = builder.stringWeights;
		this.isContextApplicable = builder.isContextApplicable;
		this.priority = builder.priority;
		this.wasBuilt = builder.wasBuilt;
	}
	
	@ZenMethod
	public void setWeight(ICTDDDDamageType type, float weight) {
		this.weights.put(type.asDDDDamageType(), weight);
	}
	
	@ZenMethod
	public void setWeight(String type, float weight) {
		this.stringWeights.put(DDDDamageType.removeDDDPrefixIfPresent(type), weight);
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
		Map<String, Float> combined = Maps.newHashMap();
		combined.putAll(this.stringWeights);
		this.weights.forEach((t, f) -> combined.merge(DDDDamageType.removeDDDPrefixIfPresent(t.getTypeName()), f, Float::sum));
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
		BUILDERS.add(new CoTDDDDistributionBuilder(this));
		USED_NAMES.add(this.name);
		this.wasBuilt = true;
	}
	
	public static void registerDists() {
		BUILDERS.forEach((b) -> {
			if(DDDRegistries.distributions.get(b.name) != null) {
				throw new ZenRuntimeException(String.format("%s is a distribution name already registered! Original registration source: %s", b.name, DDDRegistries.distributions.get(b.name).getCreationSourceString()));
			}
			//@formatter:off
			Optional<RuntimeException> e = b.stringWeights.keySet().stream()
					.filter(CTConsts.IS_NOT_REGISTERED)
					.reduce(CTConsts.CONCAT_WITH_LINEBREAK)
					.map((s) -> new RuntimeException(String.format("Unregistered damage type(s) used for %s: %s", b.name, s)));
			//@formatter:on
			if(e.isPresent()) {
				throw e.get();
			}
			b.stringWeights.forEach((s, f) -> b.weights.put(DDDRegistries.damageTypes.get(DDDDamageType.addDDDPrefixIfNeeded(s)), f));
			DDDRegistries.distributions.register(new CTDDDCustomDistribution(b.name, b.weights, b.isContextApplicable, b.priority));
		});
		USED_NAMES.clear();
		BUILDERS.clear();
	}
}
