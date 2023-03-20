package coding.toast.bread.http_client_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.fail;

/**
 * This is a test class that tests the Java built-in Http Client API.<br>
 * The endpoints used here are all provided by <a href="https://jsonplaceholder.typicode.com/">jsonplaceholder</a>.<br><br>
 * Here are the endpoints I used:
 * <ul>
 *     <li>GET: https://jsonplaceholder.typicode.com/posts/1</li>
 *     <li>POST: https://jsonplaceholder.typicode.com/posts (content-type: application/json)</li>
 *     <li>PUT: https://jsonplaceholder.typicode.com/posts/1 (content-type: application/json)</li></li>
 *     <li>DELETE: https://jsonplaceholder.typicode.com/posts/1</li>
 * </ul>
 * @see <a href="https://jsonplaceholder.typicode.com/guide/">jsonplaceholder guide</a>
 * @see <a href="https://www.baeldung.com/java-9-http-client">baeldung - java 9 http client</a>
 * @see <a href="https://openjdk.org/groups/net/httpclient/recipes.html">openjdk - httpclient recipes</a>
 */
@Slf4j
public class JavaHttpClientTests {


    private static final ObjectMapper mapper = new ObjectMapper();
    
    // predefine Http Request body contents
    private static final Map<String, ?> httpBody = Map.of(
        "title", "toast bread",
        "body", "toast bread responseBody",
        "userId", 2314
    );

    @Test
    @DisplayName("Retrieve data using HTTP GET method")
    void HttpClientGetMethodTest() {

        //Setting Http Request...
        HttpRequest getMethodRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                .GET()
                .headers(
                    "Content-Type", "application/json",
                    "Accept", "application/json;charset=UTF-8")
                .timeout(Duration.ofSeconds(2))
                .version(HttpClient.Version.HTTP_2)
                .build();


        // remark: if you need to append complicate query string on url,
        // use Spring's UriComponentsBuilder!
        // ==> UriComponentsBuilder.fromUri(URI.create("https://jsonplaceholder.typicode.com/posts"))
        // ==>         .queryParam("id", 1)
        // ==>         .queryParam("userName", "toastBread")
        // ==>         .build().toUri();

        // the code
        HttpClient httpClient = HttpClient.newHttpClient();
        try {
            
            HttpResponse<String> httpResponse
                = httpClient.send(getMethodRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            
            // just test whether http status code is 200 or not
            assertThat(httpResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            String body = httpResponse.body();
            log.info("http response body:\n {}", body);
        } catch (IOException | InterruptedException e) {
            fail(e.getMessage());
        }

    }

    @Test
    @DisplayName("Send data using HTTP POST method")
    void HttpClientPostMethodTest() throws IOException, InterruptedException {
        
        // creating HttpRequest
        HttpRequest postHttpMethodRequest = HttpRequest.newBuilder()
            .header("Content-Type", "application/json;charset=UTF-8")
            // .header("Accept", "application/json")
            .timeout(Duration.ofSeconds(2))
            .version(HttpClient.Version.HTTP_2)
            // .POST(noBody()) // if you want nothing to send on the http responseBody...
            .POST(ofString(mapper.writeValueAsString(httpBody)))
            .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
            .build();
    
        
        // create HttpClient and send request
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> httpResponse
            = httpClient.send(postHttpMethodRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        
        // log response headers and body.
        log.info("http headers after Post Method Request:\n{}", httpResponse.headers());
        log.info("httpBody after Post method request:\n{}", httpResponse.body());
        
        
        // test~ test~
        assertThat(httpResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        testBody(httpResponse.body(), httpBody);
        
        
        
        // you can get a headers map~
        HttpHeaders headers = httpResponse.headers();
        Map<String, List<String>> map = headers.map();
        String location1 = map.get("location").get(0);
        
        // or you can read it directly from headers object
        String location = headers.firstValue("location").orElse("");
        
        // checking created resource location.
        assertThat(location).isEqualTo("http://jsonplaceholder.typicode.com/posts/101");
    
    }
    
    // testing Json Body key, value
    private static void testBody(String responseBody, Map<String, ?> mapHttpBody) throws JsonProcessingException {
        
        JsonNode responseNode = mapper.readValue(responseBody, ObjectNode.class);
        ObjectNode compareNode = mapper.convertValue(mapHttpBody, ObjectNode.class);
        
        System.out.println("compareNode = " + compareNode);
        
        assertThat(responseNode.path("title").asText())
            .isEqualTo(compareNode.path("title").asText());
        
        assertThat(responseNode.path("body").asText())
            .isEqualTo(compareNode.path("body").asText());
        
        assertThat(responseNode.path("userId").asText())
            .isEqualTo(compareNode.path("userId").asText());
    }
    
    @Test
    @DisplayName("Send data using HTTP PUT method")
    void HttpClientPutMethodTest() throws IOException, InterruptedException {
        // REMARK: there is no PATCH (WHY?). if you want to use PATCH
        //          ==> .method("PATCH", ...)
    
        HttpRequest putMethodRequest = HttpRequest.newBuilder()
            .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
            .header("Content-Type", "application/json")
            .timeout(Duration.ofSeconds(2))
            .PUT(ofString(mapper.writeValueAsString(httpBody)))
            .build();
        
        // Create a new Http Client using Http Request Object you just made
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> httpResponse
            = httpClient.send(putMethodRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    
        System.out.println(httpResponse.headers());
        System.out.println(httpResponse.body());
    
        assertThat(httpResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Send HTTP DELETE request")
    void HttpClientDeleteMethodTest() throws IOException, InterruptedException {
        
        //Setting Http Request...
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
            .DELETE().build();
    
    
        // Create a new Http Client using Http Request Object you just made
        HttpResponse<Void> httpResponse
            = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.discarding()); // nothing to get
    
        assertThat(httpResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
