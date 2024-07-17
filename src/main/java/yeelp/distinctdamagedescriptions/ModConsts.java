package yeelp.distinctdamagedescriptions;

import com.google.common.collect.ImmutableList;

import net.minecraft.inventory.EntityEquipmentSlot;

public interface ModConsts {
	
	final String MODID = "distinctdamagedescriptions";
	final String MODID_SHORT = "ddd";
	final String NAME = "Distinct Damage Descriptions";
	final String VERSION = "${version}";
	
	public interface IntegrationIds {
		final String CRAFTTWEAKER_ID = "crafttweaker";
		final String CONTENTTWEAKER_ID = "contenttweaker";
		final String HWYLA_ID = "waila";
		final String TCONSTRUCT_ID = "tconstruct";
		final String CONARM_ID = "conarm";
		final String TETRA_ID = "tetra";
		final String LYCANITES_ID = "lycanitesmobs";
		final String SPARTAN_WEAPONRY_ID = "spartanweaponry";
		final String BETWEENLANDS_ID = "thebetweenlands";
	}
	
	public interface IntegrationTitles {
		final String CRAFTWEAKER_TITLE = "CraftTweaker";
		final String CONTENTTWEAKER_TITLE = "ContentTweaker";
		final String HWYLA_TITLE = "HWYLA";
		final String TCONSTUCT_TITLE = "Tinker's Construct";
		final String CONARM_TITLE = "Construct's Armory";
		final String TETRA_TITLE = "Tetra";
		final String LYCANITES_TITLE = "Lycanite's Mobs";
		final String SPARTAN_WEAPONRY_TITLE = "Spartan Weaponry";
		final String BETWEENLANDS_TITLE = "The Betweenlands";
	}

	final EntityEquipmentSlot[] ARMOR_SLOTS = {
			EntityEquipmentSlot.CHEST,
			EntityEquipmentSlot.FEET,
			EntityEquipmentSlot.HEAD,
			EntityEquipmentSlot.LEGS};
	final Iterable<EntityEquipmentSlot> ARMOR_SLOTS_ITERABLE = ImmutableList.copyOf(ARMOR_SLOTS);
	
	final String BUILT_IN = "Built-in";
}
