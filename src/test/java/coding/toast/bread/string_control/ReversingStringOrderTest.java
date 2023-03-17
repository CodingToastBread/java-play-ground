package coding.toast.bread.string_control;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * very simple test code class that reverses a string.
 */
public class ReversingStringOrderTest {
	
	private static final String exampleString = "I'm a coding toast bread";
	private static final String reversedString = "daerb tsaot gnidoc a m'I";
	
	
	@Test
	@DisplayName("reverse String with StringBuilder")
	void reverseStringTest() {
		StringBuilder sb = new StringBuilder(exampleString);
		assertThat(sb.reverse().toString()).isEqualTo(reversedString);
	}
	
	
	@Test
	@DisplayName("reverse String using for loop")
	void reverseStringWithForLoopTest() {
		
		char[] charList = exampleString.toCharArray();
		char[] reverseCharList = new char[charList.length];
		for (int i = 0; i < charList.length; i++) {
			reverseCharList[i] = charList[charList.length - i - 1];
		}
		String convertResult = new String(reverseCharList);
		assertThat(convertResult).isEqualTo(reversedString);
		
	}
}
