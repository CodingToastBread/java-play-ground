package coding.toast.bread.http_client_api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class SendingManyRequestAtOnceTest {
	static AtomicInteger counter = new AtomicInteger(0);
	
	@Test
	void atOnceTest()
		throws InterruptedException, BrokenBarrierException {
		
		ExecutorService es = Executors.newFixedThreadPool(100);
		
		RestTemplate rt = new RestTemplate();
		String url = "https://jsonplaceholder.typicode.com/todos/1";
		
		CyclicBarrier barrier = new CyclicBarrier(101);
		
		StopWatch mainWatch = new StopWatch();
		mainWatch.start();
		
		for (int i = 0; i < 100; i++) {
			es.submit(() -> {
				int idx = counter.addAndGet(1);
				barrier.await();
				
				log.info("Thread {}", idx);
				StopWatch threadWatch = new StopWatch();
				threadWatch.start();
				
				JsonNode res = rt.getForObject(url, JsonNode.class);
				
				threadWatch.stop();
				log.info("Thread {} -> Elapsed: {} / {}",
					idx, threadWatch.getTotalTimeSeconds(), res);
				
				return (Void)null;
			});
		}
		
		barrier.await();
		es.shutdown();
		es.awaitTermination(100, TimeUnit.SECONDS);
		
		mainWatch.stop();
		log.info("Total: {}", mainWatch.getTotalTimeSeconds());
		
	}
}
