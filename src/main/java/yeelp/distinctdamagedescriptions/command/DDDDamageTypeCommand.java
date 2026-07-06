package yeelp.distinctdamagedescriptions.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.command.AbstractDDDCommand.DDDCommandSubTreeBase;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public abstract class DDDDamageTypeCommand extends DDDCommandSubTreeBase {

	private final int damageTypeIndex;
	protected DDDDamageTypeCommand(@SuppressWarnings("SameParameterValue") int damageTypeIndex, String name) {
		super(name);
		this.damageTypeIndex = damageTypeIndex;
	}
	
	@Nonnull
    @Override
	public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, BlockPos targetPos) {
		int index = args.length - 1;
		if(index == this.damageTypeIndex) {
			return getMatchingDamageTypeArguments(args);
		}
		return super.getTabCompletions(server, sender, args, targetPos);
	}
	
	static List<String> getMatchingDamageTypeArguments(String[] args) {
		return getListOfStringsMatchingLastWord(args, DDDRegistries.damageTypes.getAll().stream().map(DDDDamageType::getTypeName).collect(Collectors.toList()));
	}
	
	protected int getDamageTypeIndex() {
		return this.damageTypeIndex;
	}

}
