package coding.toast.bread.data_uri;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.fail;

/**
 *  <h3>Simple [ file ↔ dataUri ] convert Test Class</h3>
 *  just executed. you will know whats gonna happen...
 */
@Slf4j
public class DataUriAndFileConvertingTest {

    /**
     * A Simple Smiling Face Png Data Uri<br>
     * This string was generated from  <a href="https://onlineimagetools.com/convert-image-to-data-uri">online converter</a><br>
     * you can also convert dataUri to image in <a href="https://onlineimagetools.com/convert-data-uri-to-image">this link</a>
     */
    private static final String dataUri
            = "data:image/png;base64,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";
    @Test
    void createImageBasedOnDataUriTest() {

        ///////////////// Creating Image From Data URI!!! /////////////////////

        // creating image file path based on "HOME_PATH".
        String testImageFilePath = System.getProperty("user.home") + File.separator + "smiling_image_for_java_test.png";
        Path testPath = Paths.get(testImageFilePath);

        // get only dataString from dataUri
        String onlyDataString = dataUri.substring(dataUri.indexOf(",") + 1);

        // Base 64 Decode
        byte[] decode = Base64.getDecoder().decode(onlyDataString.getBytes());

        // Create an image file.
        log.info("creating test Image File!!");
        try (OutputStream outputStream = Files.newOutputStream(testPath,StandardOpenOption.CREATE)) {

            outputStream.write(decode);
            log.info("successfully create test image file!");

        } catch (IOException e) {
            fail(e.getMessage());
        }


        ///////////////// Read the Data URI of the image file that you just created. /////////////////////
        try (InputStream inputStream = Files.newInputStream(testPath)) {

            // get Content-Type. if you don't know need exact type, and just want to print,
            // then just use "png".
            String contentType = Files.probeContentType(testPath);

            // read all bytes and transform to Base64 String! (using basic java api - Base64)
            byte[] bytes = inputStream.readAllBytes();
            String dataString =  Base64.getEncoder().encodeToString(bytes);

            // at last create DataUri String
            String checkingDataUri = "data:%s;base64,%s".formatted(contentType, dataString);

            ////////////// Final Check! //////////////
            assertThat(checkingDataUri).isEqualTo(dataUri);

        } catch (IOException e) {
            fail(e.getMessage());
        }

    }
}
