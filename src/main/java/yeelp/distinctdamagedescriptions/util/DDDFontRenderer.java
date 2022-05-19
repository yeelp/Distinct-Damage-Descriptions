package yeelp.distinctdamagedescriptions.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
	private byte colourState;
	private int red, green, blue;
	private static DDDFontRenderer instance;
	private boolean dropShadow;
	private static final Method renderUnicodeChar = ObfuscationReflectionHelper.findMethod(FontRenderer.class, "func_78277_a", float.class, char.class, boolean.class);

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
					return this.renderUnicodeChar(marker.get().replaceWith().charAt(0), italic);
				case END:
					this.setColourState(255, 255, 255);
				default:
					break;
			}
		}
		else if(this.colourState > 0) {
			int val = (ch & 0xFF);
			switch(this.colourState++) {
				case 1:
					this.red = val;
					break;
				case 2:
					this.green = val;
					break;
				case 3:
					this.blue = val;
					break;
				default:
					break;
			}
			this.colourState %= 4;
			if(this.colourState == 0) {
				this.setColourState(this.red, this.green, this.blue);
			}
		}
		else {
			try {
				this.internal.posX = this.posX;
				this.internal.posY = this.posY;
				return (Float) renderUnicodeChar.invoke(this.internal, ch, italic);
				// return super.renderUnicodeChar(ch, italic);
			}
			catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				DistinctDamageDescriptions.err("Encountered problem invoking renderUnicodeChar(char, boolean)!");
				Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).forEach(DistinctDamageDescriptions::err);
				DistinctDamageDescriptions.warn("Will attempt to try to salvage the situation, calling super.renderUnicodeChar(char, boolean). If problems arise, open an issue at https://github.com/yeelp/Distinct-Damage-Descriptions/issues");
				return super.renderUnicodeChar(ch, italic);
			}
		}
		return 0;
	}

	@Override
	public List<String> listFormattedStringToWidth(String str, int wrapWidth) {
		return this.internal.listFormattedStringToWidth(str, wrapWidth);
	}

	@Override
	public int renderString(String text, float x, float y, int color, boolean dropShadow) {
		this.dropShadow = dropShadow;
		this.internal.renderString("", x, y, color, dropShadow);
		return super.renderString(text, x, y, color, dropShadow);
	}

	private void setColourState(int red, int green, int blue) {
		int colour = (red << 16) | (green << 8) | blue | (0xFF << 24);
		if((colour & -67108864) == 0) {
			colour |= -16777216;
		}
		if(this.dropShadow) {
			colour = (colour & 16579836) >> 2 | colour & -16777216;
		}
		this.setColor(((colour >> 16) & 255) / 255f, ((colour >> 8) & 255) / 255f, ((colour >> 0) & 255) / 255f, ((colour >> 24) & 255) / 255f);
	}

	/**
	 * Resets and returns the current custom FontRenderer instance which wraps the
	 * current FontRenderer adding additional functionality, if it exists. Else, a
	 * new one is created. The passed {@code currentFontRender} is updated in the
	 * instance to reflect the current FontRenderer that was supposed to be used.
	 * 
	 * @param currentFontRenderer . If null, DDD will fallback to Minecraft's font
	 *                             renderer
	 * @return The DDDFontRender instance which wraps the specified font renderer
	 */
	public static FontRenderer getInstance(FontRenderer currentFontRenderer) {
		FontRenderer fr = currentFontRenderer != null ? currentFontRenderer : Minecraft.getMinecraft().fontRenderer;
		if(fr == instance) {
			return currentFontRenderer;
		}
		if(instance == null) {
			instance = new DDDFontRenderer(fr);
			Minecraft mc = Minecraft.getMinecraft();
			((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(instance);
		}
		instance.internal = fr;
		return instance;
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		super.onResourceManagerReload(resourceManager);
		Minecraft mc = Minecraft.getMinecraft();
		LanguageManager lm = mc.getLanguageManager();
		this.setUnicodeFlag(lm.isCurrentLocaleUnicode() || mc.gameSettings.forceUnicodeFont);
		this.setBidiFlag(lm.isCurrentLanguageBidirectional());
	}
}
