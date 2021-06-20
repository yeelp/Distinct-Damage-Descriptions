package yeelp.distinctdamagedescriptions.util;

import net.minecraft.util.DamageSource;

public final class DamageMap extends DDDBaseMap<Float> {

	private static final long serialVersionUID = 1800888433080051037L;

	public DamageMap() {
		super(0.0f);
	}

	public DDDDamageSource makeDamageSource(DamageSource src) {
		return new DDDDamageSource(src, this.keySet());
	}
}
