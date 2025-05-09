package yeelp.distinctdamagedescriptions.event;

import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.event.calculation.ShieldBlockEvent;
import yeelp.distinctdamagedescriptions.event.calculation.UpdateAdaptiveResistanceEvent;
import yeelp.distinctdamagedescriptions.event.classification.DetermineDamageEvent;
import yeelp.distinctdamagedescriptions.event.classification.GatherDefensesEvent;
import yeelp.distinctdamagedescriptions.util.development.DeveloperModeKernel;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.DamageMap;
import yeelp.distinctdamagedescriptions.util.lib.DDDMaps.ResistMap;

public final class DDDHooks {

	private static <E extends Event> E fire(E evt) {
		MinecraftForge.EVENT_BUS.post(evt);
		DeveloperModeKernel.fireCallbacks(evt);
		return evt;
	}

	public static ShieldBlockEvent fireShieldBlock(Entity directAttacker, Entity indirectAttacker, @Nonnull EntityLivingBase defender, @Nonnull DamageSource src, @Nonnull DamageMap map, @Nonnull ItemStack shield) {
		return fire(new ShieldBlockEvent(directAttacker, indirectAttacker, defender, src, map, shield));
	}

	public static DetermineDamageEvent fireDetermineDamage(Entity directAttacker, Entity indirectAttacker, @Nonnull EntityLivingBase defender, @Nonnull DamageSource src, @Nonnull DamageMap map) {
		return fire(new DetermineDamageEvent(directAttacker, indirectAttacker, defender, src, map));
	}

	public static GatherDefensesEvent fireGatherDefenses(Entity directAttacker, Entity indirectAttacker, @Nonnull EntityLivingBase defender, @Nonnull DamageSource src, @Nonnull ResistMap map, @Nonnull Set<DDDDamageType> immunities) {
		return fire(new GatherDefensesEvent(directAttacker, indirectAttacker, defender, src, map, immunities));
	}

	public static UpdateAdaptiveResistanceEvent fireUpdateAdaptiveResistances(Entity directAttacker, Entity indirectAttacker, @Nonnull EntityLivingBase defender, @Nonnull DamageSource src, @Nonnull DamageMap map, @Nonnull ResistMap resists, @Nonnull Set<DDDDamageType> immunities) {
		return fire(new UpdateAdaptiveResistanceEvent(directAttacker, indirectAttacker, defender, src, map, resists, immunities));
	}
	
	public static ShouldDrawIconsEvent fireShouldDrawIcons() {
		ShouldDrawIconsEvent evt = new ShouldDrawIconsEvent();
		MinecraftForge.EVENT_BUS.post(evt);
		return evt;
	}
}
