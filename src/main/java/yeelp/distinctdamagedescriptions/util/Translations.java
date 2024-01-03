package yeelp.distinctdamagedescriptions.util;

import java.util.HashMap;

import com.google.common.collect.Maps;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.util.lib.YLib;

public enum Translations {
	INSTANCE;

	private final HashMap<String, BasicTranslator> basicCache = Maps.newHashMap();
	private final HashMap<String, LayeredTranslator> layeredCache = Maps.newHashMap();
	
	public abstract class AbstractTranslator {
		private final String root;
		
		protected AbstractTranslator(String root) {
			this.root = root;
		}
		
		public final ITextComponent getComponent(String key) {
			return new TextComponentTranslation(this.getKey(key));
		}

		public final ITextComponent getComponent(String key, Object... objects) {
			return new TextComponentTranslation(this.getKey(key), objects);
		}

		public final ITextComponent getComponent(String key, Style style) {
			return new TextComponentTranslation(this.getKey(key)).setStyle(style);
		}

		public final ITextComponent getComponent(String key, Style style, Object... objects) {
			return new TextComponentTranslation(this.getKey(key), objects).setStyle(style);
		}

		public final String translate(String key) {
			return this.getComponent(key).getFormattedText();
		}

		public final String translate(String key, Object... objects) {
			return this.getComponent(key, objects).getFormattedText();
		}

		public final String translate(String key, Style style) {
			return this.getComponent(key, style).getFormattedText();
		}

		public final String translate(String key, Style style, Object... objects) {
			return this.getComponent(key, style, objects).getFormattedText();
		}

		public abstract String getKey(String key);

		protected final String getRoot() {
			return this.root;
		}
	}

	public class BasicTranslator extends AbstractTranslator {

		protected BasicTranslator(String root) {
			super(root);
		}

		@Override
		public String getKey(String key) {
			return YLib.joinNiceString(false, ".", this.getRoot(), ModConsts.MODID, key);
		}
	}
	
	public class LayeredTranslator extends AbstractTranslator {

		private String subRoot;
		protected LayeredTranslator(String root, String...subRoots) {
			super(root);
			this.subRoot = YLib.joinNiceString(false, ".", subRoots);
		}

		@Override
		public String getKey(String key) {
			return YLib.joinNiceString(false, ".", this.getRoot(), ModConsts.MODID, this.subRoot, key);
		}
		
	}

	public BasicTranslator getTranslator(String root) {
		return this.basicCache.compute(root, (r, t) -> t == null ? new BasicTranslator(r) : t);
	}
	
	public LayeredTranslator getLayeredTranslator(String root, String...subRoots) {
		StringBuilder sb = new StringBuilder();
		sb.append(root);
		for(int i = 0; i < subRoots.length; sb.append(subRoots[i++]));
		return this.layeredCache.compute(sb.toString(), (r, t) -> t == null ? new LayeredTranslator(root, subRoots) : t);
	}

	public String translate(String root, String key) {
		return this.getTranslator(root).translate(key);
	}

	public String translate(String root, String key, Object... objects) {
		return this.getTranslator(root).translate(key, objects);
	}
}
