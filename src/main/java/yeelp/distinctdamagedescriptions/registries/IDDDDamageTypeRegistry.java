package yeelp.distinctdamagedescriptions.registries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.sun.istack.internal.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.registries.impl.dists.DDDExplosionDist;
import yeelp.distinctdamagedescriptions.util.DDDDamageSource;
import yeelp.distinctdamagedescriptions.util.DamageTypeData;
import yeelp.distinctdamagedescriptions.util.lib.YMath;

public interface IDDDDamageTypeRegistry extends IDDDRegistry<DDDDamageType>
{	
	/**
	 * Get the display name for a custom damage type
	 * @param name the internal name, with the "ddd_" prefix.
	 * @return the display name
	 */
	default String getDisplayName(String name)
	{
		return get(name).getDisplayName();
	}
	
	/**
	 * Register information about what in game damage sources count as a particular DDDDamageType
	 * @param type
	 * @param datas
	 */
	void registerDamageTypeData(DDDDamageType type, DamageTypeData... datas);
	
	/**
	 * Get custom damage types
	 * @param evt
	 * @return a Set of DDDDamageTypes
	 */
	Set<DDDDamageType> getCustomDamageContext(LivingAttackEvent evt);
	
	/**
	 * Get extra damage types
	 * @param evt
	 * @return a Set of DDDDamageTypes
	 */
	Set<DDDDamageType> getExtraDamageContext(LivingAttackEvent evt);
	
	/**
	 * Get damage context
	 * @param evt
	 * @return a Set of DDDDamageTypes
	 */
	Set<DDDDamageType> getRegularDamageContext(LivingAttackEvent evt);
	
	/**
	 * Get the full context for a damage source
	 * @param evt
	 * @return the full Set of DDDDamageTypes
	 */
	default Set<DDDDamageType> getFullDamageContext(LivingAttackEvent evt)
	{
		return YMath.setUnion(YMath.setUnion(getCustomDamageContext(evt), getExtraDamageContext(evt)), getRegularDamageContext(evt));
	}
	
	/**
	 * Update the explosion damage from the config
	 */
	default void updateExplosionDamage()
	{
		DDDExplosionDist.update();
	}
	
	default ITextComponent getDeathMessage(IDamageDistribution dist, DDDDamageSource src, @Nullable Entity attacker, @Nonnull EntityLivingBase defender)
	{
		double weight = Math.random();
		ArrayList<DDDDamageType> lst = Lists.newArrayList(dist.getCategories());
		Collections.shuffle(lst);
		for(DDDDamageType type : lst)
		{
			weight -= dist.getWeight(type);
			if(weight <= 0)
			{
				return getDeathMessageForType(type, src, attacker, defender);
			}
		}
		return getDeathMessageForType(lst.get(0), src, attacker, defender);
	}
	
	static ITextComponent getDeathMessageForType(DDDDamageType type, DDDDamageSource src, @Nullable Entity attacker, @Nonnull EntityLivingBase defender)
	{
		boolean hasAttacker = attacker != null;
		String msg = type.getDeathMessage(hasAttacker);
		if(msg == null)
		{
			return src.getDeathMessage(defender);
		}
		if(hasAttacker)
		{
			msg.replaceAll("#attacker", attacker.getName());
		}
		msg.replaceAll("#defender", defender.getName());
		return new TextComponentString(msg);
	}
}
