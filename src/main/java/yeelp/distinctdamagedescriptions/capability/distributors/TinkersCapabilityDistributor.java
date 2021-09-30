package yeelp.distinctdamagedescriptions.capability.distributors;

import c4.conarm.lib.tinkering.TinkersArmor;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.tools.TinkerToolCore;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.AbstractTinkersDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.ConarmArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.TinkerToolDistribution;

public abstract class TinkersCapabilityDistributor<C extends AbstractTinkersDistribution<? extends IDistribution>> extends ModCompatCapabilityDistributor<ItemStack, C> {

	public static abstract class Tool<D extends IDistribution> extends TinkersCapabilityDistributor<TinkerToolDistribution<D>> {
		
		@Override
		public boolean isApplicable(ItemStack t) {
			return t.getItem() instanceof TinkerToolCore;
		}
		
		public static final class Damage extends Tool<IDamageDistribution> {
			
			private static Damage instance;
			
			@Override
			protected TinkerToolDistribution<IDamageDistribution> getCapability(ItemStack t, String key) {
				return new TinkerToolDistribution.Tool();
			}
			
			public static Damage getInstance() {
				return instance == null ? instance = new Damage() : instance;
			}
		}
		
		public static final class Shield extends Tool<ShieldDistribution> {
			
			private static Shield instance;

			@Override
			public boolean isApplicable(ItemStack t) {
				return super.isApplicable(t) && t.getItemUseAction() == EnumAction.BLOCK;
			}

			@Override
			protected TinkerToolDistribution<ShieldDistribution> getCapability(ItemStack t, String key) {
				return new TinkerToolDistribution.Shield();
			}
			
			public static Shield getInstance() {
				return instance == null ? instance = new Shield() : instance;
			}
		}
	}
	
	public static final class Armor extends TinkersCapabilityDistributor<ConarmArmorDistribution> {
		
		private static Armor instance;
		
		@Override
		public boolean isApplicable(ItemStack t) {
			return t.getItem() instanceof TinkersArmor;
		}

		@Override
		protected ConarmArmorDistribution getCapability(ItemStack t, String key) {
			return new ConarmArmorDistribution();
		}
		
		public static Armor getInstance() {
			return instance == null ? instance = new Armor() : instance;
		}
	}
}
