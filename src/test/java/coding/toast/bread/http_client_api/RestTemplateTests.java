package coding.toast.bread.http_client_api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Collections.list;
import static java.util.Collections.singletonList;
import static org.springframework.http.HttpEntity.EMPTY;
import static org.springframework.http.HttpMethod.GET;

/**
 * Testing RestTemplate API
 */
@Slf4j
public class RestTemplateTests {
	
	private static final RestTemplate restTemplate = new RestTemplate();
	private static final String API_BASE_URL = "https://jsonplaceholder.typicode.com/posts/";
	private static final UriComponentsBuilder URI_COMPONENTS = UriComponentsBuilder.fromUriString(API_BASE_URL);
	
	private static UriComponentsBuilder getCopyBaseURI() {
		return URI_COMPONENTS.cloneBuilder();
	}
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	
	// Jackson is Compatible with Java Record class!
	record Posts(long userId, long id, String title, String body) { }
	
	@Test
	void getMethodTest() {
		
		// create request uri
		URI uri = getCopyBaseURI().path("1").build().toUri();
		
		
		ResponseEntity<Posts> entity = restTemplate.getForEntity(uri, Posts.class);
		printEntity(entity);
		
		
		// if you want to add header, use exchange!
		HttpHeaders httpHeaders = new HttpHeaders();
		
		
		// ==> byte[] authByte = "id:password".getBytes(StandardCharsets.UTF_8);
		// ==> String dataString =  Base64.getEncoder().encodeToString(authByte);
		// ==> httpHeaders.setBasicAuth(HttpHeaders.AUTHORIZATION, "Basic %s".formatted(dataString));
		// ==> the code above can be shortened. look the code below:
		httpHeaders.setBasicAuth("id", "password");
		
		// the parameterized type represents the http payload type
		HttpEntity<Void> requestHttp = new HttpEntity<>(httpHeaders);
		
		
		ResponseEntity<Posts> exchange = restTemplate.exchange(uri, GET, requestHttp, Posts.class);
		printEntity(exchange);
		
		// exchange is very handy when you have to convert complex Type Converting.
		// look at the code below:
		ResponseEntity<List<Posts>> listOfPosts
			= restTemplate.exchange(getCopyBaseURI().build().toUri(), GET, EMPTY, new ParameterizedTypeReference<List<Posts>>() {});
		log.debug("get First Posts : {}", listOfPosts.getBody().get(0));
		
		
		// if you need only Http Response Body and don't need
		// any HTTP Header, StatusCode, etc... then just use "getForObject".
		Posts forObject = restTemplate.getForObject(uri, Posts.class);
		log.debug("restTemplate.getForObject : {}", forObject);
		
		// if you don't have any POJO to Convert the Http Response Body,
		// use "com.fasterxml.jackson.databind.JsonNode"!
		JsonNode jsonNode = restTemplate.getForObject(uri, JsonNode.class);
		log.debug("jsonNode Print : {}", jsonNode);
		
	}
	
	/**
	 * printing ResponseEntity
	 * @param entity printing target
	 */
	private void printEntity(ResponseEntity<?> entity) {
		printEntityCaptureResolver(entity);
	}
	
	/**
	 * escape from capture error
	 */
	private <T> void printEntityCaptureResolver(ResponseEntity<T> entity) {
		log.debug("result entity status code : {}", entity.getStatusCode());
		log.debug("result entity headers : {}", entity.getHeaders());
		log.debug("result entity body : {}\n", entity.getBody());
	}
}
