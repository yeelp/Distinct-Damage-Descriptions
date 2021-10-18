package yeelp.distinctdamagedescriptions.integration.tic;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.capability.distributors.DDDCapabilityDistributors;
import yeelp.distinctdamagedescriptions.capability.distributors.ModCompatCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.impl.AbstractTinkersDistribution;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipDistributor;

public abstract class DDDTiCIntegration implements IModIntegration {

	protected abstract DDDBookTransformer getBookTransformer();
	
	protected abstract Iterable<ModCompatCapabilityDistributor<ItemStack, ? extends AbstractTinkersDistribution<? extends IDistribution, ?>>> getItemDistributors();

	protected abstract void addOtherDistributors();
	
	protected abstract Iterable<IModCompatTooltipFormatter<ItemStack>> getFormatters();
	
	protected abstract void registerCapabilities();
	
	protected abstract boolean doSpecificInit(FMLInitializationEvent evt);
	
	@Override
	public final boolean preInit(FMLPreInitializationEvent evt) {
		if(evt.getSide() == Side.CLIENT) {
			this.getBookTransformer().register();
		}
		this.getItemDistributors().forEach(DDDCapabilityDistributors::addItemCapUpdater);
		this.addOtherDistributors();
		return true;
	}

	@Override
	public boolean init(FMLInitializationEvent evt) {
		this.registerCapabilities();
		this.getFormatters().forEach(TooltipDistributor::registerModCompat);
		return this.doSpecificInit(evt);
	}

	@Override
	public boolean postInit(FMLPostInitializationEvent evt) {
		
		return true;
	}
	
	
}
