package coding.toast.bread.proxy;

import coding.toast.bread.proxy.service.NoInterfaceService;
import coding.toast.bread.proxy.service.WorkerManageService;
import coding.toast.bread.proxy.service.WorkerManageServiceImpl;
import coding.toast.bread.proxy.vo.DEPT;
import coding.toast.bread.proxy.vo.Worker;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

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
	
}















