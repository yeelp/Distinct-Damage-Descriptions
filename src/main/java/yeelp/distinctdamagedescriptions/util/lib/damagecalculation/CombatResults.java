package yeelp.distinctdamagedescriptions.util.lib.damagecalculation;

public final class CombatResults {
	public static final CombatResults NO_RESULTS = new CombatResults();
	
	private final boolean weakness, resisted, immunity, adaptability, shieldEffective;
	
	private final float amount;
	
	private CombatResults() {
		this(false, false, false, false, false, Float.NaN);
	}

	CombatResults(boolean weakness, boolean resisted, boolean immunity, boolean adaptability, boolean effectiveShield, float amount) {
		this.immunity = immunity;
		this.resisted = resisted;
		this.shieldEffective = effectiveShield;
		this.adaptability = adaptability;
		this.weakness = weakness;
		this.amount = amount;
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
	
	public float getAmount() {
		return this.amount;
	}
	
	public static final class ResultsBuilder {
		private boolean shield, weakness, resistance, adaptive, immunity;
		private float amount;
		
		public ResultsBuilder hasEffectiveShield() {
			this.shield = true;
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
		
		public CombatResults build() {
			return new CombatResults(this.weakness, this.resistance, this.immunity, this.adaptive, this.shield, this.amount);
		}
	}
}
