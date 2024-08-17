package yeelp.distinctdamagedescriptions.command;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.command.SelectorHandlerManager;
import net.minecraftforge.server.command.CommandTreeBase;
import yeelp.distinctdamagedescriptions.ModConsts;
import yeelp.distinctdamagedescriptions.ModConsts.CommandConsts;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.api.impl.DDDBuiltInDamageType;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;
import yeelp.distinctdamagedescriptions.util.Translations;
import yeelp.distinctdamagedescriptions.util.Translations.BasicTranslator;

public abstract class DDDCommand extends CommandTreeBase {

	private static final Pattern NUM_REGEX = Pattern.compile("^-?\\d+(\\.\\d+)?$");
	protected static final BasicTranslator TRANSLATOR = Translations.INSTANCE.getTranslator(CommandConsts.COMMANDS_ROOT);
	protected static final String NO_TARGETS_ERROR_KEY = TRANSLATOR.getKey(CommandConsts.NO_TARGETS);
	protected static final String INVALID_TYPE_ERROR_KEY = TRANSLATOR.getKey(CommandConsts.INVALID_TYPE);

	private final int damageTypeIndex;

	protected DDDCommand(int damageTypeIndex) {
		this.damageTypeIndex = damageTypeIndex;
	}

	@Override
	public String getName() {
		return ModConsts.MODID;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public List<String> getAliases() {
		return Lists.newArrayList(ModConsts.MODID_SHORT);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
		int index = args.length - 1;
		if(index == this.damageTypeIndex) {
			return getMatchingDamageTypeArguments(args);
		}
		return super.getTabCompletions(server, sender, args, targetPos);
	}
	
	static final List<String> getMatchingDamageTypeArguments(String[] args) {
		return getListOfStringsMatchingLastWord(args, DDDRegistries.damageTypes.getAll().stream().map(DDDDamageType::getTypeName).collect(Collectors.toList()));
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

}
