package me.modmuss50.ftbeggs.util;

import me.modmuss50.ftbeggs.FTBEggs;
import me.modmuss50.ftbeggs.api.RunManger;
import me.modmuss50.ftbeggs.api.RunMode;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MapHandler {

	public static void play(RunMode runMode) throws IOException {
		RunManger.runMode = runMode;
		RunManger.start();
		FTBEggs.dataManager.resetWorldHash(); //Forces the run hash to be reset

		Minecraft mc = Minecraft.getMinecraft();

		File saveDir = new File(mc.mcDataDir, "saves");
		File worldDir = new File(saveDir, "world");
		File mapDir = new File(mc.mcDataDir, "map");

		if (!mapDir.exists()) {
			mapDir.mkdir();
		}
		if (worldDir.exists()) {
			FileUtils.deleteDirectory(worldDir);
		}
		FileUtils.copyDirectory(mapDir, worldDir);

		WorldInfo worldInfo = getWorldInfo("world", saveDir);
		WorldSummary worldSummary = new WorldSummary(worldInfo, "world", worldInfo.getWorldName(), 0L, false);
		FMLClientHandler.instance().tryLoadExistingWorld(null, worldSummary);

	}

	public static WorldInfo getWorldInfo(String saveName, File savesDirectory) {
		File worldDir = new File(savesDirectory, saveName);
		if (!worldDir.exists()) {
			return null;
		} else {
			File file2 = new File(worldDir, "level.dat");
			return getWorldData(file2);
		}
	}

	public static WorldInfo getWorldData(File level) {
		try {
			NBTTagCompound levelNBT = CompressedStreamTools.readCompressed(new FileInputStream(level));
			NBTTagCompound dataNBT = levelNBT.getCompoundTag("Data");
			return new WorldInfo(dataNBT);
		} catch (Exception exception) {
			throw new RuntimeException("Failed to read world data", exception);
		}
	}

}
