package yeelp.distinctdamagedescriptions.integration.hwyla;

import java.util.List;

import javax.annotation.Nonnull;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import yeelp.distinctdamagedescriptions.integration.hwyla.client.HwylaTooltipMaker;

public class EntityHandler implements IWailaEntityProvider {
	public EntityHandler() {
	}

	/**
	 * Callback used to add lines to one of the three sections of the tooltip (Head,
	 * Body, Tail).</br>
	 * Will only be called if the implementing class is registered via
	 * {@link IWailaRegistrar#registerBodyProvider}.</br>
	 * You are supposed to always return the modified input currenttip.</br>
	 * <p>
	 * You may return null if you have not registered this as a body provider.
	 * However, you should return the provided list to be safe.
	 * <p>
	 * This method is only called on the client side. If you require data from the
	 * server, you should also implement
	 * {@link #getNBTData(EntityPlayerMP, Entity, NBTTagCompound, World)} and add
	 * the data to the {@link NBTTagCompound} there, which can then be read back
	 * using {@link IWailaDataAccessor#getNBTData()}. If you rely on the client
	 * knowing the data you need, you are not guaranteed to have the proper values.
	 *
	 * @param entity     Current Entity scanned.
	 * @param currenttip Current list of tooltip lines (might have been processed by
	 *                   other providers and might be processed by other providers).
	 * @param accessor   Contains most of the relevant information about the current
	 *                   environment.
	 * @param config     Current configuration of Waila.
	 * @return Modified input currenttip
	 */
	@Nonnull
	@Override
	public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		if(entity instanceof EntityLivingBase) {
			currenttip.addAll(HwylaTooltipMaker.makeHwylaTooltipStrings((EntityLivingBase) accessor.getEntity()));
		}
		return currenttip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) {
		return tag;
	}

}
