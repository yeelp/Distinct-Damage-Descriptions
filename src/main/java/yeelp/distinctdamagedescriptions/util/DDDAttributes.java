package yeelp.distinctdamagedescriptions.util;

import java.util.UUID;

import com.google.common.collect.Multimap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.handlers.Handler;

public class DDDAttributes extends Handler
{
	public static final IAttribute SLASHING = new RangedAttribute((IAttribute) null, "distinctdamagedescriptions.slashingResistance", 0.0, -2048.0, 2048.0).setShouldWatch(true);
	public static final IAttribute BLUDGEONING = new RangedAttribute((IAttribute) null, "distinctdamagedescriptions.bludgeoningResistance", 0.0, -2048.0, 2048.0).setShouldWatch(true);
	public static final IAttribute PIERCING = new RangedAttribute((IAttribute) null, "distinctdamagedescriptions.piercingResistance", 0.0, -2048.0, 2048.0).setShouldWatch(true);
	public static final IAttribute SLASHING_DMG = new RangedAttribute((IAttribute) null, "distinctdamagedescriptions.slashingDmg", 0.0, -2048.0, 2048.0).setShouldWatch(true);
	public static final IAttribute BLUDGEONING_DMG = new RangedAttribute((IAttribute) null, "distinctdamagedescriptions.bludgeoningDmg", 0.0, -2048.0, 2048.0).setShouldWatch(true);
	public static final IAttribute PIERCING_DMG = new RangedAttribute((IAttribute) null, "distinctdamagedescriptions.piercingDmg", 0.0, -2048.0, 2048.0).setShouldWatch(true);	
	
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
			ResourceLocation loc = EntityList.getKey(livingEntity);
			if(loc != null)
			{
				Tuple<BaseDamage, BaseResistances> properties = DistinctDamageDescriptions.getProperties(loc.toString());
				BaseResistances baseResist = properties.getSecond();
				attributes.getAttributeInstance(SLASHING).setBaseValue(baseResist.getSlashingResistance());
				attributes.getAttributeInstance(BLUDGEONING).setBaseValue(baseResist.getBludgeoningResistance());
				attributes.getAttributeInstance(PIERCING).setBaseValue(baseResist.getPiercingResistance());
				BaseDamage baseDamage = properties.getFirst();
				attributes.getAttributeInstance(SLASHING_DMG).setBaseValue(baseDamage.getSlashingDamage());
				attributes.getAttributeInstance(BLUDGEONING_DMG).setBaseValue(baseDamage.getBludgeoningDamage());
				attributes.getAttributeInstance(PIERCING_DMG).setBaseValue(baseDamage.getPiercingDamage());
			}
			else
			{
				attributes.getAttributeInstance(BLUDGEONING_DMG).setBaseValue(livingEntity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue());
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
		if(itemStack.getItem() instanceof ItemArmor)
		{
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
		}
	}
}
