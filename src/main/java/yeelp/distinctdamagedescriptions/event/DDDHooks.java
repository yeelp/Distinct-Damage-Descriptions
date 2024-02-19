package yeelp.distinctdamagedescriptions.event;

import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.event.calculation.ShieldBlockEvent;
import yeelp.distinctdamagedescriptions.event.calculation.UpdateAdaptiveResistanceEvent;
import yeelp.distinctdamagedescriptions.event.classification.DetermineDamageEvent;
import yeelp.distinctdamagedescriptions.event.classification.GatherDefensesEvent;
import yeelp.distinctdamagedescriptions.util.development.DeveloperModeKernel;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.ResistMap;

public final class DDDHooks {

	private static <E extends Event> E fire(E evt, Consumer<E> callback) {
		MinecraftForge.EVENT_BUS.post(evt);
		callback.accept(evt);
		return evt;
	}

	public static ShieldBlockEvent fireShieldBlock(Entity directAttacker, Entity indirectAttacker, @Nonnull EntityLivingBase defender, @Nonnull DamageSource src, @Nonnull DamageMap map, @Nonnull ItemStack shield) {
		return fire(new ShieldBlockEvent(directAttacker, indirectAttacker, defender, src, map, shield), DeveloperModeKernel::onShieldBlockCallback);
	}

	public static DetermineDamageEvent fireDetermineDamage(Entity directAttacker, Entity indirectAttacker, @Nonnull EntityLivingBase defender, @Nonnull DamageSource src, @Nonnull DamageMap map) {
		return fire(new DetermineDamageEvent(directAttacker, indirectAttacker, defender, src, map), DeveloperModeKernel::onDetermineDamageCallback);
	}

	public static GatherDefensesEvent fireGatherDefenses(Entity directAttacker, Entity indirectAttacker, @Nonnull EntityLivingBase defender, @Nonnull DamageSource src, @Nonnull ResistMap map, @Nonnull Set<DDDDamageType> immunities) {
		return fire(new GatherDefensesEvent(directAttacker, indirectAttacker, defender, src, map, immunities), DeveloperModeKernel::onGatherDefensesCallback);
	}

	public static UpdateAdaptiveResistanceEvent fireUpdateAdaptiveResistances(Entity directAttacker, Entity indirectAttacker, @Nonnull EntityLivingBase defender, @Nonnull DamageSource src, @Nonnull DamageMap map, @Nonnull ResistMap resists, @Nonnull Set<DDDDamageType> immunities) {
		return fire(new UpdateAdaptiveResistanceEvent(directAttacker, indirectAttacker, defender, src, map, resists, immunities), DeveloperModeKernel::onUpdateAdaptabilityCallback);
	}
	
	public static AssignMobResistancesEvent fireAssignMobResistances(EntityLivingBase entity, World world, IMobResistances resistances) {
		AssignMobResistancesEvent evt = new AssignMobResistancesEvent(entity, world, resistances);
		MinecraftForge.EVENT_BUS.post(evt);
		return evt;
	}
}
