package me.modmuss50.ftbeggs.commands.subcommands.timer;

import me.modmuss50.ftbeggs.commands.FTBCommandBase;
import me.modmuss50.ftbeggs.util.TimerServerHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by modmuss50 on 14/02/2017.
 */
public class CommandStop extends FTBCommandBase {
	@Override
	public String getName() {
		return "stop";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "stop";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		TimerServerHandler.stop();
		sender.sendMessage(new TextComponentString("Timer stopped"));
		super.execute(server, sender, args);
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
}
