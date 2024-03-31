package coding.toast.bread.virtual_thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Simple Virtual Thread API Test
 */
@Slf4j
public class VirtualThreadTests {
	@Test
	void basicApiUseTest() {
		
		Thread vThread = Thread.ofVirtual().unstarted(() ->
			log.info("current thread => {}", Thread.currentThread()));
		vThread.start();
		
		try {
			vThread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@Test
	void executorServiceTest() {
		// although it is ExecutorService Type,
		// Virtual Thread is not pooled! it creates new Virtual Thread
		// every time if you call submit method.
		ExecutorService es = Executors.newVirtualThreadPerTaskExecutor();
		
		// try - catch - resource block will wait until
		// all the work submit to es is done.
		try (es) {
			for (int i = 0; i < 10; i++) {
				final int ii = i;
				es.submit(() -> {
					try {
						// Watch The Carrier Thread(ForkJoinPool-1-worker-?) Changes after sleep!
						System.out.printf("%d, %s%n ", ii , Thread.currentThread());
						Thread.sleep(1000L);
						System.out.printf("%d, %s%n ", ii , Thread.currentThread());
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				});
			}
		}
	}
}
