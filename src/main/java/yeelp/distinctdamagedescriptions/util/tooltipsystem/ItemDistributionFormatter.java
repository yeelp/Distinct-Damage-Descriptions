package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.Collection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.ModConfig;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.init.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

/**
 * The singleton instance for formatting instances for generic items
 * @author Yeelp
 *
 */
public class ItemDistributionFormatter extends AbstractDamageDistributionFormatter {
	
	private static ItemDistributionFormatter instance;
	
	private ItemDistributionFormatter() {
		super(KeyTooltip.SHIFT, DDDNumberFormatter.PERCENT, DDDDamageFormatter.COLOURED, DDDAPI.accessor::getDamageDistribution, new TextComponentTranslation("tooltips.distinctdamagedescriptions.damagedistribution").setStyle(new Style().setColor(TextFormatting.GRAY)));
	}

	/**
	 * Get the singleton instance for this ItemDistributionFormatter if it exists, creating a new instance if it doesn't
	 * @return The singleton instance, or a new instance if it doesn't exist.
	 */
	public static ItemDistributionFormatter getInstance() {
		return instance == null ? instance = new ItemDistributionFormatter() : instance;
	}
	
	@Override
	public boolean supportsNumberFormat(DDDNumberFormatter f) {
		return true;
	}
	
	@Override
	protected boolean shouldShowDist(ItemStack stack) {
		return DDDConfigurations.items.configured(YResources.getRegistryString(stack)) || ModConfig.client.alwaysShowDamageDistTooltip;
	}
	
	@Override
	protected float getDamageToDistribute(ItemStack stack) {
		double dmg;
		switch(this.getNumberFormatter()) {
			case PLAIN:
				EntityPlayerSP player = Minecraft.getMinecraft().player;
				if(player == null) {
					dmg = 1.0;
					break;
				}
				dmg = Minecraft.getMinecraft().player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
				Collection<AttributeModifier> mods = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
				dmg += mods.stream().filter((m) -> m.getOperation() == 0).mapToDouble((m) -> m.getAmount()).sum();
				dmg *= mods.stream().filter((m) -> m.getOperation() == 1).mapToDouble((m) -> m.getAmount()).reduce(Double::sum).orElse(1);
				dmg *= mods.stream().filter((m) -> m.getOperation() == 2).mapToDouble((m) -> m.getAmount()).reduce((d1, d2) -> d1 * d2).orElse(1);
				dmg += EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED);
				break;
			case PERCENT:
			default:
				dmg = 1.0;
				break;
		}
		return (float) dmg;
	}
}
