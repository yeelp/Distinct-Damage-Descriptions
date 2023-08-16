package yeelp.distinctdamagedescriptions.command;

import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.util.Translations;

public final class DDDStatCommand extends DDDCommand {

	private static final String USAGE = Translations.INSTANCE.getLayeredTranslator("commands", "stat").getKey("usage");
	public DDDStatCommand() {
		super(3);
		this.addSubcommand(new DDDResistanceSubCommand());
		this.addSubcommand(new DDDImmunitySubCommand());
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return USAGE;
	}

	public static abstract class DDDStatCommandSubTreeBase extends CommandTreeBase {

		@FunctionalInterface
		interface CommandConstructor<T, U, V, R> {
			R apply(T t, U u, V v);
		}
		
		private final String name;
		private final String usage;

		protected DDDStatCommandSubTreeBase(String name) {
			this.name = name;
			this.usage = Translations.INSTANCE.getLayeredTranslator("commands", this.name).getKey("usage");
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public String getUsage(ICommandSender sender) {
			return this.usage;
		}
		
		public <T> void addSubcommand(String name, T t, CommandConstructor<String, String, T, DDDStatCommandBase> command) {
			super.addSubcommand(command.apply(name, this.name, t));
		}



		public static abstract class DDDStatCommandBase extends CommandBase {
			
			private final String name, parent, usage;
			
			protected DDDStatCommandBase(String name, String parent) {
				this.name = name;
				this.parent = parent;
				this.usage = Translations.INSTANCE.getLayeredTranslator("commands", this.parent, this.name).getKey("usage");
			}

			@Override
			public String getName() {
				return this.name;
			}

			@Override
			public String getUsage(ICommandSender sender) {
				return this.usage;
			}
		}

	}

	public static final class DDDResistanceSubCommand extends DDDStatCommandSubTreeBase {

		public DDDResistanceSubCommand() {
			super("resistance");
			this.addSubcommand("set", (f1, f2) -> f2, DDDResistanceSubSubCommand::new);
			this.addSubcommand("add", Float::sum, DDDResistanceSubSubCommand::new);
		}

		public static final class DDDResistanceSubSubCommand extends DDDStatCommandBase {

			private final BinaryOperator<Float> op;

			protected DDDResistanceSubSubCommand(String name, String parent, BinaryOperator<Float> op) {
				super(name, parent);
				this.op = op;
			}

			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if(args.length != 3) {
					throw new WrongUsageException(this.getUsage(sender));
				}
				if(!DDDCommand.isNumerical(args[2])) {
					DDDCommand.sendErrorMessage(sender, DDDCommand.TRANSLATOR.getKey("numerr"), args[2]);
					return;
				}
				float amount = Float.parseFloat(args[2]);
				Optional<DDDDamageType> oType = DDDCommand.parseDamageTypeArgument(sender, args[1]);
				if(oType.isPresent()) {
					DDDDamageType type = oType.get();
					for(EntityLivingBase entity : DDDCommand.getTargetSelection(server, sender, args[0])) {
						DDDAPI.accessor.getMobResistances(entity).ifPresent((resists) -> {
							resists.setResistance(type, this.op.apply(resists.getResistance(type), amount));
							if(entity instanceof EntityPlayer) {
								resists.sync((EntityPlayer) entity);
							}
						});
					}
				}
			}
		}
	}

	public static final class DDDImmunitySubCommand extends DDDStatCommandSubTreeBase {

		public DDDImmunitySubCommand() {
			super("immunity");
			this.addSubcommand("give", (b) -> true, DDDImmunitySubSubCommand::new);
			this.addSubcommand("take", (b) -> false, DDDImmunitySubSubCommand::new);
			this.addSubcommand("toggle", (b) -> !b, DDDImmunitySubSubCommand::new);
		}

		public static final class DDDImmunitySubSubCommand extends DDDStatCommandBase {

			private final UnaryOperator<Boolean> op;
			protected DDDImmunitySubSubCommand(String name, String parent, UnaryOperator<Boolean> op) {
				super(name, parent);
				this.op = op;
			}


			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
				if(args.length != 2) {
					throw new WrongUsageException(this.getUsage(sender));
				}
				Optional<DDDDamageType> oType = DDDCommand.parseDamageTypeArgument(sender, args[1]);
				if(oType.isPresent()) {
					DDDDamageType type = oType.get();
					for(EntityLivingBase entity : DDDCommand.getTargetSelection(server, sender, args[0])) {
						DDDAPI.accessor.getMobResistances(entity).ifPresent((resists) -> {
							resists.setImmunity(type, this.op.apply(resists.hasImmunity(type)));
							if(entity instanceof EntityPlayer) {
								resists.sync((EntityPlayer) entity);
							}
						});
					}
				}
			}

		}
	}
}
