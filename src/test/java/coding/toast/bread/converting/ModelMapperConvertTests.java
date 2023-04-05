package coding.toast.bread.converting;

import lombok.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelMapperConvertTests {
	
	
	@Getter
	@Builder @NoArgsConstructor @AllArgsConstructor
	public static class PersonVO {
		private String name;
		private int age;
		private int pageNo; // for paging request
	}
	
	
	@Data
	@Builder @NoArgsConstructor @AllArgsConstructor
	public static class PersonDTO {
		private String name;
		private int age;
		private int pageNo; // for paging request
	}
	
	private ModelMapper mapper;
	
	@BeforeEach
	void setUp() {
		this.mapper = new ModelMapper();
	}
	
	
	@Test
	void test2() {
		PersonVO personData = PersonVO.builder().name("toast bread").age(29).build();
		System.out.println("personData = " + personData);
		
		PersonDTO dto = this.mapper.map(personData, PersonDTO.class);
		System.out.println("dto = " + dto);
		assertThat(personData.getAge()).isEqualTo(dto.getAge());
	}
	
	
	
}
