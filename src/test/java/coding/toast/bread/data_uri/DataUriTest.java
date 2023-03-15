package coding.toast.bread.data_uri;

import org.junit.jupiter.api.Test;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class DataUriTest {
	
	@Test
	void testMe() {
		
		Path path = Paths.get("C:/study/img.png");
		
		String contentType = null;
		try {
			contentType = Files.probeContentType(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		String dataUri = "data:" + contentType + ";base64,";
		
		byte[] bytes;
		try (InputStream inputStream = Files.newInputStream(path)) {
			bytes = inputStream.readAllBytes();
			dataUri += Base64.getEncoder().encodeToString(bytes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		System.out.println("dataUri = " + dataUri);
		
	}
}
