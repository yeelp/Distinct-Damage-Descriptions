package yeelp.distinctdamagedescriptions.integration.tic.conarm.capability;

import java.util.Collection;
import java.util.Map;

import c4.conarm.lib.materials.ArmorMaterialType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.DDDCapabilityBase;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.ArmorDistribution;
import yeelp.distinctdamagedescriptions.integration.tic.TiCConfigurations;
import yeelp.distinctdamagedescriptions.integration.tic.TiCUtil;
import yeelp.distinctdamagedescriptions.util.DDDBaseMap;

public class ConarmArmorDistribution extends ArmorDistribution {

	@CapabilityInject(ConarmArmorDistribution.class)
	public static Capability<ConarmArmorDistribution> cap;

	private Collection<String> cachedMats;

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == cap;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return this.hasCapability(capability, facing) ? cap.cast(this) : null;
	}

	@Override
	public IArmorDistribution update(ItemStack owner) {
		Collection<String> mats = TiCUtil.getKeyMaterialIdentifiers(owner, ArmorMaterialType.PLATES);
		if(this.cachedMats == null || !this.cachedMats.containsAll(mats)) {
			Map<DDDDamageType, Float> map = new DDDBaseMap<Float>(() -> 0.0f);
			mats.forEach((mat) -> {
				IArmorDistribution dist = TiCConfigurations.armorMaterialDist.getOrFallbackToDefault(mat);
				dist.getCategories().forEach((t) -> map.merge(t, dist.getWeight(t) / mats.size(), Float::sum));
			});
			TiCUtil.getBaseDist(owner).forEach((t, f) -> map.merge(t, f, Float::sum));
			this.setNewWeights(map);
			this.cachedMats = mats;
		}
		return super.update(owner);
	}

	public static void register() {
		DDDCapabilityBase.register(ConarmArmorDistribution.class, NBTTagList.class, ConarmArmorDistribution::new);
	}

	@CapabilityInject(ConarmArmorDistribution.class)
	private static void onRegister(Capability<ConarmArmorDistribution> cap) {
		DDDAPI.mutator.registerItemCap(IArmorDistribution.class, cap);
	}
}
