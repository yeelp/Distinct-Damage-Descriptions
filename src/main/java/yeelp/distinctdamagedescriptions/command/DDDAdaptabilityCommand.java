package yeelp.distinctdamagedescriptions.command;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import yeelp.distinctdamagedescriptions.ModConsts.CommandConsts;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.capability.impl.DefaultResistances;
import yeelp.distinctdamagedescriptions.util.Translations;

public final class DDDAdaptabilityCommand extends AbstractDDDCommand {

	private static final String USAGE = Translations.INSTANCE.getLayeredTranslator(CommandConsts.COMMANDS_ROOT, "adaptability").getKey(CommandConsts.USAGE);
	
	public DDDAdaptabilityCommand() {
		this.addSubcommand(new DDDAdaptabilityStatusSubCommand());
		this.addSubcommand(new DDDAdaptabilityAmountSubCommand());
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return USAGE;
	}
	
	@Override
	public String getName() {
		return "adaptability";
	}
	
	@Override
	protected int getSubCommandDepth(int index) {
		if(index < 3) {
			return index - 1;
		}
		return -1;
	}
	
	@Override
	protected boolean isTargetsIndex(int index) {
		return index == 3;
	}
	
	@Override
	protected boolean hasCompletionsAvailable(int index) {
		return index <= 3; 
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length == 0) {
			throw new WrongUsageException(USAGE);
		}
		super.execute(server, sender, args);
	}
	
	public static abstract class DDDAdaptabilitySubCommand extends DDDCommandSubTreeBase {
		protected DDDAdaptabilitySubCommand(String name) {
			super("adaptability", name);
		}
		
		@FunctionalInterface
		public interface CommandConstructor<T, U, V, R> {
			R apply(T t, U u, V v);
		}
		
		protected <T> void addSubcommand(Operation name, T t, CommandConstructor<String, String, T, DDDCommandBase> constructor) {
			this.addSubcommand(constructor.apply(name.toString(), this.getName(), t));
		}
		
		@Override
		protected int getSubCommandDepth(int index) {
			if(index < 3) {
				return index - 1;
			}
			return -1;
		}
		
		@Override
		protected boolean isTargetsIndex(int index) {
			return index == 3;
		}
		
		@Override
		protected boolean hasCompletionsAvailable(int index) {
			return index <= 3;
		}
	}
	
	public static final class DDDAdaptabilityStatusSubCommand extends DDDAdaptabilitySubCommand {
		public DDDAdaptabilityStatusSubCommand() {
			super("status");
			this.addSubcommand(Operation.GIVE, (b) -> true, DDDAdaptabilityStatusSubSubCommand::new);
			this.addSubcommand(Operation.TAKE, (b) -> false, DDDAdaptabilityStatusSubSubCommand::new);
			this.addSubcommand(Operation.TOGGLE, (b) -> !b, DDDAdaptabilityStatusSubSubCommand::new);
		}
		
		public static final class DDDAdaptabilityStatusSubSubCommand extends DDDCommandBase {
			
			private final UnaryOperator<Boolean> op;
			public DDDAdaptabilityStatusSubSubCommand(String name, String parent, UnaryOperator<Boolean> op) {
				super(name, "adaptability", parent);
				this.op = op;
			}

			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if(args.length != 1) {
					throw new WrongUsageException(this.getUsage(sender));
				}
				for(EntityLivingBase entity : AbstractDDDCommand.getTargetSelection(server, sender, args[0])) {
					DDDAPI.accessor.getMobResistances(entity).ifPresent((resists) -> {
						if(resists instanceof DefaultResistances) {
							return;
						}
						resists.setAdaptiveResistance(this.op.apply(resists.hasAdaptiveResistance()), true);
						if(entity instanceof EntityPlayer) {
							resists.sync((EntityPlayer) entity);
						}
					});
				}
				sendSuccess(sender);
			}
		}
	}
	
	public static final class DDDAdaptabilityAmountSubCommand extends DDDAdaptabilitySubCommand {
		public DDDAdaptabilityAmountSubCommand() {
			super("amount");
			this.addSubcommand(Operation.SET, (f1, f2) -> f2, DDDAdaptabilityAmountSubSubCommand::new);
			this.addSubcommand(Operation.ADD, Float::sum, DDDAdaptabilityAmountSubSubCommand::new);
		}
		
		public static final class DDDAdaptabilityAmountSubSubCommand extends DDDCommandBase {
			private final BinaryOperator<Float> op;
			public DDDAdaptabilityAmountSubSubCommand(String name, String parent, BinaryOperator<Float> op) {
				super(name, "adaptability", parent);
				this.op = op;
			}
			
			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if(args.length != 2) {
					throw new WrongUsageException(this.getUsage(sender));
				}
				if(!AbstractDDDCommand.isNumerical(args[1])) {
					AbstractDDDCommand.sendErrorMessage(sender, AbstractDDDCommand.TRANSLATOR.getKey("numerr"), args[1]);
					return;
				}
				float amount = Float.parseFloat(args[1]);
				for(EntityLivingBase entity : AbstractDDDCommand.getTargetSelection(server, sender, args[0])) {
					DDDAPI.accessor.getMobResistances(entity).ifPresent((resists) -> {
						if(resists instanceof DefaultResistances) {
							return;
						}
						resists.setBaseAdaptiveAmount(this.op.apply(resists.getAdaptiveAmount(), amount));
						resists.setAdaptiveAmount(resists.getBaseAdaptiveAmount());
						if(entity instanceof EntityPlayer) {
							resists.sync((EntityPlayer) entity);
						}
					});
				}
				sendSuccess(sender);
			}
		}
	}
}
