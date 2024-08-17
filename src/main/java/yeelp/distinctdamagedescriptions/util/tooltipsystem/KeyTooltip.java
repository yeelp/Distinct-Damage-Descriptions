package yeelp.distinctdamagedescriptions.util.tooltipsystem;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import yeelp.distinctdamagedescriptions.ModConsts.TooltipConsts;
import yeelp.distinctdamagedescriptions.util.Translations;
import yeelp.distinctdamagedescriptions.util.lib.KeyHelper;

/**
 * Tooltip pieces corresponding to the info displayed when the user is prompted
 * to hold down either &lt;SHIFT&gt; or &lt;CTRL&gt;
 * 
 * @author Yeelp
 *
 */
public enum KeyTooltip {

	/**
	 * The tooltip corresponding to holding down &lt;SHIFT&gt;
	 */
	SHIFT(TooltipConsts.SHIFT) {
		@Override
		public boolean checkKeyIsHeld() {
			return KeyHelper.isShiftHeld();
		}
	},

	/**
	 * The tooltip corresponding to holding down &lt;CTRL&gt;
	 */
	CTRL(TooltipConsts.CTRL) {
		@Override
		public boolean checkKeyIsHeld() {
			return KeyHelper.isCtrlHeld();
		}
	};

	private ITextComponent comp;

	private KeyTooltip(String string) {
		this.comp = Translations.INSTANCE.getTranslator(TooltipConsts.KEYS_ROOT).getComponent(string, new Style().setColor(TextFormatting.YELLOW));
	}

	/**
	 * Get the formatted String associated with this key but only when the key is
	 * NOT held down.
	 * 
	 * @return the formatted String when the key is not held down, otherwise an
	 *         empty String
	 */
	public String getKeyText() {
		return this.checkKeyIsHeld() ? "" : " " + this.comp.getFormattedText();
	}

	/**
	 * Is the key for this KeyTooltip held down?
	 * 
	 * @return true if held down, false if not.
	 */
	public abstract boolean checkKeyIsHeld();
}
