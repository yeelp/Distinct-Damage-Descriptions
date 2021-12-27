package yeelp.distinctdamagedescriptions.integration.tic.tinkers.client;

enum Flexibility implements IEnumTranslation {
	FLEXIBLE,
	MODERATE,
	INFLEXIBLE;

	@Override
	public String getRootString() {
		return "flexibility";
	}

	static IBookString determineFlexibility(float variability) {
		Flexibility flexibility;
		if(variability > 0.7) {
			flexibility = INFLEXIBLE;
		}
		else if(variability > 0.3) {
			flexibility = MODERATE;
		}
		else {
			flexibility = FLEXIBLE;
		}
		return new EnumBookString<Flexibility>(flexibility);
	}
}
