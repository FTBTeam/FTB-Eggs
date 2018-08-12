package me.modmuss50.ftbeggs.packets;

import me.modmuss50.ftbeggs.client.GuiGameOver;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;

import java.io.IOException;

public class PacketFinishRun implements INetworkPacket<PacketFinishRun> {

    public int eggs;
    public long time;
    String autoUploadReponse;

    public PacketFinishRun(int eggs, long time, String autoUploadReponse) {
        this.eggs = eggs;
        this.time = time;
        this.autoUploadReponse = autoUploadReponse;
    }

    public PacketFinishRun() {
    }

    @Override
    public void writeData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
        extendedPacketBuffer.writeInt(eggs);
        extendedPacketBuffer.writeLong(time);
        extendedPacketBuffer.writeInt(autoUploadReponse.length());
        extendedPacketBuffer.writeString(autoUploadReponse);
    }

    @Override
    public void readData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
        eggs = extendedPacketBuffer.readInt();
        time = extendedPacketBuffer.readLong();
        autoUploadReponse = extendedPacketBuffer.readString(extendedPacketBuffer.readInt());
    }

    @Override
    public void processData(PacketFinishRun packetFinishRun, MessageContext messageContext) {
        openGameOver();
    }

    @SideOnly(Side.CLIENT)
    public void openGameOver(){
	    Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(new GuiGameOver(eggs, time, autoUploadReponse)));
    }
}
