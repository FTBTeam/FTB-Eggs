package me.modmuss50.ftbeggs.api;

import org.apache.commons.io.FileUtils;
import reborncore.RebornCore;
import reborncore.common.util.serialization.SerializationUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by modmuss50 on 01/03/2017.
 */
public class SavedRunManager {

	public static File mcDir;
	public static File saveDir;

	public static final String FILE_EXT = ".json";
	public static final String FILE_EXT_NEW = ".dat";

	public static void load() {
		saveDir = new File(mcDir, "runs");
		if (!saveDir.exists()) {
			saveDir.mkdir();
		}
		SecretManager.file = new File(saveDir, "secrets");
		SecretManager.load();
	}

	public static List<RunData> getRuns() {
		load();
		List<RunData> list = new ArrayList<>();
		for (File file : saveDir.listFiles()) {
			if (!file.isDirectory()) {
				if (file.getName().endsWith(FILE_EXT)) {
					try {
						RunData data = SerializationUtil.GSON.fromJson(FileUtils.readFileToString(file), RunData.class);
						list.add(data);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (file.getName().endsWith(FILE_EXT_NEW)) {
					try {
						FileInputStream fi = new FileInputStream(file);
						ObjectInputStream oi = new ObjectInputStream(fi);
						RunData data = (RunData) oi.readObject();
						oi.close();
						fi.close();
						if (data != null) {
							list.add(data);
						}
					} catch (Exception e) {
						RebornCore.logHelper.error(e);
						RebornCore.logHelper.error("Failed to read: " + file.getName());
					}

				}

			}
		}
		return list;
	}

	public static void saveRun(RunData data) {
		load();
		File file = new File(saveDir, data.runDate.replace(":", ".") + FILE_EXT_NEW);
		//		try {
		//			FileUtils.writeStringToFile(file, SerializationUtil.GSON.toJson(data), false);
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
		try {
			FileOutputStream f = new FileOutputStream(file);
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(data);
			o.close();
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
