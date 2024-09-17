package yeelp.distinctdamagedescriptions.event;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.Event.HasResult;

/**
 * An event DDD fires client side to determine if it should draw damage type icons.
 * Mainly used for mod integration for when icons should never been drawn (no reason for icons, icons can't be drawn due to incorrect information, etc), even if enabled in the config.
 * 
 * This event is not {@link Cancelable}.
 * This event has a {@link Result} {@link HasResult}:
 * <br>
 * - {@link Result#ALLOW} or {@link Result#DEFAULT}: Icons are drawn as normal.
 * <br>
 * - {@link Result#DENY}: Icons are not drawn.
 * @author Yeelp
 *
 */
@HasResult
public class ShouldDrawIconsEvent extends Event {
	//intentionally empty (for now)
}
