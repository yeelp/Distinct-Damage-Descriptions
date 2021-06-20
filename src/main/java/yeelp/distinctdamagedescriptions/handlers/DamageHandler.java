package yeelp.distinctdamagedescriptions.handlers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.capability.IMobResistances;
import yeelp.distinctdamagedescriptions.event.DamageDescriptionEvent;
import yeelp.distinctdamagedescriptions.init.DDDSounds;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.ArmorMap;
import yeelp.distinctdamagedescriptions.util.DDDCombatRules;
import yeelp.distinctdamagedescriptions.util.DDDCombatRules.CombatResults;
import yeelp.distinctdamagedescriptions.util.DDDCombatRules.HitInfo;
import yeelp.distinctdamagedescriptions.util.DDDDamageSource;
import yeelp.distinctdamagedescriptions.util.DDDEffects;
import yeelp.distinctdamagedescriptions.util.DamageMap;
import yeelp.distinctdamagedescriptions.util.ResistMap;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;
import yeelp.distinctdamagedescriptions.util.lib.YMath;

public class DamageHandler extends Handler {
	private static Set<UUID> noKnockback = new HashSet<UUID>();

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onDeath(LivingDeathEvent evt) {
		if(ModConfig.dmg.useCustomDeathMessages) {
			EntityLivingBase entity = evt.getEntityLiving();
			HitInfo info = DDDCombatRules.getLastHit(entity.getUniqueID());
			DamageSource src = info.getLastSource();
			ITextComponent comp = DDDRegistries.distributions.getDeathMessageForDist(info.getLastDist().orElse(null), src, src.getTrueSource(), entity);
			if(entity instanceof EntityPlayerMP) {
				((EntityPlayerMP) entity).mcServer.getPlayerList().sendMessage(comp);
			}
			else if(evt.getEntityLiving() instanceof EntityTameable) {
				EntityTameable tamedEntity = (EntityTameable) evt.getEntityLiving();
				if(tamedEntity.isTamed()) {
					tamedEntity.getOwner().sendMessage(comp);
				}
			}
		}
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onAttack(LivingAttackEvent evt) {
		EntityLivingBase defender = evt.getEntityLiving();
		DamageSource src = evt.getSource();
		Entity attacker = src.getImmediateSource();
		HitInfo info = DDDCombatRules.setLastHit(src, defender);
		DDDCombatRules.setModifiers(src, defender);
		if(info.getLastSource() instanceof DDDDamageSource) {
			// Let the damage bypass armor so the shield can't block it normally.
			evt.getSource().setDamageBypassesArmor();
			if(attacker instanceof EntityLivingBase && defender instanceof EntityPlayer) {
				EntityLivingBase livingAttacker = (EntityLivingBase) attacker;
				EntityPlayer defendingPlayer = (EntityPlayer) defender;
				ItemStack weapon = livingAttacker.getHeldItemMainhand();
				if(defender.isActiveItemStackBlocking() && weapon.getItem().canDisableShield(weapon, defender.getActiveItemStack(), defender, livingAttacker)) {
					defendingPlayer.disableShield(true);
				}
			}
		}
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onHit(LivingHurtEvent evt) {
		EntityLivingBase defender = evt.getEntityLiving();
		if(defender == null) {
			return;
		}
		DamageSource dmgSource = evt.getSource();
		Entity attacker = dmgSource.getImmediateSource();
		if(ModConfig.showDotsOn) {
			Entity trueAttacker = dmgSource.getTrueSource();
			ResourceLocation attackerLoc = attacker == null || attacker instanceof EntityPlayer ? null : EntityList.getKey(attacker),
					defendLoc = EntityList.getKey(defender),
					trueAttackerLoc = trueAttacker == null ? null : EntityList.getKey(trueAttacker);
			String s1 = attackerLoc != null ? attackerLoc.toString() : "null",
					s2 = defendLoc != null ? defendLoc.toString() : "null",
					s3 = trueAttackerLoc != null ? trueAttackerLoc.toString() : "null";
			DistinctDamageDescriptions.debug("Damage Type: " + dmgSource.damageType + " Attacker: " + (attacker != null && attacker instanceof EntityPlayer ? "player" : s1) + ", True Attacker: " + s3 + " Defender: " + s2);
		}
		IMobResistances mobResists = DDDAPI.accessor.getMobResistances(defender);
		DamageMap dmgMap = DDDCombatRules.getLastHit(defender.getUniqueID()).getLastDist().map((dist) -> dist.distributeDamage(evt.getAmount())).orElse(DDDBuiltInDamageType.UNKNOWN.getBaseDistribution().distributeDamage(evt.getAmount()));
		DistinctDamageDescriptions.debug("starting damage: " + evt.getAmount());
		DistinctDamageDescriptions.debug("Damage Total: " + DebugLib.entriesToString(dmgMap));
		if(dmgMap == null) {
			// couldn't classify damage, so further calculations are meaningless/undefined.
			// Return, let vanilla handle the rest,
			return;
		}
		ResistMap resistMap = DDDAPI.accessor.classifyResistances(dmgMap.keySet(), mobResists);
		ArmorMap armors = DDDCombatRules.getApplicableArmorValues(attacker, defender);
		DamageDescriptionEvent.Pre pre = new DamageDescriptionEvent.Pre(attacker, defender, dmgMap, resistMap, armors);
		MinecraftForge.EVENT_BUS.post(pre);
		if(pre.isCanceled()) {
			return;
		}
		CombatResults results = DDDCombatRules.computeNewDamage(attacker, defender, pre.getAllDamages(), pre.getAllResistances(), pre.getAllArmor(), mobResists);
		// determine if knockback should occur
		float totalDamage = (float) YMath.sum(results.getDamage().values());
		if(results.wasImmunityTriggered() && totalDamage == 0.0) {
			noKnockback.add(defender.getUniqueID());
		}
		else if(results.wasShieldEffective()) {
			noKnockback.add(defender.getUniqueID());
		}

		// One more reduction for natural armor/enchants/potions
		totalDamage = CombatRules.getDamageAfterAbsorb(totalDamage, (float) defender.getEntityAttribute(SharedMonsterAttributes.ARMOR).getAttributeValue(), (float) defender.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
		int enchantMods = EnchantmentHelper.getEnchantmentModifierDamage(defender.getArmorInventoryList(), dmgSource);
		if(enchantMods > 0) {
			totalDamage = CombatRules.getDamageAfterMagicAbsorb(totalDamage, enchantMods);
		}
		DamageDescriptionEvent.Post post = new DamageDescriptionEvent.Post(attacker, defender, results.getDamage(), results.getResistances(), results.getArmor());
		MinecraftForge.EVENT_BUS.post(post);
		totalDamage = (float) YMath.sum(post.getAllDamages().values());
		DistinctDamageDescriptions.debug("new damage after deductions: " + totalDamage);
		boolean resistancesUpdated = DDDAPI.mutator.updateAdaptiveResistances(defender, dmgMap.keySet().toArray(new DDDDamageType[0]));
		// Only spawn particles and play sounds for players.
		if(dmgSource.getTrueSource() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) dmgSource.getTrueSource();
			evt.setCanceled(DDDEffects.doEffects(player, defender, results, MathHelper.clamp(totalDamage / evt.getAmount(), 0, Float.MAX_VALUE)) && ModConfig.dmg.cancelLivingHurtEventOnImmunity);
			if(resistancesUpdated) {
				DDDSounds.playSound(player, DDDSounds.ADAPTABILITY_CHANGE, 2.0f, 1.0f);
			}
		}
		evt.setAmount(totalDamage < 0 ? 0 : totalDamage);
		dmgSource.setDamageBypassesArmor();
		DDDCombatRules.wipeModifiers(attacker, defender);
	}

	@SuppressWarnings("static-method")
	@SubscribeEvent
	public void onKnockback(LivingKnockBackEvent evt) {
		UUID uuid = evt.getEntityLiving().getUniqueID();
		if(noKnockback.contains(uuid)) {
			evt.setCanceled(noKnockback.remove(uuid));
		}
	}
}
