package yeelp.distinctdamagedescriptions.integration.crafttweaker.brackets;

import java.util.List;
import java.util.stream.Collectors;

import crafttweaker.zenscript.GlobalRegistry;
import crafttweaker.zenscript.IBracketHandler;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.type.natives.JavaMethod;

public abstract class AbstractDDDBracketHandler implements IBracketHandler {

	private final String root;
	public final IJavaMethod method;
	
	protected AbstractDDDBracketHandler(String root, Class<?> clazz, String methodName) {
		this.root = root;
		this.method = JavaMethod.get(GlobalRegistry.getTypes(), clazz, methodName, String.class);
	}

	@Override
	public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens) {
		if(tokens == null || tokens.size() < 3 || !tokens.get(0).getValue().equalsIgnoreCase(this.root)) {
			return null;
		}
		return position -> new ExpressionCallStatic(position, environment, this.method, new ExpressionString(position, String.join("", tokens.subList(2, tokens.size()).stream().map(Token::getValue).collect(Collectors.toList()))));
	}

}
