package yeelp.distinctdamagedescriptions.client.screen;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Optional;

import com.google.common.base.Functions;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.proxy.ClientProxy;
import yeelp.distinctdamagedescriptions.util.Translations;
import yeelp.distinctdamagedescriptions.util.Translations.BasicTranslator;
import yeelp.distinctdamagedescriptions.util.lib.FileHelper;

@SideOnly(Side.CLIENT)
public final class GuiFingerprintViolationWarning extends GuiScreen {
	
	static final BasicTranslator TRANSLATOR = Translations.INSTANCE.getTranslator("fingerprint");
	private final GuiScreen base;
	private static final int BUTTON_SPACING = 24;
	private enum ButtonId {
		IGNORE("ignore") {
			@Override
			Optional<String> getURL() {
				return Optional.empty();
			}
		},
		CURSEFORGE("curseforge") {
			@Override
			Optional<String> getURL() {
				return Optional.of("https://www.curseforge.com/minecraft/mc-mods/distinct-damage-descriptions");
			}
		},
		MODRINTH("modrinth") {
			@Override
			Optional<String> getURL() {
				return Optional.of("https://modrinth.com/mod/distinct-damage-descriptions");
			}
		},
		CLOSE("close") {
			@Override
			Optional<String> getURL() {
				return Optional.empty();
			}
		};
		
		private final String translationKey;
		
		private ButtonId(String key) {
			this.translationKey = key;
		}
		
		int getID() {
			return this.ordinal();
		}
		
		GuiButton getButton(int x, int y) {
			return new GuiButton(this.getID(), x, y + this.getID() * BUTTON_SPACING, TRANSLATOR.translate(this.translationKey));
		}
		
		abstract Optional<String> getURL();
	}
	
	private String url;
	
	
	public GuiFingerprintViolationWarning(GuiScreen base) {
		this.base = base;
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
		for(int i = 0; i < 9; i++) {
			String s = TRANSLATOR.translate("info"+i);
			if(i == 0) {
				s = TextFormatting.RED + s + TextFormatting.RESET;
			}
			if(i == 2) {
				s = TextFormatting.GOLD + "Yeelp" + TextFormatting.RESET + s;
			}
			if(i == 6) {
				y += 10;
			}
			this.drawCenteredString(this.fontRenderer, s, x, y, 0xffffff);
			y += 10;
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		this.url = null;
		switch(button.id) {
			case 0: //I know what I'm doing!
				this.mc.displayGuiScreen(this.base);
				if(this.mc.currentScreen == null) {
					this.mc.setIngameFocus();
				}
				File configDirectory = DistinctDamageDescriptions.getModConfigDirectory();
				FileHelper.copyFile(getFingerprintIgnoreText(), new File(configDirectory, ClientProxy.FINGERPRINT_FILE), true);
				break;
			case 1: //Go to CurseForge page
				this.url = ButtonId.CURSEFORGE.getURL().get();
				break;
			case 2: //Go to Modrinth page
				this.url = ButtonId.MODRINTH.getURL().get();
				break;
			case 3: //Close game
				this.mc.shutdown();
				break;
		}
		if(this.url != null) {
			this.mc.displayGuiScreen(new GuiFingerprintLink(this, this.url));
		}
	}
	
	@Override
	public void confirmClicked(boolean result, int id) {
		if(id == 0) {
			try {
				if(result) {
					GuiFingerprintViolationWarning.openLink(new URI(this.url));
				}
			}
			catch(URISyntaxException e) {
				Arrays.stream(e.getStackTrace()).map(Functions.toStringFunction()).forEach(DistinctDamageDescriptions::err);
			}
			this.mc.displayGuiScreen(this);
		}
	}
	
	private static final void openLink(URI uri) {
		try {
			Class<?> desktop = Class.forName("java.awt.Desktop");
			Object desktopObj = desktop.getMethod("getDesktop").invoke(null);
			desktop.getMethod("browse", URI.class).invoke(desktopObj, uri);
		}
		catch(Throwable t) {
			Throwable cause = t.getCause();
			String msg = cause == null ? "No further information available." : cause.getMessage();
			DistinctDamageDescriptions.warn(msg);
		}
	}
	
	private static InputStream getFingerprintIgnoreText() {
		return new ByteArrayInputStream(String.format("VERSION %s removing this file will cause any fingerprint warnings to show up again!", ModConsts.VERSION).getBytes());
	}
	
	private static final class GuiFingerprintLink extends GuiConfirmOpenLink {
		private final GuiScreen screenIn;
		
		public GuiFingerprintLink(GuiScreen parentScreenIn, String url) {
			super(parentScreenIn, url, 0, true);
			this.screenIn = parentScreenIn;
		}
		
		@Override
		protected void actionPerformed(GuiButton button) throws IOException {
			super.actionPerformed(button);
			if(button.id == 1) {
				this.returnToParent();
			}
		}
		
		@Override
		protected void keyTyped(char typedChar, int keyCode) throws IOException {
			//Esc
			if(keyCode == 1) {
				this.returnToParent();
			}
		}
		
		private void returnToParent() {
			this.mc.displayGuiScreen(this.screenIn);
				if(this.mc.currentScreen == null) {
					this.mc.setIngameFocus();
				}
		}
	}
	
}
