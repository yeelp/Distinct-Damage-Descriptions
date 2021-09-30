package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;

/**
 * A formatter for mob damage distributions
 * @author Yeelp
 *
 */
public class MobDamageDistributionFormatter extends AbstractDamageDistributionFormatter {

	private static MobDamageDistributionFormatter instance;
	private Map<ResourceLocation, Float> cache;
	
	private MobDamageDistributionFormatter() {
		super(KeyTooltip.SHIFT, DDDNumberFormatter.PERCENT, DDDDamageFormatter.COLOURED, (s) -> DDDConfigurations.mobDamage.get(Optional.ofNullable(ItemMonsterPlacer.getNamedIdFrom(s)).map(ResourceLocation::toString).orElse("")), new TextComponentTranslation("tooltips.distinctdamagedescriptions.mobdistribution").setStyle(new Style().setColor(TextFormatting.GRAY)));
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
				Optional<ResourceLocation> oLoc = Optional.ofNullable(ItemMonsterPlacer.getNamedIdFrom(stack));
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
}
