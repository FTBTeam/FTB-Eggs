package me.modmuss50.ftbeggs.proxy;

import me.modmuss50.ftbeggs.client.guiTweaks.GuiEventHandler;
import me.modmuss50.ftbeggs.client.hud.ClientHudRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import prospector.shootingstar.ShootingStar;

/**
 * Created by modmuss50 on 09/02/2017.
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		MinecraftForge.EVENT_BUS.register(new ClientHudRenderer());
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ShootingStar.registerModels("ftbeggs");
		FMLCommonHandler.instance().bus().register(GuiEventHandler.class);
	}

	private static void registerBlockstate(Item i, int meta, String variant, String dir) {
		ResourceLocation loc = new ResourceLocation("ftbeggs", dir + i.getRegistryName().getResourcePath());
		ModelLoader.setCustomModelResourceLocation(i, meta, new ModelResourceLocation(loc, "type=" + variant));
	}

	@Override
	public String getWorldSalt() {
		return Minecraft.getMinecraft().getSession().getProfile().getId().toString();
	}

	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().player;
	}

	@Override
	public void clientSideTask(Runnable runnable) {
		Minecraft.getMinecraft().addScheduledTask(runnable);
	}
}
