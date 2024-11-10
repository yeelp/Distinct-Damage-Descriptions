package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.common.base.Functions;
import com.google.common.collect.Sets;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import net.minecraft.entity.EntityLivingBase;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.IDDDCapModifier;
import yeelp.distinctdamagedescriptions.api.IDDDResistanceModifier;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.CTConsts;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.modifiers.CTEntityLivingBaseResistanceModifier;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

@ZenClass(CTConsts.CTClasses.COTRESISTANCESMODIFIERBUILDER)
@ZenRegister
public final class CoTMobResistancesModifierBuilder extends CoTDDDModifierBuilder<IEntityLivingBase, EntityLivingBase> {

	private boolean isAdaptabilityUnchanged = true, adaptabilityStatus;
	
	@ZenProperty
	public float adaptabilityMod;
	
	@ZenProperty
	public IsModifierApplicableForEntityLivingBase isModifierApplicable;
	
	private Set<String> stringImmunitiesAdded = Sets.newHashSet(), stringImmunitiesRemoved = Sets.newHashSet();
	
	private CoTMobResistancesModifierBuilder(String name) {
		super(ModifierType.ENTITY_LIVING_BASE_RESISTANCE, name);
	}

	@Override
	protected IDDDCapModifier<EntityLivingBase> makeModifierSpecific() {
		IDDDResistanceModifier mod;
		if(this.isAdaptabilityUnchanged) {
			mod = new CTEntityLivingBaseResistanceModifier(this.name, this.shouldReallocate, this.adaptabilityMod, this.isModifierApplicable);			
		}
		else {
			mod = new CTEntityLivingBaseResistanceModifier(this.name, this.shouldReallocate, this.adaptabilityMod, this.isModifierApplicable, this.adaptabilityStatus);
		}
		//@formatter:off
		Optional<RuntimeException> e = Stream.<Set<String>>builder().add(this.stringImmunitiesAdded).add(this.stringImmunitiesRemoved).build()
				.map((set) -> set.stream().filter(CTConsts.IS_NOT_REGISTERED).reduce(CTConsts.CONCAT_WITH_LINEBREAK))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.reduce(CTConsts.CONCAT_WITH_LINEBREAK)
				.map((s) -> new RuntimeException(String.format("Unregistered damage type(s) used for %s: %s", this.name, s)));
		//@formatter:on
		if(e.isPresent()) {
			throw e.get();
		}
		addTypesToMod(this.stringImmunitiesAdded, mod::addImmunity);
		addTypesToMod(this.stringImmunitiesRemoved, mod::removeImmunity);
		return mod;
	}

	@Override
	protected CoTDDDModifierBuilder<IEntityLivingBase, EntityLivingBase> copySpecific() {
		CoTMobResistancesModifierBuilder builder = new CoTMobResistancesModifierBuilder(this.name);
		builder.adaptabilityStatus = this.adaptabilityStatus;
		builder.adaptabilityMod = this.adaptabilityMod;
		builder.isAdaptabilityUnchanged = this.isAdaptabilityUnchanged;
		builder.isModifierApplicable = this.isModifierApplicable;
		builder.stringImmunitiesAdded.addAll(this.stringImmunitiesAdded);
		builder.stringImmunitiesRemoved.addAll(this.stringImmunitiesRemoved);
		return builder;
	}

	@Override
	protected Predicate<IEntityLivingBase> getPredicate() {
		return this.isModifierApplicable;
	}
	
	@ZenMethod
	public void setAdaptability(boolean status) {
		this.isAdaptabilityUnchanged = false;
		this.adaptabilityStatus = status;
	}
	
	@ZenMethod
	public void addImmunity(String type) {
		this.stringImmunitiesAdded.add(type);
	}
	
	@ZenMethod
	public void removeImmunity(String type) {
		this.stringImmunitiesRemoved.add(type);
	}

	@ZenMethod
	public static CoTMobResistancesModifierBuilder create(String name) {
		return new CoTMobResistancesModifierBuilder(name);
	}
	
	private static void addTypesToMod(Set<String> stringTypes, Consumer<DDDDamageType> consumer) {
		stringTypes.stream().map(Functions.compose(DDDRegistries.damageTypes::get, DDDDamageType::addDDDPrefixIfNeeded)).forEach(consumer);
	}
}
