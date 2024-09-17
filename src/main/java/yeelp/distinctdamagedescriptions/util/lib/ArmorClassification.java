package yeelp.distinctdamagedescriptions.util.lib;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.inventory.EntityEquipmentSlot;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.ArmorMap;

public final class ArmorClassification {

	private final Map<EntityEquipmentSlot, ArmorMap> distributedArmorEffectiveness;
	private final Map<EntityEquipmentSlot, ArmorValues> originalArmorValues;
	
	public ArmorClassification(Map<EntityEquipmentSlot, ArmorMap> aMap, Map<EntityEquipmentSlot, ArmorValues> avMap) {
		this.distributedArmorEffectiveness = aMap;
		this.originalArmorValues = avMap;
	}
	
	@Nullable
	public ArmorMap getArmorMapForSlot(EntityEquipmentSlot slot) {
		return this.distributedArmorEffectiveness.get(slot);
	}
	
	@Nonnull
	public ArmorValues getOriginalArmorValues(EntityEquipmentSlot slot) {
		return this.originalArmorValues.getOrDefault(slot, new ArmorValues());
	}
	
	public void forEachArmorMap(BiConsumer<EntityEquipmentSlot, ArmorMap> action) {
		Objects.requireNonNull(action, "Action to take for forEachArmorMap can not be null!");
		this.distributedArmorEffectiveness.forEach(action);
	}
	
	public void forEachArmorValues(BiConsumer<EntityEquipmentSlot, ArmorValues> action) {
		Objects.requireNonNull(action, "Action to take for forEachArmorValues can not be null!");
		this.originalArmorValues.forEach(action);
	}
}
