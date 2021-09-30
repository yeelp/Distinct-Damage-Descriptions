package yeelp.distinctdamagedescriptions.capability.distributors;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.util.ConfigGenerator;

public abstract class DamageDistributionCapabilityDistributor<T> extends AbstractCapabilityDistributorGeneratable<T, IDamageDistribution, IDamageDistribution> {
	static final ResourceLocation LOC = new ResourceLocation(ModConsts.MODID, "dmgDistribution");

	protected DamageDistributionCapabilityDistributor(ResourceLocation loc) {
		super(loc);
	}
	
	protected DamageDistributionCapabilityDistributor() {
		this(LOC);
	}

	@Override
	public boolean isApplicable(T t) {
		return true;
	}
	
	public static final class ForEntity extends DamageDistributionCapabilityDistributor<EntityLivingBase> {
		
		private static ForEntity instance;
		
		@Override
		protected IDDDConfiguration<IDamageDistribution> getConfig() {
			return DDDConfigurations.mobDamage;
		}

		@Override
		protected IDamageDistribution generateCapability(EntityLivingBase t, ResourceLocation key) {
			return ConfigGenerator.getOrGenerateMobDamage(t, key);
		}
		
		public static final ForEntity getInstance() {
			return instance == null ? instance = new ForEntity() : instance;
		}
	}
	
	public static final class ForItem extends DamageDistributionCapabilityDistributor<ItemStack> {
		
		private static ForItem instance;
		@Override
		protected IDamageDistribution generateCapability(ItemStack t, ResourceLocation key) {
			Item i = t.getItem();
			if(i instanceof ItemSword) {
				return ConfigGenerator.getOrGenerateWeaponCapabilities((ItemSword) i, t);
			}
			else if (i instanceof ItemTool) {
				return ConfigGenerator.getOrGenerateWeaponCapabilities((ItemTool) i, t);
			}
			else if (i instanceof ItemHoe) {
				return ConfigGenerator.getOrGenerateWeaponCapabilities((ItemHoe) i, t);
			}
			else {
				return this.getConfig().getDefaultValue();
			}
		}

		@Override
		protected IDDDConfiguration<IDamageDistribution> getConfig() {
			return DDDConfigurations.items;
		}
		
		public static final ForItem getInstance() {
			return instance == null ? instance = new ForItem() : instance;
		}
	}
	
	public static class ForProjectile extends DamageDistributionCapabilityDistributor<IProjectile> {
		
		static final ResourceLocation LOC = new ResourceLocation(ModConsts.MODID, "projectileDmgDistribution");
		private static ForProjectile instance;
		
		private ForProjectile() {
			super(LOC);
		}

		@Override
		protected IDamageDistribution generateCapability(IProjectile t, ResourceLocation key) {
			return ConfigGenerator.getOrGenerateProjectileDistribution(t, key);
		}

		@Override
		protected IDDDConfiguration<IDamageDistribution> getConfig() {
			return DDDConfigurations.projectiles;
		}
		
		public static final ForProjectile getInstance() {
			return instance == null ? instance = new ForProjectile() : instance;
		}
	}
}
