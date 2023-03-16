package coding.toast.bread.data_uri;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

import static org.springframework.test.util.AssertionErrors.fail;

/**
 * <h3>Simple [ file ↔ dataUri ] convert Test Class</h3>
 * You Have To Prepare your Own testing image file First! Or the test will never success!
 * if you want to test without any file creation process without "YOUR BARE HAND", then test with execute {@link DataUriAndFileConvertingTest}
 */
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataUriTest {

	// test file you have created Before Testing...
	private static final Path testingFile = Paths.get("C:/study/img.png");
	
	// this file will be the copy file Path from "testingFile"...
	private static final Path copyFilePath = Paths.get("C:/study/img2.png");
	
	private static String dataUri = "";
	
	@Test
	@Order(0)
	void convertFileToDataUri() {

		try (InputStream inputStream = Files.newInputStream(testingFile)) {

			// get Content-Type. if you don't know need exact type, and just want to print,
			// then just use "png".
			String contentType = Files.probeContentType(testingFile);

			// read all bytes and transform to Base64 String! (using basic java api - Base64)
			byte[] bytes = inputStream.readAllBytes();
			String dataString =  Base64.getEncoder().encodeToString(bytes);

			// at last create DataUri String
			dataUri = "data:%s;base64,%s".formatted(contentType, dataString);

			// copy the dataUri and then go to https://onlineimagetools.com/convert-data-uri-to-image. Test it!
			log.info("dataUri ==> " + dataUri);
			
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	@Order(1)
	void convertDataUriToFile() {
		String onlyDataString = dataUri.substring(dataUri.indexOf(",") + 1);
		byte[] decode = Base64.getDecoder().decode(onlyDataString.getBytes(StandardCharsets.UTF_8));
		
		try (OutputStream outputStream = Files.newOutputStream(copyFilePath, StandardOpenOption.CREATE)) {
			
			// after this test end check your image file is successfully copied.
			outputStream.write(decode);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
