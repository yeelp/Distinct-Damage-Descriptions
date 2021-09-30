package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.base.Functions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.lycanitesmobs.core.item.equipment.ItemEquipment;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public final class LycanitesEquipmentDistribution extends DistributionRequiresUpdate<IDamageDistribution> {
	
	private static final Set<String> acceptableParts = ImmutableSet.of("blade", "axe", "pike", "jewel");
	private static final Predicate<ItemEquipmentPart> filter = Predicates.and(Objects::nonNull, Predicates.compose(acceptableParts::contains, (i) -> i.slotType));
	
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
	protected IDamageDistribution getDistributionCapabilityOnStack(ItemStack stack) {
		return DDDAPI.accessor.getDamageDistribution(stack);
	}

	@Override
	protected Optional<Map<DDDDamageType, Float>> calculateNewMap(ItemStack stack) {
		ItemEquipment equipment = (ItemEquipment) stack.getItem();
		Map<DDDDamageType, Float> map = new HashMap<DDDDamageType, Float>();
		List<IDistribution> dists = equipment.getEquipmentPartStacks(stack).stream().map(equipment::getEquipmentPart).filter(filter).map(Functions.<ItemEquipmentPart, String, IDamageDistribution>compose(DDDConfigurations.items::getOrFallbackToDefault, YResources::getRegistryString).andThen(IDamageDistribution::copy)).collect(Collectors.toList());
		int size = dists.size();
		if(size == 0) {
			return Optional.empty();
		}
		for(IDistribution d : dists) {
			d.getCategories().forEach((type) -> map.merge(type, d.getWeight(type)/size, Float::sum));
		}
		return Optional.of(map);
	}

}
