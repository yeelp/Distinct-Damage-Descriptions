package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.PrimitiveIterator.OfDouble;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.util.lib.ArmorClassification;
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.DDDAttributeModifierCollections;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.ArmorMap;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.IDDDCalculationInjector.IArmorValuesInjector;

class ArmorClassifier implements IClassifier<ArmorClassification> {

	private static final Set<IArmorValuesInjector> STACK_ARMOR_VALUES_INJECTORS = Sets.newTreeSet();
	@Override
	public Optional<ArmorClassification> classify(CombatContext context) {
		Map<EntityEquipmentSlot, ArmorMap> aMap = Maps.newHashMap();
		Map<EntityEquipmentSlot, ArmorValues> avMap = Maps.newHashMap();
		for(EntityEquipmentSlot slot : context.getValidArmorSlots()) {
			ItemStack slottedStack = context.getDefender().getItemStackFromSlot(slot);
			Item slottedItem = slottedStack.getItem();
			if(slottedItem instanceof ItemArmor) {
				ArmorValues av = getArmorValuesFromStack(slottedStack, slot, context.getDefender(), context.getSource(), context.getAmount());
				DebugLib.outputFormattedDebug("%s: %s", slottedItem.getItemStackDisplayName(slottedStack), av.toString());
				avMap.put(slot, av);
				final float armor = av.getArmor(), toughness = av.getToughness();
				DDDAPI.accessor.getArmorResistances(slottedStack).map((dist) -> dist.distributeArmor(armor, toughness)).ifPresent((dist) -> aMap.put(slot, dist));
			}
		}
		return Optional.of(new ArmorClassification(aMap, avMap));
	}
	
	static void registerInjector(IArmorValuesInjector injector) {
		STACK_ARMOR_VALUES_INJECTORS.add(injector);
	}
	
	private static ArmorValues getStackOnlyArmorValues(ItemStack stack, EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> map = stack.getItem().getAttributeModifiers(slot, stack);
		OfDouble it = Arrays.stream(DDDAttributeModifierCollections.ArmorModifiers.values()).mapToDouble((armorModEnum) -> map.get(armorModEnum.getAttribute().getName()).stream().mapToDouble(AttributeModifier::getAmount).sum()).iterator();
		return new ArmorValues((float) it.nextDouble(), (float) it.nextDouble());
	}
	
	private static ArmorValues getArmorValuesFromStack(ItemStack stack, EntityEquipmentSlot slot, EntityLivingBase entity, DamageSource src, double amount) {
		ArmorValues av = ArmorValues.merge(getStackOnlyArmorValues(stack, slot), STACK_ARMOR_VALUES_INJECTORS.stream().map((f) -> f.apply(stack, slot)).reduce(ArmorValues::merge).orElse(new ArmorValues()));
		if(stack.getItem() instanceof ISpecialArmor) {
			av = ArmorValues.merge(av, getISpecialArmorValues(stack, (ISpecialArmor) stack.getItem(), slot, entity, src, amount));
		}
		return av;
	}
	
	public static ArmorValues getISpecialArmorValues(ItemStack stack, ISpecialArmor armor, EntityEquipmentSlot slot, EntityLivingBase entity, DamageSource src, double amount) {
		return new ArmorValues(armor.getProperties(entity, stack, src, amount, slot.getIndex()));
	}
}
