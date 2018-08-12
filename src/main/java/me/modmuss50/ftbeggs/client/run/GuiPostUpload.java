package me.modmuss50.ftbeggs.client.run;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;
import reborncore.common.util.serialization.SerializationUtil;

import java.io.IOException;

/**
 * Created by modmuss50 on 01/03/2017.
 */
public class GuiPostUpload extends GuiScreen {

	private static final int CANCEL_BUTTON_ID = 1;
	public GuiScreen parent;
	String response;
	ResponseJSON json;
	boolean invalidResponse;
	String successMessage;

	public GuiPostUpload(GuiScreen parent, String response, String successMessage) {
		this.parent = parent;
		this.response = response;
		this.successMessage = successMessage;
		try {
			json = SerializationUtil.GSON.fromJson(response, ResponseJSON.class);
			if(json.params != null){

			}
		} catch (Exception e) {
			e.printStackTrace();
			invalidResponse = true;
		}

	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new GuiButton(CANCEL_BUTTON_ID, this.width / 2 - 100, this.height - 35, 200, 20, "Done"));
	}

	@Override
	public void actionPerformed(GuiButton guiButton) throws IOException {
		super.actionPerformed(guiButton);
		if (guiButton.id == CANCEL_BUTTON_ID) {
			Minecraft.getMinecraft().displayGuiScreen(parent);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		if (!invalidResponse && json != null) {
			if (json.status.equals("ok")) {
				this.drawCenteredString(this.fontRenderer, TextFormatting.BLUE + "F" + TextFormatting.GREEN + "T" + TextFormatting.RED + "B" + TextFormatting.WHITE + " " + successMessage, this.width / 2, 20, -1);
			} else {
				this.drawCenteredString(this.fontRenderer, "An error occurred when submitting the request (" + json.status + ")", this.width / 2, 20, -1);
				this.drawCenteredString(this.fontRenderer, json.message, this.width / 2, 30, -1);
			}

			this.drawCenteredString(this.fontRenderer, json.message, this.width / 2, this.height / 2, -1);
			if(!json.description.isEmpty()){
				//TODO wrap?
				this.drawCenteredString(this.fontRenderer, json.description, this.width / 2, this.height / 2 -20, -1);
			}
		} else {
			this.drawCenteredString(this.fontRenderer, "An error occurred when submitting the request", this.width / 2, 20, -1);
			this.drawCenteredString(this.fontRenderer, "Server returned an invalid response!", this.width / 2, this.height / 2, -1);
		}

	}

	private class ResponseJSON {
		String status;
		String message;
		String description;
		Params params;
	}

	private class Params {
		String secret;
	}

}
