package yeelp.distinctdamagedescriptions.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import yeelp.distinctdamagedescriptions.ModConsts.CommandConsts;
import yeelp.distinctdamagedescriptions.api.DDDAPI;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.capability.impl.DefaultResistances;
import yeelp.distinctdamagedescriptions.util.Translations;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public abstract class DDDStatCommand extends AbstractDDDCommand {

	private static final String USAGE = Translations.INSTANCE.getLayeredTranslator(CommandConsts.COMMANDS_ROOT, "stat").getKey(CommandConsts.USAGE);
	private DDDStatCommand() {
		this.addSubcommand(new DDDResistanceSubCommand());
		this.addSubcommand(new DDDImmunitySubCommand());
	}

	@Nonnull
	@Override
	public String getUsage(@Nonnull ICommandSender sender) {
		return USAGE;
	}
	
	public static abstract class DDDStatSubCommand extends DDDDamageTypeCommand {

		@FunctionalInterface
		protected interface CommandConstructor<T, U, V, R> {
			R apply(T t, U u, V v);
		}
		
		protected DDDStatSubCommand(String name) {
			super(3, name);
		}
		
		protected <T> void addSubcommand(Operation name, T t, CommandConstructor<String, String, T, DDDCommandBase> command) {
			super.addSubcommand(command.apply(name.toString(), this.getName(), t));
		}
		
		@Override
		protected int getSubCommandDepth(int index) {
			return index == this.getDamageTypeIndex() - 2 ? 0 : -1;
		}
		
		@Override
		protected boolean isTargetsIndex(int index) {
			return index == this.getDamageTypeIndex() - 1;
		}
		
		@Override
		protected boolean hasCompletionsAvailable(int index) {
			return index <= this.getDamageTypeIndex();
		}
		
	}

	public static final class DDDResistanceSubCommand extends DDDStatSubCommand {

		public DDDResistanceSubCommand() {
			super("resistance");
			this.addSubcommand(Operation.SET, (f1, f2) -> f2, DDDResistanceSubSubCommand::new);
			this.addSubcommand(Operation.ADD, Float::sum, DDDResistanceSubSubCommand::new);
		}

		public static final class DDDResistanceSubSubCommand extends DDDCommandBase {

			private final BinaryOperator<Float> op;

			private DDDResistanceSubSubCommand(String name, String parent, BinaryOperator<Float> op) {
				super(name, parent);
				this.op = op;
			}

			@Override
			public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
				if(args.length != 3) {
					throw new WrongUsageException(this.getUsage(sender));
				}
				if(!AbstractDDDCommand.isNumerical(args[2])) {
					AbstractDDDCommand.sendErrorMessage(sender, AbstractDDDCommand.TRANSLATOR.getKey("numerr"), args[2]);
					return;
				}
				float amount = Float.parseFloat(args[2]);
				Optional<DDDDamageType> oType = AbstractDDDCommand.parseDamageTypeArgument(sender, args[1]);
				if(oType.isPresent()) {
					DDDDamageType type = oType.get();
					for(EntityLivingBase entity : AbstractDDDCommand.getTargetSelection(server, sender, args[0])) {
						DDDAPI.accessor.getMobResistances(entity).ifPresent((resists) -> {
							if(resists instanceof DefaultResistances) {
								return;
							}
							resists.setBaseResistance(type, this.op.apply(resists.getResistance(type), amount));
							resists.setResistance(type, resists.getBaseResistance(type));
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

	public static final class DDDImmunitySubCommand extends DDDStatSubCommand {

		public DDDImmunitySubCommand() {
			super("immunity");
			this.addSubcommand(Operation.GIVE, (b) -> true, DDDImmunitySubSubCommand::new);
			this.addSubcommand(Operation.TAKE, (b) -> false, DDDImmunitySubSubCommand::new);
			this.addSubcommand(Operation.TOGGLE, (b) -> !b, DDDImmunitySubSubCommand::new);
		}

		public static final class DDDImmunitySubSubCommand extends DDDCommandBase {

			private final UnaryOperator<Boolean> op;
			private DDDImmunitySubSubCommand(String name, String parent, UnaryOperator<Boolean> op) {
				super(name, parent);
				this.op = op;
			}


			@Override
			public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
				if(args.length != 2) {
					throw new WrongUsageException(this.getUsage(sender));
				}
				Optional<DDDDamageType> oType = AbstractDDDCommand.parseDamageTypeArgument(sender, args[1]);
				if(oType.isPresent()) {
					DDDDamageType type = oType.get();
					for(EntityLivingBase entity : AbstractDDDCommand.getTargetSelection(server, sender, args[0])) {
						DDDAPI.accessor.getMobResistances(entity).ifPresent((resists) -> {
							if(resists instanceof DefaultResistances) {
								return;
							}
							resists.setBaseImmunity(type, this.op.apply(resists.hasImmunity(type)));
							resists.setImmunity(type, resists.hasBaseImmunity(type));
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
}
