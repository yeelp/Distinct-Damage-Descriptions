package yeelp.distinctdamagedescriptions.capability.distributors;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

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
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.capability.distributors.DamageDistributionCapabilityDistributor.ForEntity;
import yeelp.distinctdamagedescriptions.capability.distributors.DamageDistributionCapabilityDistributor.ForItem;
import yeelp.distinctdamagedescriptions.capability.distributors.DamageDistributionCapabilityDistributor.ForProjectile;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public final class DDDCapabilityDistributors {
	private DDDCapabilityDistributors() {
		throw new RuntimeException("Class can't be instantiated!");
	}

	private static final List<AbstractCapabilityDistributorGeneratable<ItemStack, ?, ? extends IDistribution>> BASE_ITEM_CAPS = ImmutableList.of(ForItem.getInstance(), ShieldDistributionCapabilityDistributor.getInstance(), ArmorDistributionCapabilityDistributor.getInstance());
	private static final List<AbstractCapabilityDistributorGeneratable<IProjectile, ?, ? extends IDistribution>> BASE_PROJ_CAPS = ImmutableList.of(ForProjectile.getInstance());
	private static final List<AbstractCapabilityDistributorGeneratable<EntityLivingBase, ?, ? extends DDDCapabilityBase<? extends NBTBase>>> BASE_MOB_CAPS = ImmutableList.of(ForEntity.getInstance(), MobResistancesCapabilityDistributor.getInstance());
	private static final List<AbstractCapabilityDistributor<ItemStack, ?, ? extends IDistribution>> itemCaps = Lists.newArrayList();
	private static final List<AbstractCapabilityDistributor<IProjectile, ?, ? extends IDistribution>> projCaps = Lists.newArrayList();
	private static final List<AbstractCapabilityDistributor<EntityLivingBase, ?, ? extends DDDCapabilityBase<? extends NBTBase>>> mobCaps = Lists.newArrayList(CombatTrackerDistributor.getInstance());

	public static void addItemCap(AbstractCapabilityDistributor<ItemStack, ?, ? extends IDistribution> distributor) {
		itemCaps.add(distributor);
	}

	public static void addProjCap(AbstractCapabilityDistributor<IProjectile, ?, ? extends IDistribution> distributor) {
		projCaps.add(distributor);
	}

	public static void addMobCap(AbstractCapabilityDistributor<EntityLivingBase, ?, ? extends DDDCapabilityBase<? extends NBTBase>> distributor) {
		mobCaps.add(distributor);
	}

	public static Optional<Map<ResourceLocation, ? extends DDDCapabilityBase<? extends NBTBase>>> getCapabilities(ItemStack stack) {
		return YResources.getRegistryStringWithMetadata(stack).map((s) -> getAllCaps(stack, s, BASE_ITEM_CAPS, itemCaps));
	}

	public static Optional<Map<ResourceLocation, ? extends DDDCapabilityBase<? extends NBTBase>>> getCapabilities(IProjectile projectile) {
		return YResources.getEntityIDString((Entity) projectile).map((s) -> getAllCaps(projectile, s, BASE_PROJ_CAPS, projCaps));
	}

	public static Optional<Map<ResourceLocation, ? extends DDDCapabilityBase<? extends NBTBase>>> getCapabilities(EntityLivingBase entity) {
		return YResources.getEntityIDString(entity).map((s) -> getAllCaps(entity, s, BASE_MOB_CAPS, mobCaps));
	}

	public static Map<ResourceLocation, DDDCapabilityBase<? extends NBTBase>> getPlayerCapabilities(EntityPlayer player) {
		return getAllCaps(player, "player", BASE_MOB_CAPS, mobCaps);
	}

	private static <T, V extends DDDCapabilityBase<? extends NBTBase>> void getCaps(T t, String key, AbstractCapabilityDistributor<T, ?, V> distributor, BiConsumer<ResourceLocation, V> action) {
		if(distributor.isApplicable(t)) {
			Tuple<ResourceLocation, V> result = distributor.getResourceLocationAndCapability(t, key);
			action.accept(result.getFirst(), result.getSecond());
		}
	}

	private static <T, V extends DDDCapabilityBase<? extends NBTBase>> Map<ResourceLocation, V> getAllCaps(T t, String key, List<AbstractCapabilityDistributorGeneratable<T, ?, ? extends V>> baseCaps, List<AbstractCapabilityDistributor<T, ?, ? extends V>> extraCaps) {
		Map<ResourceLocation, V> caps = Maps.newHashMap();
		baseCaps.forEach((d) -> getCaps(t, key, d, caps::put));
		extraCaps.forEach((d) -> getCaps(t, key, d, caps::put));
		return caps;
	}
}
