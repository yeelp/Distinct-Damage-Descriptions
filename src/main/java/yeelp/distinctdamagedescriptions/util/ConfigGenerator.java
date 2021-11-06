package yeelp.distinctdamagedescriptions.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;

import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.capability.IDistribution;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.ArmorDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.DamageDistribution;
import yeelp.distinctdamagedescriptions.capability.impl.MobResistances;
import yeelp.distinctdamagedescriptions.capability.impl.ShieldDistribution;
import yeelp.distinctdamagedescriptions.util.lib.NonNullMap;
import yeelp.distinctdamagedescriptions.util.lib.YLib;

/**
 * Generates config values on the fly for newly encountered mobs.
 * 
 * @author Yeelp
 *
 */
public final class ConfigGenerator {
	private static final Random rng = new Random();
	private static final Map<ResourceLocation, IDamageDistribution> MOB_DAMAGE_CACHE = new HashMap<ResourceLocation, IDamageDistribution>();
	private static final Map<ResourceLocation, IMobResistances> MOB_RESISTS_CACHE = new HashMap<ResourceLocation, IMobResistances>();
	private static final Map<ResourceLocation, Float> ADAPTABILITY_CHANCE_CACHE = new HashMap<ResourceLocation, Float>();
	private static final Map<ResourceLocation, IDamageDistribution> WEAPON_CACHE = new HashMap<ResourceLocation, IDamageDistribution>();
	private static final Map<ResourceLocation, IDamageDistribution> PROJECTILE_CACHE = new HashMap<ResourceLocation, IDamageDistribution>();
	private static final Map<ResourceLocation, IArmorDistribution> ARMOR_CACHE = new HashMap<ResourceLocation, IArmorDistribution>();
	private static final Map<ResourceLocation, ShieldDistribution> SHIELD_CACHE = new HashMap<ResourceLocation, ShieldDistribution>();
	private static final Field efficiencyField = ObfuscationReflectionHelper.findField(ItemTool.class, "field_77864_a");
	private static boolean updated = false;

	/**
	 * Generate Mob Capabilities on the fly and save them to be injected in the
	 * config later.
	 * 
	 * @param entity entity in question. Needed to get health, armor for generating
	 *               values.
	 * @param loc    entity's ResourceLocation. CapabilityHandler already has this
	 *               value when attaching capabilities, so might as well pass it
	 *               here.
	 * @return a Tuple from the cache or a new Tuple representing the generated
	 *         config values.
	 */
	public static final IDamageDistribution getOrGenerateMobDamage(EntityLivingBase entity, ResourceLocation loc) {
		if(MOB_DAMAGE_CACHE.containsKey(loc)) {
			return MOB_DAMAGE_CACHE.get(loc);
		}
		IDamageDistribution damageDist;
		EnumCreatureAttribute creatureAttribute = entity.getCreatureAttribute();

		switch(creatureAttribute == null ? EnumCreatureAttribute.UNDEFINED : creatureAttribute) {
			case ARTHROPOD:
				// Arthropods typically bite, so give them piercing usually. Up to 20% of their
				// damage can be bludgeoning instead.
				float bludgeAmount = rng.nextInt(20) / 100.0f;
				float pierceAmount = 1 - bludgeAmount;
				damageDist = new DamageDistribution(new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.PIERCING, pierceAmount), new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.BLUDGEONING, bludgeAmount));
				break;
			default:
				damageDist = new DamageDistribution();
				break;
		}

		MOB_DAMAGE_CACHE.put(loc, damageDist);
		updated = true;

		return damageDist;
	}

	public static final IMobResistances getOrGenerateMobResistances(EntityLivingBase entity, ResourceLocation loc) {
		if(MOB_RESISTS_CACHE.containsKey(loc)) {
			return MOB_RESISTS_CACHE.get(loc);
		}
		final boolean isMonster = entity.isCreatureType(EnumCreatureType.MONSTER, false);
		final boolean isWaterMob = entity.isCreatureType(EnumCreatureType.WATER_CREATURE, false);
		final boolean isPeaceful = !(isMonster || isWaterMob);
		final boolean isBoss = !entity.isNonBoss();
		final boolean isArthropod = entity.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD;
		IMobResistances mobResists;
		/*
		 * Monsters will get no more than 30% resistance to any category by default. Non
		 * monsters (isPeaceful) will get no more than 10% to any category by default.
		 * otherwise base value is 25%.
		 * 
		 * - Bosses get an additional 20% to resistance values below 50% - arthropods
		 * gain a bonus 25% to adaptability. - A mob's adaptability amount is set to 25%
		 * by default. - This increases by 5% for each resistance below 50%.
		 * 
		 * Further changes happen depending on the Entity's superclass.
		 * 
		 * AbstractSkeleton: Mob receives an additional 15% slashing resistance and
		 * loses all adaptability chance. EntityBlaze: Mob receives an additional 5%
		 * slashing, piercing, bludgeoning resistance and loses all adaptability chance.
		 * EntityCreeper: Mob receives an additional 10% bludgeoning resistance and
		 * loses 10% adaptability chance. EntityEnderman: Mob receives an additional 15%
		 * adaptability chance and 5% adaptability amount and loses 10% to all
		 * resistances. EntityEndermite: Mob receives an additional 70% adaptability
		 * chance and 50% adaptability amount and loses 15% to all resistances.
		 * EntityGuardian: Mob receives an additional 15% bludgeoning resistance and
		 * loses 15% piercing resistance. EntitySilverfish: Mob receives an additional
		 * 40% adaptability chance, 45% adaptability amount, and loses 40% bludgeoning
		 * resistance and 10% slashing resistance. EntitySpider: Mob receives an
		 * additional 20% adaptability chance and 20% piercing resistance, and loses 35%
		 * bludgeoning resistance. EntityZombie: Mob receives an additional 10%
		 * bludgeoning resistance, and loses all adaptability chance. EntityFlying: Mob
		 * loses 60% bludgeoning resistance. EntitySlime: Mob gains a slashing immunity
		 * and loses a piercing immunity if it had it and loses 30% slashing and
		 * piercing resistance. EntityGolem: Mob gains a random immunity it didn't have
		 * before. otherwise: If not a boss, Mob gains a random amount (5-20%) in a
		 * resistance and loses that amount in another resistance.
		 */
		float slash = 0, pierce = 0, bludge = 0, adaptChance = 0, adaptAmount = 0;
		boolean slashImmune = false, pierceImmune = false, bludgeImmune = false;

		float modifier = (float) (isMonster ? 0.3 : isPeaceful ? 0.1 : 0.25);

		// generate base resistance
		slash = generateResistance(modifier);
		pierce = generateResistance(modifier);
		bludge = generateResistance(modifier);

		// boss bonus
		if(isBoss) {
			slash += slash < 0.5 ? 0.2 : 0.0;
			pierce += pierce < 0.5 ? 0.2 : 0.0;
			bludge += bludge < 0.5 ? 0.2 : 0.0;
		}

		// adaptability bonus
		adaptChance = 0.1f * rng.nextFloat();
		if(isArthropod) {
			adaptChance += 0.25;
		}
		adaptAmount = 0.25f;
		adaptAmount += slash < 0.5 ? 0.05 : 0.0;
		adaptAmount += pierce < 0.5 ? 0.05 : 0.0;
		adaptAmount += bludge < 0.5 ? 0.05 : 0.0;

		if(entity instanceof AbstractSkeleton) {
			slash += 0.15;
			adaptChance = 0;
			adaptAmount = 0;
		}
		else if(entity instanceof EntityBlaze) {
			slash += 0.05;
			pierce += 0.05;
			bludge += 0.05;
			adaptChance = 0;
			adaptAmount = 0;
		}
		else if(entity instanceof EntityCreeper) {
			bludge += 0.1;
			adaptChance -= 0.1;
		}
		else if(entity instanceof EntityEnderman) {
			bludge -= 0.1;
			slash -= 0.1;
			pierce -= 0.1;
			adaptChance += 0.15;
			adaptAmount += 0.05;
		}
		else if(entity instanceof EntityEndermite) {
			adaptChance += 0.7;
			adaptAmount += 0.5;
			bludge -= 0.15;
			slash -= 0.15;
			pierce -= 0.15;
		}
		else if(entity instanceof EntityGuardian) {
			bludge += 0.15;
			pierce -= 0.15;
		}
		else if(entity instanceof EntitySilverfish) {
			adaptChance += 0.4;
			adaptAmount += 0.45;
			bludge -= 0.4;
			slash -= 0.1;
		}
		else if(entity instanceof EntitySpider) {
			adaptChance += 0.2;
			pierce += 0.2;
			bludge -= 0.35;
		}
		else if(entity instanceof EntityZombie) {
			bludge += 0.1;
			adaptChance = 0;
			adaptAmount = 0;
		}
		else if(entity instanceof EntityFlying) {
			bludge -= 0.6;
		}
		else if(entity instanceof EntitySlime) {
			slashImmune = true;
			pierceImmune = false;
			slash -= 0.3;
			pierce -= 0.3;
		}
		else if(entity instanceof EntityGolem) {
			List<Integer> lst = new ArrayList<Integer>(3);
			if(!slashImmune) {
				lst.add(0);
			}
			if(!pierceImmune) {
				lst.add(1);
			}
			if(!bludgeImmune) {
				lst.add(2);
			}
			int index = lst.get(rng.nextInt(lst.size()));
			switch(index) {
				case 0:
					slashImmune = true;
					break;
				case 1:
					pierceImmune = true;
					break;
				case 2:
				default:
					bludgeImmune = true;
					break;
			}
		}
		else {
			if(!isBoss) {
				float bonusResist = 0.15f * rng.nextFloat() + 0.05f;
				int bonusIndex = rng.nextInt(3);
				int penaltyIndex = (bonusIndex + (rng.nextBoolean() ? 1 : 2)) % 3;
				switch(bonusIndex) {
					case 0:
						slash += bonusResist;
						break;
					case 1:
						pierce += bonusResist;
						break;
					case 2:
					default:
						bludge += bonusResist;
						break;
				}
				switch(penaltyIndex) {
					case 0:
						slash -= bonusResist;
						break;
					case 1:
						pierce -= bonusResist;
						break;
					case 2:
					default:
						bludge -= bonusResist;
						break;
				}
			}
		}
		adaptChance = MathHelper.clamp(adaptChance, 0, 1);
		adaptAmount = adaptAmount < 0 ? 0 : adaptAmount;
		slash = roundToTwoDecimals(slash);
		pierce = roundToTwoDecimals(pierce);
		bludge = roundToTwoDecimals(bludge);
		adaptChance = roundToTwoDecimals(adaptChance);
		adaptAmount = roundToTwoDecimals(adaptAmount);
		ADAPTABILITY_CHANCE_CACHE.put(loc, adaptChance);
		Map<DDDDamageType, Float> resists = new NonNullMap<DDDDamageType, Float>(0.0f);
		Set<DDDDamageType> immunities = new HashSet<DDDDamageType>();
		resists.put(DDDBuiltInDamageType.SLASHING, slash);
		resists.put(DDDBuiltInDamageType.PIERCING, pierce);
		resists.put(DDDBuiltInDamageType.BLUDGEONING, bludge);
		if(slashImmune) {
			immunities.add(DDDBuiltInDamageType.SLASHING);
		}
		if(pierceImmune) {
			immunities.add(DDDBuiltInDamageType.PIERCING);
		}
		if(bludgeImmune) {
			immunities.add(DDDBuiltInDamageType.BLUDGEONING);
		}
		mobResists = new MobResistances(resists, immunities, Math.random() < adaptChance, adaptAmount);
		DistinctDamageDescriptions.debug(String.format("Values for %s: %f, %f, %f", loc, slash, pierce, bludge));
		MOB_RESISTS_CACHE.put(loc, mobResists);
		updated = true;
		return mobResists;
	}

	/**
	 * Get or generate tool capabilities on the fly
	 * 
	 * @param tool  the ItemTool
	 * @param stack the stack that this ItemTool is in.
	 * @return An IDamageDistribution, freshly generated or from the cache.
	 */
	public static final IDamageDistribution getOrGenerateWeaponCapabilities(ItemTool tool, ItemStack stack) {
		if(WEAPON_CACHE.containsKey(tool.getRegistryName())) {
			return WEAPON_CACHE.get(tool.getRegistryName());
		}
		/*
		 * We take a look at a tool's durability, enchantability, harvest level and
		 * efficiency to determine how 'good' of a tool it is. This determines how
		 * narrow a spread we give it.
		 * 
		 * 
		 * Then we check to see which tool type we have. We give precedence to pickaxes,
		 * axes and shovels in that order. pickaxes get a piercing biased distribution.
		 * Then, favor bludgeoning. Axes get a slashing biased distribution. Then, favor
		 * bludgeoning. shovels get a bludgeoning biased distribution Then, ever so
		 * lightly favor piercing.
		 */
		int durability = tool.getMaxDamage(stack);
		int enchantability = tool.getItemEnchantability(stack);
		Set<String> classes = tool.getToolClasses(stack);
		float avgHarvestLevel = 0;
		for(String s : classes) {
			avgHarvestLevel += tool.getHarvestLevel(stack, s, null, null);
		}
		avgHarvestLevel /= classes.size();
		float efficiency = 0;
		try {
			efficiency = efficiencyField.getFloat(tool);
		}
		catch(IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		// Use this information to get a tool rating in (0,1]. First, convert the values
		// to z scores, then add 3.5 to each value (this will make negative values small
		// positive values and positive values even larger)
		// Then, combine the z scores in a 4D vector and get the Euclidean norm.
		// Finally, squash the values into the range (0,1] using tanh.
		double rating = Math.pow(2, 2 * getDurabilityZScore(durability)) + Math.pow(2, 2 * getHarvestLevelZScore(avgHarvestLevel)) + Math.pow(2, 2 * getEfficiencyZScore(efficiency)) + Math.pow(2, 2 * getEnchantabilityZScore(enchantability));
		rating = Math.tanh(Math.sqrt(rating));

		// Now we choose our bias. We can use Forge's getToolClasses() set as
		// ItemPickaxe, ItemAxe and ItemShovel set this string to "pickaxe", "axe" and
		// "shovel" respectively. No instanceof checks needed.
		float slash = 0, pierce = 0, bludge = 0;
		if(classes.contains("pickaxe")) {
			pierce = roundToTwoDecimals(rating);
			bludge = 1 - pierce;
		}
		else if(classes.contains("axe")) {
			slash = roundToTwoDecimals(rating);
			bludge = 1 - slash;
		}
		else if(classes.contains("shovel")) {
			bludge = roundToTwoDecimals(rating);
			float rem = (1 - bludge) / 2;
			bludge += rem;
			pierce = 1 - bludge;
		}
		else {
			bludge = roundToTwoDecimals(rating);
			pierce = 1 - bludge;
		}
		IDamageDistribution dist = new DamageDistribution(new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.SLASHING, slash), new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.PIERCING, pierce), new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.BLUDGEONING, bludge));
		WEAPON_CACHE.put(tool.getRegistryName(), dist);
		updated = true;
		return dist;
	}

	/**
	 * Get or generate weapon capabilities on the fly, but for ItemHoe instances.
	 * 
	 * @param hoe
	 * @param stack
	 * @return an IDamageDistribution from the cache or a fresh one.
	 */
	public static final IDamageDistribution getOrGenerateWeaponCapabilities(ItemHoe hoe, ItemStack stack) {
		if(WEAPON_CACHE.containsKey(hoe.getRegistryName())) {
			return WEAPON_CACHE.get(hoe.getRegistryName());
		}
		double rating = Math.tanh(Math.pow(2, getDurabilityZScore(hoe.getMaxDamage(stack))));
		float pierce = roundToTwoDecimals(rating);
		float bludge = 1 - pierce;
		IDamageDistribution dist = new DamageDistribution(new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.PIERCING, pierce), new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.BLUDGEONING, bludge));
		WEAPON_CACHE.put(hoe.getRegistryName(), dist);
		updated = true;
		return dist;
	}

	/**
	 * Get or generate weapon capabilities on the fly, but for ItemSword instances.
	 * 
	 * @param sword
	 * @param stack
	 * @return an IDamageDistribution from the cache, or a fresh one.
	 */
	public static final IDamageDistribution getOrGenerateWeaponCapabilities(ItemSword sword, ItemStack stack) {
		if(WEAPON_CACHE.containsKey(sword.getRegistryName())) {
			return WEAPON_CACHE.get(sword.getRegistryName());
		}
		int durability = sword.getMaxDamage(stack), enchantability = sword.getItemEnchantability();
		double rating = Math.pow(2, 2 * getDurabilityZScore(durability)) + Math.pow(2, getEnchantabilityZScore(enchantability));
		rating = Math.tanh(Math.sqrt(rating));
		float slash = roundToTwoDecimals(rating), pierce = 0, bludge = 0;
		if(rating < 0.4) {
			bludge = 1 - slash;
		}
		else {
			pierce = 1 - slash;
		}
		IDamageDistribution dist = new DamageDistribution(new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.SLASHING, slash), new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.PIERCING, pierce), new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.BLUDGEONING, bludge));
		WEAPON_CACHE.put(sword.getRegistryName(), dist);
		updated = true;
		return dist;
	}

	/**
	 * Get or generate projectile capabilities on the fly
	 * 
	 * @param projectile the IProjectile
	 * @param loc        the ResourceLocation for the IProjectile
	 * @return an IDamageDistribution.
	 */
	public static final IDamageDistribution getOrGenerateProjectileDistribution(IProjectile projectile, ResourceLocation loc) {
		if(PROJECTILE_CACHE.containsKey(loc)) {
			return PROJECTILE_CACHE.get(loc);
		}
		IDamageDistribution dist = null;
		if(projectile instanceof EntityArrow) {
			dist = DDDBuiltInDamageType.PIERCING.getBaseDistribution();
		}
		else {
			dist = DDDBuiltInDamageType.BLUDGEONING.getBaseDistribution();
		}
		PROJECTILE_CACHE.put(loc, dist);
		updated = true;
		return dist;
	}

	/**
	 * Generate armor resistances on the fly
	 * 
	 * @param armor
	 * @param stack
	 * @return an IArmorDistribution.
	 */
	public static final IArmorDistribution getOrGenerateArmorResistances(ItemArmor armor, ItemStack stack) {
		if(ARMOR_CACHE.containsKey(armor.getRegistryName())) {
			return ARMOR_CACHE.get(armor.getRegistryName());
		}
		int durability = armor.getMaxDamage(stack);
		int enchantability = armor.getItemEnchantability(stack);
		float toughness = armor.toughness;

		float bludge = 0.1f + MathHelper.clamp(0.01f * durability, 0.0f, 0.9f);
		float pierce = 0.1f + MathHelper.clamp(toughness / 20.0f, 0.0f, 0.9f);
		float slash = 0.15f + MathHelper.clamp(0.01f * enchantability, 0.0f, 0.85f);
		IArmorDistribution dist = new ArmorDistribution(new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.SLASHING, slash), new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.PIERCING, pierce), new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.BLUDGEONING, bludge));
		ARMOR_CACHE.put(armor.getRegistryName(), dist);
		updated = true;
		return dist;
	}

	/**
	 * Generate shield distributions on the fly. No real way to generate anything
	 * concrete, only one shield to reference in vanilla, so we randomize values.
	 * 
	 * @param shield
	 * @param stack
	 * @return A ShieldDistribution
	 */
	public static final ShieldDistribution getOrGenerateShieldDistribution(ItemShield shield, ItemStack stack) {
		if(SHIELD_CACHE.containsKey(shield.getRegistryName())) {
			return SHIELD_CACHE.get(shield.getRegistryName());
		}
		ShieldDistribution dist = new ShieldDistribution(new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.SLASHING, generateResistance(1.0f)), new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.PIERCING, generateResistance(1.0f)), new Tuple<DDDDamageType, Float>(DDDBuiltInDamageType.BLUDGEONING, generateResistance(1.0f)));
		SHIELD_CACHE.put(shield.getRegistryName(), dist);
		return dist;
	}

	public static final String[] getNewMobResistanceConfigValues() {
		int index = -1;
		String[] vals = new String[MOB_RESISTS_CACHE.size()];
		for(Entry<ResourceLocation, IMobResistances> entry : MOB_RESISTS_CACHE.entrySet()) {
			IMobResistances resists = entry.getValue();
			String val = entry.getKey().toString() + ";";
			val += getVals(resists, (r, type) -> r.getResistance(type)) + ";";
			val += getImmunitiesForConfig(resists) + ";";
			val += ADAPTABILITY_CHANCE_CACHE.get(entry.getKey()) + ";";
			val += resists.getAdaptiveAmount();

			vals[++index] = val;
		}
		return vals;
	}

	public static final String[] getNewMobDamageConfigValues() {
		return getNewDistConfigVals(MOB_DAMAGE_CACHE);
	}

	public static final String[] getNewWeaponConfigValues() {
		return getNewDistConfigVals(WEAPON_CACHE);
	}

	public static final String[] getNewProjectileConfigValues() {
		return getNewDistConfigVals(PROJECTILE_CACHE);
	}

	public static final String[] getNewArmorConfigValues() {
		return getNewDistConfigVals(ARMOR_CACHE);
	}

	public static final String[] getNewShieldConfigValues() {
		return getNewDistConfigVals(SHIELD_CACHE);
	}

	private static <T extends IDistribution> String[] getNewDistConfigVals(Map<ResourceLocation, T> cache) {
		int index = -1;
		String[] vals = new String[cache.size()];
		for(Entry<ResourceLocation, T> entry : cache.entrySet()) {
			vals[++index] = entry.getKey().toString() + ";" + getVals(entry.getValue(), (dist, type) -> dist.getWeight(type));
		}
		return vals;
	}

	public static boolean hasUpdated() {
		return updated;
	}

	public static void markUpdated() {
		updated = false;
	}

	private static final float generateResistance(float modifier) {
		return modifier * rng.nextFloat();
	}

	private static final double getDurabilityZScore(int durability) {
		return (durability - 406.6) / 582.111535704284;
	}

	private static final double getHarvestLevelZScore(float avgLevel) {
		return (avgLevel - 1.2) / 1.1661903789690602;
	}

	private static final double getEfficiencyZScore(float efficiency) {
		return (efficiency - 6.4) / 3.4409301068170506;
	}

	private static final double getEnchantabilityZScore(float enchantability) {
		return (enchantability - 13.2) / 5.635601121442148;
	}

	private static final float roundToTwoDecimals(double a) {
		return (Math.round(a * 100) / 100.f);
	}

	private static String getImmunitiesForConfig(IMobResistances resists) {
		String res = "";
		for(DDDDamageType type : DDDBuiltInDamageType.PHYSICAL_TYPES) {
			if(resists.hasImmunity(type)) {
				res = YLib.joinNiceString(true, ",", res, type.getTypeName());
			}
		}
		return String.format("[%s]", res);
	}

	private static <T> String getVals(T t, BiFunction<T, DDDDamageType, Float> f) {
		return String.format("[%s, %s, %s]", makePair(t, DDDBuiltInDamageType.SLASHING, f), makePair(t, DDDBuiltInDamageType.PIERCING, f), makePair(t, DDDBuiltInDamageType.BLUDGEONING, f));
	}

	private static <T> String makePair(T t, DDDDamageType type, BiFunction<T, DDDDamageType, Float> f) {
		return String.format("(%s, %f)", type.getTypeName(), f.apply(t, type));
	}
}
