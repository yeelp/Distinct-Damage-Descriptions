package yeelp.distinctdamagedescriptions.capability.distributors;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.MobResistances;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.util.ConfigGenerator;
import yeelp.distinctdamagedescriptions.util.MobResistanceCategories;

public final class MobResistancesCapabilityDistributor extends AbstractCapabilityDistributorGeneratable<EntityLivingBase, MobResistanceCategories, IMobResistances> {
	private static final ResourceLocation LOC = new ResourceLocation(ModConsts.MODID, "mobResists");
	private static MobResistancesCapabilityDistributor instance;
	
	private MobResistancesCapabilityDistributor() {
		super(LOC);
	}

	@Override
	protected IMobResistances generateCapability(EntityLivingBase t, ResourceLocation key) {
		return ConfigGenerator.getOrGenerateMobResistances(t, key);
	}

	@Override
	public boolean isApplicable(EntityLivingBase t) {
		return true;
	}

	@Override
	protected IDDDConfiguration<MobResistanceCategories> getConfig() {
		return DDDConfigurations.mobResists;
	}
	
	@Override
	protected IMobResistances createCapability(MobResistanceCategories configResult) {
		return new MobResistances(configResult.getResistanceMap(), configResult.getImmunities(), Math.random() < configResult.adaptiveChance(), configResult.getAdaptiveAmount());
	}

	public static final MobResistancesCapabilityDistributor getInstance() {
		return instance == null ? instance = new MobResistancesCapabilityDistributor() : instance;
	}
}
