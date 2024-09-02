package coding.toast.bread.converting;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ChangeEncodingTest {

    @Test
    @Description("UTF-8 => EUC-KR")
    void testEncodingChange() throws IOException {
        Path utf8File = Path.of("src/test/resources/sample_csv/customers-100.csv").toAbsolutePath();
        System.out.println(utf8File.toAbsolutePath().getParent().toString());
        Path eucKrFile = Path.of(utf8File.toAbsolutePath().getParent().toString() + "/customers-100-euc-kr.csv");

        try (BufferedReader br = Files.newBufferedReader(utf8File, StandardCharsets.UTF_8);
             BufferedWriter bw = Files.newBufferedWriter(eucKrFile, Charset.forName("EUC-KR"))) {
            br.transferTo(bw);
        }
    }
}
