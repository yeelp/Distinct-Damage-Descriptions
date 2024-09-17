package yeelp.distinctdamagedescriptions;

import com.google.common.collect.ImmutableList;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public interface ModConsts {
	
	final String MODID = "distinctdamagedescriptions";
	final String MODID_SHORT = "ddd";
	final String NAME = "Distinct Damage Descriptions";
	final String VERSION = "@version@";
	
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
		final String FIRST_AID_ID = "firstaid";
		final String QUALITY_TOOLS_ID = "qualitytools";
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
		final String FIRST_AID_TITLE = "First Aid";
		final String QUALITY_TOOLS_TITLE = "Quality Tools";
	}
	
	public interface TooltipConsts {
		final String CTRL = "ctrl";
		final String SHIFT = "shift";
		final String KEYS_ROOT = "keys";
		final String TOOLTIPS_ROOT = "tooltips";
		final String NO_RESISTS = "noresists";
		final String UNCHANGED = "unchanged";
		final String NO_ICONS = "dddnoicons";
	}
	
	public interface CommandConsts {
		final String COMMANDS_ROOT = "commands";
		final String NO_TARGETS = "notargets";
		final String INVALID_TYPE = "invalidtype";
	}
	
	public interface NBT {
		final byte COMPOUND_TAG_ID = (new NBTTagCompound()).getId();
		final byte LIST_TAG_ID = (new NBTTagList()).getId();
		final String ATTRIBUTE_MODIFIERS_KEY = "AttributeModifiers";
		final String NAME_KEY = "Name";
		final String ATTRIBUTE_NAME_KEY = "AttributeName";
	}

	final EntityEquipmentSlot[] ARMOR_SLOTS = {
			EntityEquipmentSlot.CHEST,
			EntityEquipmentSlot.FEET,
			EntityEquipmentSlot.HEAD,
			EntityEquipmentSlot.LEGS};
	final Iterable<EntityEquipmentSlot> ARMOR_SLOTS_ITERABLE = ImmutableList.copyOf(ARMOR_SLOTS);
	
	final String BUILT_IN = "Built-in";
}
