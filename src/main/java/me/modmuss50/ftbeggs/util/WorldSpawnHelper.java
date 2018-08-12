package me.modmuss50.ftbeggs.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class WorldSpawnHelper {

	public static BlockPos getSpawn(String difficutally, World world) throws IOException {
		difficutally = difficutally.toLowerCase();
		File file = new File(world.getSaveHandler().getWorldDirectory(), difficutally + ".txt");
		if(!file.exists()){
			return null;
		}
		long data = Long.parseLong(FileUtils.readFileToString(file, Charset.defaultCharset()));
		return BlockPos.fromLong(data);
	}

	public static void setSpawn(String difficutally, BlockPos pos, World world) throws IOException {
		difficutally = difficutally.toLowerCase();
		File file = new File(world.getSaveHandler().getWorldDirectory(), difficutally + ".txt");
		if(file.exists()){
			file.delete();
		}
		String posFile = String.valueOf(pos.toLong());
		FileUtils.writeStringToFile(file, posFile, Charset.defaultCharset());
	}

}
