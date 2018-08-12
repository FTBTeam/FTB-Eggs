package me.modmuss50.ftbeggs.api;

import com.google.common.base.Charsets;
import com.google.gson.JsonObject;
import me.modmuss50.ftbeggs.util.HttpExecutorService;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.io.IOUtils;
import reborncore.common.util.serialization.SerializationUtil;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class RunStatusManager {

	private static HashMap<String, String> statusMap = new HashMap<>();

	public static String getRunStatus(String hash){
		if(statusMap.containsKey(hash)){
			return statusMap.get(hash);
		}
		queue(hash);

		return statusMap.get(hash);
	}

	public static TextFormatting getRunColor(String hash){
		String status = getRunStatus(hash);
		if(status.equals("Verified")){
			return TextFormatting.GREEN;
		}
		if(status.equals("Pending verification") || status.equals("Run not uploaded")){
			return TextFormatting.YELLOW;
		}
		if(status.contains("No Video Provided")){
			return TextFormatting.BLUE;
		}
		if(status.equals("unknown status") || status.equals("hidden") || status.equals("Failed to load status")){
			return TextFormatting.RED;
		}
		return TextFormatting.WHITE;
	}

	public static void reset(){
		statusMap.clear();
	}

	public static void queue(String hash){
		statusMap.put(hash, "Loading...");
		HttpExecutorService.queue(() -> {
			try {
				String response = IOUtils.toString(new URL(RunManger.getAPIURL() + "/check/" + hash), Charsets.UTF_8);
				JsonObject jsonObject = SerializationUtil.GSON.fromJson(response, JsonObject.class);
				if(jsonObject.getAsJsonPrimitive("status").isNumber()){
					int status = jsonObject.getAsJsonPrimitive("status").getAsInt();
					String result = "unknown status";
					if(status == 0){
						result = "Pending verification";
					} else if(status == 1){
						result = "Verified";
					} else if(status == 2){
						result = "hidden";
					} else if(status == 3){
						result = "No Video Provided";
						if(SecretManager.hasSecrete(hash)){
							result = result + " (Editable)";
						}
					}
					statusMap.put(hash, result);
				} else {
					statusMap.put(hash, "Run not uploaded");
				}
			} catch (IOException e) {
				statusMap.put(hash, "Failed to load status");
			}
		});
	}



}
