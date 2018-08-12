package me.modmuss50.ftbeggs.files.worldData;

import com.google.common.base.Charsets;
import me.modmuss50.ftbeggs.util.TimerServerHandler;
import net.minecraft.world.World;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.FileUtils;
import reborncore.common.util.serialization.SerializationUtil;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

/**
 * Created by Mark on 05/02/2017.
 */
public class WorldDataManager {

	public static final String savename = "_ftbeggs.json";
	private WorldFormat format;
	private String worldHash;
	private boolean reset = false;

	@SubscribeEvent
	public void worldSave(WorldEvent.Save event) {
		if (event.getWorld().isRemote) {
			return;
		}
		ThreadedFileIOBase.getThreadedIOInstance().queueIO(() -> {
			try {
				save(event.getWorld());
			} catch (IOException e) {
				throw new RuntimeException("Failed to save world data manager", e);
			}
			return false;
		});

	}

	@SubscribeEvent
	public void worldLoad(WorldEvent.Load event) throws IOException {
		if (event.getWorld().isRemote) {
			return;
		}
		load(event.getWorld());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void worldClosed(WorldEvent.Unload event) throws IOException {
		if (event.getWorld().isRemote) {
			return;
		}
		save(event.getWorld());
	}

	public void load(World world) throws IOException {
		File file = new File(world.getSaveHandler().getWorldDirectory(), savename);
		WorldFormat format;
		if (file.exists()) {
			format = SerializationUtil.GSON.fromJson(FileUtils.readFileToString(file), WorldFormat.class);
		} else {
			format = new WorldFormat();
		}
		this.format = format;
		if (format.timerData != null) {
			TimerServerHandler.load(format.timerData);
		} else {
			TimerServerHandler.reset(); //Reset if the data isnt there, as the current data could be another world in singleplayer
		}
		File hashFile = new File(world.getSaveHandler().getWorldDirectory(), "ftbeggs.dat");
		if (hashFile.exists() && !reset) {
			worldHash = FileUtils.readFileToString(hashFile);
		} else {
			worldHash = WorldHashManager.generateHash();
			FileUtils.writeStringToFile(hashFile, worldHash);
			reset = false;
		}

	}

	public void save(World world) throws IOException {
		this.format.timerData = TimerServerHandler.save();
		File file = new File(world.getSaveHandler().getWorldDirectory(), savename);
		FileUtils.writeStringToFile(file, SerializationUtil.GSON.toJson(getFormatServer()), Charsets.UTF_8);
	}

	@Nullable
	public WorldFormat getFormatServer() {
		return format;
	}

	public String getWorldHash() {
		return worldHash;
	}

	//Resets the world hash on next world load
	public void resetWorldHash(){
		reset = true;
	}

}
