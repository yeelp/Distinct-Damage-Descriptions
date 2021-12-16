package yeelp.distinctdamagedescriptions.integration.tic;

import yeelp.distinctdamagedescriptions.util.Translations;
import yeelp.distinctdamagedescriptions.util.Translations.Translator;

public enum TiCBookTranslator {
	TINKERS("tinkers"),
	CONARM("conarm");
	
	private final Translator translator;
	private TiCBookTranslator(String root) {
		this.translator = new TiCTranslator(root);
	}
	
	public Translator getTranslator() {
		return this.translator;
	}
	
	private final class TiCTranslator extends Translator {

		private final String key;
		protected TiCTranslator(String root) {
			Translations.INSTANCE.super("");
			this.key = root.concat(".book");
		}

		@Override
		protected String getKey(String key) {
			return super.getKey(this.key.concat(".").concat(key)).substring(1);
		}
	}
}
