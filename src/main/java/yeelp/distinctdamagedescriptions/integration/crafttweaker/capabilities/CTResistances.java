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
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.MobResistances;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDDDDamageType;

@ZenClass("mods.ddd.Resistances")
@ZenRegister
public class CTResistances {
	private final IMobResistances resists;

	private final EntityPlayerMP player;

	private final boolean isPlayer;

	public CTResistances(IEntityLivingBase entityLiving) {
		EntityLivingBase base = CraftTweakerMC.getEntityLivingBase(entityLiving);
		this.resists = DDDAPI.accessor.getMobResistances(base).orElseGet(MobResistances::new);
		this.player = base instanceof EntityPlayerMP ? (EntityPlayerMP) base : null;
		this.isPlayer = this.player != null ? true : false;
	}

	@ZenMethod("getResistance")
	public float getResistance(ICTDDDDamageType type) {
		return this.resists.getResistance(type.asDDDDamageType());
	}

	@ZenMethod("setResistance")
	public void setResistance(ICTDDDDamageType type, float amount) {
		this.resists.setResistance(type.asDDDDamageType(), amount);
		update();
	}

	@ZenGetter("adaptability")
	public boolean getAdaptability() {
		return this.resists.hasAdaptiveResistance();
	}

	@ZenGetter("adaptabilityAmount")
	public float getAdaptabilityAmount() {
		return this.resists.getAdaptiveAmount();
	}

	@ZenMethod("hasImmunity")
	public boolean hasImmunity(ICTDDDDamageType type) {
		return this.resists.hasImmunity(type.asDDDDamageType());
	}

	@ZenSetter("adaptability")
	public void setAdaptability(boolean status) {
		this.resists.setAdaptiveResistance(status);
		update();
	}

	@ZenSetter("adaptabilityAmount")
	public void setAdaptabilityAmount(float amount) {
		this.resists.setAdaptiveAmount(amount);
		update();
	}

	@ZenMethod("setImmunity")
	public void setImmunity(ICTDDDDamageType type, boolean status) {
		this.resists.setImmunity(type.asDDDDamageType(), status);
		update();
	}

	private void update() {
		if(this.isPlayer) {
			this.resists.sync(this.player);
		}
	}
}
