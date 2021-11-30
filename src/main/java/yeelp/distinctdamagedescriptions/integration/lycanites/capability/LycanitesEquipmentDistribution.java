package yeelp.distinctdamagedescriptions.integration.lycanites.capability;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.base.Functions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.lycanitesmobs.core.item.equipment.ItemEquipment;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.util.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public final class LycanitesEquipmentDistribution extends DamageDistribution {

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
	public IDamageDistribution update(ItemStack owner) {
		ItemEquipment equipment = (ItemEquipment) owner.getItem();
		Map<DDDDamageType, Float> map = new DDDBaseMap<Float>(() -> 0.0f);
		List<ItemEquipmentPart> parts = equipment.getEquipmentPartStacks(owner).stream().map(equipment::getEquipmentPart).filter(filter).collect(Collectors.toList());
		if(this.cachedUsefulParts == null || !(parts.size() == this.cachedUsefulParts.size() && this.cachedUsefulParts.containsAll(parts))) {
			List<IDamageDistribution> dists = parts.stream().map(Functions.<ItemEquipmentPart, String, IDamageDistribution>compose(DDDConfigurations.items::getOrFallbackToDefault, YResources::getRegistryString)).collect(Collectors.toList());
			if(!dists.isEmpty()) {
				dists.forEach((d) -> d.getCategories().forEach((type) -> map.merge(type, d.getWeight(type)/dists.size(), Float::sum)));
				this.setNewWeights(map);
				this.cachedUsefulParts = parts;
			}
		}
		return super.update(owner);
	}
	
	public static void register() {
		DDDCapabilityBase.register(LycanitesEquipmentDistribution.class, NBTTagList.class, LycanitesEquipmentDistribution::new);
	}
	
	@CapabilityInject(LycanitesEquipmentDistribution.class)
	private static void onRegister(Capability<LycanitesEquipmentDistribution> cap) {
		DDDAPI.mutator.registerItemCap(IDamageDistribution.class, cap);
	}

}
