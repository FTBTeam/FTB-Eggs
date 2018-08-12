package me.modmuss50.ftbeggs.commands.subcommands.spawn;

import me.modmuss50.ftbeggs.commands.FTBCommandBase;
import me.modmuss50.ftbeggs.util.WorldSpawnHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import reborncore.common.util.StringUtils;

import java.io.IOException;

public class CommandSetSpawn extends FTBCommandBase {

	String difficultly;

	public CommandSetSpawn(String difficultly) {
		this.difficultly = difficultly;
	}

	@Override
	public String getName() {
		return "setSpawn" + StringUtils.toFirstCapital(difficultly.toLowerCase());
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "setSpawn"+ StringUtils.toFirstCapital(difficultly.toLowerCase());
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		try {
			WorldSpawnHelper.setSpawn(difficultly, sender.getPosition(), sender.getEntityWorld());
		} catch (IOException e) {
			e.printStackTrace();
			sender.sendMessage(new TextComponentString(e.getLocalizedMessage()));
		}
		sender.sendMessage(new TextComponentString("Set " + difficultly + " spawn point to " + sender.getPosition()));
	}
}
