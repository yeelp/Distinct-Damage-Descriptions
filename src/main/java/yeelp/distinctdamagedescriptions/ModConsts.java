package yeelp.distinctdamagedescriptions;

import com.google.common.collect.ImmutableList;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public interface ModConsts {
	
	String MODID = "distinctdamagedescriptions";
	String MODID_SHORT = "ddd";
	String NAME = "Distinct Damage Descriptions";
	String VERSION = "@version@";
	
	interface DamageTypeNames {
		String UNKNOWN = "unknown";
		String NORMAL = "normal";
		String SLASHING = "slashing";
		String PIERCING = "piercing";
		String BLUDGEONING = "bludgeoning";
		String ACID = "acid";
		String COLD = "cold";
		String FIRE = "fire";
		String FORCE = "force";
		String LIGHTNING = "lightning";
		String NECROTIC = "necrotic";
		String POISON = "poison";
		String PSYCHIC = "psychic";
		String RADIANT = "radiant";
		String THUNDER = "thunder";
	}
	
	interface IntegrationIds {
		String CRAFTTWEAKER_ID = "crafttweaker";
		String CONTENTTWEAKER_ID = "contenttweaker";
		String HWYLA_ID = "waila";
		String TCONSTRUCT_ID = "tconstruct";
		String CONARM_ID = "conarm";
		String TETRA_ID = "tetra";
		String LYCANITES_ID = "lycanitesmobs";
		String SPARTAN_WEAPONRY_ID = "spartanweaponry";
		String BETWEENLANDS_ID = "thebetweenlands";
		String FIRST_AID_ID = "firstaid";
		String QUALITY_TOOLS_ID = "qualitytools";
		String BAUBLES_ID	= "baubles";
		String TECHGUNS_ID = "techguns";
		String WIZARDRY_ID = "ebwizardry";
		String THAUMCRAFT_ID = "thaumcraft";
		String FERMIUM_ID = "fermiumbooter";
		String BETTER_SURVIVAL_ID = "mujmajnkraftsbettersurvival";
		String ENDER_SKILLS_ID = "enderskills";
	}
	
	interface IntegrationTitles {
		String CRAFTTWEAKER_TITLE = "CraftTweaker";
		String CONTENTTWEAKER_TITLE = "ContentTweaker";
		String HWYLA_TITLE = "HWYLA";
		String TCONSTUCT_TITLE = "Tinker's Construct";
		String CONARM_TITLE = "Construct's Armory";
		String TETRA_TITLE = "Tetra";
		String LYCANITES_TITLE = "Lycanite's Mobs";
		String SPARTAN_WEAPONRY_TITLE = "Spartan Weaponry";
		String BETWEENLANDS_TITLE = "The Betweenlands";
		String FIRST_AID_TITLE = "First Aid";
		String QUALITY_TOOLS_TITLE = "Quality Tools";
		String BAUBLES_TITLE = "Baubles";
		String TECHGUNS_TITLE = "Techguns";
		String WIZARDRY_NAME = "Electroblob's Wizardry";
		String THAUMCRAFT_TITLE = "Thaumcraft";
		String FERMIUM_TITLE = "Fermium Booter";
		String BETTER_SURVIVAL_TITLE = "Better Survival";
		String ENDER_SKILLS_TITLE = "Ender Skills";
	}
	
	interface TooltipConsts {
		String CTRL = "ctrl";
		String SHIFT = "shift";
		String KEYS_ROOT = "keys";
		String TOOLTIPS_ROOT = "tooltips";
		String NO_RESISTS = "noresists";
		String NO_TOOLTIP = "notooltip";
		
		String DAMAGE = "damage";
		String RESISTANCE = "resistance";
		String WEAKNESS = "weakness";
		String EFFECTIVENESS = "effectiveness";
		String ADAPTABILITY_CHANCE = "adaptivechance";
		String ADAPTABILITY_AMOUNT = "adaptiveamount";
		String IMMUNITIES = "immunities";
	}
	
	interface CommandConsts {
		String COMMANDS_ROOT = "commands";
		String NO_TARGETS = "notargets";
		String INVALID_TYPE = "invalidtype";
		String USAGE = "usage";
		String SUCCESS = "success";
	}
	
	interface NBT {
		byte COMPOUND_TAG_ID = (new NBTTagCompound()).getId();
		byte LIST_TAG_ID = (new NBTTagList()).getId();
		byte STRING_TAG_ID = (new NBTTagString()).getId();
		@SuppressWarnings("unused")
		byte FLOAT_TAG_ID = (new NBTTagFloat(0)).getId();
		String ATTRIBUTE_MODIFIERS_KEY = "AttributeModifiers";
		String NAME_KEY = "Name";
	}

	EntityEquipmentSlot[] ARMOR_SLOTS = {
			EntityEquipmentSlot.CHEST,
			EntityEquipmentSlot.FEET,
			EntityEquipmentSlot.HEAD,
			EntityEquipmentSlot.LEGS};
	Iterable<EntityEquipmentSlot> ARMOR_SLOTS_ITERABLE = ImmutableList.copyOf(ARMOR_SLOTS);
	
	String BUILT_IN = "Built-in";
}
