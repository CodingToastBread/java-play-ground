package coding.toast.bread.http_client_api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.springframework.test.util.AssertionErrors.fail;

/**
 * Simple Video Downloading Using Java HttpClient API<br>
 * <strong>Note: This Test Download a *.mp4 video from <a href="https://sample-videos.com">sample-videos.com</a>.<br>
 * The file download location for this test code is the "{user home directory}/sample_video/" of the local computer user</strong>
 */
@Slf4j
public class JavaHttpClientDownLoadFileTests {
	
	static private final Path sampleVideoPath
		= Path.of(System.getProperty("user.home")).resolve("sample_video");
	
	static private final HttpRequest httpRequest = HttpRequest.newBuilder()
		.GET()
		.uri(URI.create("https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_2mb.mp4"))
		.build();
	
	/**
	 * creating Test Directory before test begins<br>
	 * @apiNote : if you want to automatically open directory as soon as it is created,
	 * uncomment the last line of this method.
	 */
	@BeforeAll
	static void beforeAll() throws IOException {
		FileSystemUtils.deleteRecursively(sampleVideoPath);
		Files.createDirectories(sampleVideoPath);
		// Desktop.getDesktop().open(sampleVideoPath.toFile());
	}
	
	@Test
	void downloadFileTest() throws IOException, InterruptedException {
		
		HttpClient httpClient = HttpClient.newHttpClient();
		
		HttpResponse<Path> video1 = httpClient.send(httpRequest,
			HttpResponse.BodyHandlers.ofFile(sampleVideoPath.resolve("1.mp4")));
		
		HttpResponse<InputStream> videoStream = httpClient.send(httpRequest,
			HttpResponse.BodyHandlers.ofInputStream());
		try (InputStream is = videoStream.body()) {
			is.transferTo(Files.newOutputStream(sampleVideoPath.resolve("2.mp4")));
		}
		
	}
	
	
	@Test
	@DisplayName("Showing Loading Process While Downloading")
	void showLoadingBarTest() {
		
		HttpClient httpClient = HttpClient.newHttpClient();
		
		try {
			
			HttpResponse<InputStream> videoStream = httpClient.send(httpRequest,
				HttpResponse.BodyHandlers.ofInputStream());
			
			String contentLengthString = videoStream.headers().firstValue("content-length").orElseGet(() -> "");
			
			int contentLength = Integer.parseInt(contentLengthString);
			
			try (InputStream is = videoStream.body();
			     OutputStream os = Files.newOutputStream(sampleVideoPath.resolve("loading.mp4"), StandardOpenOption.CREATE)
			) {
				byte[] buffer = new byte[8192 * 4];
				int bufferSize = buffer.length;
				int readByteLength;
				int readTotalByte = 0;
				while ((readByteLength = is.read(buffer, 0, bufferSize)) >= 0) {
					readTotalByte += readByteLength;
					log.info("{} % downloaded...",
						 roundString((((double) readTotalByte) / contentLength) * 100, 2)
					);
					os.write(buffer, 0, readByteLength);
				}
				log.info("download completed!!!");
			}
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (InterruptedException e) {
			fail("Request canceled!!");
		}
		
	}
	
	private String roundString(double value, int cutting) {
		double pow = Math.pow(10, cutting);
		double v = Math.round(value * pow) / pow;
		return String.valueOf(v);
	}
	
	// how can i cancel my request...?
	// https://stackoverflow.com/questions/55209385/cancellation-of-http-request-in-java-11-httpclient
	
}
