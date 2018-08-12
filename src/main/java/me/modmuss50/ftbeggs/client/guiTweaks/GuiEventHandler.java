package me.modmuss50.ftbeggs.client.guiTweaks;

import me.modmuss50.ftbeggs.FTBEggsGlobal;
import me.modmuss50.ftbeggs.client.GuiPlay;
import me.modmuss50.ftbeggs.client.ItemButton;
import me.modmuss50.ftbeggs.client.run.GuiRunList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class GuiEventHandler {

	public static final int buttonID = 542;
	public static final String customMainMenu = "lumien.custommainmenu.gui.GuiFakeMain";

	@SubscribeEvent
	public static void initGui(GuiScreenEvent.InitGuiEvent.Post event) {
		if (!FTBEggsGlobal.convention) {
			return;
		}
		if (isMainMenu(event.getGui())) {
			event.getButtonList().add(new ItemButton(buttonID, 10, 10, new ItemStack(Blocks.DRAGON_EGG), "Submit a run", true, true));
		}
		if (event.getGui() instanceof GuiIngameMenu) {
			event.getButtonList().remove(event.getButtonList().stream().filter(guiButton -> guiButton.id == 7).findFirst().orElse(null));

			GuiButton modOptions = event.getButtonList().stream().filter(guiButton -> guiButton.id == 12).findFirst().orElse(null);
			modOptions.width = 200;
			modOptions.y = modOptions.y - 24;
			modOptions.x = modOptions.x - 102;

			event.getButtonList().stream().filter(guiButton -> guiButton.id == 0).findFirst().orElse(null).width = 200;

			GuiButton quitButon = event.getButtonList().stream().filter(guiButton -> guiButton.id == 1).findFirst().orElse(null);
			quitButon.displayString = "Quit (Without saving!)";
		}
		if(event.getGui() instanceof GuiWorldSelection){
			Minecraft.getMinecraft().displayGuiScreen(new GuiPlay());
		}
	}

	@SubscribeEvent
	public static void drawScreenEvent(GuiScreenEvent.DrawScreenEvent event){
		if(isCustomMainMenu(event.getGui())){
			try {
				Method method = event.getGui().getClass().getDeclaredMethod("getButtonList");
				method.setAccessible(true);
				List<GuiButton> buttonList = (List<GuiButton>) method.invoke(event.getGui());
				for(GuiButton guiButton : buttonList){
					if(guiButton instanceof ItemButton){
						return;
					}
				}
				buttonList.add(new ItemButton(buttonID, 10, 10, new ItemStack(Blocks.DRAGON_EGG), "Submit a run", true, true));
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	@SubscribeEvent
	public static void actionPeformedEvent(GuiScreenEvent.ActionPerformedEvent event) {
		if (!FTBEggsGlobal.convention) {
			return;
		}
		if (event.getGui() instanceof GuiMainMenu) {
			if (event.getButton().id == 1) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiPlay());
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void keyboardInput(GuiScreenEvent.KeyboardInputEvent.Pre event) {
		if (!FTBEggsGlobal.convention) {
			return;
		}
		if (event.getGui() instanceof GuiMainMenu) {
			if (Keyboard.getEventCharacter() == 'o') {
				Minecraft.getMinecraft().displayGuiScreen(new GuiOptions(event.getGui(), Minecraft.getMinecraft().gameSettings));
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void buttonClick(GuiScreenEvent.ActionPerformedEvent event) {
		if (event.getButton().id == buttonID) {
			if (event.getGui() instanceof GuiMainMenu || isMainMenu(event.getGui())) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiRunList(event.getGui()));
			}

		}
	}


	public static boolean isMainMenu(GuiScreen screen) {
		return screen instanceof GuiMainMenu || screen.getClass().getName().equals(customMainMenu) || screen.getClass().getName().equals("lumien.custommainmenu.gui.GuiCustom");
	}

	public static boolean isCustomMainMenu(GuiScreen screen) {
		return screen.getClass().getName().equals(customMainMenu) || screen.getClass().getName().equals("lumien.custommainmenu.gui.GuiCustom");
	}

}
