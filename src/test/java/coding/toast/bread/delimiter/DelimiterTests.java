package coding.toast.bread.delimiter;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class DelimiterTests {

    private final String testTxt = "2022|10|11530|10100|0|서울특별시|구로구|신도림동|1|108.51|||||||||||||||||||";

    // length 29
    // empty string must be element

    @Test
    @DisplayName("java built in 메소드인 delimit 사용")
    void onlyJavaUsingDelimitMethodTest() {

    }


    @Test
    @DisplayName("java built in 메소드인 delimit 사용")
    void onlyJavaUsingStringTokenizerMethodTest() {

    }


    @Test
    void withSpringUtilTest() {
        String[] strings = StringUtils.delimitedListToStringArray(testTxt, "|");
        assertThat(strings.length).isEqualTo(29);
        for (String string : strings) {
            log.debug(string);
        }
    }
}
