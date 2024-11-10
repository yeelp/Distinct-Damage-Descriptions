package yeelp.distinctdamagedescriptions.command;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.command.SelectorHandlerManager;
import net.minecraftforge.server.command.CommandTreeBase;
import yeelp.distinctdamagedescriptions.ModConsts.CommandConsts;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.Translations;
import yeelp.distinctdamagedescriptions.util.Translations.BasicTranslator;

public abstract class AbstractDDDCommand extends CommandTreeBase {

	private static final Pattern NUM_REGEX = Pattern.compile("^-?\\d+(\\.\\d+)?$");
	protected static final BasicTranslator TRANSLATOR = Translations.INSTANCE.getTranslator(CommandConsts.COMMANDS_ROOT);
	protected static final String NO_TARGETS_ERROR_KEY = TRANSLATOR.getKey(CommandConsts.NO_TARGETS);
	protected static final String INVALID_TYPE_ERROR_KEY = TRANSLATOR.getKey(CommandConsts.INVALID_TYPE);

	protected AbstractDDDCommand() {
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	static final Iterable<EntityLivingBase> getTargetSelection(MinecraftServer server, ICommandSender sender, String selection) throws CommandException {
		List<EntityLivingBase> targets;
		if(selection.startsWith("@")) {
			targets =  SelectorHandlerManager.matchEntities(sender, selection, EntityLivingBase.class);			
		}
		else {
			targets = server.getPlayerList().getPlayers().stream().filter((player) -> player.getName().equals(selection)).collect(Collectors.toList());			
		}
		if(targets.size() == 0) {
			sendErrorMessage(sender, NO_TARGETS_ERROR_KEY, selection);
		}
		return targets;
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		int index = args.length - 1;
		if(!this.hasCompletionsAvailable(index)) {
			return Collections.emptyList();
		}
		if(this.isTargetsIndex(index)) {
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		}
		int depth = this.getSubCommandDepth(index);
		if(depth >= 0) {
			return getListOfStringsMatchingLastWord(args, this.getSubCommands(index, depth, args, Lists.newArrayList(this)).stream().map(ICommand::getName).collect(Collectors.toList()));			
		}
		return super.getTabCompletions(server, sender, args, pos);
	}
	
	private Collection<ICommand> getSubCommands(int index, int depth, String[] args, Collection<ICommand> c) {
		Collection<ICommand> collection = c.stream().flatMap((command) -> {
			if(command instanceof CommandTreeBase) {
				return ((CommandTreeBase) command).getSubCommands().stream();
			}
			return Stream.empty();
		}).collect(Collectors.toList());
		if(depth == 0) {
			return collection;
		}
		collection.removeIf((command) -> !command.getName().equals(args[index - depth]));
		return this.getSubCommands(index, depth - 1, args, collection);
	}
	
	protected abstract boolean isTargetsIndex(int index);
	
	protected abstract int getSubCommandDepth(int index);
	
	protected abstract boolean hasCompletionsAvailable(int index);
	
	static final Optional<DDDDamageType> parseDamageTypeArgument(ICommandSender sender, String type) {
		Optional<DDDDamageType> oType = Optional.ofNullable(DDDRegistries.damageTypes.get(type));
		if(!oType.isPresent() || oType.get() == DDDBuiltInDamageType.UNKNOWN) {
			sendErrorMessage(sender, INVALID_TYPE_ERROR_KEY, type);
		}
		return oType;
	}
	
	static boolean isNumerical(String s) {
		return NUM_REGEX.matcher(s).matches();
	}
	
	static void sendErrorMessage(ICommandSender sender, String msg, Object... args) {
		sender.sendMessage(new TextComponentTranslation(msg, args).setStyle(new Style().setColor(TextFormatting.RED)));
	}
	
	public static abstract class DDDCommandSubTreeBase extends AbstractDDDCommand {
		
		private final String name;
		private final String usage;

		protected DDDCommandSubTreeBase(String name) {
			this.name = name;
			this.usage = Translations.INSTANCE.getLayeredTranslator(CommandConsts.COMMANDS_ROOT, this.name).getKey(CommandConsts.USAGE);
		}
		
		protected DDDCommandSubTreeBase(String root, String... subRoots) {
			this.name = subRoots[subRoots.length - 1];
			String[] args = new String[subRoots.length + 1];
			args[0] = root;
			System.arraycopy(subRoots, 0, args, 1, subRoots.length);
			this.usage = Translations.INSTANCE.getLayeredTranslator(CommandConsts.COMMANDS_ROOT, args).getKey(CommandConsts.USAGE);
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public String getUsage(ICommandSender sender) {
			return this.usage;
		}
		
		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
			if(args.length == 0) {
				throw new WrongUsageException(this.getUsage(sender));
			}
			super.execute(server, sender, args);
		}

		public static abstract class DDDCommandBase extends CommandBase {
			
			private final String name, parent, usage;
			
			protected DDDCommandBase(String name, String parent) {
				this.name = name;
				this.parent = parent;
				this.usage = Translations.INSTANCE.getLayeredTranslator(CommandConsts.COMMANDS_ROOT, this.parent, this.name).getKey(CommandConsts.USAGE);
			}
			
			protected DDDCommandBase(String name, String... parents) {
				this.name = name;
				this.parent = parents[parents.length - 1];
				String[] args = new String[parents.length + 1];
				System.arraycopy(parents, 0, args, 0, parents.length);
				args[args.length - 1] = name;
				this.usage = Translations.INSTANCE.getLayeredTranslator(CommandConsts.COMMANDS_ROOT, args).getKey(CommandConsts.USAGE);
			}

			@Override
			public String getName() {
				return this.name;
			}

			@Override
			public String getUsage(ICommandSender sender) {
				return this.usage;
			}
			
			protected static void sendSuccess(ICommandSender sender) {
				sender.sendMessage(Translations.INSTANCE.getTranslator(CommandConsts.COMMANDS_ROOT).getComponent(CommandConsts.SUCCESS));
			}
		}
	}
	
	enum Operation {
		GIVE,
		TAKE,
		TOGGLE,
		SET,
		ADD;
		
		@Override
		public String toString() {
			return this.name().toLowerCase();
		}
	}
}
