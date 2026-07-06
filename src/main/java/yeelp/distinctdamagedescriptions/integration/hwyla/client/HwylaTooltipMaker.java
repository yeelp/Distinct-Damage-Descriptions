package yeelp.distinctdamagedescriptions.integration.hwyla.client;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import yeelp.distinctdamagedescriptions.integration.hwyla.Hwyla.HwylaConsts;
import yeelp.distinctdamagedescriptions.integration.hwyla.IHwylaTooltipInjectors.IHwylaArmorTooltipInjector;

import java.util.List;

public final class HwylaTooltipMaker {
	private static final HwylaMobResistanceFormatter resistances = HwylaMobResistanceFormatter.getInstance();
	private static final HwylaMobDamageFormatter damage = HwylaMobDamageFormatter.getInstance();

	/**
	 * Make a tooltip addition for HWYLA.
	 * 
	 * @param entity entity being looked at
	 * @param nbt NBT data from the server that is relevant
	 * @return a List with all the HWYLA info for this mob, if applicable, otherwise
	 *         an empty list.
	 */
	public static List<String> makeHwylaTooltipStrings(EntityLivingBase entity, NBTTagCompound nbt) {
		List<String> tip = Lists.newArrayList();
		if(damage.enabled()) {
			tip.addAll(damage.format(entity));
		}
		if(resistances.enabled()) {
			tip.addAll(resistances.format(entity, nbt.getCompoundTag(HwylaConsts.HWYLA_RESISTS)));
		}
		return tip;
	}
	
	public static void registerHwylaArmorTooltipInjector(IHwylaArmorTooltipInjector injector) {
		HwylaMobResistanceFormatter.registerArmorTooltipInjector(injector);
	}

}
