package coding.toast.bread.converting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.util.AssertionErrors.fail;

/**
 * get Excel Like Json String, then deserialize to Pojo
 */
public class ExcelLikeJsonToPojoTests {

    private record ExcelLikeJson(String tableComment, String type, List<String> header, List<List<?>> body) { }

    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @Test
    void getJsonToPojo() {

        // example json that looks similar with Excel file contents
        String payload = """
                {
                    "tableComment": "table description",
                    "type": "city",
                    "header": ["cityName", "attr1", "attr2", "attr3", "attr4"],
                    "body": [
                         ["거제시", 10, 20, 30, 40],
                         ["거창군", 20, 40, 60, 0],
                         ["고성군", 30, 60, 90, 120],
                         ["김해시", 40, 80, 120, 160],
                         ["남해군", 50, 100, 150, 200],
                         ["밀양시", 60, 120, 180, 240],
                         ["사천시", 70, 140, 210, 280],
                         ["산청군", 80, 160, 240, 320],
                         ["양산시", 90, 180, 270, 360],
                         ["의령군", 100, 200, 300, 400],
                         ["진주시", 110, 220, 330, 440],
                         ["창녕군", 120, 240, 360, 480],
                         ["창원시_마산합포구", 130, 260, 390, 520],
                         ["창원시_마산회원구", 140, 280, 420, 560],
                         ["창원시_성산구", 150, 300, 450, 600],
                         ["창원시_의창구", 160, 320, 480, 640],
                         ["창원시_진해구", 170, 340, 510, 680],
                         ["통영시", 180, 360, 540, 720],
                         ["하동군", 190, 380, 570, 760],
                         ["함안군", 200, 400, 600, 800],
                         ["함양군", 210, 420, 630, 840],
                         ["합천군", 220, 440, 660, 880]
                    ]
                }
                """;


        try {

            // 1. convert ExcelLike Format json to Pojo
            ExcelLikeJson excelLikeJson = mapper.readValue(payload, ExcelLikeJson.class);


            // 2. visualize the data to console
            String tableComment = excelLikeJson.tableComment();
            System.out.println("tableComment = " + tableComment);

            List<String> header = excelLikeJson.header();
            for (String elem : header) {
                System.out.print(elem + "\t");
            }
            System.out.println();

            List<List<?>> body = excelLikeJson.body();
            for (List<?> objects : body) {
                System.out.println(objects.stream().map(String::valueOf).collect(Collectors.joining("\t")));
            }

        } catch (JsonProcessingException e) {
            fail(e.getMessage());
        }
    }
}
