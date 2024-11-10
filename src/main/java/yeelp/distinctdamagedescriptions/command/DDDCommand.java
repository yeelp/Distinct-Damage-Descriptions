package yeelp.distinctdamagedescriptions.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.command.CommandTreeBase;
import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.ModConsts.CommandConsts;
import yeelp.distinctdamagedescriptions.util.Translations;
import yeelp.distinctdamagedescriptions.util.lib.DebugLib;

public final class DDDCommand extends CommandTreeBase {

	private static final String USAGE = Translations.INSTANCE.getTranslator(CommandConsts.COMMANDS_ROOT).getKey(CommandConsts.USAGE);
	public DDDCommand() {
		this.addSubcommand(new DDDStatCommand.DDDResistanceSubCommand());
		this.addSubcommand(new DDDStatCommand.DDDImmunitySubCommand());
		this.addSubcommand(new DDDAdaptabilityCommand());
	}
	
	@Override
	public String getName() {
		return ModConsts.MODID;
	}

	@Override
	public List<String> getAliases() {
		return ImmutableList.of(ModConsts.MODID_SHORT);
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return USAGE; 
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		DebugLib.outputFormattedDebug("%s, %s", args.length, Arrays.toString(args));
		if(args.length > 1) {
			ICommand command = this.getSubCommand(args[0]);
			if(command == null) {
				DistinctDamageDescriptions.fatal("Incorrect command type for tab completions! "+args[0]);
				return Lists.newArrayList();
			}
			List<String> completions = command.getTabCompletions(server, sender, args, pos);
			if(!completions.isEmpty()) {
				return completions;
			}
		}
		return args.length == 1 ? super.getTabCompletions(server, sender, args, pos) : Collections.emptyList();
	}
	
	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		if(args.length > 1) {
			ICommand command = this.getSubCommand(args[0]);
			if(command != null) {
				return command.isUsernameIndex(args, index);
			}
		}
		return false;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length == 0) {
			throw new WrongUsageException(USAGE);
		}
		super.execute(server, sender, args);
	}

}
