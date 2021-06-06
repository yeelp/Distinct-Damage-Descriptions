package yeelp.distinctdamagedescriptions.util.lib;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;

/**
 * Simple helper class for keyboard inputs Borrwed from AppleSkin's own <a
 * href=https://github.com/squeek502/AppleSkin/blob/821f83b6405f434ad4e8969a103bf6396cdd4a86/java/squeek/appleskin/helpers/KeyHelper.java>KeyHelper</a>
 * 
 * @author Yeelp
 *
 */
public final class KeyHelper {
	/**
	 * Is the shift key held?
	 * 
	 * @return true if left or right shift is held.
	 */
	public static boolean isShiftHeld() {
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
	}

	/**
	 * Is control held? Will use option key as well for Mac.
	 * 
	 * @return true if control is held or option is held (Mac only).
	 */
	public static boolean isCtrlHeld() {
		boolean isCtrlKeyDown = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
		if(!isCtrlKeyDown && Minecraft.IS_RUNNING_ON_MAC) {
			isCtrlKeyDown = Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA);
		}
		return isCtrlKeyDown;
	}
}
