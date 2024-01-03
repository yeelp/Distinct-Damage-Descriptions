package yeelp.distinctdamagedescriptions.capability.impl;

import net.minecraft.item.ItemStack;
import yeelp.distinctdamagedescriptions.capability.IArmorDistribution;
import yeelp.distinctdamagedescriptions.util.ArmorMap;
import yeelp.distinctdamagedescriptions.util.ArmorValues;

public final class DefaultArmorDistribution extends DefaultDistribution implements IArmorDistribution {

	private static DefaultArmorDistribution instance;
	
	public static DefaultArmorDistribution getInstance() {
		return instance == null ? instance = new DefaultArmorDistribution() : instance;
	}
	
	private DefaultArmorDistribution() {
		super();
	}
	
	@Override
	public ArmorMap distributeArmor(float armor, float toughness) {
		return super.distribute(new ArmorMap(), (f) -> new ArmorValues(armor * f, toughness * f));
	}

	@Override
	public IArmorDistribution copy() {
		return this;
	}

	@Override
	public IArmorDistribution update(ItemStack owner) {
		return this;
	}

}
