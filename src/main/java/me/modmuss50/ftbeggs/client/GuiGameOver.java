package me.modmuss50.ftbeggs.client;

import me.modmuss50.ftbeggs.util.TimerServerHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import reborncore.common.util.serialization.SerializationUtil;

import java.awt.*;
import java.io.IOException;

public class GuiGameOver extends GuiScreen {
    int eggCount;
    long time;
	String autoUploadReponse;
	ResponseJSON json;

    public GuiGameOver(int eggCount, long time, String autoUploadReponse) {
        this.eggCount = eggCount;
        this.time = time;
        this.autoUploadReponse = autoUploadReponse;

	    json = SerializationUtil.GSON.fromJson(autoUploadReponse, ResponseJSON.class);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width /2 - 100, 210, "Main Menu"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        this.drawCenteredString(fontRenderer, "Game Over!", this.width /2 , 10, Color.RED.getRGB());

        this.drawCenteredString(fontRenderer, "Well done you collected:", this.width /2 , 100, Color.WHITE.getRGB());
        this.drawCenteredString(fontRenderer,  eggCount +" out of 32 eggs", this.width /2 , 110, Color.GREEN.getRGB());
	    this.drawCenteredString(fontRenderer,  "In " + TimerServerHandler.getNiceTimeFromLong(time), this.width /2 , 120, Color.YELLOW.getRGB());

	    if(json != null && json.status.equals("ok")){
		    this.drawCenteredString(fontRenderer, "Your run has been updated to https://ftb.world", this.width /2 , 150, Color.CYAN.getRGB());
		    this.drawCenteredString(fontRenderer, "You can attach a video to it from the main menu", this.width /2 , 160, Color.CYAN.getRGB());
	    } else {
		    this.drawCenteredString(fontRenderer, "You can upload your run from the main menu", this.width /2 , 150, Color.CYAN.getRGB());
	    }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 0){
            button.enabled = false;
            this.mc.world.sendQuittingDisconnectingPacket();
            this.mc.loadWorld(null);
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
        super.actionPerformed(button);
    }

	@Override
	public boolean doesGuiPauseGame() {
		return false;
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
