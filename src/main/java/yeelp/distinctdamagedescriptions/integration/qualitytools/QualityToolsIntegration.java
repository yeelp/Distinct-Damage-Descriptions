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
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.DDDAttributeModifierCollections;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.DDDCombatCalculations;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.IDDDCalculationInjector.IArmorValuesInjector;

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
		DDDCombatCalculations.registerArmorValuesInjector(new IArmorValuesInjector() {
			
			@Override
			public ArmorValues apply(ItemStack t, EntityEquipmentSlot u) {
				return Optional.ofNullable(t.getTagCompound())
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
									if(name.equalsIgnoreCase(DDDAttributeModifierCollections.ARMOR.getAttributeModified().getName())) {
										armorVal += val;
									}
									else if(name.equalsIgnoreCase(DDDAttributeModifierCollections.TOUGHNESS.getAttributeModified().getName())) {
										toughnessVal += val;
									}
								}
							}
							return new ArmorValues((float) armorVal, (float) toughnessVal);
						}).orElse(new ArmorValues());
			}

			@Override
			public int priority() {
				return -1;
			}
		});
		return IModIntegration.super.init(evt);
	}
}
