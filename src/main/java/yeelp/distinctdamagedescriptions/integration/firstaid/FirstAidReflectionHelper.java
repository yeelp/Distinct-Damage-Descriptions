package yeelp.distinctdamagedescriptions.integration.firstaid;

import ichttt.mods.firstaid.api.enums.EnumPlayerPart;
import ichttt.mods.firstaid.common.damagesystem.distribution.DamageDistribution;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

final class FirstAidReflectionHelper {

	private static FirstAidReflectionHelper instance;
	private final Method getPartListMethod;
	
	public static FirstAidReflectionHelper getInstance() {
		if(instance == null) {
			throw new RuntimeException("Reflection Helper for FirstAid accessed before initialized!");
		}
		return instance;
	}
	
	private FirstAidReflectionHelper() throws NoSuchMethodException {
		this.getPartListMethod = DamageDistribution.class.getDeclaredMethod("getPartList");
		this.getPartListMethod.setAccessible(true);
	}
	
	static void init() throws NoSuchMethodException {
		instance = new FirstAidReflectionHelper();
	}
	
	@SuppressWarnings("unchecked")
	@Nonnull
	List<Pair<EntityEquipmentSlot, EnumPlayerPart[]>> getPartList(DamageDistribution dist) throws IllegalAccessException, InvocationTargetException {
		return (List<Pair<EntityEquipmentSlot, EnumPlayerPart[]>>)this.getPartListMethod.invoke(dist, new Object[0]);
	}
}
