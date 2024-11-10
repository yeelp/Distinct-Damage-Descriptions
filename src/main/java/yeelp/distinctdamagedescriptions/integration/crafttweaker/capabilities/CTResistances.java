package yeelp.distinctdamagedescriptions.integration.crafttweaker.capabilities;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.EntityLivingBase;
import stanhebben.zenscript.annotations.IterableMap;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.DefaultResistances;
import yeelp.distinctdamagedescriptions.capability.impl.MobResistances;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.CTConsts;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.ICTDDDDamageType;
import yeelp.distinctdamagedescriptions.integration.crafttweaker.types.impl.CTDDDDamageType;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;

@ZenClass(CTConsts.CTClasses.CTRESISTANCES)
@IterableMap(key = CTConsts.CTClasses.CTDAMAGETYPE, value = "float")
@ZenRegister
public class CTResistances extends NonNullMap<ICTDDDDamageType, Float> {
	private final IMobResistances resists;


	public CTResistances(IEntityLivingBase entityLiving) {
		super(() -> 0.0f);
		EntityLivingBase base = CraftTweakerMC.getEntityLivingBase(entityLiving);
		this.resists = DDDAPI.accessor.getMobResistances(base).orElseGet(MobResistances::new);
		this.resists.getAllResistancesCopy().forEach((t, f) -> super.put(CTDDDDamageType.getFromDamageType(t), f));
	}

	@ZenMethod("getResistance")
	public float getResistance(ICTDDDDamageType type) {
		return this.resists.getResistance(type.asDDDDamageType());
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
	
	@ZenMethod
	public boolean isDefault() {
		return this.resists == DefaultResistances.getInstance();
	}
}
