package yeelp.distinctdamagedescriptions.event.classification;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Base event for all classification DDD does to let other mods and features hook into
 * this to alter information during damage calculations. <br>
 * Some of these events are {@link Cancelable}. <br>
 * None of these events have a result {@link HasResult}. <br>
 * All children are fired on the {@link MinecraftForge#EVENT_BUS}.
 * 
 * @author Yeelp
 *
 */
public abstract class DDDClassificationEvent extends Event {

	private final Entity attacker, trueAttacker;
	private final EntityLivingBase defender;
	private final DamageSource src;

	protected DDDClassificationEvent(Entity attacker, Entity trueAttacker, EntityLivingBase defender, DamageSource src) {
		super();
		this.attacker = attacker;
		this.defender = defender;
		this.trueAttacker = trueAttacker;
		this.src = src;
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
}
