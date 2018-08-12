package me.modmuss50.ftbeggs;

import me.modmuss50.ftbeggs.api.RunManger;
import me.modmuss50.ftbeggs.block.BlockEgg;
import me.modmuss50.ftbeggs.block.ItemBlockEgg;
import me.modmuss50.ftbeggs.commands.FTBECommand;
import me.modmuss50.ftbeggs.events.EventHandler;
import me.modmuss50.ftbeggs.files.worldData.WorldDataManager;
import me.modmuss50.ftbeggs.packets.PacketFinishRun;
import me.modmuss50.ftbeggs.packets.PacketSendTimerData;
import me.modmuss50.ftbeggs.proxy.CommonProxy;
import me.modmuss50.ftbeggs.util.TimerServerHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.GameData;
import reborncore.common.network.RegisterPacketEvent;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Mod(name = "FTBEggs", modid = "ftbeggs", version = "1.0.0", dependencies = "required-after:reborncore", certificateFingerprint = "8727a3141c8ec7f173b87aa78b9b9807867c4e6b")
public class FTBEggs {
	public static WorldDataManager dataManager;
	@Mod.Instance("ftbeggs")
	public static FTBEggs INSTANCE;
	@SidedProxy(clientSide = "me.modmuss50.ftbeggs.proxy.ClientProxy", serverSide = "me.modmuss50.ftbeggs.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static File CONFIG_DIR;

	public static BlockEgg egg1;
	public static BlockEgg egg2;
	public static BlockEgg egg3;
	public static BlockEgg egg4;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) throws IOException {
		CONFIG_DIR = new File(event.getSuggestedConfigurationFile().getParent(), "ftbeggs");
		if (!CONFIG_DIR.exists()) {
			CONFIG_DIR.mkdir();
		}

		egg1 = new BlockEgg("egg1", 0);
		registerBlock(egg1, ItemBlockEgg.class, new ResourceLocation("ftbeggs", "egg1"));
		egg2 = new BlockEgg("egg2", 16);
		registerBlock(egg2, ItemBlockEgg.class, new ResourceLocation("ftbeggs", "egg2"));

		egg3 = new BlockEgg("egg3", 32);
		registerBlock(egg3, ItemBlockEgg.class, new ResourceLocation("ftbeggs", "egg3"));
		egg4 = new BlockEgg("egg4", 48);
		registerBlock(egg4, ItemBlockEgg.class, new ResourceLocation("ftbeggs", "egg4"));

		dataManager = new WorldDataManager();
		FMLCommonHandler.instance().bus().register(dataManager);
		FMLCommonHandler.instance().bus().register(EventHandler.class);
		FMLCommonHandler.instance().bus().register(this);
		proxy.preInit(event);
		RunManger.checkOnlineURL();
	}

	public static void registerBlock(Block block, Class<? extends ItemBlock> itemclass, ResourceLocation name) {
		block.setRegistryName(name);
		GameData.register_impl(block);

		try {
			ItemBlock e = itemclass.getConstructor(new Class[] { Block.class }).newInstance(block);
			e.setRegistryName(name);
			GameData.register_impl(e);
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException var4) {
			var4.printStackTrace();
		}

	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
		TimerServerHandler.reset(); //Sets this to 0 and what not
	}

	@Mod.EventHandler
	public void load(FMLPostInitializationEvent event) throws IOException {
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new FTBECommand());
	}

	@SubscribeEvent
	public void loadPacket(RegisterPacketEvent event) {
		event.registerPacket(PacketSendTimerData.class, Side.CLIENT);
		event.registerPacket(PacketFinishRun.class, Side.CLIENT);
	}
}
