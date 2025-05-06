package yeelp.distinctdamagedescriptions.integration.qualitytools;

import java.util.Optional;

import com.google.common.collect.ImmutableList;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationTitles;
import yeelp.distinctdamagedescriptions.ModConsts.NBT;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;
import yeelp.distinctdamagedescriptions.integration.ModIntegrationKernel;
import yeelp.distinctdamagedescriptions.integration.hwyla.IHwylaTooltipInjectors.IHwylaArmorTooltipInjector;
import yeelp.distinctdamagedescriptions.integration.hwyla.client.HwylaTooltipMaker;
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.DDDAttributeModifierCollections;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.DDDCombatCalculations;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.IDDDCalculationInjector.IArmorValuesInjector;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.IDDDTooltipInjector.IArmorTooltipInjector;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipDistributor;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipTypeFormatter.Armor;

public final class QualityToolsIntegration implements IModIntegration {

	private static interface QualityToolsConsts {
		final String QUALITY_TAG = "Quality";
		final String ATTRIBUTE_MODIFIERS_TAG = "AttributeModifiers";
		final String ATTRIBUTE_NAME_TAG = "AttributeName";
		final String AMOUNT_TAG = "Amount";
	}

	@Override
	public String getModTitle() {
		return IntegrationTitles.QUALITY_TOOLS_TITLE;
	}

	@Override
	public String getModID() {
		return IntegrationIds.QUALITY_TOOLS_ID;
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return ImmutableList.of();
	}

	@Override
	public boolean init(FMLInitializationEvent evt) {
		TooltipDistributor.registerArmorTooltipInjector(new IArmorTooltipInjector() {
			
			@Override
			public boolean shouldUseFormatter(ItemStack stack) {
				return false;
			}
			
			@Override
			public Armor getFormatterToUse() {
				return null;
			}
			
			@Override
			public boolean applies(ItemStack stack) {
				return hasQuality(stack);
			}
			
			@Override
			public ArmorValues alterArmorValues(ItemStack stack, float armor, float toughness) {
				return getArmorValuesFromQuality(stack).add(armor, toughness);
			}
		});
		
		DDDCombatCalculations.registerArmorValuesInjector(new IArmorValuesInjector() {
			
			@Override
			public ArmorValues apply(ItemStack t, EntityEquipmentSlot u) {
				return getArmorValuesFromQuality(t);
			}

			@Override
			public int priority() {
				return -1;
			}
		});
		return IModIntegration.super.init(evt);
	}
	
	@Override
	public void registerCrossModCompat() {
		if(ModIntegrationKernel.wasIntegrationLoaded(IntegrationIds.HWYLA_ID)) {
			HwylaTooltipMaker.registerHwylaArmorTooltipInjector(new IHwylaArmorTooltipInjector() {
				
				@Override
				public boolean applies(ItemStack stack) {
					return hasQuality(stack);
				}
				
				@Override
				public ArmorValues alterArmorValues(ItemStack stack, float armor, float toughness) {
					return getArmorValuesFromQuality(stack);
				}
			});
		}
	}
	
	static ArmorValues getArmorValuesFromQuality(ItemStack stack) {
		//@formatter:off
		return Optional.ofNullable(stack.getTagCompound())
				.map((tag) -> tag.getCompoundTag(QualityToolsConsts.QUALITY_TAG))
				.map((tag) -> tag.getTagList(QualityToolsConsts.ATTRIBUTE_MODIFIERS_TAG, NBT.COMPOUND_TAG_ID))
				.map((list) -> {
					double armorVal = 0.0;
					double toughnessVal = 0.0;
					for(NBTBase nbt : list) {
						if(nbt instanceof NBTTagCompound) {
							NBTTagCompound tag = (NBTTagCompound) nbt;
							String name = tag.getString(QualityToolsConsts.ATTRIBUTE_NAME_TAG);
							double val = tag.getDouble(QualityToolsConsts.AMOUNT_TAG);
							if(name.equalsIgnoreCase(DDDAttributeModifierCollections.ArmorModifiers.ARMOR.getAttribute().getName())) {
								armorVal += val;
							}
							else if(name.equalsIgnoreCase(DDDAttributeModifierCollections.ArmorModifiers.TOUGHNESS.getAttribute().getName())) {
								toughnessVal += val;
							}
						}
					}
					return new ArmorValues((float) armorVal, (float) toughnessVal);
				}).orElse(new ArmorValues());
		//@formatter:on
	}
	
	static boolean hasQuality(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey(QualityToolsConsts.QUALITY_TAG);
	}
}
