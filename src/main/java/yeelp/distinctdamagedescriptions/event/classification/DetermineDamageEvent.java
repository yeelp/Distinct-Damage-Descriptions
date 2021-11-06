package yeelp.distinctdamagedescriptions.event.classification;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.DamageMap;

public final class DetermineDamageEvent extends DDDClassificationEvent {

	private final DamageMap map;
	public DetermineDamageEvent(Entity attacker, Entity trueAttacker, EntityLivingBase defender, DamageSource src, DamageMap map) {
		super(attacker, trueAttacker, defender, src);
		this.map = map;
	}

	public float getDamage(DDDDamageType type) {
		return this.map.get(type);
	}
	
	public void setDamage(DDDDamageType type, float amount) {
		this.map.put(type, amount);
	}
}
