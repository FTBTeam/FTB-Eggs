package me.modmuss50.ftbeggs.events;

import me.modmuss50.ftbeggs.FTBEggsGlobal;
import me.modmuss50.ftbeggs.api.AchievementData;
import me.modmuss50.ftbeggs.api.RunData;
import me.modmuss50.ftbeggs.api.RunManger;
import me.modmuss50.ftbeggs.api.SavedRunManager;
import me.modmuss50.ftbeggs.block.BlockEgg;
import me.modmuss50.ftbeggs.client.run.GuiUpload;
import me.modmuss50.ftbeggs.packets.PacketFinishRun;
import me.modmuss50.ftbeggs.util.TimerServerHandler;
import me.modmuss50.ftbeggs.util.WorldSpawnHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import reborncore.common.network.NetworkManager;

import java.io.IOException;

/**
 * Created by Mark on 05/02/2017.
 */
public class EventHandler {

    @SubscribeEvent
    public static void blockBreak(net.minecraftforge.event.world.BlockEvent.BreakEvent event) throws IOException {
        if (event.getWorld().isRemote) {
            return;
        }
        if (!FTBEggsGlobal.convention) {
            return;
        }
        IBlockState blockState = event.getState();
        if (blockState.getBlock() instanceof BlockEgg) {
            int egg = BlockEgg.getEgg(blockState);
            //System.out.println("Collected egg at: " + TimerServerHandler.getTimeDifference() + "=" + TimerServerHandler.getNiceTimeFromLong(TimerServerHandler.getTimeDifference()));
            RunManger.runData.achievementData.add(new AchievementData("egg" + egg, TimerServerHandler.getTimeDifference()));
            RunManger.eggCount++;
        } else {
            //Only allow blocks that are eggs to be broken, stop all other mods.
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void playerJoin(PlayerEvent.PlayerLoggedInEvent event) throws IOException {
	    if (!FTBEggsGlobal.convention) {
		    return;
	    }
	    BlockPos pos = WorldSpawnHelper.getSpawn(RunManger.runMode.name().toLowerCase(), event.player.world);
	    if(pos != null){
	    	EntityPlayerMP entityPlayerMP = (EntityPlayerMP) event.player;
	    	entityPlayerMP.connection.setPlayerLocation(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
		    //event.player.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
	    }
        TimerServerHandler.syncWith((EntityPlayerMP) event.player);
        if (!TimerServerHandler.isActive()) {
            TimerServerHandler.startTimer(TimerServerHandler.getStoppedTime());
            TimerServerHandler.syncWithAll();
        }
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
	    if (!FTBEggsGlobal.convention) {
		    return;
	    }
        if (event.world.isRemote) {
            return;
        }
        if (event.world.provider.getDimension() != 0) {
            return;
        }
        if(event.phase == TickEvent.Phase.END){
            return;
        }
        long timeLeft = RunManger.getMaxTime() - TimerServerHandler.getTimeDifference();
        if ((timeLeft <= 0 || RunManger.eggCount >= 32) && !RunManger.hasFinished) {
        	RunManger.hasFinished = true;
            TimerServerHandler.stop();
	        RunData data = RunManger.getFinalData();
	        SavedRunManager.saveRun(data);
	        String respose = "error";
	        try {
		        respose = GuiUpload.postDataToServer(data);
	        } catch (IOException e) {
		        e.printStackTrace();
	        }
	        NetworkManager.sendToAll(new PacketFinishRun(RunManger.eggCount, TimerServerHandler.getTimeDifference(), respose));

        }
    }

    @SubscribeEvent
    public static void entityDamage(LivingDamageEvent event){
    	if(event.getEntity() instanceof EntityPlayer){
    		event.setCanceled(true);
	    }
    }

	@SubscribeEvent
    public static void setHealth(TickEvent.PlayerTickEvent event){
    	event.player.setHealth(100);
    	event.player.getFoodStats().setFoodLevel(20);
    	event.player.getFoodStats().setFoodSaturationLevel(10F);
	}
}
