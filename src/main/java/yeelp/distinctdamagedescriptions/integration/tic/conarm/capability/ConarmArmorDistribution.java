package yeelp.distinctdamagedescriptions.integration.tic.conarm.capability;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import c4.conarm.lib.materials.ArmorMaterialType;
import c4.conarm.lib.tinkering.TinkersArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.TiCConfigurations;
import yeelp.distinctdamagedescriptions.integration.capability.IDistributionRequiresUpdate;
import yeelp.distinctdamagedescriptions.integration.tic.capability.AbstractTinkersDistribution;
import yeelp.distinctdamagedescriptions.util.DDDBaseMap;

public class ConarmArmorDistribution extends AbstractTinkersDistribution<IArmorDistribution, IArmorDistribution> {

	@CapabilityInject(ConarmArmorDistribution.class)
	public static Capability<ConarmArmorDistribution> cap;
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == cap;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return this.hasCapability(capability, facing) ? cap.cast(this) : null;
	}

	@Override
	protected String getPartType() {
		return ArmorMaterialType.PLATES;
	}

	@Override
	protected Iterator<PartMaterialType> getParts(ItemStack stack) {
		return ((TinkersArmor) stack.getItem()).getRequiredComponents().iterator();
	}

	@Override
	protected IDDDConfiguration<IArmorDistribution> getConfiguration() {
		return TiCConfigurations.armorMaterialDist;
	}

	@Override
	protected IArmorDistribution getDistributionCapabilityOnStack(ItemStack stack) {
		return DDDAPI.accessor.getArmorResistances(stack);
	}

	@Override
	protected Optional<Map<DDDDamageType, Float>> determineNewMap(ItemStack stack, Collection<String> mats, IDDDConfiguration<IArmorDistribution> config) {
		Map<DDDDamageType, Float> map = new DDDBaseMap<Float>(0.0f);
		mats.forEach((mat) -> {
			IArmorDistribution dist = config.getOrFallbackToDefault(mat);
			dist.getCategories().forEach((t) -> map.merge(t, dist.getWeight(t)/mats.size(), Float::sum));
		});
		this.baseDist.forEach((t, f) -> map.merge(t, f, Float::sum));
		return Optional.of(map);
	}
	
	public static void register() {
		IDistributionRequiresUpdate.register(ConarmArmorDistribution.class, ConarmArmorDistribution::new);
	}
	
	@CapabilityInject(ConarmArmorDistribution.class)
	public static void onRegister(Capability<ConarmArmorDistribution> cap) {
		IDistributionRequiresUpdate.PlayerHandler.allowCapabilityUpdates(cap);
	}
}
