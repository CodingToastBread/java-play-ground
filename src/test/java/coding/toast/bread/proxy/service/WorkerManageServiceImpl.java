package coding.toast.bread.proxy.service;

import coding.toast.bread.proxy.vo.Worker;

import java.util.*;

/**
 *
 */
public class WorkerManageServiceImpl implements WorkerManageService {
	
	// private final java.util.concurrent.ConcurrentLinkedQueue<Worker> workerList
	//             = new java.util.concurrent.ConcurrentLinkedQueue<>();
	
	private final ArrayList<Worker> workerList = new ArrayList<>();
	
	public WorkerManageServiceImpl(List<Worker> workers) {
		workerList.addAll(workers);
	}
	
	@Override
	public List<Worker> workerList() {
		return workerList.stream().sorted((o1, o2) -> (int)(o1.id() - o2.id())).toList();// with read-only list
	}
	
	@Override
	public List<Worker> workerListSort(Comparator<Worker> comp) {
		return workerList.stream().sorted(comp).toList();
	}
	
	@Override
	public Worker findWorkerWithId(long id) {
		return workerList.stream()
			.filter(worker -> id == worker.id()).findAny()
			.orElseThrow(NoSuchElementException::new);
	}
	
	@Override
	public boolean insertWorker(Worker worker) {
		if (workerList.stream().noneMatch(w -> w.id() == worker.id())) {
			return workerList.add(worker);
		}
		
		throw new RuntimeException("Found Same Id Worker!!!");
	}
	
	@Override
	public boolean removeWorker(Long id) {
		return workerList.removeIf(worker -> worker.id() == id);
	}
}
