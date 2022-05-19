package yeelp.distinctdamagedescriptions.integration.tic.tinkers.capability;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;

import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.integration.capability.AbstractBiasedDamageDistribution;
import yeelp.distinctdamagedescriptions.integration.tic.TiCConfigurations;
import yeelp.distinctdamagedescriptions.integration.tic.TiCUtil;
import yeelp.distinctdamagedescriptions.util.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public class TinkerDamageDistribution extends AbstractBiasedDamageDistribution {

	@CapabilityInject(TinkerDamageDistribution.class)
	public static Capability<TinkerDamageDistribution> cap = null;

	private Collection<String> cachedKeys;

	public TinkerDamageDistribution() {
		super();
	}

	public TinkerDamageDistribution(IDamageDistribution base) {
		super(base);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == cap;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return this.hasCapability(capability, facing) ? cap.cast(this) : null;
	}

	@Override
	public IDamageDistribution update(IProjectile owner) {
		if(owner instanceof EntityProjectileBase) {
			EntityProjectileBase epb = ((EntityProjectileBase) owner);
			if(epb.tinkerProjectile != null) {
				DDDAPI.accessor.getDamageDistribution(epb.tinkerProjectile.getItemStack()).ifPresent((dist) -> this.setNewWeights(dist.getCategories().stream().collect(DDDBaseMap.typesToDDDBaseMap(() -> 0.0f, dist::getWeight))));
			}
		}
		return super.update(owner);
	}

	@Override
	protected Set<DDDBaseMap<Float>> computeBiasedMaps(ItemStack owner) {
		Collection<String> mats = TiCUtil.getKeyMaterialIdentifiers(owner, MaterialTypes.HEAD);
		if(this.cachedKeys == null || !this.cachedKeys.containsAll(mats)) {
			float biasResist = TiCConfigurations.toolBiasResistance.getOrFallbackToDefault(YResources.getRegistryString(owner));
			DDDBaseMap<Float> base = TiCUtil.getBaseDist(owner);
			Set<DDDBaseMap<Float>> dists = mats.stream().map(Functions.compose((b) -> b.getBiasedDistributionMap(base, biasResist), TiCConfigurations.toolMaterialBias::getOrFallbackToDefault)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());
			if(dists.size() > 0) {
				this.cachedKeys = mats;
				return dists;
			}
		}
		return Collections.emptySet();
	}

	public static void register() {
		DDDCapabilityBase.register(TinkerDamageDistribution.class, NBTTagList.class, TinkerDamageDistribution::new);
	}

	@CapabilityInject(TinkerDamageDistribution.class)
	private static void onRegister(Capability<TinkerDamageDistribution> cap) {
		ImmutableList.<BiConsumer<Class<IDamageDistribution>, Capability<TinkerDamageDistribution>>>of(DDDAPI.mutator::registerItemCap, DDDAPI.mutator::registerProjectileCap).forEach((f) -> f.accept(IDamageDistribution.class, cap));
	}
}
