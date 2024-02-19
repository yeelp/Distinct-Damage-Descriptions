package yeelp.distinctdamagedescriptions.api.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsAccessor;
import yeelp.distinctdamagedescriptions.api.IDistinctDamageDescriptionsMutator;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IMobCreatureType;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.ArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DefaultArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DefaultDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DefaultResistances;
import yeelp.distinctdamagedescriptions.capability.impl.DefaultShieldDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.MobCreatureType;
import yeelp.distinctdamagedescriptions.capability.impl.MobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.ResistMap;

public enum DistinctDamageDescriptionsAPIImpl implements IDistinctDamageDescriptionsAccessor, IDistinctDamageDescriptionsMutator {
	INSTANCE;

	private DistinctDamageDescriptionsAPIImpl() {
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

	/************
	 * ACCESSOR *
	 ************/

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(@Nullable ItemStack stack) {
		return Optional.of(this.findCaps(ITEM, stack, IDamageDistribution.class, DamageDistribution.cap).map((c) -> ((IDamageDistribution) c).update(stack)).orElse(DefaultDamageDistribution.getInstance()));
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(@Nullable EntityLivingBase entity) {
		return Optional.of(this.findCaps(ENTITY, entity, IDamageDistribution.class, DamageDistribution.cap).map((c) -> ((IDamageDistribution) c).update(entity)).orElse(DefaultDamageDistribution.getInstance()));
	}

	@Override
	public Optional<IDamageDistribution> getDamageDistribution(@Nullable IProjectile projectile) {
		if(projectile != null && projectile instanceof Entity) {
			return Optional.of(this.findCaps(PROJECTILE, (Entity) projectile, IDamageDistribution.class, DamageDistribution.cap).map((c) -> ((IDamageDistribution) c).update(projectile)).orElse(DefaultDamageDistribution.getInstance()));
		}
		return Optional.empty();
	}

	@Override
	public Optional<IArmorDistribution> getArmorResistances(@Nullable ItemStack stack) {
		return Optional.of(this.findCaps(ITEM, stack, IArmorDistribution.class, ArmorDistribution.cap).map((c) -> ((IArmorDistribution) c).update(stack)).orElse(DefaultArmorDistribution.getInstance()));
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
		return Optional.of(this.findCaps(ITEM, stack, ShieldDistribution.class, ShieldDistribution.cap).map((c) -> ((ShieldDistribution) c).update(stack)).orElse(DefaultShieldDistribution.getInstance()));
	}

	private Optional<? extends DDDCapabilityBase<? extends NBTBase>> findCaps(String type, ICapabilityProvider thing, Class<? extends DDDCapabilityBase<? extends NBTBase>> cap, Capability<? extends DDDCapabilityBase<? extends NBTBase>> fallback) {
		return this.caps.get(type).get(cap).stream().filter((c) -> thing.hasCapability(c, null)).findFirst().<Optional<? extends DDDCapabilityBase<? extends NBTBase>>>map((c) -> getDDDCap(c, thing)).orElseGet(() -> getDDDCap(fallback, thing));
	}

	private static <Cap extends DDDCapabilityBase<? extends NBTBase>> Optional<Cap> getDDDCap(Capability<Cap> cap, ICapabilityProvider thing) {
		if(thing == null) {
			return null;
		}
		return Optional.ofNullable(thing.getCapability(cap, null));
	}

	/***********
	 * MUTATOR *
	 ***********/
	@Override
	public void setPlayerResistances(EntityPlayer player, ResistMap newResists, Set<DDDDamageType> newImmunities, boolean adaptive, float adaptiveAmount) {
		IMobResistances mobResists = getMobResistances(player).get();
		for(Entry<DDDDamageType, Float> entry : newResists.entrySet()) {
			mobResists.setResistance(entry.getKey(), entry.getValue());
		}
		mobResists.clearImmunities();
		for(DDDDamageType type : newImmunities) {
			mobResists.setImmunity(type, true);
		}
		mobResists.setAdaptiveResistance(adaptive);
		mobResists.setAdaptiveAmount(adaptiveAmount);
		mobResists.sync(player);
	}

	@Override
	public boolean updateAdaptiveResistances(EntityLivingBase entity, DamageMap dmgMap) {
		IMobResistances resists = getMobResistances(entity).get();
		if(resists.hasAdaptiveResistance()) {
			boolean sync = resists.updateAdaptiveResistance(dmgMap);
			if(entity instanceof EntityPlayer) {
				resists.sync((EntityPlayer) entity);
			}
			return sync;
		}
		return false;
	}

	@Override
	public <T extends DDDCapabilityBase<? extends NBTBase>> void registerItemCap(Class<T> clazz, Capability<? extends T> cap) {
		this.registerCap(ITEM, clazz, cap);
	}

	@Override
	public <T extends DDDCapabilityBase<? extends NBTBase>> void registerProjectileCap(Class<T> clazz, Capability<? extends T> cap) {
		this.registerCap(PROJECTILE, clazz, cap);
	}

	@Override
	public <T extends DDDCapabilityBase<? extends NBTBase>> void registerEntityCap(Class<T> clazz, Capability<? extends T> cap) {
		this.registerCap(ENTITY, clazz, cap);
	}

	private final <T extends DDDCapabilityBase<? extends NBTBase>> void registerCap(String key, Class<T> clazz, Capability<? extends T> cap) {
		this.caps.get(key).put(clazz, cap);
	}
}