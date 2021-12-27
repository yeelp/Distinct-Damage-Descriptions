package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

import java.util.OptionalDouble;

import yeelp.distinctdamagedescriptions.util.DamageMap;

public final class CombatResults {
	public static final CombatResults NO_RESULTS = new CombatResults();

	private final boolean weakness, resisted, immunity, adaptability, shieldEffective;

	private final float amount, shieldRatio, ratio;

	private CombatResults() {
		this(false, false, false, false, false, Float.NaN, Float.NaN, Float.NaN);
	}

	CombatResults(boolean weakness, boolean resisted, boolean immunity, boolean adaptability, boolean effectiveShield, float amount, float startingAmount, float shieldDmg) {
		this.immunity = immunity;
		this.resisted = resisted;
		this.shieldEffective = effectiveShield;
		this.adaptability = adaptability;
		this.weakness = weakness;
		this.amount = amount;
		this.shieldRatio = startingAmount > 0 ? shieldDmg / startingAmount : Float.NaN;
		this.ratio = startingAmount > 0 ? amount / startingAmount : Float.NaN;
	}

	public boolean wasWeaknessHit() {
		return this.weakness;
	}

	public boolean wasResistanceHit() {
		return this.resisted;
	}

	public boolean wasImmunityTriggered() {
		return this.immunity;
	}

	public boolean wasAdaptabilityTriggered() {
		return this.adaptability;
	}

	public boolean wasShieldEffective() {
		return this.shieldEffective;
	}

	public OptionalDouble getAmount() {
		return Float.isNaN(this.amount) ? OptionalDouble.empty() : OptionalDouble.of(this.amount);
	}

	public OptionalDouble getRatio() {
		return Float.isNaN(this.ratio) ? OptionalDouble.empty() : OptionalDouble.of(this.ratio);
	}

	public OptionalDouble getShieldRatio() {
		return Float.isNaN(this.shieldRatio) ? OptionalDouble.empty() : OptionalDouble.of(this.shieldRatio);
	}

	public static final class ResultsBuilder {
		private boolean shield, weakness, resistance, adaptive, immunity;
		private float startingAmount, shieldBlockDmg;
		private float amount = Float.NaN;

		public ResultsBuilder hasEffectiveShield(DamageMap newDmg) {
			this.shield = true;
			this.shieldBlockDmg = newDmg.values().stream().reduce(Float::sum).orElse(0.0f);
			return this;
		}

		public ResultsBuilder hasWeakness() {
			this.weakness = true;
			return this;
		}

		public ResultsBuilder hasResistance() {
			this.resistance = true;
			return this;
		}

		public ResultsBuilder hasImmunity() {
			this.immunity = true;
			return this;
		}

		public ResultsBuilder wasAdaptive() {
			this.adaptive = true;
			return this;
		}

		public ResultsBuilder withAmount(float amount) {
			this.amount = amount;
			return this;
		}

		public ResultsBuilder withStartingDamage(float amount) {
			this.startingAmount = amount;
			return this;
		}

		public CombatResults build() {
			return new CombatResults(this.weakness, this.resistance, this.immunity, this.adaptive, this.shield, this.amount, this.startingAmount, this.shieldBlockDmg);
		}
	}
}
