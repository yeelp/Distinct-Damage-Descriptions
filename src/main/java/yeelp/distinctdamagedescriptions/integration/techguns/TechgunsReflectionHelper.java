package yeelp.distinctdamagedescriptions.integration.techguns;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import yeelp.distinctdamagedescriptions.integration.techguns.TechgunsCompat.TechgunsConsts;

final class TechgunsReflectionHelper {
	
	private static TechgunsReflectionHelper instance;
	private final Class<?> tgDamageSource;
	private final Class<?> npcTGDamageSystem;

	
	public static TechgunsReflectionHelper getInstance() {
		if(instance == null) {
			throw new RuntimeException("Reflection Helper for Techguns accessed before initialized!");
		}
		return instance;
	}
	
	private TechgunsReflectionHelper() throws ClassNotFoundException {
		this.tgDamageSource = Class.forName(TechgunsConsts.TG_DAMAGESOURCE_CLASS);
		this.npcTGDamageSystem = Class.forName(TechgunsConsts.INPCTG_DAMAGESYSTEM_CLASS);
	}
	
	static void init() throws ClassNotFoundException {
		instance = new TechgunsReflectionHelper();
	}
	
	boolean isTGDamageSource(DamageSource src) {
		return this.tgDamageSource.isInstance(src);
	}
	
	boolean isNpcTGDamageSystem(EntityLivingBase entity) {
		return this.npcTGDamageSystem.isInstance(entity);
	}
}
