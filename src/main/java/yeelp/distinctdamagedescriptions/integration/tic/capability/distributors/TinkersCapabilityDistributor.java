package yeelp.distinctdamagedescriptions.integration.tic.capability.distributors;

import c4.conarm.lib.tinkering.TinkersArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import slimeknights.tconstruct.library.tools.TinkerToolCore;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.integration.capability.distributors.ModCompatCapabilityDistributor;
import yeelp.distinctdamagedescriptions.integration.tic.capability.AbstractTinkersDistribution;
import yeelp.distinctdamagedescriptions.integration.tic.conarm.capability.ConarmArmorDistribution;
import yeelp.distinctdamagedescriptions.integration.tic.tinkers.capability.TinkerToolDistribution;
import yeelp.distinctdamagedescriptions.util.DistributionBias;

public abstract class TinkersCapabilityDistributor<T, C extends AbstractTinkersDistribution<? extends IDistribution, T>> extends ModCompatCapabilityDistributor<ItemStack, C> {

	protected TinkersCapabilityDistributor(String type) {
		super(new ResourceLocation(ModConsts.MODID, new StringBuilder().append(ModCompatCapabilityDistributor.LOC.getResourcePath()).append(type).toString()));
	}

	public static abstract class Tool<D extends IDistribution> extends TinkersCapabilityDistributor<DistributionBias, TinkerToolDistribution<D>> {

		protected Tool(String type) {
			super(type);
		}

		@Override
		public boolean isApplicable(ItemStack t) {
			return t.getItem() instanceof TinkerToolCore && this.hasDist(t);
		}
		
		protected abstract boolean hasDist(ItemStack stack);

		public static final class Damage extends Tool<IDamageDistribution> {
			private static Damage instance;

			protected Damage() {
				super("ToolDamage");
			}

			@Override
			protected TinkerToolDistribution<IDamageDistribution> getCapability(ItemStack t, String key) {
				return new TinkerToolDistribution.Tool();
			}

			public static Damage getInstance() {
				return instance == null ? instance = new Damage() : instance;
			}

			@Override
			protected boolean hasDist(ItemStack stack) {
				return true; // all items have damage distribution
			}
		}
	}

	public static final class Armor extends TinkersCapabilityDistributor<IArmorDistribution, ConarmArmorDistribution> {
		private static Armor instance;

		protected Armor() {
			super("Armor");
		}

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
