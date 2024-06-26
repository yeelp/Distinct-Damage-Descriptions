package yeelp.distinctdamagedescriptions.registries.impl;

import java.util.Map;
import java.util.stream.Stream;

import com.google.common.base.Functions;
import com.google.common.collect.Maps;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.potion.AbstractDDDPotion;
import yeelp.distinctdamagedescriptions.potion.DDDDamagePotion;
import yeelp.distinctdamagedescriptions.potion.DDDResistPotion;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.registries.IDDDPotionsRegistry;

public class DDDPotionsRegistry extends DDDUnsourcedRegistry<AbstractDDDPotion> implements IDDDPotionsRegistry {

	private static final int DURATION = 180 * 20;
	private static final Map<AbstractDDDPotion, Map<String, PotionType>> POTION_TYPES = Maps.newHashMap();

	public DDDPotionsRegistry() {
		super(Functions.compose(ResourceLocation::getPath, AbstractDDDPotion::getRegistryName), "Potions");
	}

	@Override
	public void init() {
		if(!ModConfig.core.enablePotionEffectRegistration) {
			return;
		}
		DDDRegistries.damageTypes.forEach((t) -> {
			if(t != DDDBuiltInDamageType.NORMAL && t != DDDBuiltInDamageType.UNKNOWN) {
				Stream.of(AbstractDDDPotion.EffectType.values()).flatMap((e) -> Stream.of(new DDDResistPotion(e, t), new DDDDamagePotion(e, t))).forEach(this::register);
			}
		});		
		if(!ModConfig.core.enablePotionRegistration) {
			return;
		}
		this.iterator().forEachRemaining((p) -> {
			PotionType normal = new PotionType(new PotionEffect[] {
					new PotionEffect(p, DURATION, 0)});
			PotionType extended = new PotionType(new PotionEffect[] {
					new PotionEffect(p, 2 * DURATION, 0)});
			PotionType strong = new PotionType(new PotionEffect[] {
					new PotionEffect(p, DURATION / 2, 1)});
			String name = p.getRegistryName().getPath();
			normal.setRegistryName(new ResourceLocation(ModConsts.MODID, name));
			extended.setRegistryName(new ResourceLocation(ModConsts.MODID, name.concat("_extended")));
			strong.setRegistryName(new ResourceLocation(ModConsts.MODID, name.concat("_strong")));
			Map<String, PotionType> map = Maps.newHashMap();
			map.put("base", normal);
			map.put("extended", extended);
			map.put("strong", strong);
			checkIfRegisteredByForge(ForgeRegistries.POTION_TYPES, normal, extended, strong);
			ForgeRegistries.POTION_TYPES.registerAll(normal, extended, strong);
			POTION_TYPES.put(p, map);
		});
	}

	@Override
	public void register(AbstractDDDPotion obj) {
		if(ForgeRegistries.POTIONS.containsKey(obj.getRegistryName())) {
			DistinctDamageDescriptions.err(String.format("The object %s was registered by Forge before DDD registered it!", obj.getRegistryName()));
		}
		super.register(obj);
		ForgeRegistries.POTIONS.register(obj);
	}
	
	@SafeVarargs
	private static final <T extends IForgeRegistryEntry<T>> void checkIfRegisteredByForge(IForgeRegistry<T> registry, T...ts) {
		Stream.of(ts).map(IForgeRegistryEntry::getRegistryName).filter(registry::containsKey).forEach((loc) -> DistinctDamageDescriptions.err(String.format("The object %s was registered by Forge before DDD registered it!", loc)));
	}
}
