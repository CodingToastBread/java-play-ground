package coding.toast.bread.proxy;

import coding.toast.bread.proxy.service.WorkerManageService;
import coding.toast.bread.proxy.service.WorkerManageServiceImpl;
import coding.toast.bread.proxy.vo.DEPT;
import coding.toast.bread.proxy.vo.Worker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Java Built-in Proxy API Test Class<br>
 *
 * @see WorkerManageServiceImpl
 * @see Worker
 */
@Slf4j
public class JdkProxyTests {
	private static final List<Worker> predefinedWorkerList = List.of(
		new Worker(1L, "Norberto Weyand", DEPT.DEVELOP),
		new Worker(2L, "Nadene Hoelscher", DEPT.DEVELOP),
		new Worker(3L, "Salvadore Cardona", DEPT.HR),
		new Worker(4L, "Kaija Brittingham", DEPT.HR),
		new Worker(5L, "Tarsha Steiger", DEPT.HR),
		new Worker(6L, "Lashun Eades", DEPT.DEVELOP),
		new Worker(7L, "Aranda Laney", DEPT.ACCOUNT),
		new Worker(8L, "Chevy Choy", DEPT.DEVELOP),
		new Worker(9L, "Talayah Gassaway", DEPT.ACCOUNT),
		new Worker(10L, "Kandee Pabon", DEPT.ACCOUNT),
		new Worker(11L, "Chanette Mcduffie", DEPT.ACCOUNT),
		new Worker(12L, "Graham Obregon", DEPT.HR)
	);
	
	private static final WorkerManageServiceImpl service = new WorkerManageServiceImpl(predefinedWorkerList);
	
	
	@Test
	@DisplayName("how to use Proxy.newProxyInstance?")
	void simpleProxyTest() {
		
		WorkerManageService proxyService = (WorkerManageService) Proxy.newProxyInstance(
			WorkerManageService.class.getClassLoader(),
			new Class[]{WorkerManageService.class},
			new InvocationHandler() { // after this test code, won't use InvocationHandler but will use lambda expression.
				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
					return method.invoke(service, args);
				}
			}
		);
		
		Worker workerWithId = proxyService.findWorkerWithId(1L);
		Worker compareWorker = new Worker(1L, "Norberto Weyand", DEPT.DEVELOP);
		assertThat(workerWithId).isEqualTo(compareWorker);
	}
	
	
	@Test
	@DisplayName("Logs 👍 for all method calls")
	void DecorateLoggingProxyTest() {
		
		WorkerManageService proxyService = (WorkerManageService) Proxy.newProxyInstance(
			WorkerManageService.class.getClassLoader(),
			new Class[]{WorkerManageService.class},
			new ThumbsUpDecoInvocationHandler(service)
		);
		
		Worker workerWithId = proxyService.findWorkerWithId(1L);
		Worker compareWorker = new Worker(1L, "Norberto Weyand", DEPT.DEVELOP);
		assertThat(workerWithId).isEqualTo(compareWorker);
	}
	
	
	/**
	 * relate with {@link #DecorateLoggingProxyTest() DecorateLoggingProxy Test Method}
	 */
	static final class ThumbsUpDecoInvocationHandler implements InvocationHandler {
		private final Object proxyTarget;
		
		private ThumbsUpDecoInvocationHandler(Object proxyTarget) {
			Objects.requireNonNull(proxyTarget);
			this.proxyTarget = proxyTarget;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			log.info("👍👍👍👍👍👍👍👍👍👍👍👍👍👍👍👍👍");
			
			Object returnVal = method.invoke(proxyTarget, args);
			log.info("method return value = {}", returnVal);
			
			log.info("👍👍👍👍👍👍👍👍👍👍👍👍👍👍👍👍👍");
			return returnVal;
		}
	}
	
	
	@Test
	@DisplayName("If there is a name value in the return value, it will be hidden.")
	void HideSensitiveProxyTest() {
		
		// create proxy
		WorkerManageService proxyService = (WorkerManageService) Proxy.newProxyInstance(
			WorkerManageService.class.getClassLoader(),
			new Class[]{WorkerManageService.class},
			new HideSensitiveInvocationHandler(service)
		);
		
		// get security String
		String securityString = HideSensitiveInvocationHandler.securityString;
		
		// find Worker who has id = 1
		Worker workerWithId = proxyService.findWorkerWithId(1L);
		
		// create testWorker
		Worker findWorker = new Worker(workerWithId.id(), securityString, workerWithId.dept());
		
		// test (1)
		assertThat(workerWithId).isEqualTo(findWorker);
		
		// test (2)
		assertThat(proxyService.workerList()).allSatisfy(worker
			-> assertThat(worker.name()).isEqualTo(securityString));
	}
	
	
	/**
	 * This InvocationHandler performs encryption if the return value contains a name.
	 * (relate with {@link #HideSensitiveProxyTest() EncryptedReturnValTest Test Method})<br>
	 * <br>
	 * The following are the methods that expose names:<br>
	 * <ul>
	 *   <li>{@link WorkerManageService#workerList() workerList}</li>
	 *   <li>{@link WorkerManageService#workerListSort(Comparator) workerListSort}</li>
	 *   <li>{@link WorkerManageService#findWorkerWithId(long) findWorkerWithId}</li>
	 * </ul>
	 */
	static final class HideSensitiveInvocationHandler implements InvocationHandler {
		private final Object proxyTarget;
		
		private final List<String> targetMethodNameList = List.of(
			"workerList",
			"workerListSort",
			"findWorkerWithId"
		);
		static final String securityString = "[SENSITIVE INFORMATION CANNOT BE EXPOSED]";
		
		private HideSensitiveInvocationHandler(Object proxyTarget) {
			Objects.requireNonNull(proxyTarget);
			this.proxyTarget = proxyTarget;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			
			Object returnVal = method.invoke(proxyTarget, args);
			
			String name = method.getName();
			if (targetMethodNameList.contains(name)) {
				log.debug("method name: {}", name);
				returnVal = hideNameValue(name, returnVal);
			}
			
			return returnVal;
		}
		
		@SuppressWarnings("unchecked")
		private Object hideNameValue(String invokedMethodName, Object convertTarget) {
			return switch (invokedMethodName) {
				case "workerList", "workerListSort" -> {
					List<Worker> returnVal1 = (List<Worker>) convertTarget;
					yield returnVal1.stream()
						.map(worker -> new Worker(worker.id(), securityString, worker.dept()))
						.toList();
				}
				case "findWorkerWithId" -> {
					Worker worker = (Worker) convertTarget;
					yield new Worker(worker.id(), securityString, worker.dept());
				}
				default -> throw new IllegalStateException("Unexpected value: " + invokedMethodName);
			};
		}
	}
}
