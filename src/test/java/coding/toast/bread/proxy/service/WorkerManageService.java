package coding.toast.bread.proxy.service;

import coding.toast.bread.proxy.vo.Worker;

import java.util.Comparator;
import java.util.List;

public interface WorkerManageService {
    
    List<Worker> workerList();
    
    List<Worker> workerListSort(Comparator<Worker> comp);
    
    Worker findWorkerWithId(long id);
    
    boolean insertWorker(Worker worker);
    
    boolean removeWorker(Long id);

}
