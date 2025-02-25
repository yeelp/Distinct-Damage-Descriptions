package yeelp.distinctdamagedescriptions.integration.firstaid;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;

import ichttt.mods.firstaid.api.IDamageDistribution;
import ichttt.mods.firstaid.common.EventHandler;
import ichttt.mods.firstaid.common.apiimpl.FirstAidRegistryImpl;
import ichttt.mods.firstaid.common.damagesystem.distribution.DamageDistribution;
import ichttt.mods.firstaid.common.damagesystem.distribution.RandomDamageDistribution;
import ichttt.mods.firstaid.common.util.ArmorUtils;
import ichttt.mods.firstaid.common.util.PlayerSizeHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationTitles;
import yeelp.distinctdamagedescriptions.ModConsts.TooltipConsts;
import yeelp.distinctdamagedescriptions.handlers.Handler;
import yeelp.distinctdamagedescriptions.integration.IModIntegration;
import yeelp.distinctdamagedescriptions.util.Translations;
import yeelp.distinctdamagedescriptions.util.Translations.LayeredTranslator;
import yeelp.distinctdamagedescriptions.util.lib.ArmorValues;
import yeelp.distinctdamagedescriptions.util.lib.DDDAttributeModifierCollections;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.DDDCombatCalculations;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.IDDDCalculationInjector.IArmorModifierInjector;
import yeelp.distinctdamagedescriptions.util.lib.damagecalculation.IDDDCalculationInjector.IValidArmorSlotInjector;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.IDDDTooltipInjector.IArmorTooltipInjector;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipDistributor;
import yeelp.distinctdamagedescriptions.util.tooltipsystem.TooltipTypeFormatter.Armor;

public final class FirstAidIntegration implements IModIntegration { 

	final Armor formatter = this.new FirstAidTooltipTypeFormatter();
	
	@Override
	public String getModTitle() {
		return IntegrationTitles.FIRST_AID_TITLE;
	}

	@Override
	public String getModID() {
		return IntegrationIds.FIRST_AID_ID;
	}

	@Override
	public Iterable<Handler> getHandlers() {
		return ImmutableList.of(new FirstAidEventHandler());
	}
	
	@Override
	public boolean initStart(FMLInitializationEvent evt) {
		//just in case preInit is too early before FirstAid classes has been initialized
		try {
			FirstAidReflectionHelper.init();
			return IModIntegration.super.initStart(evt);
		}
		catch(NoSuchMethodException | SecurityException e) {
			DistinctDamageDescriptions.fatal("DDD was unable to hook into FirstAid for integration.");
			DistinctDamageDescriptions.fatal("DDD will continue loading as normal, but it is encouraged to report this error! DDD will inform on world load as well. See the error below!");
			DistinctDamageDescriptions.fatal(e.getClass().getSimpleName());
			DistinctDamageDescriptions.fatal(e.getLocalizedMessage());
			Arrays.stream(e.getStackTrace()).map(Functions.toStringFunction()).forEach(DistinctDamageDescriptions::fatal);
			new Handler() {
				@SubscribeEvent
				public void onJoinWorld(PlayerLoggedInEvent evt) {
					if(evt.player.world.isRemote) {
						return;
					}
					evt.player.sendMessage(new TextComponentString("DDD's FirstAid Integration failed to load successfully! Please check the logs for the error and report it!").setStyle(new Style().setColor(TextFormatting.RED)));
				}
			}.register();
			return false;
		}
	}
	
	@Override
	public boolean init(FMLInitializationEvent evt) {
		TooltipDistributor.registerArmorTooltipInjector(new IArmorTooltipInjector() {
			
			@Override
			public boolean shouldUseFormatter(ItemStack stack) {
				return true;
			}
			
			@Override
			public Armor getFormatterToUse() {
				return FirstAidIntegration.this.formatter;
			}
			
			@Override
			public boolean applies(ItemStack stack) {
				return true;
			}
			
			@Override
			public ArmorValues alterArmorValues(ItemStack stack, float armor, float toughness) {
				EntityEquipmentSlot slot = ((ItemArmor) stack.getItem()).armorType;
				return new ArmorValues((float) ArmorUtils.applyArmorModifier(slot, armor), (float) ArmorUtils.applyToughnessModifier(slot, toughness));
			}
			
			@Override
			public int priority() {
				return -1;
			}
		});
		
		//debatable if this is needed. FirstAid grabs only what it needs,
		//so if we work with every armor slot, FirstAid will only grab what it uses and ignore the rest.
		DDDCombatCalculations.registerValidArmorSlotInjector(new IValidArmorSlotInjector() {
			
			@Override
			public void accept(EntityLivingBase defender, DamageSource t, Set<EntityEquipmentSlot> u) {
				EntityPlayer player;
				if(defender instanceof EntityPlayer) {
					player = (EntityPlayer) defender;
				}
				else {
					return;
				}
				IDamageDistribution idist = FirstAidRegistryImpl.INSTANCE.getDamageDistributionForSource(t);
				if(t.isProjectile()) {
					Pair<Entity, RayTraceResult> result = EventHandler.HIT_LIST.get(player);
					if(result != null) {
						Entity projectile = result.getLeft();
						EntityEquipmentSlot slot = PlayerSizeHelper.getSlotTypeForProjectileHit(projectile, player);
						if(slot != null) {
							u.removeIf((ees) -> ees != slot);
							return;
						}
					}
				}
				if(idist == null) {
					//no dist for source found, follow First Aid's logic and try for melee dist.
					if((idist = PlayerSizeHelper.getMeleeDistribution(player, t)) == null) {
						idist = RandomDamageDistribution.getDefault();
					}
				}
				if(idist instanceof DamageDistribution) {
					DamageDistribution dist = (DamageDistribution) idist;
					try {
						u.retainAll(FirstAidReflectionHelper.getInstance().getPartList(dist).stream().map(Pair::getLeft).collect(Collectors.toList()));
					}
					catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						DistinctDamageDescriptions.err("An exception occurred and DDD was unable to use FirstAid to determine valid player parts being damaged. Please report!");
						Arrays.stream(e.getStackTrace()).map(Functions.toStringFunction()).forEach(DistinctDamageDescriptions::err);
						Entity entity = t.getTrueSource();
						if(!entity.world.isRemote) {
							entity.getServer().getPlayerList().getPlayers().forEach((p) -> p.sendMessage(new TextComponentString("An exception occurred with DDD's FirstAid integration. Please check the logs and report the error!")));
						}
					}
				}
			}
			
			@Override
			public int priority() {
				return -1;
			}
		});
		
		DDDCombatCalculations.registerArmorModifierInjector(new IArmorModifierInjector() {

			@Override
			public boolean modify(boolean willModsBeApplied, EntityLivingBase defender, Map<EntityEquipmentSlot, ArmorValues> deltaArmor) {
				if(!(defender instanceof EntityPlayer)) {
					return willModsBeApplied;
				}
				
				EntityPlayer player = (EntityPlayer) defender;
				AtomicBoolean appliedMods = new AtomicBoolean(false);
				deltaArmor.forEach((slot, armorValues) -> {
					ItemStack stack = player.getItemStackFromSlot(slot);
					if(stack.isEmpty()) {
						return;
					}
					DebugLib.outputFormattedDebug("Attribute Modifiers Before First Aid Compat: %s", DebugLib.entriesToString(stack.getAttributeModifiers(slot).asMap()));
					Iterator<Float> armorValuesIt = armorValues.iterator();
					Arrays.asList(DDDAttributeModifierCollections.ArmorModifiers.values()).forEach((modifier) -> {
						//Only apply the delta armor, FirstAid applies the rest
						AttributeModifier mod = new AttributeModifier(modifier.getUUID(), modifier.getName(), armorValuesIt.next(), 0);
						stack.addAttributeModifier(modifier.getAttribute().getName(), mod, slot);
						DebugLib.outputFormattedDebug("Added Attribute Modifier to %s slot: %s", slot.toString(), mod.toString());
					});
					appliedMods.set(true);
				});
				if(appliedMods.get()) {
					FirstAidEventHandler.markPlayerHasMods(player);
				}
				return false;
			}
			
			@Override
			public int priority() {
				return Integer.MAX_VALUE;
			}
			
			@Override
			public boolean shouldFireIfNotBeingApplied() {
				return true;
			}
		});
		return IModIntegration.super.init(evt);
	}

	class FirstAidTooltipTypeFormatter extends Armor {

		private final LayeredTranslator translator = Translations.INSTANCE.getLayeredTranslator(TooltipConsts.TOOLTIPS_ROOT, FirstAidIntegration.this.getModID()); 
		
		private final ITextComponent armor, toughness;
		FirstAidTooltipTypeFormatter() {
			super();
			this.armor = this.translator.getComponent("armor");
			this.toughness = this.translator.getComponent("toughness");
		}

		@Override
		protected ITextComponent getArmorText() {
			return this.armor;
		}

		@Override
		protected ITextComponent getToughnessText() {
			return this.toughness;
		}
		
	}
}
