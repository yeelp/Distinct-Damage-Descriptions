package yeelp.distinctdamagedescriptions.integration.lycanites.capability;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.lycanitesmobs.core.item.equipment.ItemEquipment;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.DDDUpdatableCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.integration.capability.ModUpdatingDamageDistribution;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public final class LycanitesEquipmentDistribution extends ModUpdatingDamageDistribution {

	private static final Set<String> acceptableParts = ImmutableSet.of("blade", "axe", "pike", "jewel");
	private static final Predicate<ItemEquipmentPart> filter = Predicates.and(Objects::nonNull, Predicates.compose(acceptableParts::contains, (i) -> i.slotType));
	private List<ItemEquipmentPart> cachedUsefulParts;

	@CapabilityInject(LycanitesEquipmentDistribution.class)
	public static Capability<LycanitesEquipmentDistribution> cap;

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == cap;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return this.hasCapability(capability, facing) ? cap.cast(this) : null;
	}

	@Override
	public Optional<DDDBaseMap<Float>> getUpdatedWeights(ItemStack owner) {
		ItemEquipment equipment = (ItemEquipment) owner.getItem();
		List<ItemEquipmentPart> parts = equipment.getEquipmentPartStacks(owner).stream().map(equipment::getEquipmentPart).filter(filter).collect(Collectors.toList());
		if(this.cachedUsefulParts == null || !(parts.size() == this.cachedUsefulParts.size() && this.cachedUsefulParts.containsAll(parts))) {
			DDDBaseMap<Float> map = new DDDBaseMap<Float>(() -> 0.0f);
			List<IDamageDistribution> dists = parts.stream().map(LycanitesEquipmentDistribution::retrieveFromConfig).collect(Collectors.toList());
			if(!dists.isEmpty()) {
				dists.forEach((d) -> d.getCategories().forEach((type) -> map.merge(type, d.getWeight(type) / dists.size(), Float::sum)));
				this.cachedUsefulParts = parts;
				return Optional.of(map);
			}
		}
		return Optional.empty();
	}

	public static void register() {
		DDDUpdatableCapabilityBase.register(LycanitesEquipmentDistribution.class, LycanitesEquipmentDistribution::new);
	}

	@CapabilityInject(LycanitesEquipmentDistribution.class)
	private static void onRegister(Capability<LycanitesEquipmentDistribution> cap) {
		DDDAPI.mutator.registerItemCap(IDamageDistribution.class, cap);
	}
	
	private static IDamageDistribution retrieveFromConfig(ItemEquipmentPart part) {
		return YResources.getRegistryString(part).map(DDDConfigurations.items::getOrFallbackToDefault).orElse(DDDConfigurations.items.getDefaultValue());
	}
	
	@Override
	protected Optional<DDDBaseMap<Float>> getUpdatedWeights(EntityLivingBase owner) {
		return Optional.empty();
	}
	
	@Override
	protected Optional<DDDBaseMap<Float>> getUpdatedWeights(IProjectile owner) {
		return Optional.empty();
	}

}
