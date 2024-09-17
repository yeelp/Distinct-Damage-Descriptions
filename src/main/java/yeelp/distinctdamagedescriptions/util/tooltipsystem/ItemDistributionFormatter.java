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
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.config.DDDConfigurations;
import yeelp.distinctdamagedescriptions.config.ModConfig;
import yeelp.distinctdamagedescriptions.util.lib.YResources;

/**
 * The singleton instance for formatting instances for generic items
 * 
 * @author Yeelp
 *
 */
public class ItemDistributionFormatter extends AbstractDamageDistributionFormatter {

	private static ItemDistributionFormatter instance;

	private ItemDistributionFormatter() {
		super(KeyTooltip.SHIFT, DDDDamageFormatter.COLOURED, DDDAPI.accessor::getDamageDistribution, "damagedistribution");
	}

	/**
	 * Get the singleton instance for this ItemDistributionFormatter if it exists,
	 * creating a new instance if it doesn't
	 * 
	 * @return The singleton instance, or a new instance if it doesn't exist.
	 */
	public static ItemDistributionFormatter getInstance() {
		return instance == null ? instance = new ItemDistributionFormatter() : instance;
	}

	@Override
	protected boolean shouldShowDist(ItemStack stack) {
		return YResources.getRegistryString(stack).filter(DDDConfigurations.items::configured).isPresent() || ModConfig.client.alwaysShowDamageDistTooltip;
	}

	@Override
	protected float getDamageToDistribute(ItemStack stack) {
		double dmg;
		if(this.getNumberFormattingStrategy() == DamageDistributionNumberFormat.PLAIN) {
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			if(player == null) {
				dmg = 1.0;
			}
			else {
				dmg = Minecraft.getMinecraft().player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
				Collection<AttributeModifier> mods = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
				dmg += mods.stream().filter((m) -> m.getOperation() == 0).mapToDouble((m) -> m.getAmount()).sum();
				dmg *= mods.stream().filter((m) -> m.getOperation() == 1).mapToDouble((m) -> m.getAmount()).reduce(Double::sum).orElse(1);
				dmg *= mods.stream().filter((m) -> m.getOperation() == 2).mapToDouble((m) -> m.getAmount()).reduce((d1, d2) -> d1 * d2).orElse(1);
				dmg += EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED);
			}
		}
		else {
			dmg = 1.0;
		}
		return (float) dmg;
	}

	@Override
	public TooltipOrder getType() {
		return TooltipOrder.DAMAGE;
	}

	@Override
	public ObjectFormatter<Float> getNumberFormattingStrategy() {
		return ModConfig.client.itemDamageFormat;
	}
}
