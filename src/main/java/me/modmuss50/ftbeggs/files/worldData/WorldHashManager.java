package me.modmuss50.ftbeggs.files.worldData;

import me.modmuss50.ftbeggs.FTBEggs;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Created by Mark on 04/03/2017.
 */
public class WorldHashManager {

	public static String generateHash() {
		Random random = new Random();
		return genHash(FTBEggs.proxy.getWorldSalt() + LocalDateTime.now().toString() + random.nextFloat() + System.nanoTime());
	}

	public static String genHash(String md5) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
		}
		return null;
	}

}
