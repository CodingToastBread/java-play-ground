package coding.toast.bread.proxy.vo;

/**
 * This Class is for {@link coding.toast.bread.proxy proxy test classes}<br>
 * this record instance will be managed by {@link coding.toast.bread.proxy.service.WorkerManageService} instance.
 * @param id - worker id
 * @param name - worker name
 * @param dept - worker dept
 */
public record Worker(long id, String name, DEPT dept) {}
