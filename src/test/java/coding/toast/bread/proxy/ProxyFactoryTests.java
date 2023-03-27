package coding.toast.bread.proxy;

import coding.toast.bread.proxy.service.NoInterfaceService;
import coding.toast.bread.proxy.service.WorkerManageService;
import coding.toast.bread.proxy.service.WorkerManageServiceImpl;
import coding.toast.bread.proxy.vo.DEPT;
import coding.toast.bread.proxy.vo.Worker;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
@Slf4j
public class ProxyFactoryTests {
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
	
	private static final WorkerManageServiceImpl implInterfaceProxyTarget = new WorkerManageServiceImpl(predefinedWorkerList);
	private static final NoInterfaceService noInterfaceProxyTarget = new NoInterfaceService(predefinedWorkerList);
	
	
	@Test
	void testProxyFactory() {
		ProxyFactory proxyFactory = new ProxyFactory(implInterfaceProxyTarget);
		proxyFactory.addAdvice(new MethodInterceptor() {
			@Override
			public Object invoke(MethodInvocation invocation) throws Throwable {
				log.info("proxy intercept!... by JDK PROXY");
				return invocation.proceed();
			}
		});
		
		Object proxy = (WorkerManageService) proxyFactory.getProxy();
		
		assertThat(AopUtils.isAopProxy(proxy)).isTrue();
		assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
		
		ProxyFactory proxyFactory2 = new ProxyFactory(noInterfaceProxyTarget);
		proxyFactory2.addAdvice((MethodInterceptor) invocation -> {
			log.info("proxy intercept!... by CGLIB PROXY");
			return invocation.proceed();
		});
		Object proxy2 = proxyFactory2.getProxy();
		
		assertThat(AopUtils.isAopProxy(proxy2)).isTrue();
		assertThat(AopUtils.isCglibProxy(proxy2)).isTrue();
		
	}


	@Test
	@DisplayName("how to use point cut")
	void pointCutTest() {
		ProxyFactory proxyFactory = new ProxyFactory(implInterfaceProxyTarget);
		proxyFactory.addAdvisor(new DefaultPointcutAdvisor(new MyPointcut(), new MyAdvice()));
		WorkerManageService proxyService = (WorkerManageService) proxyFactory.getProxy();

		// get security String
		String securityString = MyAdvice.securityString;

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

	static class MyAdvice implements MethodInterceptor {

		static final String securityString = "[SENSITIVE INFORMATION CANNOT BE EXPOSED]";

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			Object proceed = invocation.proceed();
			if (proceed instanceof List<?> workerList) {
				return workerList.stream()
						.map(worker -> new Worker(((Worker) worker).id(), securityString, ((Worker) worker).dept()))
						.toList();
			} else if (proceed instanceof Worker worker) {
				return new Worker(worker.id(), securityString, worker.dept());
			}
			throw new IllegalStateException("Unexpected value: " + invocation.getMethod().getName());
		}
	}

	static class MyPointcut implements Pointcut {

		private final List<String> targetMethodNames = List.of(
				"workerList",
				"workerListSort",
				"findWorkerWithId"
		);

		@Override
		public ClassFilter getClassFilter() {
			return ClassFilter.TRUE;
		}

		@Override
		public MethodMatcher getMethodMatcher() {
			return new MethodMatcher() {
				@Override
				public boolean matches(Method method, Class<?> targetClass) {
					return targetMethodNames.contains(method.getName());
				}

				@Override
				public boolean isRuntime() {
					return false;
				}

				@Override
				public boolean matches(Method method, Class<?> targetClass, Object... args) {
					throw new UnsupportedOperationException();
				}
			};
		}

	}
}















