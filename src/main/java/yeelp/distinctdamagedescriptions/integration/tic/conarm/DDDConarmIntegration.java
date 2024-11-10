package yeelp.distinctdamagedescriptions.integration.tic.conarm;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Functions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import c4.conarm.common.ConstructsRegistry;
import c4.conarm.common.armor.utils.ArmorHelper;
import c4.conarm.lib.materials.ArmorMaterialType;
import c4.conarm.lib.materials.ArmorMaterials;
import c4.conarm.lib.tinkering.TinkersArmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.capability.distributors.AbstractCapabilityDistributor;
import yeelp.distinctdamagedescriptions.capability.impl.ArmorDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigLoader;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.config.readers.DDDConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.ConfigInvalidException;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.client.IModCompatTooltipFormatter;
import yeelp.distinctdamagedescriptions.integration.tic.DDDBookTransformer;
import yeelp.distinctdamagedescriptions.integration.tic.DDDTiCIntegration;
import yeelp.distinctdamagedescriptions.integration.tic.conarm.capability.ConarmArmorDistribution;
import yeelp.distinctdamagedescriptions.integration.tic.conarm.capability.distributors.ConarmArmorDistributor;
import yeelp.distinctdamagedescriptions.integration.tic.conarm.client.ConarmArmorFormatter;
import yeelp.distinctdamagedescriptions.integration.tic.conarm.client.ConarmPlatesFormatter;
import yeelp.distinctdamagedescriptions.integration.tic.conarm.client.DDDConarmBookTransformer;
import yeelp.distinctdamagedescriptions.integration.tic.conarm.traits.DDDImmunityTrait;
import yeelp.distinctdamagedescriptions.integration.tic.conarm.traits.DDDImmunityTrait.DamageHandler;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.ConfigReaderUtilities;
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.DDDCombatCalculations;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.IDDDCalculationInjector.IArmorValuesInjector;

public final class DDDConarmIntegration extends DDDTiCIntegration {

	private static final class ImmunityTraitRecord {
		private final String mat, types;

		ImmunityTraitRecord(String mat, String types) {
			this.mat = mat;
			this.types = types;
		}

		/**
		 * @return the mat
		 */
		String getMat() {
			return this.mat;
		}

		/**
		 * @return the type
		 */
		String getTypes() {
			return this.types;
		}
	}

	public static final class ConfigReader implements DDDConfigReader {

		private static final String REGEX = "\\w+;" + ConfigReaderUtilities.DDD_TYPE_REGEX + "(,(\\s)?" + ConfigReaderUtilities.DDD_TYPE_REGEX + ")*";
		private static Collection<DDDConfigReaderException> errors = Lists.newArrayList();

		@Override
		public void read() {
			immunities = Arrays.stream(ModConfig.compat.conarm.armorImmunities).filter(Predicates.not(ConfigReaderUtilities::isCommentEntry)).map((s) -> {
				if(s.matches(REGEX)) {
					String[] arr = s.split(";");
					return new ImmunityTraitRecord(arr[0], arr[1]);
				}
				errors.add(new ConfigInvalidException(this.getName(), s));
				return null;
			}).filter(Predicates.notNull()).collect(Collectors.toList());
		}

		@Override
		public String getName() {
			return "Conarm Immunity Traits";
		}

		@Override
		public boolean shouldTime() {
			return false;
		}

		@Override
		public boolean doesSelfReportErrors() {
			return true;
		}

		@Override
		public Collection<DDDConfigReaderException> getSelfReportedErrors() {
			return errors;
		}
	}

	public static final ConfigReader READER = new ConfigReader();
	static Iterable<ImmunityTraitRecord> immunities = Lists.newArrayList();

	@Override
	protected boolean doSpecificPreInit(FMLPreInitializationEvent evt) {
		DDDConfigLoader.getInstance().enqueue(READER);
		return true;
	}

	@Override
	public boolean doSpecificInit(FMLInitializationEvent evt) {
		String location;
		switch(ModConfig.compat.conarm.traitLocation) {
			case CORE:
				location = ArmorMaterialType.CORE;
				break;
			default:
				location = ArmorMaterialType.PLATES;
				break;
		}
		Stream.Builder<DDDDamageType> builder = Stream.builder();
		DDDRegistries.damageTypes.iterator().forEachRemaining(builder::add);
		Map<String, DDDImmunityTrait> traits = builder.build().map(DDDImmunityTrait::new).collect(Collectors.toMap(Functions.compose(DDDDamageType::getTypeName, DDDImmunityTrait::getType), Function.identity()));
		immunities.forEach((record) -> {
			Material mat = TinkerRegistry.getMaterial(record.getMat());
			Arrays.stream(record.getTypes().split(",")).map(Functions.compose(traits::get, String::trim)).forEach((t) -> {
				if(t != null) {
					ArmorMaterials.addArmorTrait(mat, t, location);
				}
				else {
					throw new RuntimeException("One of the Conarm immunity trait config entries refers to a damage type that doesn't exist!");
				}
			});
		});
		// Must register an armor values injector to correct the way conarm deals with
		// armor properties.
		DDDCombatCalculations.registerArmorValuesInjector(new IArmorValuesInjector() {
			@Override
			public ArmorValues apply(ItemStack t, EntityEquipmentSlot u) {
				if(t.getItem() instanceof TinkersArmor) {
					return new ArmorValues(ArmorHelper.getArmor(t, u.getIndex()), ArmorHelper.getToughness(t));
				}
				return new ArmorValues();
			}
		});
		return true;
	}

	@Override
	public boolean postInit(FMLPostInitializationEvent evt) {
		ImmutableList.of(ConstructsRegistry.helmet, ConstructsRegistry.chestplate, ConstructsRegistry.leggings, ConstructsRegistry.boots).forEach((armor) -> DDDConfigurations.armors.put(ForgeRegistries.ITEMS.getKey(armor).toString(), new ArmorDistribution()));
		return super.postInit(evt);
	}

	@Override
	public String getModTitle() {
		return ModConsts.IntegrationTitles.CONARM_TITLE;
	}

	@Override
	public String getModID() {
		return ModConsts.IntegrationIds.CONARM_ID;
	}

	@Override
	protected DDDBookTransformer getBookTransformer() {
		return new DDDConarmBookTransformer();
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return ImmutableList.of(new DamageHandler());
	}

	@Override
	protected Iterable<AbstractCapabilityDistributor<ItemStack, ?, ? extends IDistribution>> getItemDistributors() {
		return ImmutableList.of(ConarmArmorDistributor.getInstance());
	}

	@Override
	protected Iterable<IModCompatTooltipFormatter<ItemStack>> getFormatters() {
		return ImmutableList.of(ConarmArmorFormatter.getInstance(), ConarmPlatesFormatter.getInstance());
	}

	@Override
	protected void registerCapabilities() {
		ConarmArmorDistribution.register();
	}
}
