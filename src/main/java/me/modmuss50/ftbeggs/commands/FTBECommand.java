package me.modmuss50.ftbeggs.commands;

import me.modmuss50.ftbeggs.commands.subcommands.spawn.CommandSetSpawn;
import me.modmuss50.ftbeggs.commands.subcommands.timer.CommandReset;
import me.modmuss50.ftbeggs.commands.subcommands.timer.CommandTimer;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * Created by Mark on 05/02/2017.
 */
public class FTBECommand extends CommandTreeBase {
	public FTBECommand() {
		addSubcommand(new CommandReset());
		addSubcommand(new CommandTimer());
		addSubcommand(new CommandSetSpawn("normal"));
		addSubcommand(new CommandSetSpawn("easy"));
	}

	@Override
	public String getName() {
		return "ftbe";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "ftbe";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
}
