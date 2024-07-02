package yeelp.distinctdamagedescriptions.integration.crafttweaker;

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
}
