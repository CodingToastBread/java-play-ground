package coding.toast.bread.proxy.vo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Worker {
    private String id;
    private String name;
    private DEPT dept;
}
