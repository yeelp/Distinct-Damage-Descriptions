package yeelp.distinctdamagedescriptions.capability.distributors;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.integration.capability.IDistributionRequiresUpdate;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public final class DDDCapabilityDistributors {
	private DDDCapabilityDistributors() {
		throw new RuntimeException("Class can't be instantiated!");
	}
	private static final Iterable<AbstractCapabilityDistributor<ItemStack, ?, ? extends DDDCapabilityBase<? extends NBTBase>>> baseItemCaps = ImmutableList.of(DamageDistributionCapabilityDistributor.ForItem.getInstance(), ArmorDistributionCapabilityDistributor.getInstance(), ShieldDistributionCapabilityDistributor.getInstance());
	private static final Iterable<AbstractCapabilityDistributor<EntityLivingBase, ?, ? extends DDDCapabilityBase<? extends NBTBase>>> baseMobCaps = ImmutableList.of(DamageDistributionCapabilityDistributor.ForEntity.getInstance(), MobResistancesCapabilityDistributor.getInstance());
	private static final Iterable<AbstractCapabilityDistributor<IProjectile, ?, ? extends DDDCapabilityBase<? extends NBTBase>>> baseProjectileCaps = ImmutableList.of(DamageDistributionCapabilityDistributor.ForProjectile.getInstance());
	
	private static final List<AbstractCapabilityDistributor<ItemStack, ?, ? extends DDDCapabilityBase<? extends NBTBase>>> modItemCapOverride = Lists.newArrayList();
	private static final List<AbstractCapabilityDistributor<EntityLivingBase, ?, ? extends DDDCapabilityBase<? extends NBTBase>>> modMobCapOverride = Lists.newArrayList();
	private static final List<AbstractCapabilityDistributor<IProjectile, ?, ? extends DDDCapabilityBase<? extends NBTBase>>> modProjectileCapOverride = Lists.newArrayList();
	
	private static final List<AbstractCapabilityDistributor<ItemStack, ?, ? extends IDistributionRequiresUpdate>> modItemCapUpdater = Lists.newArrayList();
	private static final List<AbstractCapabilityDistributor<EntityLivingBase, ?, ? extends IDistributionRequiresUpdate>> modMobCapUpdater = Lists.newArrayList();
	private static final List<AbstractCapabilityDistributor<IProjectile, ?, ? extends IDistributionRequiresUpdate>> modProjCapUpdater = Lists.newArrayList();
	
	public static void addItemCapUpdater(AbstractCapabilityDistributor<ItemStack, ?, ? extends IDistributionRequiresUpdate> distributor) {
		modItemCapUpdater.add(distributor);
	}
	
	public static void addMobCapUpdater(AbstractCapabilityDistributor<EntityLivingBase, ?, ? extends IDistributionRequiresUpdate> distributor) {
		modMobCapUpdater.add(distributor);
	}
	
	public static void addProjectileCapUpdater(AbstractCapabilityDistributor<IProjectile, ?, ? extends IDistributionRequiresUpdate> distributor) {
		modProjCapUpdater.add(distributor);
	}
	
	public static void addItemDistributor(AbstractCapabilityDistributor<ItemStack, ?, ? extends DDDCapabilityBase<? extends NBTBase>> distributor) {
		modItemCapOverride.add(distributor);
	}
	
	public static void addMobDistributor(AbstractCapabilityDistributor<EntityLivingBase, ?, ? extends DDDCapabilityBase<? extends NBTBase>> distributor) {
		modMobCapOverride.add(distributor);
	}
	
	public static void addProjectileDistributor(AbstractCapabilityDistributor<IProjectile, ?, IDamageDistribution> distributor) {
		modProjectileCapOverride.add(distributor);
	}
	
	public static Map<ResourceLocation, DDDCapabilityBase<? extends NBTBase>> getCapabilities(ItemStack stack) {
		return getCapabilities(stack, YResources.getRegistryString(stack), baseItemCaps, modItemCapUpdater, modItemCapOverride);
	}
	
	public static Optional<Map<ResourceLocation, DDDCapabilityBase<? extends NBTBase>>> getCapabilities(IProjectile projectile) {
		return YResources.getEntityIDString((Entity) projectile).map((s) -> getCapabilities(projectile, s, baseProjectileCaps, modProjCapUpdater, modProjectileCapOverride));
	}
	
	public static Optional<Map<ResourceLocation, DDDCapabilityBase<? extends NBTBase>>> getCapabilities(EntityLivingBase entity) {
		return YResources.getEntityIDString(entity).map((s) -> getCapabilities(entity, s, baseMobCaps, modMobCapUpdater, modMobCapOverride));
	}
	
	public static Map<ResourceLocation, DDDCapabilityBase<? extends NBTBase>> getPlayerCapabilities(EntityPlayer player) {
		return getCapabilities(player, "player", baseMobCaps, modMobCapUpdater, modMobCapOverride);
	}
	
	private static <T, V> Map<ResourceLocation, V> getFromIterable(T t, String key, Iterable<AbstractCapabilityDistributor<T, ?, ? extends V>> iterable) {
		Map<ResourceLocation, V> map = Maps.newHashMap();
		iterable.forEach((d) -> {
			if(d.isApplicable(t)) {
				Tuple<ResourceLocation, ? extends V> result = d.getResourceLocationAndCapability(t, key);
				map.put(result.getFirst(), result.getSecond());
			}
		});
		return map;
	}
	
	private static <T> Map<ResourceLocation, DDDCapabilityBase<? extends NBTBase>> getCapabilities(T t, String key, Iterable<AbstractCapabilityDistributor<T, ?, ? extends DDDCapabilityBase<? extends NBTBase>>> baseIterable, Iterable<AbstractCapabilityDistributor<T, ?, ? extends IDistributionRequiresUpdate>> modIterable, Iterable<AbstractCapabilityDistributor<T, ?, ? extends DDDCapabilityBase<? extends NBTBase>>> modOverrideIterable) {
		Map<ResourceLocation, DDDCapabilityBase<? extends NBTBase>> map = getFromIterable(t, key, baseIterable);
		map.putAll(getFromIterable(t, key, modIterable));
		map.putAll(getFromIterable(t, key, modOverrideIterable));
		return map;
	}
}
