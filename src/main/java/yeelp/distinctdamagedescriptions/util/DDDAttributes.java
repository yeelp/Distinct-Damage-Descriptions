package yeelp.distinctdamagedescriptions.util;

import java.lang.reflect.Field;
import java.util.UUID;

import com.google.common.collect.Multimap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.handlers.Handler;

@Deprecated
public class DDDAttributes extends Handler
{
	public static final IAttribute SLASHING = new RangedAttribute((IAttribute) null, "distinctdamagedescriptions.slashingResistance", 0.0, -2048.0, 2048.0).setShouldWatch(true);
	public static final IAttribute BLUDGEONING = new RangedAttribute((IAttribute) null, "distinctdamagedescriptions.bludgeoningResistance", 0.0, -2048.0, 2048.0).setShouldWatch(true);
	public static final IAttribute PIERCING = new RangedAttribute((IAttribute) null, "distinctdamagedescriptions.piercingResistance", 0.0, -2048.0, 2048.0).setShouldWatch(true);
	public static final IAttribute SLASHING_DMG = new RangedAttribute((IAttribute) null, "distinctdamagedescriptions.slashingDmg", 0.0, -2048.0, 2048.0).setShouldWatch(true);
	public static final IAttribute BLUDGEONING_DMG = new RangedAttribute((IAttribute) null, "distinctdamagedescriptions.bludgeoningDmg", 0.0, -2048.0, 2048.0).setShouldWatch(true);
	public static final IAttribute PIERCING_DMG = new RangedAttribute((IAttribute) null, "distinctdamagedescriptions.piercingDmg", 0.0, -2048.0, 2048.0).setShouldWatch(true);	
	
	private static final UUID bludgeResistUUID = UUID.fromString("08ba5853-b982-4ba6-b118-d5431863071f");
	private static final UUID slashResistUUID = UUID.fromString("94d0a1ef-9946-40fa-9496-e9beffb15430");
	private static final UUID pierceResistUUID = UUID.fromString("cb43e76f-e0a2-42fb-9ffa-c1f69ad4f78a");
	private static final UUID bludgeUUID = UUID.fromString("2b03b755-d1d8-48ae-a837-24121e64eaec");
	private static final UUID slashUUID = UUID.fromString("d3bba132-cba4-4089-83ae-86035b8a6154");
	private static final UUID pierceUUID = UUID.fromString("869b0fcd-7bf1-4218-b560-e0328a45765d");
	
	private static final UUID vanillaSpeedUUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
	private static Field speedField = null;
	static
	{
		speedField = ObfuscationReflectionHelper.findField(ItemTool.class, "field_185065_c");
	}
	
	@SubscribeEvent
	public void onEntityConstruction(EntityConstructing evt)
	{
		Entity entity = evt.getEntity();
		if(entity instanceof EntityLivingBase)
		{
			EntityLivingBase livingEntity = (EntityLivingBase) entity;
			AbstractAttributeMap attributes = livingEntity.getAttributeMap();
			attributes.registerAttribute(SLASHING);
			attributes.registerAttribute(BLUDGEONING);
			attributes.registerAttribute(PIERCING);
			attributes.registerAttribute(SLASHING_DMG);
			attributes.registerAttribute(BLUDGEONING_DMG);
			attributes.registerAttribute(PIERCING_DMG);
			if(!(entity instanceof EntityPlayer))
			{
				ResourceLocation loc = EntityList.getKey(livingEntity);
				if(loc != null)
				{
					ResistanceCategories baseResist = DistinctDamageDescriptions.getMobResistances(loc.toString());
					attributes.getAttributeInstance(SLASHING).setBaseValue(baseResist.getSlashingResistance());
					attributes.getAttributeInstance(BLUDGEONING).setBaseValue(baseResist.getBludgeoningResistance());
					attributes.getAttributeInstance(PIERCING).setBaseValue(baseResist.getPiercingResistance());
					DamageCategories baseDamage = DistinctDamageDescriptions.getMobDamage(loc.toString());
					if(baseDamage != null)
					{
						attributes.getAttributeInstance(SLASHING_DMG).setBaseValue(baseDamage.getSlashingDamage());
						attributes.getAttributeInstance(BLUDGEONING_DMG).setBaseValue(baseDamage.getBludgeoningDamage());
						attributes.getAttributeInstance(PIERCING_DMG).setBaseValue(baseDamage.getPiercingDamage());
					}
					else
					{
						setAttackDefault(attributes, livingEntity);
					}
				}
				else
				{
					setAttackDefault(attributes, livingEntity);
				}
			}
		}
	}
	
	
	
	@SubscribeEvent
	public void onCraft(PlayerEvent.ItemCraftedEvent evt)
	{
		checkAndAddAttributes(evt.crafting);
	}
	
	@SubscribeEvent
	public void onSmelt(PlayerEvent.ItemSmeltedEvent evt)
	{
		checkAndAddAttributes(evt.smelting);
	}
	
	@SubscribeEvent
	public void onPickup(PlayerEvent.ItemPickupEvent evt)
	{
		checkAndAddAttributes(evt.pickedUp.getItem());
	}
	
	@SubscribeEvent
	public void onTooltip(ItemTooltipEvent evt)
	{
		checkAndAddAttributes(evt.getItemStack());
	}

	private void checkAndAddAttributes(ItemStack itemStack)
	{
		NBTTagCompound tag = null;
		NBTTagList modifiers = null;
		boolean hasTag = false;
		
		if(itemStack.hasTagCompound())
		{
			tag = itemStack.getTagCompound();
			if(tag.getBoolean("DDDMod"))
			{
				return;
			}
			modifiers = tag.getTagList("AttributeModifiers", tag.getId());
			hasTag = true;
		}
		else
		{
			tag = new NBTTagCompound();
			modifiers = new NBTTagList();
			hasTag = false;
		}
		tag.setBoolean("DDDMod", true);
		Item item = itemStack.getItem();
		DamageCategories dmg = DistinctDamageDescriptions.getWeaponDamage(item.getRegistryName().toString());
		NBTTagCompound slash = buildNBTModifier(0, dmg.getSlashingDamage(), (int) slashUUID.getLeastSignificantBits(), (int) slashUUID.getMostSignificantBits(), SLASHING_DMG.getName(), SLASHING_DMG.getName(), EntityEquipmentSlot.MAINHAND);
		NBTTagCompound pierce = buildNBTModifier(0, dmg.getPiercingDamage(), (int) pierceUUID.getLeastSignificantBits(), (int) pierceUUID.getMostSignificantBits(), PIERCING_DMG.getName(), PIERCING_DMG.getName(), EntityEquipmentSlot.MAINHAND);
		NBTTagCompound bludge = buildNBTModifier(0, dmg.getBludgeoningDamage(), (int) bludgeUUID.getLeastSignificantBits(), (int) bludgeUUID.getMostSignificantBits(), BLUDGEONING_DMG.getName(), BLUDGEONING_DMG.getName(), EntityEquipmentSlot.MAINHAND);
		modifiers.appendTag(slash);
		modifiers.appendTag(pierce);
		modifiers.appendTag(bludge);
		if(item instanceof ItemArmor)
		{
			ItemArmor itemArmor = (ItemArmor) itemStack.getItem();
			EntityEquipmentSlot slot = itemArmor.getEquipmentSlot();
			ResistanceCategories resists = DistinctDamageDescriptions.getArmorResist(itemArmor.getRegistryName().toString());
			NBTTagCompound bludgeoningResistTag = buildNBTModifier(0, resists.getBludgeoningResistance(), (int) bludgeResistUUID.getLeastSignificantBits(), (int) bludgeResistUUID.getMostSignificantBits(), BLUDGEONING.getName(), BLUDGEONING.getName(), slot);
			NBTTagCompound piercingResistTag = buildNBTModifier(0, resists.getPiercingResistance(), (int) pierceResistUUID.getLeastSignificantBits(), (int) pierceResistUUID.getMostSignificantBits(), PIERCING.getName(), PIERCING.getName(), slot);
			NBTTagCompound slashingResistTag = buildNBTModifier(0, resists.getSlashingResistance(), (int) slashResistUUID.getLeastSignificantBits(), (int) slashResistUUID.getMostSignificantBits(), SLASHING.getName(), SLASHING.getName(), slot);
			if(hasTag)
			{
				boolean shouldAddArmor = true;
				boolean shouldAddToughness = true;
				for(NBTBase nbt : modifiers)
				{
					if(!(shouldAddArmor || shouldAddToughness))
					{
						break;
					}
					NBTTagCompound comp = (NBTTagCompound) nbt;
					if(comp.getString("AttributeName").equals("generic.armor"))
					{
						shouldAddArmor = false;
					}
					else if(comp.getString("AttributeName").equals("generic.armorToughness"))
					{
						shouldAddToughness = false;
					}
				}
				if(shouldAddArmor)
				{
					int armor = itemArmor.damageReduceAmount;
					NBTTagCompound armorTag = buildNBTModifier(0, armor, 747959, 306022, "generic.armor", "generic.armor", slot);
					modifiers.appendTag(armorTag);
				}
				if(shouldAddToughness)
				{
					float toughness = itemArmor.toughness;
					NBTTagCompound toughnessTag = buildNBTModifier(0, toughness, 201697, 678309, "generic.armorToughness", "generic.armorToughness", slot);
					modifiers.appendTag(toughnessTag);
				}
			}
			else
			{
				int armor = itemArmor.damageReduceAmount;
				float toughness = itemArmor.toughness;
				NBTTagCompound armorTag = buildNBTModifier(0, armor, 747959, 306022, "generic.armor", "generic.armor", slot);
				NBTTagCompound toughnessTag = buildNBTModifier(0, toughness, 201697, 678309, "generic.armorToughness", "generic.armorToughness", slot);
				modifiers.appendTag(armorTag);
				modifiers.appendTag(toughnessTag);
			}
			modifiers.appendTag(bludgeoningResistTag);
			modifiers.appendTag(piercingResistTag);
			modifiers.appendTag(slashingResistTag);
		}
		else if(item instanceof ItemTool || item instanceof ItemSword)
		{
			double amount = 0;
			if(item instanceof ItemTool)
			{
				ItemTool tool = (ItemTool) item;
				try
				{
					amount = speedField.getDouble(tool);
				}
				catch(IllegalAccessException e)
				{
					
				}
			}
			else
			{
				amount = -2.4; // this gives the default 1.6 swing speed for swords.
			}
			NBTTagCompound speed;
			boolean addAttackSpeed = true;
			if(hasTag)
			{
				for(NBTBase nbt : modifiers)
				{
					if(((NBTTagCompound) nbt).getString("AttributeName").equals("generic.attackSpeed"))
					{
						addAttackSpeed = false;
						break;
					}
				}
				if(addAttackSpeed)
				{
					speed = buildNBTModifier(0, amount, (int) vanillaSpeedUUID.getLeastSignificantBits(), (int) vanillaSpeedUUID.getMostSignificantBits(), "generic.attackSpeed", "generic.attackSpeed", EntityEquipmentSlot.MAINHAND);
					modifiers.appendTag(speed);
				}
			}
			else
			{
				speed = buildNBTModifier(0, amount, (int) vanillaSpeedUUID.getLeastSignificantBits(), (int) vanillaSpeedUUID.getMostSignificantBits(), "generic.attackSpeed", "generic.attackSpeed", EntityEquipmentSlot.MAINHAND);
				modifiers.appendTag(speed);
			}	
		}
		
		if(!(modifiers.hasNoTags()))
		{
			tag.setTag("AttributeModifiers", modifiers);
			itemStack.setTagCompound(tag);
		}
	}
	
	private NBTTagCompound buildNBTModifier(int op, double amount, int UUIDLeast, int UUIDMost, String attributeName, String name, EntityEquipmentSlot slot)
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("Operation", 0);
		tag.setDouble("Amount", amount);
		tag.setInteger("UUIDLeast", UUIDLeast);
		tag.setInteger("UUIDMost", UUIDMost);
		tag.setString("AttributeName", attributeName);
		tag.setString("Name", name);
		tag.setString("Slot", slot.getName());
		return tag;
	}
	
	private void setAttackDefault(AbstractAttributeMap attributes, EntityLivingBase livingEntity)
	{
		IAttributeInstance attack = livingEntity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		double base = attack != null ? attack.getBaseValue() : 1.0;
		attributes.getAttributeInstance(BLUDGEONING_DMG).setBaseValue(base);
	}
}
