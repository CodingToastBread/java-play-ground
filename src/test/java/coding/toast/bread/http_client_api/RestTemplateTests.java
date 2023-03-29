package coding.toast.bread.http_client_api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
public class RestTemplateTests {
	
	private static final RestTemplate restTemplate = new RestTemplate();
	private static final String API_BASE_URL = "https://jsonplaceholder.typicode.com/posts/";
	private static final UriComponentsBuilder URI_COMPONENTS = UriComponentsBuilder.fromUriString(API_BASE_URL);
	
	@Test
	void getMethodTest() {
		
		URI uri = URI_COMPONENTS.path("1").build().toUri();
		
		ResponseEntity<JsonNode> entity = restTemplate.getForEntity(
			uri,
			JsonNode.class
		);
		
		printEntity(entity);
		
	}
	
	private void printEntity(ResponseEntity<?> entity) {
		log.info("result entity status code : {}", entity.getStatusCode());
		log.info("result entity headers : {}",entity.getHeaders());
		log.info("result entity body : {}",entity.getBody());
	}
}
