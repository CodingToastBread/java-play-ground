package coding.toast.bread.converting;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ChangeEncodingTest {

    @Test
    @Description("UTF-8 => EUC-KR")
    void testEncodingChange() throws IOException {
        Path utf8File = Path.of("src/test/resources/text/korean_lang.txt").toAbsolutePath();
        System.out.println("utf8File path: " + utf8File);

        String inputEncoding = "UTF-8",
                outputEncoding = "EUC-KR";

        String fileAbsolutePath = StringUtils.cleanPath(utf8File.toString());
        String fileName = StringUtils.stripFilenameExtension(fileAbsolutePath);
        String fileExtension = StringUtils.getFilenameExtension(fileAbsolutePath);

        Path eucKrFile = Path.of("%s-%s.%s".formatted(fileName, outputEncoding,fileExtension));
        System.out.println("eucKrFile path: " + eucKrFile);

        try (BufferedReader br = Files.newBufferedReader(utf8File, Charset.forName(inputEncoding));
             BufferedWriter bw = Files.newBufferedWriter(eucKrFile, Charset.forName(outputEncoding))) {
            br.transferTo(bw);
        }
    }

    @Test
    @Description("UTF-8 => EUC-KR : For Under jdk 11")
    void testEncodingChangeUnderJdk11() throws IOException {
        Path utf8File = Path.of("src/test/resources/text/korean_lang.txt").toAbsolutePath();
        System.out.println("utf8File path: " + utf8File);

        String inputEncoding = "UTF-8",
                outputEncoding = "EUC-KR";

        String fileAbsolutePath = StringUtils.cleanPath(utf8File.toString());
        String fileName = StringUtils.stripFilenameExtension(fileAbsolutePath);
        String fileExtension = StringUtils.getFilenameExtension(fileAbsolutePath);

        Path eucKrFile = Paths.get("%s-%s.%s".formatted(fileName, outputEncoding,fileExtension));
        System.out.println("eucKrFile path: " + eucKrFile);

        try (BufferedReader br = Files.newBufferedReader(utf8File, Charset.forName(inputEncoding));
             BufferedWriter bw = Files.newBufferedWriter(eucKrFile, Charset.forName(outputEncoding))) {

            char[] buffer = new char[8192];
            int readCnt;
            while ((readCnt = br.read(buffer, 0, buffer.length)) != -1) {
                bw.write(buffer, 0, readCnt);
            }
        }
    }
}
