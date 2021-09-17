package yeelp.distinctdamagedescriptions.integration.tic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.materials.MaterialTypes;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.TinkerToolCore;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.handlers.Handler;

public class TiCHelper extends Handler
{
	@SuppressWarnings("static-method")
	@SubscribeEvent
	public final void onCraft(ItemCraftedEvent evt) {
		if(evt.crafting.getItem() instanceof TinkerToolCore) {
			TinkerToolCore tool = (TinkerToolCore) evt.crafting.getItem();
			Iterator<Material> materials = TinkerUtil.getMaterialsFromTagList(TagUtil.getBaseMaterialsTagList(evt.crafting)).iterator();
			Iterator<PartMaterialType> parts = tool.getRequiredComponents().iterator();
			Set<String> materialIdentifiers = new HashSet<String>();
			while(materials.hasNext() && parts.hasNext()) {
				Material mat = materials.next();
				if(parts.next().usesStat(MaterialTypes.HEAD)) {
					materialIdentifiers.add(mat.identifier);
				}
			}
			materialIdentifiers.forEach(DistinctDamageDescriptions::debug);
		}
	}
	
	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public final void onTooltip(ItemTooltipEvent evt) {
		if(evt.getItemStack().getItem() instanceof TinkerToolCore) {
			Map<DDDDamageType, Float> map = new HashMap<DDDDamageType, Float>();
			map.put(DDDBuiltInDamageType.SLASHING, 1.0f);
			DDDAPI.accessor.getDamageDistribution(evt.getItemStack()).setNewWeights(map);			
		}
	}
}
