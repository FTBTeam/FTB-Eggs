package me.modmuss50.ftbeggs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import reborncore.client.gui.GuiUtil;

/**
 * Created by modmuss50 on 09/02/2017.
 */
public class ItemButton extends GuiButton {
	ItemStack stack;
	String hoverText;
	boolean showHoverText;
	boolean rightSide = false;

	protected static final ResourceLocation BUTTON_NEW_TEXTURES = new ResourceLocation("ftbeggs", "textures/buttonsquare.png");

	public ItemButton(int buttonId, int x, int y, ItemStack stack) {
		super(buttonId, x, y, 20, 20, "");
		this.hoverText = "Open Achievement's";
		this.showHoverText = true;
		this.stack = stack;
	}

	public ItemButton(int buttonId, int x, int y, ItemStack stack, String text, boolean showHoverText) {
		super(buttonId, x, y, 20, 20, "");
		this.hoverText = text;
		this.showHoverText = showHoverText;
		this.stack = stack;
	}

	public ItemButton(int buttonId, int x, int y, ItemStack stack, String text, boolean showHoverText, boolean rightSide) {
		super(buttonId, x, y, 20, 20, "");
		this.hoverText = text;
		this.showHoverText = showHoverText;
		this.stack = stack;
		this.rightSide = rightSide;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float ticks) {
		this.x -= 5;
		this.y -= 5;
		if (this.visible)
		{
			FontRenderer fontrenderer = mc.fontRenderer;
			mc.getTextureManager().bindTexture(BUTTON_NEW_TEXTURES);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			GlStateManager.enableBlend();
			float scale = 0.1F;
			GlStateManager.scale(scale, scale, scale);
			this.drawTexturedModalRect(this.x, this.y, 0, 0, 260, 260);
			GlStateManager.scale(10F, 10F, 10F);
			this.mouseDragged(mc, mouseX, mouseY);
		}
		RenderHelper.enableStandardItemLighting();
		RenderHelper.enableGUIStandardItemLighting();
		RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
		itemRenderer.renderItemIntoGUI(this.stack, this.x , this.y);

		if (this.hovered && showHoverText) {
			if (!rightSide) {
				GuiUtil.drawTooltipBox(this.x - (hoverText.length() * 6) - 5, this.y + 3, (hoverText.length() * 6), 12);
				drawString(mc.fontRenderer, hoverText, this.x - (hoverText.length() * 6), this.y + 5, 0xFFFFFF);
			} else {
				GuiUtil.drawTooltipBox(this.x + 20, this.y , (hoverText.length() * 5) + 5, 12);
				drawString(mc.fontRenderer, hoverText, this.x + 21, this.y + 2, 0xFFFFFF);
			}

		}
		this.x += 5;
		this.y += 5;
	}
}
