package yeelp.distinctdamagedescriptions.integration.techguns;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.integration.techguns.TechgunsCompat.TechgunsConsts;

final class TechgunsReflectionHelper {
	
	private static TechgunsReflectionHelper instance;
	private final Class<?> tgDamageSource;
	private final Class<?> npcTGDamageSystem;
	private final Method attackEntityFrom, livingHurt;

	
	public static TechgunsReflectionHelper getInstance() {
		if(instance == null) {
			throw new RuntimeException("Reflection Helper for Techguns accessed before initialized!");
		}
		return instance;
	}
	
	private TechgunsReflectionHelper() throws ClassNotFoundException, NoSuchMethodException, SecurityException {
		this.tgDamageSource = Class.forName(TechgunsConsts.TG_DAMAGESOURCE_CLASS);
		this.npcTGDamageSystem = Class.forName(TechgunsConsts.INPCTG_DAMAGESYSTEM_CLASS);
		Class<?> damageSystem = Class.forName(TechgunsConsts.DAMAGESYSTEM_CLASS);
		this.attackEntityFrom = damageSystem.getMethod("attackEntityFrom", EntityLivingBase.class, DamageSource.class, float.class);
		this.livingHurt = damageSystem.getMethod("livingHurt", EntityLivingBase.class, DamageSource.class, float.class);
	}
	
	static void init() throws ClassNotFoundException, NoSuchMethodException, SecurityException {
		instance = new TechgunsReflectionHelper();
	}
	
	boolean isTGDamageSource(DamageSource src) {
		return this.tgDamageSource.isInstance(src);
	}
	
	boolean isNpcTGDamageSystem(EntityLivingBase entity) {
		return this.npcTGDamageSystem.isInstance(entity);
	}
	
	void invokeAttackEntityFrom(EntityLivingBase elb, DamageSource src, float amount) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.attackEntityFrom.invoke(null, elb, src, amount);
	}
	
	void invokeLivingHurt(EntityLivingBase elb, DamageSource src, float amount) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.livingHurt.invoke(null, elb, src, amount);
	}
}
