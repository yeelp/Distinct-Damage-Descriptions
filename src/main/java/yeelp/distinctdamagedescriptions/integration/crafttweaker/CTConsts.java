package yeelp.distinctdamagedescriptions.integration.crafttweaker;

import java.util.function.BinaryOperator;
import java.util.function.Predicate;

import com.google.common.base.Functions;
import com.google.common.base.Predicates;

import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

/**
 * Useful constants for CraftTweaker. Mainly contains ZenClass names so references to ZenClasses agree with eachother. 
 * @author Yeelp
 *
 */
public interface CTConsts {

	public interface CTClasses {
		final String CTRESISTANCES = "mods.ddd.Resistances";
		final String CTCREATURETYPE = "mods.ddd.CreatureType";
		final String CTCREATURETYPEDEF = "mods.ddd.CreatureTypeDefinition";
		final String CTCUSTOMDISTRIBUTION = "mods.ddd.ICustomDistribution";
		final String COTDAMAGETYPEBUILDER = "mods.ddd.DamageTypeBuilder";
		
		final String CTDAMAGETYPE = "mods.ddd.damagetypes.IDDDDamageType";

		final String CTDAMAGEDISTRIBUTION = "mods.ddd.distributions.IDamageDistribution";
		final String CTDISTRIBUTION = "mods.ddd.distributions.IDistribution";
		final String COTDISTBUILDER = "mods.ddd.distributions.DistributionBuilder";
		
		final String COTMODIFIERBUILDER = "mods.ddd.modifiers.ModifierBuilder";
		final String COTITEMMODIFIERBUILDER = "mods.ddd.modifiers.ItemModifierBuilder";
		final String COTMOBDAMAGEMODIFIERBUILDER = "mods.ddd.modifiers.MobDamageModifierBuilder";
		final String COTPROJDAMAGEMODIFIERBUILDER = "mods.ddd.modifiers.ProjectileModifierBuilder";
		final String COTRESISTANCESMODIFIERBUILDER = "mods.ddd.modifiers.ResistancesModifierBuilder";
		
		final String DDDMAP = "mods.ddd.lib.IDDDBaseMap";

		final String EVENTMANAGER = "mods.ddd.events.DDDEvents";
		final String EVENT = "mods.ddd.events.DDDEvent";
		final String EVENTCALC = "mods.ddd.events.DDDCalculationEvent";
		final String EVENTCLASSIFY = "mods.ddd.events.DDDClassificationEvent";
		final String EVENTDETERMINEDAMAGE = "mods.ddd.events.DetermineDamageEvent";
		final String EVENTGATHERDEFENSES = "mods.ddd.events.GatherDefensesEvent";
		final String EVENTSHIELDBLOCK = "mods.ddd.events.ShieldBlockEvent";
		final String EVENTUPDATEADAPTIVERESISTANCES = "mods.ddd.events.UpdateAdaptiveResistanceEvent";
		final String EVENTASSIGNRESISTANCES = "mods.ddd.events.AssignMobResistancesEvent";
	}
	
	public interface CTStrings {
		final String GET_FROM_STRING_METHOD = "getFromString";
	}
	
	final Predicate<String> IS_NOT_REGISTERED = Predicates.compose(Predicates.isNull(), Functions.compose(DDDRegistries.damageTypes::get, DDDDamageType::addDDDPrefixIfNeeded));
	final BinaryOperator<String> CONCAT_WITH_LINEBREAK = (s1, s2) -> s1.concat("\n").concat(s2);
}
