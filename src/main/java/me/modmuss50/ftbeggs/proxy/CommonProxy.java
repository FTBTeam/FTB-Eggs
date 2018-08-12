package me.modmuss50.ftbeggs.proxy;

import com.google.gson.Gson;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by modmuss50 on 09/02/2017.
 */
public class CommonProxy {

	private static Gson gson = new Gson();

	public void init(FMLInitializationEvent event) {

	}

	public void preInit(FMLPreInitializationEvent event) {

	}

	public String getWorldSalt() {
		return "server";
	}

	public EntityPlayer getPlayer() {
		return null;
	}

	public void clientSideTask(Runnable runnable) {
		throw new UnsupportedOperationException("Not supported on the server side");
	}

}
