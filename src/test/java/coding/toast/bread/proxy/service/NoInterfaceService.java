package coding.toast.bread.proxy.service;

import coding.toast.bread.proxy.vo.Worker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class NoInterfaceService {
	protected NoInterfaceService() {
	}
	
	private final ArrayList<Worker> workerList = new ArrayList<>();
	
	public NoInterfaceService(List<Worker> workers) {
		workerList.addAll(workers);
	}
	
	public List<Worker> workerList() {
		return workerList.stream().sorted((o1, o2) -> (int)(o1.id() - o2.id())).toList();// with read-only list
	}
	
	public List<Worker> workerListSort(Comparator<Worker> comp) {
		return workerList.stream().sorted(comp).toList();
	}
	
	public Worker findWorkerWithId(long id) {
		return workerList.stream()
			.filter(worker -> id == worker.id()).findAny()
			.orElseThrow(NoSuchElementException::new);
	}
	
	public boolean insertWorker(Worker worker) {
		if (workerList.stream().noneMatch(w -> w.id() == worker.id())) {
			return workerList.add(worker);
		}
		
		throw new RuntimeException("Found Same Id Worker!!!");
	}
	
	public boolean removeWorker(Long id) {
		return workerList.removeIf(worker -> worker.id() == id);
	}
}
