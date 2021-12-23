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
	public static final String CONARM_ID = "conarm";
	public static final String TCONSTRUCT_ID = "tconstruct";
	public static final String LYCANITES_ID = "lycanitesmobs";
	public static final String TETRA_ID = "tetra";
	public static final String HWYLA_ID = "waila";
	public static final String SPARTAN_WEAPONRY_ID = "spartanweaponry";

	/** MOD NAMES SECTION END **/

	public static final EntityEquipmentSlot[] ARMOR_SLOTS = {EntityEquipmentSlot.CHEST, EntityEquipmentSlot.FEET, EntityEquipmentSlot.HEAD, EntityEquipmentSlot.LEGS};
	public static final Iterable<EntityEquipmentSlot> ARMOR_SLOTS_ITERABLE = ImmutableList.copyOf(ARMOR_SLOTS);
}
