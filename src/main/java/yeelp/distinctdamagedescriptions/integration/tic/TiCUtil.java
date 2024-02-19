package yeelp.distinctdamagedescriptions.integration.tic;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Sets;

import c4.conarm.lib.tinkering.TinkersArmor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.client.CustomFontColor;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.TinkerToolCore;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.IDDDConfiguration;
import yeelp.distinctdamagedescriptions.util.lib.DDDBaseMap;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

public final class TiCUtil {

	private TiCUtil() {
		throw new RuntimeException("Class can't be instatiated!");
	}

	public static Iterator<PartMaterialType> getParts(ItemStack stack) {
		return getParts(stack.getItem());
	}

	public static Iterator<PartMaterialType> getParts(Item item) {
		if(item instanceof TinkerToolCore) {
			TinkerToolCore tool = (TinkerToolCore) item;
			return getParts(tool);
		}
		if(item instanceof TinkersArmor) {
			TinkersArmor armor = (TinkersArmor) item;
			return getParts(armor);
		}
		return Collections.emptyIterator();
	}

	public static Iterator<PartMaterialType> getParts(TinkerToolCore tool) {
		return tool.getRequiredComponents().iterator();
	}

	public static Iterator<PartMaterialType> getParts(TinkersArmor armor) {
		return armor.getRequiredComponents().iterator();
	}

	public static Collection<String> getKeyMaterialIdentifiers(ItemStack stack, String partType) {
		Iterator<Material> materials = TinkerUtil.getMaterialsFromTagList(TagUtil.getBaseMaterialsTagList(stack)).iterator();
		Iterator<PartMaterialType> parts = getParts(stack);
		Set<String> identifiers = Sets.newHashSet();
		while(parts.hasNext() && materials.hasNext()) {
			Material mat = materials.next();
			if(parts.next().usesStat(partType)) {
				identifiers.add(mat.identifier);
			}
		}
		return identifiers;
	}

	public static DDDBaseMap<Float> getBaseDist(ItemStack stack) {
		return getBaseDist(stack.getItem());
	}

	public static DDDBaseMap<Float> getBaseDist(Item item) {
		String reg = YResources.getRegistryString(item);
		if(item instanceof TinkerToolCore) {
			return getMap(reg, DDDConfigurations.items);
		}
		if(item instanceof TinkersArmor) {
			return getMap(reg, DDDConfigurations.armors);
		}
		return new DDDBaseMap<Float>(() -> 0.0f);
	}

	public static String getDDDDamageTypeNameColoured(DDDDamageType type) {
		return (type.getColour() == 0xffffff ? encodeColour(0) : encodeColour(type.getColour())) + TextFormatting.BOLD + type.getDisplayName() + TextFormatting.RESET;
	}

	private static String encodeColour(int colour) {
		return CustomFontColor.encodeColor(colour);
	}

	private static <Dist extends IDistribution> DDDBaseMap<Float> getMap(String key, IDDDConfiguration<Dist> config) {
		Dist d = config.getOrFallbackToDefault(key);
		return d.getCategories().stream().collect(DDDBaseMap.typesToDDDBaseMap(() -> 0.0f, d::getWeight));
	}
}
