package yeelp.distinctdamagedescriptions.integration.crafttweaker.types;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.item.IItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import stanhebben.zenscript.ZenRuntimeException;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenProperty;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.IDDDCapModifier;
import yeelp.distinctdamagedescriptions.api.IDDDCapModifier.AppliesTo;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.CTConsts;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.registries.IDDDModifierRegistries;
import yeelp.distinctdamagedescriptions.registries.IDDDModifierRegistries.IDDDModifierRegistry;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;

@ZenClass(CTConsts.CTClasses.COTMODIFIERBUILDER)
@ZenRegister
public abstract class CoTDDDModifierBuilder<T, U extends ICapabilityProvider> {
	
	protected enum ModifierType {
		ITEM_DAMAGE(AppliesTo.ITEM_DAMAGE) {
			@Override
			IDDDModifierRegistry<?, ?, ? extends IDDDCapModifier<?>> getRegistry() {
				return DDDRegistries.modifiers.getItemStackDamageDistributionRegistry();
			}
		},
		ITEM_ARMOR(AppliesTo.ARMOR) {
			@Override
			IDDDModifierRegistry<?, ?, ? extends IDDDCapModifier<?>> getRegistry() {
				return DDDRegistries.modifiers.getItemStackArmorDistributionRegistry();
			}
		},
		ITEM_SHIELD(AppliesTo.SHIELD) {
			@Override
			IDDDModifierRegistry<?, ?, ? extends IDDDCapModifier<?>> getRegistry() {
				return DDDRegistries.modifiers.getItemStackShieldDistributionRegistry();
			}
		},
		ENTITY(AppliesTo.PROJECTILE) {					
			@Override
			IDDDModifierRegistry<?, ?, ? extends IDDDCapModifier<?>> getRegistry() {
				return DDDRegistries.modifiers.getProjectileDistributionRegistry();
			}
		},
		ENTITY_LIVING_BASE_DAMAGE(AppliesTo.MOB_DAMAGE) {		
			@Override
			IDDDModifierRegistry<?, ?, ? extends IDDDCapModifier<?>> getRegistry() {
				return DDDRegistries.modifiers.getMobDamageDistributionRegistry();
			}
		},
		ENTITY_LIVING_BASE_RESISTANCE(AppliesTo.MOB_RESISTANCES) {
			@Override
			IDDDModifierRegistry<?, ?, ? extends IDDDCapModifier<?>> getRegistry() {
				return DDDRegistries.modifiers.getEntityResistancesRegistry();
			}
		};
		
		private final AppliesTo appliesTo;
		
		ModifierType(AppliesTo appliesTo) {
			this.appliesTo = appliesTo;
		}
		
		AppliesTo appliesTo() {
			return this.appliesTo;
		}
		
		abstract IDDDModifierRegistries.IDDDModifierRegistry<?, ?, ? extends IDDDCapModifier<?>> getRegistry();
	}
	
	private static final Map<ModifierType, Collection<String>> USED_NAMES = Maps.newHashMap();
	private static final Map<ModifierType, Collection<CoTDDDModifierBuilder<?, ?>>> BUILDERS = Maps.newHashMap();
	protected static final Predicate<String> IS_NOT_REGISTERED = (s) -> DDDRegistries.damageTypes.get(DDDDamageType.addDDDPrefixIfNeeded(s)) == null;
	
	static {
		for(ModifierType type : ModifierType.values()) {
			USED_NAMES.put(type, Lists.newArrayList());
			BUILDERS.put(type, Lists.newArrayList());
		}	
	}
	private final DDDBaseMap<Float> map = new DDDBaseMap<Float>(() -> 0.0f);
	private final Map<String, Float> stringMap = Maps.newHashMap();
	protected final ModifierType type;
	private boolean wasBuilt = false;
	
	public CoTDDDModifierBuilder(ModifierType type, String name) {
		this.type = type;
		this.name = name;
	}

	@ZenClass("mods.ddd.modifiers.IsModifierApplicableForEntity")
	@ZenRegister
	public interface IsModifierApplicableForEntity extends Predicate<IEntity> {
		@ZenMethod
		boolean handle(IEntity entity);
		
		@Override
		default boolean test(IEntity t) {
			return this.handle(t);
		}
	}
	
	@ZenClass("mods.ddd.modifiers.IsModifierApplicableForEntityLivingBase")
	@ZenRegister
	public interface IsModifierApplicableForEntityLivingBase extends Predicate<IEntityLivingBase> {
		@ZenMethod
		boolean handle(IEntityLivingBase entityLivingBase);
		
		@Override
		default boolean test(IEntityLivingBase t) {
			return this.handle(t);
		}
	}
	
	@ZenClass("mods.ddd.modifiers.IsModifierApplicableForItemStack")
	@ZenRegister
	public interface IsModifierApplicableForItemStack extends Predicate<IItemStack> {
		@ZenMethod
		boolean handle(IItemStack itemStack);
		
		@Override
		default boolean test(IItemStack t) {
			return this.handle(t);
		}
	}
	
	@ZenProperty
	public boolean shouldReallocate = false;
	
	@ZenProperty
	public int priority = 0;
	
	@ZenProperty
	public String name;
	
	@ZenMethod
	public void setMod(ICTDDDDamageType type, float mod) {
		this.map.put(type.asDDDDamageType(), mod);
	}
	
	@ZenMethod
	public void setMod(String type, float mod) {
		this.stringMap.put(DDDDamageType.removeDDDPrefixIfPresent(type), mod);
	}
	
	@ZenMethod
	public void build() {
		if(this.wasBuilt) {
			return;
		}
		if(this.name == null) {
			throw new ZenRuntimeException("Modifier name can not be null!");
		}
		else if (USED_NAMES.get(this.type).contains(this.name)) {
			throw new ZenRuntimeException(String.format("%s is already used by a modifier in another script!",  this.name));
		}
		if(this.getPredicate() == null) {
			throw new ZenRuntimeException(String.format("No isModifierApplicable function defined for %s!", this.name));
		}
		BUILDERS.get(this.type).add(this.copy());
		USED_NAMES.get(this.type).add(this.name);
		this.wasBuilt = true;
	}
	
	private final CoTDDDModifierBuilder<T, U> copy() {
		CoTDDDModifierBuilder<T, U> builder = this.copySpecific();
		builder.map.putAll(this.map);
		builder.stringMap.putAll(this.stringMap);
		builder.priority = this.priority;
		builder.shouldReallocate = this.shouldReallocate;
		return builder;
	}
	
	private final IDDDCapModifier<U> makeModifier() {
		IDDDCapModifier<U> mod = this.makeModifierSpecific();
		mod.putAll(this.map);
		return mod;
	}

	protected abstract IDDDCapModifier<U> makeModifierSpecific();
	
	protected abstract CoTDDDModifierBuilder<T, U> copySpecific();
	
	
	protected abstract Predicate<T> getPredicate();
	
	public static void registerMods() {
		BUILDERS.forEach((type, mods) -> {
			mods.forEach((b) -> {
				if(type.getRegistry().get(b.name) != null) {
					throw new ZenRuntimeException(String.format("%s is a modifier name already in use! Original registration source: %s", b.name, type.getRegistry().get(b.name).getCreationSourceString()));
				}
				//@formatter:off
				Optional<RuntimeException> e = b.stringMap.keySet().stream()
						.filter(IS_NOT_REGISTERED)
						.reduce((s1, s2) -> s1.concat("\n").concat(s2))
						.map((s) -> new RuntimeException(String.format("Unregistered damage type(s) used for %s: %s", b.name, s)));
				//@formatter:on
				if(e.isPresent()) {
					throw e.get();
				}
				b.stringMap.forEach((s, f) -> b.map.put(DDDRegistries.damageTypes.get(DDDDamageType.addDDDPrefixIfNeeded(s)),  f));
				DDDRegistries.modifiers.register(b.makeModifier());
			});
		});
	}
}
