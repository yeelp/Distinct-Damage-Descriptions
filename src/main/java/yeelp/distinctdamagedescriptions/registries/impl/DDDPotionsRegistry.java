package yeelp.distinctdamagedescriptions.registries.impl;

import java.util.Map;
import java.util.stream.Stream;

import com.google.common.base.Functions;
import com.google.common.collect.Maps;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.potion.DDDPotion;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.registries.IDDDPotionsRegistry;

public class DDDPotionsRegistry extends DDDBaseRegistry<DDDPotion> implements IDDDPotionsRegistry {

	private static final int DURATION = 180 * 20;
	private static final Map<DDDPotion, Map<String, PotionType>> POTION_TYPES = Maps.newHashMap();
	
	public DDDPotionsRegistry() {
		super(Functions.compose(ResourceLocation::getResourcePath, DDDPotion::getRegistryName), "Potions");
	}
	
	@Override
	public void init() {
		DDDRegistries.damageTypes.forEach((t) -> {
			if(t != DDDBuiltInDamageType.NORMAL && t != DDDBuiltInDamageType.UNKNOWN) {
				Stream.of(DDDPotion.EffectType.values()).map((e) -> new DDDPotion(e, t)).forEach(this::register);
			}
		});
		this.iterator().forEachRemaining((p) -> {
			PotionType normal = new PotionType(new PotionEffect[] {new PotionEffect(p, DURATION, 0)});
			PotionType extended = new PotionType(new PotionEffect[] {new PotionEffect(p, 2 * DURATION, 0)});
			PotionType strong = new PotionType(new PotionEffect[] {new PotionEffect(p, DURATION / 2, 1)});
			String name = p.getType().getTypeName() + "." + p.getEffect().getEffect();
			normal.setRegistryName(new ResourceLocation(ModConsts.MODID, name));
			extended.setRegistryName(new ResourceLocation(ModConsts.MODID, name.concat("_extended")));
			strong.setRegistryName(new ResourceLocation(ModConsts.MODID, name.concat("_strong")));
			Map<String, PotionType> map = Maps.newHashMap();
			map.put("base", normal);
			map.put("extended", extended);
			map.put("strong", strong);
			ForgeRegistries.POTION_TYPES.registerAll(normal, extended, strong);
			POTION_TYPES.put(p, map);
		});
	}

	@Override
	public void register(DDDPotion obj) {
		ForgeRegistries.POTIONS.register(obj);
		super.register(obj);
	}
	
	
}
