package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Iterator;

import c4.conarm.lib.materials.ArmorMaterialType;
import c4.conarm.lib.tinkering.TinkersArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.TiCConfigurations;
import yeelp.distinctdamagedescriptions.util.DistributionBias;

public class ConarmArmorDistribution extends AbstractTinkersDistribution<IArmorDistribution> {

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
	protected IDDDConfiguration<DistributionBias> getConfiguration() {
		return TiCConfigurations.armorMaterialBias;
	}

	@Override
	protected IArmorDistribution getDistributionCapabilityOnStack(ItemStack stack) {
		return DDDAPI.accessor.getArmorResistances(stack);
	}

}
