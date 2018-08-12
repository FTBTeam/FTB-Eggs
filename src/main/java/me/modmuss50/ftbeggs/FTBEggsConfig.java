package me.modmuss50.ftbeggs;

import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;

@RebornRegistry(modID = "ftbeggs")
public class FTBEggsConfig {

	@ConfigRegistry(config = "api", key = "API_URL", comment = "API url (include / at the end)")
	public static String API_URL = "https://go.alwa.io/";

}
