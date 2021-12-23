package yeelp.distinctdamagedescriptions.event.classification;

import java.util.Objects;

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
 * None of these events are {@link Cancelable}. <br>
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

	protected DDDClassificationEvent(Entity attacker, Entity trueAttacker, @Nonnull EntityLivingBase defender, @Nonnull DamageSource src) {
		super();
		this.attacker = attacker;
		this.trueAttacker = trueAttacker;
		this.defender = Objects.requireNonNull(defender, "Defender can't be null!");
		this.src = Objects.requireNonNull(src, "Source can't be null!");
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
	
	@Nonnull
	public final DamageSource getSource() {
		return this.src;
	}
}
