package yeelp.distinctdamagedescriptions.integration.crafttweaker.capabilities;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import stanhebben.zenscript.annotations.IterableMap;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.DefaultResistances;
import yeelp.distinctdamagedescriptions.capability.impl.MobResistances;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDDDDamageType;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.CTDDDDamageType;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

@ZenClass("mods.ddd.Resistances")
@IterableMap(key = "mods.ddd.IDDDDamageType", value = "float")
@ZenRegister
public class CTResistances extends NonNullMap<ICTDDDDamageType, Float> {
	private final IMobResistances resists;

	private final EntityPlayerMP player;

	private final boolean isPlayer;

	public CTResistances(IEntityLivingBase entityLiving) {
		super(() -> 0.0f);
		EntityLivingBase base = CraftTweakerMC.getEntityLivingBase(entityLiving);
		this.resists = DDDAPI.accessor.getMobResistances(base).orElseGet(MobResistances::new);
		this.resists.getAllResistances().forEach((t, f) -> super.put(CTDDDDamageType.getFromDamageType(t), f));
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
		this.update();
	}
	
	@ZenMethod("removeResistance")
	public void removeResistance(ICTDDDDamageType type) {
		this.resists.removeResistance(type.asDDDDamageType());
		this.update();
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
		this.update();
	}

	@ZenSetter("adaptabilityAmount")
	public void setAdaptabilityAmount(float amount) {
		this.resists.setAdaptiveAmount(amount);
		this.update();
	}

	@ZenMethod("setImmunity")
	public void setImmunity(ICTDDDDamageType type, boolean status) {
		this.resists.setImmunity(type.asDDDDamageType(), status);
		this.update();
	}
	
	@ZenMethod
	public boolean isDefault() {
		return this.resists == DefaultResistances.getInstance();
	}

	private void update() {
		if(this.isPlayer) {
			this.resists.sync(this.player);
		}
	}
}
