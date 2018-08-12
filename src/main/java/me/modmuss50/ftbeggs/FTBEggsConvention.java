package me.modmuss50.ftbeggs;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(name = "FTBEggsConvention", modid = "ftbeggsconvention", version = "1.0.0", dependencies = "required-before:ftbeggs", clientSideOnly = true)
public class FTBEggsConvention {

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		//Add -Dftbeggs.disableConvention=true to VM options to disable
		if(Boolean.parseBoolean(System.getProperty("ftbeggs.disableConvention", "false"))){
			return;
		}
		FTBEggsGlobal.convention = true;
	}
}
