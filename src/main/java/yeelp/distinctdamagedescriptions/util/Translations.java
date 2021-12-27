package yeelp.distinctdamagedescriptions.util;

import java.util.HashMap;

import com.google.common.collect.Maps;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import yeelp.distinctdamagedescriptions.ModConsts;

public enum Translations {
	INSTANCE;

	private final HashMap<String, Translator> cache = Maps.newHashMap();

	public class Translator {
		private final String root;

		protected Translator(String root) {
			this.root = root;
		}

		public ITextComponent getComponent(String key) {
			return new TextComponentTranslation(this.getKey(key));
		}

		public ITextComponent getComponent(String key, Object... objects) {
			return new TextComponentTranslation(this.getKey(key), objects);
		}

		public ITextComponent getComponent(String key, Style style) {
			return new TextComponentTranslation(this.getKey(key)).setStyle(style);
		}

		public ITextComponent getComponent(String key, Style style, Object... objects) {
			return new TextComponentTranslation(this.getKey(key), objects).setStyle(style);
		}

		public String translate(String key) {
			return this.getComponent(key).getFormattedText();
		}

		public String translate(String key, Object... objects) {
			return this.getComponent(key, objects).getFormattedText();
		}

		public String translate(String key, Style style) {
			return this.getComponent(key, style).getFormattedText();
		}

		public String translate(String key, Style style, Object... objects) {
			return this.getComponent(key, style, objects).getFormattedText();
		}

		protected String getKey(String key) {
			return this.root.concat("." + ModConsts.MODID).concat("." + key);
		}

		protected final String getRoot() {
			return this.root;
		}
	}

	public Translator getTranslator(String root) {
		return this.cache.compute(root, (r, t) -> t == null ? new Translator(r) : t);
	}

	public String translate(String root, String key) {
		return this.getTranslator(root).translate(key);
	}

	public String translate(String root, String key, Object... objects) {
		return this.getTranslator(root).translate(key, objects);
	}
}
