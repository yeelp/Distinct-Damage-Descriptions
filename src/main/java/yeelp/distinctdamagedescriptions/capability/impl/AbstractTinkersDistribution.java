package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;

/**
 * An abstract distribution for tinker items
 * @author Yeelp
 *
 * @param <D> The kind of distribution this is
 * @param <C> The values the config stores
 */
public abstract class AbstractTinkersDistribution<D extends IDistribution, C> extends DistributionRequiresUpdate<D> {
	
	protected abstract String getPartType();
	
	protected abstract Iterator<PartMaterialType> getParts(ItemStack stack);
	
	protected abstract IDDDConfiguration<C> getConfiguration();
	
	protected abstract Optional<Map<DDDDamageType, Float>> determineNewMap(ItemStack stack, Collection<String> mats, IDDDConfiguration<C> config);

	@Override
	protected Optional<Map<DDDDamageType, Float>> calculateNewMap(ItemStack stack) {
		return this.determineNewMap(stack, this.getKeyMaterialIdentifiers(stack), this.getConfiguration());
	}
	
	protected final Collection<String> getKeyMaterialIdentifiers(ItemStack stack) {
		Iterator<Material> materials = TinkerUtil.getMaterialsFromTagList(TagUtil.getBaseMaterialsTagList(stack)).iterator();
		Iterator<PartMaterialType> parts = this.getParts(stack);
		Set<String> identifiers = new HashSet<String>();
		while(parts.hasNext() && materials.hasNext()) {
			Material mat = materials.next();
			if(parts.next().usesStat(this.getPartType())) {
				identifiers.add(mat.identifier);
			}
		}
		return identifiers;
	}
}
