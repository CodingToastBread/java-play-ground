package coding.toast.bread.http_client_api;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.*;

public class DownloadFileWithLoadingLogTest {
	
	private static final String FILE_URL = "https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_2mb.mp4";
	private static final String FILE_NAME = "big_buck_bunny_720p_2mb.mp4";
	private static long downloadedBytes = 0L;
	
	@Test
	void downLoadButLoggingWithAnotherThread() throws IOException, InterruptedException {
		
		// create a Log Printer
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<?> future = executor.submit(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				// Print the download progress to the console
				System.out.println("Downloaded " + downloadedBytes + " bytes...");
				try {
					Thread.sleep(1000); // print every 1 sec
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		});
		
		
		// create HttpRequest, HttpClient
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(FILE_URL))
			.build();
		
		HttpClient client = HttpClient.newBuilder()
			.followRedirects(HttpClient.Redirect.ALWAYS)
			.build();
		
		// send Http and get response
		HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
		Path sampleVideoDirectory = Path.of(System.getProperty("user.home")).resolve("sample_video");
		Path filePath = sampleVideoDirectory.resolve(FILE_NAME);
		
		// if you want percentage, use the contentLength!
		// long contentLength = response.headers().firstValue("Content-Length").map(Long::parseLong).orElse(-1L);
		
		try (InputStream is = response.body();
		     OutputStream os = Files.newOutputStream(filePath)) {
			byte[] buffer = new byte[8192 * 2];
			int bytesRead;
			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
				downloadedBytes += bytesRead;
			}
		}
		
		future.cancel(true);
		executor.shutdown();
	}
}
