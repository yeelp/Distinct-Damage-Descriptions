package yeelp.distinctdamagedescriptions.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.util.DDDFontColour.Marker;

/**
 * Singleton custom FontRenderer based off CoFH's CoFHFontRenderer Credit to
 * CoFH team, KingLemming and RWTema.
 * 
 * @author Yeelp
 *
 */
@SideOnly(Side.CLIENT)
public final class DDDFontRenderer extends FontRenderer {

	private FontRenderer internal;
	private byte colourState, red, green, blue;
	private static DDDFontRenderer instance;
	private int numChars = 0;
	private boolean dropShadow;
	private static final Method renderString = ObfuscationReflectionHelper.findMethod(FontRenderer.class, "func_180455_b", int.class, String.class, float.class, float.class, int.class, boolean.class);
	private static final Method resetStyles = ObfuscationReflectionHelper.findMethod(FontRenderer.class, "func_78265_b", Void.TYPE, new Class<?>[] {});
	private static final Method enableAlpha = ObfuscationReflectionHelper.findMethod(FontRenderer.class, "func_179141_d", Void.TYPE, new Class<?>[] {});
	
	private DDDFontRenderer(FontRenderer fr) {
		super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine, true);
		this.internal = fr;
	}

	@Override
	protected float renderUnicodeChar(char ch, boolean italic) {
		Optional<Marker> marker = DDDFontColour.Marker.getMarker(ch);
		if(marker.isPresent()) {
			switch(marker.get()) {
				case START:
					this.colourState = 1;
					break;
				case SPACE:
					return this.renderUnicodeChar(' ', italic);
				case END:
					this.setColor(1f, 1f, 1f, 1f);
				default:
					break;
			}
		}
		else if (this.colourState > 0) {
			byte val = (byte) (ch & 0xFF);
			switch(this.colourState++) {
				case 1:
					red = val;
					break;
				case 2:
					green = val;
					break;
				case 3:
					blue = val;
					break;
				default:
					break;
			}
			this.colourState %= 4;
			if(this.colourState == 0) {
				this.setColourState(red, green, blue);
			}
		}
		else {
			return super.renderUnicodeChar(ch, italic);
		}
		return 0;
	}

	@Override
	public int drawString(String text, float x, float y, int color, boolean dropShadow) {
		try {
			enableAlpha.invoke(internal, new Object[] {});
			resetStyles.invoke(internal, new Object[] {});
			int i = Integer.MIN_VALUE;
			if(dropShadow) {
				this.dropShadow = true;
				i = ((Integer)renderString.invoke(internal, text, x + 1, y + 1, color, true));
				this.dropShadow = false;
			}
			return Math.max(i, (Integer)renderString.invoke(internal, text, x, y, color, false));
		}
		catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			DistinctDamageDescriptions.fatal("Could not draw string by reflection!");
			DistinctDamageDescriptions.fatal(Arrays.toString(e.getStackTrace()));
			throw new RuntimeException(e);
		}
	}
	
	private void setColourState(byte red, byte green, byte blue) {
		int colour = (red << 16) | (green << 8) | blue | (0xFF << 24);
		if((colour & -67108864) == 0) {
			colour |= -16777216;
		}
		if(dropShadow) {
			colour = (colour & 16579836) >> 2 | colour & -16777216;
		}
		this.setColor(((colour >> 16) & 255) / 255f, ((colour >> 8) & 255) / 255f, ((colour >> 0) & 255) / 255f, ((colour >> 24) & 255) / 255f);
	}

	/**
	 * Resets and returns the current custom FontRenderer instance which wraps the current
	 * FontRenderer adding additional functionality, if it exists. Else, a new one
	 * is created. The passed {@code currentFontRender} is updated in the instance
	 * to reflect the current FontRenderer that was supposed to be used.
	 * 
	 * @param currentFontRenderer
	 * @return The DDDFontRender instance which wraps the specified font renderer
	 */
	public static FontRenderer getInstance(FontRenderer currentFontRenderer) {
		if(instance == null) {
			instance = new DDDFontRenderer(currentFontRenderer);
		}
		instance.internal = currentFontRenderer;
		instance.numChars = 0;
		return instance;
	}
}
