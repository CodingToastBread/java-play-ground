package coding.toast.bread.serializable.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;


@Setter
@Getter
public class SampleData implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 42L;
	
	private String name;
	
	@Override
	public String toString() {
		return "SampleData{" +
			"name='" + name + '\'' +
			'}';
	}
}
