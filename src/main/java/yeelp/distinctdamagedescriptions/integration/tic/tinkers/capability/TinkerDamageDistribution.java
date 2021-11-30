package yeelp.distinctdamagedescriptions.integration.tic.tinkers.capability;

import java.util.Collection;
import java.util.Map;
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
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.config.TiCConfigurations;
import yeelp.distinctdamagedescriptions.integration.tic.TiCUtil;
import yeelp.distinctdamagedescriptions.util.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public class TinkerDamageDistribution extends DamageDistribution {

	@CapabilityInject(TinkerDamageDistribution.class)
	public static Capability<TinkerDamageDistribution> cap = null;
	
	private Collection<String> cachedMats;
	
	public TinkerDamageDistribution() {
		super();
	}
	
	public TinkerDamageDistribution(IDamageDistribution base) {
		super(base.getCategories().stream().collect(DDDBaseMap.typesToDDDBaseMap(() -> 0.0f, base::getWeight)));
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
	public IDamageDistribution update(ItemStack owner) {
		float biasResistance = TiCConfigurations.toolBiasResistance.getOrFallbackToDefault(YResources.getRegistryString(owner));
		Collection<String> mats = TiCUtil.getKeyMaterialIdentifiers(owner, MaterialTypes.HEAD);
		if(this.cachedMats == null || !this.cachedMats.containsAll(mats)) {
			Set<Map<DDDDamageType, Float>> maps = mats.stream().map(Functions.compose((b) -> b.getBiasedDistributionMap(TiCUtil.getBaseDist(owner), biasResistance), TiCConfigurations.toolMaterialBias::getOrFallbackToDefault)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());			
			int size = maps.size();
			if(size != 0) {
				DDDBaseMap<Float> map = new DDDBaseMap<Float>(() -> 0.0f);
				maps.forEach((m) -> m.forEach((k, v) -> map.merge(k, v/size, Float::sum)));
				this.setNewWeights(map);				
				this.cachedMats = mats;		
			}
		}
		return super.update(owner);
	}

	@Override
	public IDamageDistribution update(IProjectile owner) {
		if(owner instanceof EntityProjectileBase) {
			DDDAPI.accessor.getDamageDistribution(((EntityProjectileBase) owner).tinkerProjectile.getItemStack()).ifPresent((dist) -> this.setNewWeights(dist.getCategories().stream().collect(DDDBaseMap.typesToDDDBaseMap(() -> 0.0f, dist::getWeight))));
		}
		return super.update(owner);
	}
	
	public static void register() {
		DDDCapabilityBase.register(TinkerDamageDistribution.class, NBTTagList.class, TinkerDamageDistribution::new);
	}
	
	@CapabilityInject(TinkerDamageDistribution.class)
	private static void onRegister(Capability<TinkerDamageDistribution> cap) {
		ImmutableList.<BiConsumer<Class<IDamageDistribution>, Capability<TinkerDamageDistribution>>>of(DDDAPI.mutator::registerItemCap, DDDAPI.mutator::registerProjectileCap).forEach((f) -> f.accept(IDamageDistribution.class, cap));
	}
}
