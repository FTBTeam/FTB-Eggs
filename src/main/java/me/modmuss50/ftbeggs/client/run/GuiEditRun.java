package me.modmuss50.ftbeggs.client.run;

import me.modmuss50.ftbeggs.api.RunData;
import me.modmuss50.ftbeggs.api.RunManger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;

/**
 * Created by modmuss50 on 01/03/2017.
 */
public class GuiEditRun extends GuiScreen {

	private static final int CANCEL_BUTTON_ID = 1;
	private static final int UPLOAD_BUTTON_ID = 0;
	public GuiScreen parent;
	GuiButton uploadButton;
	RunData data;

	private GuiTextField urlField;

	private String[] validDomains = new String[]{"youtube.com", "twitch.tv", "beam.pro"};

	public GuiEditRun(GuiScreen parent, RunData data) {
		this.parent = parent;
		this.data = data;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new GuiButton(CANCEL_BUTTON_ID, this.width / 2 + 5, this.height - 35, 150, 20, I18n.format("gui.cancel")));
		this.buttonList.add(uploadButton = new GuiButton(UPLOAD_BUTTON_ID, this.width / 2 - 155, this.height - 35, 150, 20, "Update information"));
		this.urlField = new GuiTextField(9, this.fontRenderer, this.width / 2 - 100, 60, 200, 20);
		urlField.setMaxStringLength(500);
		this.urlField.setFocused(true);
		this.urlField.setText("");
		this.uploadButton.enabled = false;
	}

	@Override
	public void actionPerformed(GuiButton guiButton) throws IOException {
		super.actionPerformed(guiButton);
		if (guiButton.id == CANCEL_BUTTON_ID) {
			Minecraft.getMinecraft().displayGuiScreen(parent);
		}
		if (guiButton.id == UPLOAD_BUTTON_ID) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiPostUpload(parent, RunManger.updateData(data, urlField.getText()), "Run updated!"));
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		this.urlField.updateCursorCounter();
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		this.urlField.textboxKeyTyped(typedChar, keyCode);
		boolean isValid = false;
		for (String domain : validDomains) {
			if (urlField.getText().contains(domain)) {
				isValid = true;
				break;
			}
		}
		uploadButton.enabled = isValid;

	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.urlField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.urlField.drawTextBox();
		this.drawString(this.fontRenderer, I18n.format("Enter Video URL"), this.width / 2 - 100, 47, -6250336);
		this.drawCenteredString(this.fontRenderer, I18n.format(TextFormatting.BLUE + "F" + TextFormatting.GREEN + "T" + TextFormatting.RED + "B" + TextFormatting.WHITE + " run edit form"), this.width / 2, 20, -1);

		this.drawString(this.fontRenderer, "Use this to add a video to a previously submitted video", this.width / 2 - 100, 90, -1);


		int offset = 140;
		if (data.players != null && !data.players.isEmpty()) {
			this.drawString(this.fontRenderer, "Players:", this.width / 2 - 100, offset, -1);
			for (RunData.PlayerData player : data.players) {
				this.drawString(this.fontRenderer, player.name, this.width / 2 - 90, offset += 10, -6250336);
			}
		}

		this.drawString(this.fontRenderer, "Unique run hash: " + data.runHash, 10, this.height - 10, -6250336);
	}
}
