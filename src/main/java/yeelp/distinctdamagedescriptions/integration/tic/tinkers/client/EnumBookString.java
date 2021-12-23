package yeelp.distinctdamagedescriptions.integration.tic.tinkers.client;

import net.minecraft.util.text.TextComponentTranslation;

final class EnumBookString<E extends Enum<E> & IEnumTranslation> implements IBookString {

	private final E type;
	private final String[] components;

	public EnumBookString(E type, String... strings) {
		this.type = type;
		this.components = strings;
	}

	@Override
	public String toBookString() {
		return new TextComponentTranslation(translationRoot + this.type.getRootString() + "." + this.type.toString().toLowerCase(), (Object[]) this.components).getFormattedText();
	}

}
