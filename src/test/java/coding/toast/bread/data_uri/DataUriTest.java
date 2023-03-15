package coding.toast.bread.data_uri;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.TestExecutionResult;
import org.springframework.test.util.AssertionErrors;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import static org.springframework.test.util.AssertionErrors.fail;

/**
 * Super Simple file-to-dataUri convert Test
 */
@Slf4j
public class DataUriTest {
	
	@Test
	void testMe() {
		
		Path path = Paths.get("C:/study/img.png");
		
		
		try (InputStream inputStream = Files.newInputStream(path)) {
			
			// get Content-Type. if you don't know need exact type, and just want to print,
			// then just use "png".
			String contentType = Files.probeContentType(path);
			
			// read all bytes and transform to Base64 String! (using basic java api - Base64)
			byte[] bytes = inputStream.readAllBytes();
			String dataString = Base64.getEncoder().encodeToString(bytes);
			
			// at last create DataUri String
			String dataUri =
                "data:%s;base64,%s".formatted(contentType, dataString);
			
			log.info("dataUri ==> " + dataUri);
			// copy the dataUri and then go to https://onlineimagetools.com/convert-data-uri-to-image. Test it!
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
