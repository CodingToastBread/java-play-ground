package coding.toast.bread.http_client_api;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Simple Video Downloading Using Java HttpClient API<br>
 * <strong>Note: This Test Download a *.mp4 video from <a href="https://sample-videos.com">sample-videos.com</a>.<br>
 * The file download location for this test code is the "{user home directory}/sample_video/" of the local computer user</strong>
 */
public class JavaHttpClientDownLoadFileTests {
	
	@Test
	void downloadFileTest() throws IOException, InterruptedException {
		HttpRequest httpRequest = HttpRequest.newBuilder()
			.GET()
			.uri(URI.create("https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_2mb.mp4"))
			.build();
		
		HttpClient httpClient = HttpClient.newHttpClient();
		
		Path sampleVideoPath
			= Path.of(System.getProperty("user.home")).resolve("sample_video");

		// if the path doesn't exist, create one.
		Files.createDirectories(sampleVideoPath);

		HttpResponse<Path> video1 = httpClient.send(httpRequest,
			HttpResponse.BodyHandlers.ofFile(sampleVideoPath.resolve("1.mp4")));
		
		HttpResponse<InputStream> videoStream = httpClient.send(httpRequest,
			HttpResponse.BodyHandlers.ofInputStream());
		try (InputStream is = videoStream.body()) {
			is.transferTo(Files.newOutputStream(sampleVideoPath.resolve("2.mp4")));
		}
		
		// Desktop.getDesktop().open(sampleVideoPath.toFile());
		
	}
	
}
