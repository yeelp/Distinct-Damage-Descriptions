package yeelp.distinctdamagedescriptions.handlers;

import net.minecraftforge.common.MinecraftForge;

public abstract class Handler {
	public boolean register() {
		MinecraftForge.EVENT_BUS.register(this);

		return true;
	}
}
