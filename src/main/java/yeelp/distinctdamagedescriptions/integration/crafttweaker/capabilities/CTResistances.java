package yeelp.distinctdamagedescriptions.integration.crafttweaker.capabilities;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.handlers.CapabilityHandler;
import yeelp.distinctdamagedescriptions.util.IMobResistances;

@ZenClass("mods.ddd.Resistances")
@ZenRegister
public class CTResistances
{
	private final IEntityLivingBase entityLiving;
	private final IMobResistances resists;
	
	private final EntityPlayerMP player;
	
	private final boolean isPlayer;
	
	public CTResistances(IEntityLivingBase entityLiving)
	{
		EntityLivingBase base = CraftTweakerMC.getEntityLivingBase(entityLiving);
		this.entityLiving = entityLiving;
		this.resists = DDDAPI.accessor.getMobResistances(base);
		this.player = base instanceof EntityPlayerMP ? (EntityPlayerMP) base : null;
		this.isPlayer = this.player != null ? true : false;
	}
	
	@ZenMethod("getResistance")
	public float getResistance(String type)
	{
		return this.resists.getResistance(type);
	}
	
	@ZenMethod("setResistance")
	public void setResistance(String type, float amount)
	{
		this.resists.setResistance(type, amount);
		update();
	}
	
	@ZenGetter("adaptability")
	public boolean getAdaptability()
	{
		return this.resists.hasAdaptiveResistance();
	}
	
	@ZenGetter("adaptabilityAmount")
	public float getAdaptabilityAmount()
	{
		return this.resists.getAdaptiveAmount();
	}
	
	@ZenMethod("hasImmunity")
	public boolean hasImmunity(String type)
	{
		return this.resists.hasImmunity(type);
	}
	
	@ZenSetter("adaptability")
	public void setAdaptability(boolean status)
	{
		this.resists.setAdaptiveResistance(status);
		update();
	}
	
	@ZenSetter("adaptabilityAmount")
	public void setAdaptabilityAmount(float amount)
	{
		this.resists.setAdaptiveAmount(amount);
		update();
	}
	
	@ZenMethod("setImmunity")
	public void setSlashingImmunity(String type, boolean status)
	{
		this.resists.setImmunity(type, status);
		update();
	}
	
	private void update()
	{
		if(isPlayer)
		{
			CapabilityHandler.syncResistances(player);
		}
	}
}
