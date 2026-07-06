package yeelp.distinctdamagedescriptions.mixin;

import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yeelp.distinctdamagedescriptions.util.DDDFontColour;
import yeelp.distinctdamagedescriptions.util.DDDFontColour.Marker;

import java.util.Optional;

//fully qualify import to ignore deprecation warnings
@SuppressWarnings("deprecation")
@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer implements net.minecraft.client.resources.IResourceManagerReloadListener {

	@Unique
	private static byte distinct_Damage_Descriptions$colourState;
	@Unique
	private static int distinct_Damage_Descriptions$red, distinct_Damage_Descriptions$green, distinct_Damage_Descriptions$blue;

	@Unique
	private static boolean distinct_Damage_Descriptions$dropShadow;
	
	@Shadow(remap = false)
	protected abstract void setColor(float r, float g, float b, float a);
	
	@Shadow
	protected abstract float renderUnicodeChar(char ch, boolean italic);
		
	@Inject(method = "renderUnicodeChar(CZ)F", at = @At("HEAD"), cancellable = true)
	public void renderUnicodeChar(char ch, boolean italic, CallbackInfoReturnable<Float> info) {
		Optional<Marker> marker = DDDFontColour.Marker.getMarker(ch);
		if(marker.isPresent()) {
			switch(marker.get()) {
				case START:
					distinct_Damage_Descriptions$colourState = 1;
					break;
				case SPACE:
					info.setReturnValue(this.renderUnicodeChar(marker.get().replaceWith().charAt(0), italic));
				case END:
					this.distinct_Damage_Descriptions$setColourState(255, 255, 255);
				default:
					break;
			}
			info.setReturnValue(0.0f);
		}
		else if(distinct_Damage_Descriptions$colourState > 0) {
			int val = (ch & 0xFF);
			switch(distinct_Damage_Descriptions$colourState++) {
				case 1:
					distinct_Damage_Descriptions$red = val;
					break;
				case 2:
					distinct_Damage_Descriptions$green = val;
					break;
				case 3:
					distinct_Damage_Descriptions$blue = val;
					break;
				default:
					break;
			}
			distinct_Damage_Descriptions$colourState %= 4;
			if(distinct_Damage_Descriptions$colourState == 0) {
				this.distinct_Damage_Descriptions$setColourState(distinct_Damage_Descriptions$red, distinct_Damage_Descriptions$green, distinct_Damage_Descriptions$blue);
			}
			info.setReturnValue(0.0f);
		}
	}
	
	@SuppressWarnings({"static-method", "SpellCheckingInspection"})
	@Inject(method = "renderString(Ljava/lang/String;FFIZ)I", at = @At("HEAD"))
	public void renderString(@SuppressWarnings("unused") String text, @SuppressWarnings("unused") float x, @SuppressWarnings("unused") float y, @SuppressWarnings("unused") int color, boolean dropShadow, @SuppressWarnings("unused") CallbackInfoReturnable<Integer> info) {
		MixinFontRenderer.distinct_Damage_Descriptions$dropShadow = dropShadow;
	}
	
	@Unique
	private void distinct_Damage_Descriptions$setColourState(int red, int green, int blue) {
		int colour = (red << 16) | (green << 8) | blue | (0xFF << 24);
        //noinspection ConstantValue
        if((colour & -67108864) == 0) {
			colour |= -16777216;
		}
		if(distinct_Damage_Descriptions$dropShadow) {
			colour = (colour & 16579836) >> 2 | colour & -16777216;
		}
		this.setColor(((colour >> 16) & 255) / 255f, ((colour >> 8) & 255) / 255f, ((colour) & 255) / 255f, ((colour >> 24) & 255) / 255f);
	}
}
