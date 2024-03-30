package coding.toast.bread.spring.spel;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class is a test class for Spring Expression Language (SpEL).<br>
 * There are two ways of testing,<br>
 * <ul>
 * <li>one is the method of injecting values through @Value</li>
 * <li>and the other is the method of extracting values directly using SpelExpressionParser.</li>
 * </ul>
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#expressions">spring-framework - core: #expressions</a>
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#expressions-language-ref">expressions-language-ref</a>
 * @see <a href="https://www.baeldung.com/spring-value-defaults">spring-value-defaults</a>
 * @see <a href="https://stackoverflow.com/questions/5322632/spring-expression-language-spel-with-value-dollar-vs-hash-vs">difference between hash and dollar in @Value annotation</a>
 */
@Slf4j
@SpringBootTest
public class SpringExpressionLangTests {
	
	// Note: difference between $ and # is...
	// - ${...} is the property placeholder syntax. It can only be used to dereference properties.
	// - #{...} is SpEL syntax, which is far more capable and complex. It can also handle property placeholders,
	//          and a lot more besides.
	//  - https://stackoverflow.com/questions/5322632/spring-expression-language-spel-with-value-dollar-vs-hash-vs
	
	
	// your home directory
	@Value("#{systemProperties['user.home']}")
	private String userHome;
	
	// your os name
	@Value("#{systemProperties['os.name']}")
	private String osName;
	
	
	// read systemProps and use ternary operator
	@Value("#{systemProperties['os.name'].toLowerCase().contains('win') ? " +
		"'my computer''s os is window!!' : " +
		"'my computer''s os is Mac(or Linux?)!!'}"
	)
	private String tellMeYourOs;
	
	
	// The path where you executed Java
	@Value("#{ systemProperties['user.dir']}")
	private String userDir;
	
	// java home directory path
	@Value("#{systemProperties['java.home']}")
	private String javaHome;
	
	// can create list(also array!)
	@Value("1,2,3,4,5")
	private List<String> list;
	
	// Even, as shown in the following code,
	// you can even retrieve the result value through method invocation!
	@Value("#{T(java.time.LocalDateTime).now()}")
	private LocalDateTime now;
	
	// you can do much more complicated process if you want.
	@Value("#{T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM-dd').format(T(java.time.LocalDateTime).now())}")
	private String formattedDateString;

	@Value("#{T(org.springframework.util.StringUtils).hasText('${checking.emtpy.string.value:}')}")
	private Boolean isNotEmptyString;
	
	// null or blank
	@Value("#{'${something.wow:}'.trim() <= ''}")
	private Boolean nullOrBlank;
	
	// not null or blank
	@Value("#{'${something.wow:}'.trim() > ''}")
	private Boolean notNullOrBlank;
	
	// Bean References test(1)
	@TestConfiguration
	static class SpringExpressionLangTestConfig {
		
		@Getter
		@Component("testMeBean")
		static class TestMe {
			private final String name = "my Name Is 'testMeBean'";
			
		}
	
		@Bean
		public Map<String,String> myMap() {
			return Map.of("good", "job");
		}
	}

	/*
 	// tip. reading Resource with @Value tag.
	@Value("classpath:static/alarm.html")
    	private Resource resourceFile;

    @Value("classpath:static/*.html")
    private Resource[] resourceList;

	@Test
	void readEmailHtmlTemplate() throws ApiException {
		try (InputStream is = resourceFile.getInputStream()){
		    log.info(IOUtils.toString(is, StandardCharsets.UTF_8));
		} catch (IOException e) {
	    		log.error(e);
		}
    }
    */
	
	// Bean References test(2)
	@Value("#{myMap}")
	private Map<String, String> mapValue;
	
	// Bean References test(3)
	@Value("#{testMeBean}")
	private SpringExpressionLangTestConfig.TestMe testMe;
	
	
	// reads properties from application.yml.
	// If the key 'no.such.value.here' does not exist in the properties file,
	// the field is injected with a default value of 'some day' as a string.
	@Value("${no.such.value.here: some day}")
	private String propertyValueWithDefault;
	
	
	// almost the same one, but give the default value using SpEL
	@Value("${no.such.value.here: #{'some day'}}")
	private String propertyValueComplexDefault;
	
	// if you want use ${} as string value inside the #{},
	// you have to wrap with single quote like below:
	@Value("#{'${my.number.is:}' eq '1'}")
	private Boolean isOne;
	
	// @Value("#{${my.number.is}}")
	// private String myNameIs;
	//
	// ==> will this work? it might be, or not.
	//     because the property value you read should be the
	//     "bean name" which is already registered in Application Context.
	
	
	/**
	 * We're not performing any validation tests.<br>
	 * We're simply logging the output to visually confirm what values were passed in.<br>
	 */
	@Test
	@DisplayName("SpEL parser test using @Value Annotation")
	void SpELWithValueAnnotationTest() {
		log.info("isOne : {}", isOne);
		log.info("testMe.getName() => {}", testMe.getName());
		log.info("mapValue = {}", mapValue);
		log.info("now = {}", now);
		log.info("formattedDateString = {}", formattedDateString);
		log.info("isNotEmptyString = {}", isNotEmptyString);
		log.info("nullOrBlank = {}", nullOrBlank);
		log.info("notNullOrBlank = {}", notNullOrBlank);
		log.info("userHome = {}", userHome);
		log.info("osName = {}", osName);
		log.info("tellMeYourOs = {}", tellMeYourOs);
		log.info("userDir = {}", userDir);
		log.info("javaHome = {}", javaHome);
		log.info("list = {}", list);
		log.info("propertyValueWithDefault = {}", propertyValueWithDefault);
		log.info("propertyValueComplexDefault = {}", propertyValueComplexDefault);
	}
	
	
	@Test
	@DisplayName("Test with SpelExpressionParser")
	@SuppressWarnings({"rawtypes","unchecked"})
	void SpeLParserTest() {
		
		SpelExpressionParser parser = new SpelExpressionParser();
		Object helloWorld = parser.parseExpression("'Hello World'").getValue();
		
		log.debug("helloWorld = {}", helloWorld);
		assertThat(helloWorld).isEqualTo("Hello World");
		// Hello World
		
		Object homeSweetHome = parser.parseExpression("'Home ''Sweet'' home'").getValue();
		
		
		log.debug("homeSweetHome = {}", homeSweetHome);
		assertThat(homeSweetHome).isEqualTo("Home 'Sweet' home");
		// Home 'Sweet' home
		
		Boolean aTrue = (Boolean) parser.parseExpression("true").getValue();
		assertThat(aTrue).isTrue();
		
		Object nullVal = parser.parseExpression("null").getValue();
		assertThat(nullVal).isNull();
		
		List list = (List) parser.parseExpression("{1,2,3,4}").getValue();
		assertThat(list).containsExactly(1, 2, 3, 4);
		
	}
}
