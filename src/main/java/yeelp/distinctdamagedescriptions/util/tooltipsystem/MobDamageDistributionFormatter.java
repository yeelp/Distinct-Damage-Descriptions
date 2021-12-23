package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * A formatter for mob damage distributions
 * @author Yeelp
 *
 */
public class MobDamageDistributionFormatter extends AbstractDamageDistributionFormatter {

	private static MobDamageDistributionFormatter instance;
	private final Function<ItemStack, Optional<ResourceLocation>> resourceLocationGetter;
	private Map<ResourceLocation, Float> cache;
	
	private MobDamageDistributionFormatter() {
		this(TooltipFormatterUtilities::getResourceLocationFromSpawnEgg);
	}
	
	protected MobDamageDistributionFormatter(Function<ItemStack, Optional<ResourceLocation>> f) {
		super(KeyTooltip.SHIFT, DDDNumberFormatter.PERCENT, DDDDamageFormatter.COLOURED, (s) -> f.apply(s).flatMap(TooltipFormatterUtilities::getMobDamageIfConfigured), "mobdistribution");
		this.resourceLocationGetter = f;
		this.cache = new HashMap<ResourceLocation, Float>();
	}
	
	/**
	 * Get the singleton instance
	 * @return the singleton instance
	 */
	public static MobDamageDistributionFormatter getInstance() {
		return instance == null ? instance = new MobDamageDistributionFormatter() : instance;
	}
	
	@Override
	public boolean supportsNumberFormat(DDDNumberFormatter f) {
		return true;
	}

	@Override
	protected boolean shouldShowDist(ItemStack stack) {
		return stack.getItem() instanceof ItemMonsterPlacer;
	}

	@Override
	protected float getDamageToDistribute(ItemStack stack) {
		switch(this.getNumberFormatter()) {
			case PERCENT:
				return 1.0f;
			case PLAIN:
			default:
				Optional<ResourceLocation> oLoc = this.resourceLocationGetter.apply(stack);
				if (oLoc.isPresent()) {
					ResourceLocation loc = oLoc.get();
					if(this.cache.containsKey(loc)) {
						return this.cache.get(loc);
					}
					Optional<Entity> oEntity = Optional.ofNullable(ForgeRegistries.ENTITIES.getValue(loc)).map((entry) -> entry.newInstance(Minecraft.getMinecraft().world));
					Optional<IAttributeInstance> oAttribute = oEntity.filter((e) -> e instanceof EntityLivingBase).<EntityLivingBase>map((e) -> (EntityLivingBase)e).map((e) -> e.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE));
					if(oAttribute.isPresent()) {
						float dmg = (float) oAttribute.get().getAttributeValue();
						this.cache.put(loc, dmg);
						return dmg;
					}
				}
				return 1.0f;
		}
	}

	@Override
	public TooltipOrder getType() {
		return TooltipOrder.MOB_DAMAGE;
	}
}
