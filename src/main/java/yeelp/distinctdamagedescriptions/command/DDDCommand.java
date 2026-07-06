package yeelp.distinctdamagedescriptions.command;

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

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class DDDCommand extends CommandTreeBase {

	private static final String USAGE = Translations.INSTANCE.getTranslator(CommandConsts.COMMANDS_ROOT).getKey(CommandConsts.USAGE);
	public DDDCommand() {
		this.addSubcommand(new DDDStatCommand.DDDResistanceSubCommand());
		this.addSubcommand(new DDDStatCommand.DDDImmunitySubCommand());
		this.addSubcommand(new DDDAdaptabilityCommand());
	}
	
	@Nonnull
	@Override
	public String getName() {
		return ModConsts.MODID;
	}

	@Nonnull
	@Override
	public List<String> getAliases() {
		return ImmutableList.of(ModConsts.MODID_SHORT);
	}
	
	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender) {
		return USAGE; 
	}
	
	@Nonnull
	@Override
	public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, BlockPos pos) {
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
	public boolean isUsernameIndex(@Nonnull String[] args, int index) {
		if(args.length > 1) {
			ICommand command = this.getSubCommand(args[0]);
			if(command != null) {
				return command.isUsernameIndex(args, index);
			}
		}
		return false;
	}
	
	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
		if(args.length == 0) {
			throw new WrongUsageException(USAGE);
		}
		super.execute(server, sender, args);
	}

}
