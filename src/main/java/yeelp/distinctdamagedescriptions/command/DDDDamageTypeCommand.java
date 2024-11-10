package yeelp.distinctdamagedescriptions.command;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import yeelp.distinctdamagedescriptions.api.DDDDamageType;
import yeelp.distinctdamagedescriptions.command.AbstractDDDCommand.DDDCommandSubTreeBase;
import yeelp.distinctdamagedescriptions.registries.DDDRegistries;

public abstract class DDDDamageTypeCommand extends DDDCommandSubTreeBase {

	private final int damageTypeIndex;
	protected DDDDamageTypeCommand(int damageTypeIndex, String name) {
		super(name);
		this.damageTypeIndex = damageTypeIndex;
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
	
	protected int getDamageTypeIndex() {
		return this.damageTypeIndex;
	}

}
