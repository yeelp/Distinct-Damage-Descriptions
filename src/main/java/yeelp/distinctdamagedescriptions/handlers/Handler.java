package yeelp.distinctdamagedescriptions.handlers;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;

public abstract class Handler {

	private boolean registered = false;
	public Handler() {
		if(this.getClass().isAnnotationPresent(EventBusSubscriber.class)) {
			String msg = String.format("%s shouldn't extend Handler if it uses EventBusSubscriber", this.getClass().getName());
			DistinctDamageDescriptions.err(msg);
			throw new UnsupportedOperationException(msg);
		}
	}

	public boolean register() {
		if(!this.registered) {
			MinecraftForge.EVENT_BUS.register(this);
			this.registered = true;
			return true;
		}
		DistinctDamageDescriptions.err("This Handler shouldn't be registered twice!");
		return false;
	}
}
