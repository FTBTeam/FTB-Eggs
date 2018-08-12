package me.modmuss50.ftbeggs.client;

import me.modmuss50.ftbeggs.api.RunMode;
import me.modmuss50.ftbeggs.util.MapHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class GuiPlay extends GuiScreen {

	public GuiButton easy;
	public GuiButton normal;

	@Override
	public void initGui() {
		super.initGui();

		this.buttonList.clear();

		this.buttonList.add(new GuiButton(0, 5, 5, 50, 20, "Back"));
		this.buttonList.add(easy = new GuiButton(1, this.width / 2 -100, 100, 100, 20, "Easy"));
		this.buttonList.add(normal = new GuiButton(2, this.width / 2, 100, 100, 20, "Normal"));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();

		this.drawCenteredString(this.fontRenderer, "Play Egg Hunt", this.width / 2, 20, -1);
		this.drawCenteredString(this.fontRenderer, "Select difficulty:", this.width / 2, 80, -1);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}


	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id == 0){
			Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
		}
		if(button.id == 1){
			MapHandler.play(RunMode.EASY);
		}
		if(button.id == 2){
			MapHandler.play(RunMode.NORMAL);
		}
	}
}