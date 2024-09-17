package yeelp.distinctdamagedescriptions.integration.firstaid;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import ichttt.mods.firstaid.api.enums.EnumPlayerPart;
import ichttt.mods.firstaid.common.damagesystem.distribution.DamageDistribution;
import net.minecraft.inventory.EntityEquipmentSlot;

final class FirstAidReflectionHelper {

	private static FirstAidReflectionHelper instance;
	private Method getPartListMethod;
	
	public static FirstAidReflectionHelper getInstance() {
		if(instance == null) {
			throw new RuntimeException("Reflection Helper for FirstAid accessed before initialized!");
		}
		return instance;
	}
	
	private FirstAidReflectionHelper() throws NoSuchMethodException, SecurityException {
		this.getPartListMethod = DamageDistribution.class.getDeclaredMethod("getPartList", new Class<?>[0]);
		this.getPartListMethod.setAccessible(true);
	}
	
	static void init() throws NoSuchMethodException, SecurityException {
		instance = new FirstAidReflectionHelper();
	}
	
	@SuppressWarnings("unchecked")
	@Nonnull
	List<Pair<EntityEquipmentSlot, EnumPlayerPart[]>> getPartList(DamageDistribution dist) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return (List<Pair<EntityEquipmentSlot, EnumPlayerPart[]>>)this.getPartListMethod.invoke(dist, new Object[0]);
	}
}
