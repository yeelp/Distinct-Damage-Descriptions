package yeelp.distinctdamagedescriptions.integration.crafttweaker.events;

import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import yeelp.distinctdamagedescriptions.event.DamageDescriptionEvent;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

public abstract class CTDDDEvent implements IDDDEvent {
	private final DamageDescriptionEvent internal;

	CTDDDEvent(DamageDescriptionEvent evt) {
		this.internal = evt;
	}

	@Override
	public IEntity getEntity() {
		return CraftTweakerMC.getIEntity(this.internal.getAttacker());
	}

	@Override
	public IEntityLivingBase getDefender() {
		return CraftTweakerMC.getIEntityLivingBase(this.internal.getDefender());
	}

	@Override
	public float getDamage(String type) {
		return this.internal.getDamage(DDDRegistries.damageTypes.get(type));
	}

	@Override
	public void setDamage(String type, float damage) {
		this.internal.setDamage(DDDRegistries.damageTypes.get(type), damage);
	}

	@Override
	public float getResistance(String type) {
		return this.internal.getResistance(DDDRegistries.damageTypes.get(type));
	}

	@Override
	public void setResistance(String type, float resistance) {
		this.internal.setResistance(DDDRegistries.damageTypes.get(type), resistance);
	}

	@Override
	public float getArmor(String type) {
		return this.internal.getArmorAndToughness(DDDRegistries.damageTypes.get(type)).getArmor();
	}

	@Override
	public void setArmor(String type, float armor) {
		this.internal.setArmorAndToughness(DDDRegistries.damageTypes.get(type), armor, getToughness(type));
	}

	@Override
	public float getToughness(String type) {
		return this.internal.getArmorAndToughness(DDDRegistries.damageTypes.get(type)).getToughness();
	}

	@Override
	public void setToughness(String type, float toughness) {
		this.internal.setArmorAndToughness(DDDRegistries.damageTypes.get(type), getArmor(type), toughness);
	}
}
