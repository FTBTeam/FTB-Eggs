package me.modmuss50.ftbeggs.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;


/**
 * To run this in dev you need to add the following to the *VM* Options in the run config
 *
 *          -Dfml.coreMods.load=me.modmuss50.ftbeggs.asm.LoadingCore
 *
 */

@IFMLLoadingPlugin.Name("FTBEggsASM")
public class LoadingCore implements IFMLLoadingPlugin {

	//True when using SRG names
	public static boolean runtimeDeobfuscationEnabled = true;

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"me.modmuss50.ftbeggs.asm.ClassTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
	    runtimeDeobfuscationEnabled = (boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}