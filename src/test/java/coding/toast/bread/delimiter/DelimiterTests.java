package coding.toast.bread.delimiter;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class DelimiterTests {

    private final String testTxt = "2022|10|11530|10100|0|Seoul|Guro-gu|Sindorim-dong|1|108.51|||||||||||||||||||";

    // after delimited with '|', the list(or array) length must be 29
    // and also Empty strings must be preserved.

    @Test
    @DisplayName("Using the built-in Java method 'delimit'")
    void onlyJavaUsingDelimitMethodTest() {

    }


    @Test
    @DisplayName("Using the built-in Java class 'StringTokenizer'")
    void onlyJavaUsingStringTokenizerMethodTest() {

    }


    @Test
    @DisplayName("Using the 'StringUtils' class")
    void withSpringUtilTest() {
        String[] strings = StringUtils.delimitedListToStringArray(testTxt, "|");
        assertThat(strings.length).isEqualTo(29);
        for (String string : strings) {
            log.debug(string);
        }
    }
}
