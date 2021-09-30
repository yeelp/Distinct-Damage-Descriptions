package yeelp.distinctdamagedescriptions.capability.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.config.TiCConfigurations;
import yeelp.distinctdamagedescriptions.util.DistributionBias;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

/**
 * An abstract distribution for tinker items
 * @author Yeelp
 *
 * @param <D> The kind of distribution this is
 */
public abstract class AbstractTinkersDistribution<D extends IDistribution> extends DistributionRequiresUpdate<D> {
	
	protected abstract String getPartType();
	
	protected abstract Iterator<PartMaterialType> getParts(ItemStack stack);
	
	protected abstract IDDDConfiguration<DistributionBias> getConfiguration();

	@Override
	protected Optional<Map<DDDDamageType, Float>> calculateNewMap(ItemStack stack) {
		Collection<String> identifiers = getHeadMaterialIdentifiers(stack);
		D cap = this.getDistributionCapabilityOnStack(stack);
		float biasResistance = TiCConfigurations.biasResistance.getOrFallbackToDefault(YResources.getRegistryString(stack));
		if(identifiers.size() == 1) {
			return this.getConfiguration().get(identifiers.iterator().next()).getBiasedDistributionMap(cap, biasResistance);
		}
		Set<Set<Map.Entry<DDDDamageType, Float>>> applicableDists = identifiers.stream().map((s) -> this.getConfiguration().getOrFallbackToDefault(s).getBiasedDistributionMap(cap, biasResistance).map(Map::entrySet).orElse(new HashSet<Map.Entry<DDDDamageType, Float>>())).filter((s) -> !s.isEmpty()).collect(Collectors.toSet());
		int numSets = applicableDists.size();
		if(numSets == 0) {
			return Optional.empty();
		}
		Map<DDDDamageType, Float> map = new HashMap<DDDDamageType, Float>();	
		for(Set<Map.Entry<DDDDamageType, Float>> set : applicableDists) {
			set.stream().forEach((e) -> map.merge(e.getKey(), e.getValue()/numSets, Float::sum));
		}
		return Optional.of(map);
	}
	
	protected final Collection<String> getHeadMaterialIdentifiers(ItemStack stack) {
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
