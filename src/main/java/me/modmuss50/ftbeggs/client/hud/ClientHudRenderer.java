package me.modmuss50.ftbeggs.client.hud;

import me.modmuss50.ftbeggs.FTBEggs;
import me.modmuss50.ftbeggs.FTBEggsGlobal;
import me.modmuss50.ftbeggs.api.RunManger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Mark on 11/02/2017.
 */
public class ClientHudRenderer {

	public static int clientTick = 0;

	private static RenderGameOverlayEvent.ElementType[] disabled = new RenderGameOverlayEvent.ElementType[]{
		RenderGameOverlayEvent.ElementType.BOSSHEALTH,
		RenderGameOverlayEvent.ElementType.BOSSINFO,
		RenderGameOverlayEvent.ElementType.HEALTH,
		RenderGameOverlayEvent.ElementType.FOOD,
		RenderGameOverlayEvent.ElementType.AIR,
		RenderGameOverlayEvent.ElementType.EXPERIENCE,
		RenderGameOverlayEvent.ElementType.HEALTHMOUNT
	};

	private ArrayList<RenderGameOverlayEvent.ElementType> disabledList;

	public ClientHudRenderer() {
		disabledList = new ArrayList<>(Arrays.asList(disabled));
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void clientTick(TickEvent.ClientTickEvent event) {
		clientTick++;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderGameEvent(RenderGameOverlayEvent.Post event) {
		if (!FTBEggsGlobal.convention) {
			return;
		}
		if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
			return;
		}
		Minecraft minecraft = Minecraft.getMinecraft();
		if (minecraft.currentScreen != null) {
			minecraft.fontRenderer.drawString("#" + FTBEggs.dataManager.getWorldHash(), 5, 5, Color.WHITE.getRGB());
		}

		if (minecraft.gameSettings.showDebugInfo || minecraft.currentScreen != null && !(minecraft.currentScreen instanceof GuiChat)) {
			return;
		}

		int height = 20;
		int xPos = event.getResolution().getScaledWidth() - (115);
		int yPos = 10;


		Gui.drawRect(xPos - 2, yPos - 2, xPos + 112, yPos + height + 2, 1056964608);
		Gui.drawRect(xPos, yPos, xPos + 110, yPos + height, 1056964608);

		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		fontRenderer.drawString("Egg Count: " + RunManger.eggCount + "/32", xPos + 15, yPos +11, Color.WHITE.getRGB());
		fontRenderer.drawString("Remaining Time: " + Timer.getNiceTimeLeft(), xPos + 5, yPos +1, Color.WHITE.getRGB());
	}

	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent event){
		if (disabledList.contains(event.getType())) {
			event.setCanceled(true);
		}
	}

}
