package yeelp.distinctdamagedescriptions.util;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
			ResourceLocation loc = EntityList.getKey(entity);
			BaseResistances base = DistinctDamageDescriptions.getResistances(loc.toString());
			AbstractAttributeMap attributes = livingEntity.getAttributeMap();
			attributes.registerAttribute(SLASHING);
			attributes.registerAttribute(BLUDGEONING);
			attributes.registerAttribute(PIERCING);
			if(base != null)
			{
				attributes.getAttributeInstance(SLASHING).applyModifier(new AttributeModifier(UUID.fromString("2a76290e-685c-4ba0-ade2-92ea2a2fdf43"), "base", base.getSlashingResistance(), 2));
				attributes.getAttributeInstance(BLUDGEONING).applyModifier(new AttributeModifier(UUID.fromString("76e37dab-3b82-472e-b8cc-e61a0488a7ba"), "base", base.getSlashingResistance(), 2));
				attributes.getAttributeInstance(PIERCING).applyModifier(new AttributeModifier(UUID.fromString("d529bf5e-07a0-4d30-9d03-8b9d0d8131e8"), "base", base.getSlashingResistance(), 2));
			}
		}
	}
}
