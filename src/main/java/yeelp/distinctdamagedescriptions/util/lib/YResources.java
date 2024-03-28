package yeelp.distinctdamagedescriptions.util.lib;

import java.util.Arrays;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.base.Functions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

/**
 * A collection of useful methods for dealing with ResourceLocations. Mainly to
 * reduce successive calls like:
 * {@code stack.getItem().getRegistryName().toString()}, at the cost of a couple
 * stack frames.
 * 
 * @author Yeelp
 *
 */
public final class YResources {
	public static Optional<ResourceLocation> getRegistryName(ItemStack stack) {
		return getRegistryName(stack.getItem());
	}

	public static Optional<ResourceLocation> getRegistryName(Item item) {
		return Optional.ofNullable(item.getRegistryName());
	}

	public static Optional<String> getRegistryString(ItemStack stack) {
		return getRegistryName(stack.getItem()).map(Functions.toStringFunction());
	}

	public static Optional<String> getRegistryString(Item item) {
		return Optional.ofNullable(item.getRegistryName().toString());
	}

	public static Optional<ResourceLocation> getEntityID(@Nullable Entity entity) {
		return Optional.ofNullable(entity).flatMap(Functions.compose(Optional::ofNullable, EntityList::getKey));
	}

	public static Optional<String> getEntityIDString(@Nullable Entity entity) {
		return getEntityID(entity).map(Functions.toStringFunction());
	}

	public static String[] getOreDictEntries(ItemStack stack) {
		return Arrays.stream(OreDictionary.getOreIDs(stack)).mapToObj(OreDictionary::getOreName).toArray(String[]::new);
	}
}
