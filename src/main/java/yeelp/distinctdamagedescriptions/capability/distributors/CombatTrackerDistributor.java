package yeelp.distinctdamagedescriptions.capability.distributors;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.capability.IDDDCombatTracker;
import yeelp.distinctdamagedescriptions.capability.impl.DDDCombatTracker;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;

public class CombatTrackerDistributor extends AbstractCapabilityDistributor<EntityLivingBase, IDDDCombatTracker, IDDDCombatTracker> {

	private static CombatTrackerDistributor instance;
	private static final ResourceLocation LOC = new ResourceLocation(ModConsts.MODID, "combatTracker");
	
	public CombatTrackerDistributor() {
		super(LOC, () -> false);
	}

	@Override
	public boolean isApplicable(EntityLivingBase t) {
		return true;
	}

	@Override
	protected IDDDCombatTracker getCapability(EntityLivingBase t, String key) {
		return new DDDCombatTracker(t);
	}

	@Override
	protected IDDDConfiguration<IDDDCombatTracker> getConfig() {
		return null;
	}
	
	public static CombatTrackerDistributor getInstance() {
		return instance == null ? new CombatTrackerDistributor() : instance;
	}

}
