package yeelp.distinctdamagedescriptions.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import yeelp.distinctdamagedescriptions.util.DDDFontColour;
import yeelp.distinctdamagedescriptions.util.DDDFontColour.Marker;

@SuppressWarnings("deprecation")
@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer implements IResourceManagerReloadListener {

	private static byte colourState;
	private static int red, green, blue;

	private static boolean dropShadow;
	
	@Shadow
	protected abstract void setColor(float r, float g, float b, float a);
	
	@Shadow
	protected abstract float renderUnicodeChar(char ch, boolean italic);
		
	@Inject(method = "renderUnicodeChar(CZ)F", at = @At("HEAD"), cancellable = true)
	public void renderUnicodeChar(char ch, boolean italic, CallbackInfoReturnable<Float> info) {
		Optional<Marker> marker = DDDFontColour.Marker.getMarker(ch);
		if(marker.isPresent()) {
			switch(marker.get()) {
				case START:
					colourState = 1;
					break;
				case SPACE:
					info.setReturnValue(this.renderUnicodeChar(marker.get().replaceWith().charAt(0), italic));
				case END:
					this.setColourState(255, 255, 255);
				default:
					break;
			}
			info.setReturnValue(0.0f);
		}
		else if(colourState > 0) {
			int val = (ch & 0xFF);
			switch(colourState++) {
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
			colourState %= 4;
			if(colourState == 0) {
				this.setColourState(red, green, blue);
			}
			info.setReturnValue(0.0f);
		}
	}
	
	@SuppressWarnings("static-method")
	@Inject(method = "renderString(Ljava/lang/String;FFIZ)I", at = @At("HEAD"))
	public void renderString(@SuppressWarnings("unused") String text, @SuppressWarnings("unused") float x, @SuppressWarnings("unused") float y, @SuppressWarnings("unused") int color, boolean dropShadow, @SuppressWarnings("unused") CallbackInfoReturnable<Integer> info) {
		MixinFontRenderer.dropShadow = dropShadow;
	}
	
	private void setColourState(int red, int green, int blue) {
		int colour = (red << 16) | (green << 8) | blue | (0xFF << 24);
		if((colour & -67108864) == 0) {
			colour |= -16777216;
		}
		if(dropShadow) {
			colour = (colour & 16579836) >> 2 | colour & -16777216;
		}
		this.setColor(((colour >> 16) & 255) / 255f, ((colour >> 8) & 255) / 255f, ((colour >> 0) & 255) / 255f, ((colour >> 24) & 255) / 255f);
	}
}
