package yeelp.distinctdamagedescriptions.api.impl;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsAccessor;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsMutator;
import yeelp.distinctdamagedescriptions.capability.*;
import yeelp.distinctdamagedescriptions.capability.impl.*;
import yeelp.distinctdamagedescriptions.config.ModConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

public enum DistinctDamageDescriptionsAPIImpl implements IDistinctDamageDescriptionsAccessor, IDistinctDamageDescriptionsMutator {
	INSTANCE;

	@SuppressWarnings("UnstableApiUsage")
    DistinctDamageDescriptionsAPIImpl() {
		DDDAPI.accessor = this;
		DDDAPI.mutator = this;
		for(String s : new String[] {
				ITEM,
				ENTITY,
				PROJECTILE}) {
			Multimap<Class<? extends DDDCapabilityBase<? extends NBTBase>>, Capability<? extends DDDCapabilityBase<? extends NBTBase>>> map = MultimapBuilder.hashKeys().arrayListValues().build();
			this.caps.put(s, map);
		}
	}

	private static final String ITEM = "item", ENTITY = "entity", PROJECTILE = "proj";
	private final Map<String, Multimap<Class<? extends DDDCapabilityBase<? extends NBTBase>>, Capability<? extends DDDCapabilityBase<? extends NBTBase>>>> caps = Maps.newHashMap();

	/*------------
	 * ACCESSOR *
	 -----------*/

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(@Nullable ItemStack stack) {
		return Optional.of(this.findCaps(ITEM, stack, IDamageDistribution.class, DamageDistribution.cap).map((c) -> ((IDamageDistribution) c).update(stack)).orElse(ModConfig.dmg.defaultItemDamage));
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(@Nullable EntityLivingBase entity) {
		return Optional.of(this.findCaps(ENTITY, entity, IDamageDistribution.class, DamageDistribution.cap).map((c) -> ((IDamageDistribution) c).update(entity)).orElse(ModConfig.dmg.defaultMobDamage));
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(@Nullable IProjectile projectile) {
		if(projectile instanceof Entity) {
			return Optional.of(this.findCaps(PROJECTILE, (Entity) projectile, IDamageDistribution.class, DamageDistribution.cap).map((c) -> ((IDamageDistribution) c).update(projectile)).orElse(ModConfig.dmg.defaultProjectileDamage));
		}
		return Optional.empty();
	}

	@Override
	public Optional<IArmorDistribution> getArmorResistances(@Nullable ItemStack stack) {
		if(!ModConfig.resist.enableArmorCalcs) {
			return Optional.empty();
		}
		return Optional.of(this.findCaps(ITEM, stack, IArmorDistribution.class, ArmorDistribution.cap).map((c) -> ((IArmorDistribution) c).update(stack)).orElse(ModConfig.resist.defaultArmorResists));
	}

	@Override
	public Optional<IMobResistances> getMobResistances(@Nullable EntityLivingBase entity) {
		return Optional.of(getDDDCap(MobResistances.cap, entity).map((c) -> c.update(entity)).orElse(DefaultResistances.getInstance()));
	}

	@Override
	public Optional<IMobCreatureType> getMobCreatureType(@Nullable EntityLivingBase entity) {
		return getDDDCap(MobCreatureType.cap, entity);
	}

	@Override
	public Optional<ShieldDistribution> getShieldDistribution(@Nullable ItemStack stack) {
		if(!ModConfig.resist.enableShieldCalcs) {
			return Optional.empty();
		}
		return Optional.of(this.findCaps(ITEM, stack, ShieldDistribution.class, ShieldDistribution.cap).map((c) -> ((ShieldDistribution) c).update(stack)).orElse(ModConfig.resist.defaultShieldResists.getShieldDistribution()));
	}

	@Override
	public Optional<IDDDCombatTracker> getDDDCombatTracker(EntityLivingBase entity) {
		return getDDDCap(DDDCombatTracker.cap, entity);
	}

	private Optional<? extends DDDCapabilityBase<? extends NBTBase>> findCaps(String type, ICapabilityProvider thing, Class<? extends DDDCapabilityBase<? extends NBTBase>> cap, Capability<? extends DDDCapabilityBase<? extends NBTBase>> fallback) {
		return this.caps.get(type).get(cap).stream().filter((c) -> thing.hasCapability(c, null)).findFirst().<Optional<? extends DDDCapabilityBase<? extends NBTBase>>>map((c) -> getDDDCap(c, thing)).orElseGet(() -> getDDDCap(fallback, thing));
	}

	private static <Cap extends DDDCapabilityBase<? extends NBTBase>> Optional<Cap> getDDDCap(Capability<Cap> cap, ICapabilityProvider thing) {
		if(thing == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(thing.getCapability(cap, null));
	}

	/*----------
	 * MUTATOR *
	 ----------*/

	@Override
	public <T extends DDDCapabilityBase<? extends NBTBase>> void registerItemCap(@Nonnull Class<T> clazz, @Nonnull Capability<? extends T> cap) {
		this.registerCap(ITEM, clazz, cap);
	}

	@Override
	public <T extends DDDCapabilityBase<? extends NBTBase>> void registerProjectileCap(@Nonnull Class<T> clazz, @Nonnull Capability<? extends T> cap) {
		this.registerCap(PROJECTILE, clazz, cap);
	}

	@Override
	public <T extends DDDCapabilityBase<? extends NBTBase>> void registerEntityCap(@Nonnull Class<T> clazz, @Nonnull Capability<? extends T> cap) {
		this.registerCap(ENTITY, clazz, cap);
	}

	private <T extends DDDCapabilityBase<? extends NBTBase>> void registerCap(@Nonnull String key, @Nonnull Class<T> clazz, @Nonnull Capability<? extends T> cap) {
		this.caps.get(key).put(clazz, cap);
	}
}