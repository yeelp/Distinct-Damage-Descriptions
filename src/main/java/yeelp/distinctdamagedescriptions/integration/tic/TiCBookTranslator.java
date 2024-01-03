package yeelp.distinctdamagedescriptions.integration.tic;

import yeelp.distinctdamagedescriptions.util.Translations;
import yeelp.distinctdamagedescriptions.util.Translations.BasicTranslator;

public enum TiCBookTranslator {
	TINKERS("tinkers"),
	CONARM("conarm");

	private final BasicTranslator translator;

	private TiCBookTranslator(String root) {
		this.translator = new TiCTranslator(root);
	}

	public BasicTranslator getTranslator() {
		return this.translator;
	}

	private final class TiCTranslator extends BasicTranslator {

		private final String key;

		protected TiCTranslator(String root) {
			Translations.INSTANCE.super("");
			this.key = root.concat(".book");
		}

		@Override
		public String getKey(String key) {
			return super.getKey(this.key.concat(".").concat(key)).substring(1);
		}
	}
}
