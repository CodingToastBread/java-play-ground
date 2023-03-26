package coding.toast.bread.proxy;

import coding.toast.bread.proxy.service.WorkerManageServiceImpl;
import coding.toast.bread.proxy.vo.DEPT;
import coding.toast.bread.proxy.vo.Worker;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.DefaultGeneratorStrategy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CglibProxyTests {
	
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
	void howToUseCglibTest() {
		
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(WorkerManageServiceImpl.class);
		enhancer.setCallback(new MethodInterceptor() {
			@Override
			public Object intercept(Object o,
			                        Method method,
			                        Object[] args,
			                        MethodProxy methodProxy) throws Throwable {
				log.info("proxy intercept!!");
				return methodProxy.invoke(service, args);
			}
		});
		
		WorkerManageServiceImpl proxyService = (WorkerManageServiceImpl) enhancer.create();
		
		Worker compareWorker = new Worker(1L, "Norberto Weyand", DEPT.DEVELOP);
		
		Worker findWorker = proxyService.findWorkerWithId(1L);
		assertThat(findWorker).isEqualTo(compareWorker);
		
	}
	
}
