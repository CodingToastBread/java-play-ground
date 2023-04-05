package coding.toast.bread.converting.vo;

import lombok.Builder;

import java.util.List;

@Builder
public record Person(String name, Integer age, List<String> favoriteFood) {
}
