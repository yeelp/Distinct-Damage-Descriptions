package yeelp.distinctdamagedescriptions.integration.fermiumbooter.client.screen;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.distinctdamagedescriptions.ModConsts.IntegrationIds;
import yeelp.distinctdamagedescriptions.util.Translations;
import yeelp.distinctdamagedescriptions.util.Translations.BasicTranslator;

@SideOnly(Side.CLIENT)
public final class GuiFermiumBooterNotFound extends GuiScreen {

	static final BasicTranslator TRANSLATOR = Translations.INSTANCE.getTranslator(IntegrationIds.FERMIUM_ID);
	private final GuiScreen base;
	private final String modTitle;
	private static final int BUTTON_SPACING = 24;
	
	private enum ButtonId {
		IGNORE("ignore"),
		CLOSE("close");
		
		private final String translationKey;
		
		private ButtonId(String translationKey) {
			this.translationKey = translationKey;
		}
		
		int getID() {
			return this.ordinal();
		}
		
		GuiButton getButton(int x, int y) {
			return new GuiButton(this.getID(), x, y + this.getID() * BUTTON_SPACING, TRANSLATOR.translate(this.translationKey));
		}
	}
	
	public GuiFermiumBooterNotFound(GuiScreen base, String modTitle) {
		this.base = base;
		this.modTitle = TextFormatting.AQUA + modTitle + TextFormatting.RESET;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		int x = this.width/2 - 100;
		int y = this.height/2 + 50;
		this.buttonList.clear();
		for(ButtonId id : ButtonId.values()) {
			this.buttonList.add(id.getButton(x, y));
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		int x = this.width/2;
		int y = this.height/6 + 10;
		for(int i = 0; i < 4; i++) {
			String s;
			if(i % 2 == 0) {
				s = TRANSLATOR.translate("info"+i, this.modTitle);
			}
			else {
				s = TRANSLATOR.translate("info"+i);
			}
			this.drawCenteredString(this.fontRenderer, s, x, y, 0xffffff);
			y += 10;
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch(button.id) {
			case 0: //Go to title
				this.mc.displayGuiScreen(this.base);
				if(this.mc.currentScreen == null) {
					this.mc.setIngameFocus();
				}
				break;
			case 1: //Close game
				this.mc.shutdown();
				break;
		}
	}
}
