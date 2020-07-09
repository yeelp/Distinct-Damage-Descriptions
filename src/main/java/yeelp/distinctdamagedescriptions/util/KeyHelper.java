package yeelp.distinctdamagedescriptions.util;

import org.lwjgl.input.Keyboard;

public final class KeyHelper
{
	public static boolean isShiftHeld()
	{
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
	}
}
