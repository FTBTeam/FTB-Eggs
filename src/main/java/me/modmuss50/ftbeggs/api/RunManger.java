package me.modmuss50.ftbeggs.api;

import com.google.common.base.Charsets;
import com.google.gson.JsonObject;
import me.modmuss50.ftbeggs.FTBEggs;
import me.modmuss50.ftbeggs.util.TimerServerHandler;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;
import reborncore.common.util.serialization.SerializationUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class RunManger {

	public static boolean hasFinished = false;
	public static int eggCount;
	public static RunData runData;
	private static Optional<String> optionalAPI = Optional.empty();
	public static RunMode runMode;

	public static String getAPIURL(){
		return optionalAPI.orElse("http://api.ftb.world/leaderboard");
	}

	//https://files.modmuss50.me/ftbeggs/api.json
	public static void checkOnlineURL(){
		new Thread(() -> {
			try {
				String json = IOUtils.toString(new URL("https://files.modmuss50.me/ftbeggs/api.json"), Charsets.UTF_8);
				JsonObject jsonObject = SerializationUtil.GSON.fromJson(json, JsonObject.class);
				String apiUrl = jsonObject.get("apiurl").getAsString();
				optionalAPI = Optional.of(apiUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	public static void start(){
		RunManger.eggCount = 0;
		RunManger.hasFinished = false;
		RunManger.runData = new RunData();
		RunManger.runData.achievementData = new ArrayList<>();
	}

	public static long getMaxTime(){
		return 1800000;
	}

	public static RunData getFinalData(){
		runData.runHash = FTBEggs.dataManager.getWorldHash();
		runData.runDate = LocalDateTime.now().toString();
		runData.userName = Minecraft.getMinecraft().player.getName();
		runData.uuid = Minecraft.getMinecraft().player.getUniqueID().toString();
		runData.totalTime = TimerServerHandler.getTimeDifference();
		runData.configHash = "ifyouguessthisthenyouareeitherhackingorareafuckinggodgoodonyou";
		if(runMode == RunMode.EASY){
			runData.packName = "EggHuntEasy";
		} else {
			runData.packName = "EggHuntNormal";
		}
		return runData;
	}
	
	public static String postData(RunData data) throws IOException {
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(getAPIURL());
		httppost.setEntity(new ByteArrayEntity(SerializationUtil.GSON_FLAT.toJson(data).getBytes()));
		httppost.setHeader("Content-Type", "application/json");
		httppost.setHeader("User-Agent", "FTB_ACHIEVEMENTS");
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			InputStream inputStream = entity.getContent();
			StringWriter writer = new StringWriter();
			IOUtils.copy(inputStream, writer);
			String theString = writer.toString();
			inputStream.close();
			checkResponseForUsefulStuff(theString, data);
			return theString;
		}
		return "Error";
	}

	public static String updateData(RunData runData, String videoURL) throws IOException{
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(getAPIURL() + "/updateSubmission");
		if(!SecretManager.hasSecrete(runData.runHash)){
			ResponseJSON responseJSON = new ResponseJSON();
			responseJSON.message = "No run secret found!";
			responseJSON.description = "The run cannot be edited as a run s secret not found";
			responseJSON.status = "error";
			return SerializationUtil.GSON.toJson(responseJSON);
		}
		RunEdit runEdit = new RunEdit(runData.runHash, SecretManager.getSecrete(runData.runHash).get(), videoURL);
		httppost.setEntity(new ByteArrayEntity(SerializationUtil.GSON_FLAT.toJson(runEdit).getBytes()));
		httppost.setHeader("Content-Type", "application/json");
		httppost.setHeader("User-Agent", "FTB_ACHIEVEMENTS");
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream inputStream = entity.getContent();
			StringWriter writer = new StringWriter();
			IOUtils.copy(inputStream, writer);
			String theString = writer.toString();
			inputStream.close();
			return theString;
		}
		return "Error";
	}

	private static class RunEdit {
		String runHash;
		String secret;
		String videoURL;

		public RunEdit(String runHash, String secret, String videoURL) {
			this.runHash = runHash;
			this.secret = secret;
			this.videoURL = videoURL;
		}
	}

	private static void checkResponseForUsefulStuff(String response, RunData data){
		ResponseJSON json = SerializationUtil.GSON.fromJson(response, ResponseJSON.class);
		if(json.params != null && json.params.secret != null){
			if(!json.params.secret.isEmpty()){
				SecretManager.add(data.runHash, json.params.secret);
			}
		}
	}

	private static class ResponseJSON {
		String status;
		String message;
		String description;
		Params params;
	}

	private class Params {
		String secret;
	}


}
