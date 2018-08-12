package me.modmuss50.ftbeggs.api;

import com.google.common.base.Charsets;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import reborncore.RebornCore;
import reborncore.common.util.serialization.SerializationUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SecretManager {

	public static File file;
	private static Map<String, String> secreteMap = new HashMap<>();

	public static void save(){
		if(file == null){
			throw new RuntimeException("Secret manager file is null");
		}
		String outputJson = SerializationUtil.GSON_FLAT.toJson(secreteMap);
		try {
			FileUtils.writeStringToFile(file, outputJson, Charsets.UTF_8);
		} catch (IOException e) {
			RebornCore.logHelper.error(e);
			RebornCore.logHelper.error("Failed to write secret's");
		}
	}

	public static void load(){
		if(file == null){
			throw new RuntimeException("Secret manager file is null");
		}
		if(!file.exists()){
			save();
			return;
		}
		try {
			String json = FileUtils.readFileToString(file, Charsets.UTF_8);
			Type type = new TypeToken<Map<String, String>>(){}.getType();
			secreteMap = SerializationUtil.GSON_FLAT.fromJson(json, type);
		} catch (Exception e){
			RebornCore.logHelper.error(e);
			RebornCore.logHelper.error("Failed to read secret's, resetting file");
			file.delete();
			save();
		}
	}

	public static void add(String runHash, String secrete){
		if(hasSecrete(runHash)){
			secreteMap.remove(runHash);
		}
		secreteMap.put(runHash, secrete);
		save();
	}

	public static Optional<String> getSecrete(String runHash){
		if(secreteMap.containsKey(runHash)){
			return Optional.of(secreteMap.get(runHash));
		}
		return Optional.empty();
	}

	public static boolean hasSecrete(String runHash){
		return getSecrete(runHash).isPresent();
	}
}
