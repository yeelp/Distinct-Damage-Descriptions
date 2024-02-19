package yeelp.distinctdamagedescriptions.integration.tetra.capability;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import se.mickelus.tetra.NBTHelper;
import se.mickelus.tetra.items.duplex_tool.ItemDuplexToolModular;
import se.mickelus.tetra.items.sword.ItemSwordModular;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.integration.capability.AbstractBiasedDamageDistribution;
import yeelp.distinctdamagedescriptions.integration.tetra.TetraConfigurations;
import yeelp.distinctdamagedescriptions.integration.tetra.TetraNBT;
import yeelp.distinctdamagedescriptions.integration.util.DistributionBias;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;

public final class TetraToolDistribution extends AbstractBiasedDamageDistribution {

	@CapabilityInject(TetraToolDistribution.class)
	public static Capability<TetraToolDistribution> cap;

	public TetraToolDistribution() {
		super();
	}

	public TetraToolDistribution(Map<DDDDamageType, Float> weights) {
		super(weights);
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
	protected Set<DDDBaseMap<Float>> computeBiasedMaps(ItemStack owner) {
		NBTTagCompound tag = NBTHelper.getTag(owner);
		if(owner.getItem() instanceof ItemSwordModular) {
			String bladeType = tag.getString(TetraNBT.SWORD_ROOT);
			DistributionBias base = TetraConfigurations.toolBiasResistance.getOrFallbackToDefault(bladeType);
			return getMaterial(tag, bladeType).flatMap((key) -> TetraConfigurations.toolMaterialBias.getSafe(key).flatMap((b) -> b.getBiasedDistributionMap(base.getPreferredMapCopy(), base.getBias()).map(ImmutableSet::of))).orElse(ImmutableSet.of(base.getPreferredMapCopy()));
		}
		else if(owner.getItem() instanceof ItemDuplexToolModular) {
			return ImmutableSet.of("left", "right").stream().map((s) -> {
				String suffix = "_".concat(s);
				String type = tag.getString(TetraNBT.DUPLEX_ROOT + suffix);
				if(type.equals("duplex/butt" + suffix)) {
					return null;
				}
				DistributionBias base = TetraConfigurations.toolBiasResistance.getOrFallbackToDefault(type.replace(suffix, "").trim());
				return getMaterial(tag, type).flatMap((key) -> TetraConfigurations.toolMaterialBias.getSafe(key).flatMap((b) -> b.getBiasedDistributionMap(base.getPreferredMapCopy(), base.getBias()))).orElse(base.getPreferredMapCopy());
			}).filter(Predicates.notNull()).collect(Collectors.toSet());
		}
		return ImmutableSet.of();
	}

	private static Optional<String> getMaterial(NBTTagCompound tag, String root) {
		String[] arr = tag.getString(root.concat(TetraNBT.MATERIAL_SUFFIX)).split("/");
		return Optional.ofNullable(arr.length == 2 ? arr[1] : null);
	}

	public static void register() {
		DDDCapabilityBase.register(TetraToolDistribution.class, NBTTagList.class, TetraToolDistribution::new);
	}

	@CapabilityInject(TetraToolDistribution.class)
	public static void onRegister(Capability<TetraToolDistribution> cap) {
		DDDAPI.mutator.registerItemCap(IDamageDistribution.class, cap);
	}
}
