package yeelp.distinctdamagedescriptions.event.calculation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.eventhandler.Event;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.DamageMap;

public abstract class DDDCalculationEvent extends Event {
	private final Entity attacker;
	private final Entity trueAttacker;
	private final EntityLivingBase defender;
	private final DamageSource src;
	protected final DamageMap dmg;
	
	protected DDDCalculationEvent(Entity attacker, Entity trueAttacker, EntityLivingBase defender, DamageSource src, DamageMap dmg) {
		this.attacker = attacker;
		this.trueAttacker = trueAttacker;
		this.defender = defender;
		this.src = src;
		this.dmg = dmg;
	}
	
	@Nonnull
	public final EntityLivingBase getDefender() {
		return this.defender;
	}
	
	@Nullable
	public final Entity getImmediateAttacker() {
		return this.attacker;
	}
	
	@Nullable
	public final Entity getTrueAttacker() {
		return this.trueAttacker;
	}
	
	public final DamageSource getSource() {
		return this.src;
	}
	
	public final float getDamage(DDDDamageType type) {
		return this.dmg.get(type);
	}
}
