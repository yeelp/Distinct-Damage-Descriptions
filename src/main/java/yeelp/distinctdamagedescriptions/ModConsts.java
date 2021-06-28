package yeelp.distinctdamagedescriptions;

import com.google.common.collect.ImmutableList;

import net.minecraft.inventory.EntityEquipmentSlot;

public class ModConsts {
	public static final String MODID = "distinctdamagedescriptions";
	public static final String MODID_SHORT = "ddd";
	public static final String NAME = "Distinct Damage Descriptions";
	public static final String VERSION = "${version}";

	/** MOD NAMES SECTION **/

	public static final String CRAFTTWEAKER_ID = "crafttweaker";
	public static final String HWYLA_ID = "waila";

	/** MOD NAMES SECTION END **/

	public static final EntityEquipmentSlot[] ARMOR_SLOTS = {EntityEquipmentSlot.CHEST, EntityEquipmentSlot.FEET, EntityEquipmentSlot.HEAD, EntityEquipmentSlot.LEGS};
	public static final Iterable<EntityEquipmentSlot> ARMOR_SLOTS_ITERABLE = ImmutableList.copyOf(ARMOR_SLOTS);
}
