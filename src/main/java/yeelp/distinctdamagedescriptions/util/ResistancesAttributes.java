package yeelp.distinctdamagedescriptions.util;

import java.util.UUID;

import com.google.common.collect.Multimap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.handlers.Handler;

public class ResistancesAttributes extends Handler
{
	public static final IAttribute SLASHING = new RangedAttribute((IAttribute) null, "distinctdamagedescriptions.slashingResistance", 1.0, -2048.0, 2048.0).setShouldWatch(true);
	public static final IAttribute BLUDGEONING = new RangedAttribute((IAttribute) null, "distinctdamagedescriptions.bludgeoningResistance", 1.0, -2048.0, 2048.0).setShouldWatch(true);
	public static final IAttribute PIERCING = new RangedAttribute((IAttribute) null, "distinctdamagedescriptions.piercingResistance", 1.0, -2048.0, 2048.0).setShouldWatch(true);

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
			ResourceLocation loc = EntityList.getKey(livingEntity);
			if(loc != null)
			{
				BaseResistances base = DistinctDamageDescriptions.getResistances(loc.toString());
				if(base != null)
				{
					attributes.getAttributeInstance(SLASHING).applyModifier(new AttributeModifier(UUID.fromString("2a76290e-685c-4ba0-ade2-92ea2a2fdf43"), "base", base.getSlashingResistance(), 2));
					attributes.getAttributeInstance(BLUDGEONING).applyModifier(new AttributeModifier(UUID.fromString("76e37dab-3b82-472e-b8cc-e61a0488a7ba"), "base", base.getSlashingResistance(), 2));
					attributes.getAttributeInstance(PIERCING).applyModifier(new AttributeModifier(UUID.fromString("d529bf5e-07a0-4d30-9d03-8b9d0d8131e8"), "base", base.getSlashingResistance(), 2));
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
		if(itemStack.getItem() instanceof ItemArmor)
		{
			EntityEquipmentSlot slot = ((ItemArmor) itemStack.getItem()).armorType;
			if(slot == EntityEquipmentSlot.HEAD)
			{
				return;
			}
			Multimap<String, AttributeModifier> map = itemStack.getAttributeModifiers(slot);
			if(map.containsKey("distinctdamagedescriptions.bludgeoningResistance"))
			{
				return;
			}
			else
			{
				itemStack.addAttributeModifier("distinctdamagedescriptions.bludgeoningResistance", new AttributeModifier(UUID.fromString("cefefd21-3cee-4398-b65b-04c03e1cd4e0"), "armorModifier", 0.1, 2), slot);
			}
		}
	}
}
