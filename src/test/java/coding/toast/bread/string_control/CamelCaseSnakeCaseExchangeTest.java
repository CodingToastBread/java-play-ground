package coding.toast.bread.string_control;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <h3>Test class for converting between CamelCase and snake_case</h3>
 */
public class CamelCaseSnakeCaseExchangeTest {
	
	private static final String snake_case_string = "life_is_enjoyable_but_now_always";
	private static final String camelCaseString = "lifeIsEnjoyableButNowAlways";
	
	@Test
	@DisplayName("snake_case ➜ camelCase converting test")
	void snakeCaseToCamelCaseTest() {
		Pattern pattern = Pattern.compile("([a-z])_([a-z])");
		Matcher matcher = pattern.matcher(snake_case_string);
		String convertResult = matcher.replaceAll(matchResult // param type is ... java.util.regex.MatchResult !
				-> String.format(
				"%s%s",
				matchResult.group(1).toLowerCase(),
				matchResult.group(2).toUpperCase()
			)
		);
		
		assertThat(convertResult).isEqualTo(camelCaseString);
		
	}
	
	@Test
	@DisplayName("camelCase ➜ snake_case converting test")
	void camelCaseToSnakeCaseTest() {
		Pattern pattern = Pattern.compile("([a-z])([A-Z])");
		Matcher matcher = pattern.matcher(camelCaseString);
		String convertResult = matcher.replaceAll(matchResult // param type is ... java.util.regex.MatchResult !
			-> String.format(
			"%s_%s",
			matchResult.group(1).toLowerCase(),
			matchResult.group(2).toLowerCase()
		));
		
		assertThat(convertResult).isEqualTo(convertResult);
	}
	
	
	@Test
	@DisplayName("for under jdk 8 : snake_case ➜ camelCase converting test")
	void java8_snakeCaseToCamelCaseTest() {
		
		Pattern pattern = Pattern.compile("([a-z])_([a-z])");
		Matcher matcher = pattern.matcher(snake_case_string);
		
		// >>>> There are two ways to approach this.
		// >>>> The first is to simply write all the necessary code,
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String format = String.format(
				"%s%s",
				matcher.group(1).toLowerCase(),
				matcher.group(2).toUpperCase()
			);
			matcher.appendReplacement(sb, format);
		}
		String convertResult = matcher.appendTail(sb).toString();
		
		
		// >>>> the second is to create and use a utility method that references the
		// >>>> replaceAll method of the Matcher class.
		// String convertResult = getConvert(matcher, matchResult -> {
		// 		return String.format(
		// 			"%s_%s",
		// 			matcher.group(1).toLowerCase(),
		// 			matcher.group(2).toLowerCase()
		// 		);
		// 	});
		assertThat(convertResult).isEqualTo(camelCaseString);
	}
	
	@Test
	@DisplayName("for under jdk 8 : camelCase ➜ snake_case converting test")
	void java8_camelCaseToSnakeCaseTest() {
		Pattern pattern = Pattern.compile("([a-z])([A-Z])");
		Matcher matcher = pattern.matcher(camelCaseString);
		
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String format = String.format(
				"%s_%s",
				matcher.group(1).toLowerCase(),
				matcher.group(2).toLowerCase()
			);
			matcher.appendReplacement(sb, format);
		}
		String convertResult = matcher.appendTail(sb).toString();
		assertThat(convertResult).isEqualTo(snake_case_string);
	}
	
	// I created this method by referencing the replaceAll method of the Matcher class.
	private String getConvert(Matcher matcher, Function<MatchResult, String> replacer) {
		// if your jdk version is higher than 8, you can use StringBuilder instead.
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			MatchResult matchResult = matcher.toMatchResult();
			String format = replacer.apply(matchResult);
			matcher.appendReplacement(sb, format);
		}
		return matcher.appendTail(sb).toString();
	}
}
