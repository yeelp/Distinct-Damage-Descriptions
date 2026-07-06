package yeelp.distinctdamagedescriptions.integration.crafttweaker;

import com.google.common.base.Functions;
import com.google.common.base.Predicates;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

import java.util.function.BinaryOperator;
import java.util.function.Predicate;

/**
 * Useful constants for CraftTweaker. Mainly contains ZenClass names so references to ZenClasses agree with each other.
 * @author Yeelp
 *
 */
public interface CTConsts {

	interface CTClasses {
		String CTRESISTANCES = "mods.ddd.Resistances";
		String CTCREATURETYPE = "mods.ddd.CreatureType";
		String CTCREATURETYPEDEF = "mods.ddd.CreatureTypeDefinition";
		String CTCUSTOMDISTRIBUTION = "mods.ddd.ICustomDistribution";
		String COTDAMAGETYPEBUILDER = "mods.ddd.DamageTypeBuilder";
		
		String CTDAMAGETYPE = "mods.ddd.damagetypes.IDDDDamageType";

		String CTDAMAGEDISTRIBUTION = "mods.ddd.distributions.IDamageDistribution";
		String CTDISTRIBUTION = "mods.ddd.distributions.IDistribution";
		String COTDISTBUILDER = "mods.ddd.distributions.DistributionBuilder";
		
		String COTMODIFIERBUILDER = "mods.ddd.modifiers.ModifierBuilder";
		String COTITEMMODIFIERBUILDER = "mods.ddd.modifiers.ItemModifierBuilder";
		String COTMOBDAMAGEMODIFIERBUILDER = "mods.ddd.modifiers.MobDamageModifierBuilder";
		String COTPROJDAMAGEMODIFIERBUILDER = "mods.ddd.modifiers.ProjectileModifierBuilder";
		String COTRESISTANCESMODIFIERBUILDER = "mods.ddd.modifiers.ResistancesModifierBuilder";
		
		String DDDMAP = "mods.ddd.lib.IDDDBaseMap";

		String EVENTMANAGER = "mods.ddd.events.DDDEvents";
		String EVENT = "mods.ddd.events.DDDEvent";
		String EVENTCALC = "mods.ddd.events.DDDCalculationEvent";
		String EVENTCLASSIFY = "mods.ddd.events.DDDClassificationEvent";
		String EVENTDETERMINEDAMAGE = "mods.ddd.events.DetermineDamageEvent";
		String EVENTGATHERDEFENSES = "mods.ddd.events.GatherDefensesEvent";
		String EVENTSHIELDBLOCK = "mods.ddd.events.ShieldBlockEvent";
		String EVENTUPDATEADAPTIVERESISTANCES = "mods.ddd.events.UpdateAdaptiveResistanceEvent";
	}
	
	interface CTStrings {
		String GET_FROM_STRING_METHOD = "getFromString";
	}
	
	Predicate<String> IS_NOT_REGISTERED = Predicates.compose(Predicates.isNull(), Functions.compose(DDDRegistries.damageTypes::get, DDDDamageType::addDDDPrefixIfNeeded));
	BinaryOperator<String> CONCAT_WITH_LINEBREAK = (s1, s2) -> s1.concat("\n").concat(s2);
}
