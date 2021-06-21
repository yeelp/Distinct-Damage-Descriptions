package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.IDamageDistribution;
import yeelp.distinctdamagedescriptions.util.DamageMap;

public class HwylaMobDamageFormatter extends HwylaTooltipFormatter<IDamageDistribution> {

	private static HwylaMobDamageFormatter instance;
	private final ITextComponent damageSuffix = new TextComponentTranslation("tooltips.distinctdamagedescriptions.damage").setStyle(new Style().setColor(TextFormatting.GRAY));

	private HwylaMobDamageFormatter() {
		super(KeyTooltip.SHIFT, DDDNumberFormatter.PERCENT, DDDDamageFormatter.STANDARD, HwylaMobDamageFormatter::getCap, new TextComponentTranslation("tooltips.distinctdamagedescriptions.mobdistribution").setStyle(new Style().setColor(TextFormatting.GRAY)));
	}
	
	/**
	 * Get the singleton instance
	 * @return the singleton instance
	 */
	public static HwylaMobDamageFormatter getInstance() {
		return instance == null ? instance = new HwylaMobDamageFormatter() : instance;
	}
	
	private static IDamageDistribution getCap(EntityLivingBase entity) {
		ItemStack heldItem = entity.getHeldItemMainhand();
		boolean hasEmptyHand = heldItem.isEmpty();
		if(!hasEmptyHand) {
			return DDDAPI.accessor.getDamageDistribution(heldItem);
		}
		return DDDAPI.accessor.getDamageDistribution(entity);
	}

	@Override
	public boolean supportsNumberFormat(DDDNumberFormatter f) {
		return true;
	}

	@Override
	protected Optional<List<String>> formatCapabilityFor(EntityLivingBase t, IDamageDistribution cap) {
		if(cap == null || t == null) {
			return Optional.empty();
		}
		double dmg;
		switch(this.getNumberFormatter()) {
			case PLAIN:
				dmg = t.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
				break;
			case PERCENT:
			default:
				dmg = 1.0;
				break;
		}
		DamageMap dMap = cap.distributeDamage((float) dmg);
		return Optional.of(dMap.entrySet().stream().sorted(Comparator.comparing(Entry::getKey)).map((e) -> this.makeOneDamageString(e.getValue(), e.getKey())).collect(Collectors.toList()));
	}
	
	private String makeOneDamageString(float amount, DDDDamageType type) {
		return String.format("   %s%s %s %s", TextFormatting.GRAY.toString(), this.getNumberFormatter().format(amount), this.getDamageFormatter().format(type), this.damageSuffix.getFormattedText());
	}
}
