package yeelp.distinctdamagedescriptions.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import yeelp.distinctdamagedescriptions.util.lib.YLib;

public final class TestYLib {

	String[] input;
	
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@ParameterizedTest(name = "Length up to {0}")
	@MethodSource("yeelp.distinctdamagedescriptions.util.TestYLib#characterProvider")
	public @interface CharTest {
		//empty
	}
	
	static Stream<Character> characterProvider() {
		return Stream.iterate('a', (c) -> (char) (c+1)).limit(('z'-'a')+1);
	}
	
	@CharTest
	@SuppressWarnings("static-method")
	@DisplayName("Testing String split and rejoin are inverses with no spaces.")
	public void testDotSeparated(Character c) {
		StringBuilder sb = new StringBuilder();
		sb.append("test.");
		for(char ch = 'a'; ch < c.charValue(); ch++) {
			sb.append(ch);
			sb.append(".");
		}
		sb.append(c.charValue());
		String s = sb.toString();
		assertEquals(s, YLib.joinNiceString(false, ".", s.split(Pattern.quote("."))));
	}
	
	@Test
	@SuppressWarnings("static-method")
	public void testNoArgs() {
		assertEquals("", YLib.joinNiceString(true, ",", new String[0]));
	}
	
	@CharTest
	@SuppressWarnings("static-method")
	@DisplayName("Testing equality of addSpace true and addSpace false with space in sep")
	public void testCommaSep(Character c) {
		StringBuilder sb = new StringBuilder();
		sb.append("test, ");
		sb.append(c.charValue());
		String s = sb.toString();
		assertEquals(s, YLib.joinNiceString(true, ",", "test", c.toString()));
		assertEquals(s, YLib.joinNiceString(false, ", ", "test", c.toString()));
	}
}
