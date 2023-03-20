package coding.toast.bread.http_client_api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.fail;

/**
 * This is a test class that tests the Java built-in Http Client API.<br>
 * The endpoints used here are all provided by <a href="https://jsonplaceholder.typicode.com/">jsonplaceholder</a>.<br><br>
 * Here are the endpoints I used:
 * <ul>
 *     <li>GET: https://jsonplaceholder.typicode.com/posts/1</li>
 *     <li>POST: https://jsonplaceholder.typicode.com/posts (content-type: application/json)</li>
 *     <li>PATCH: https://jsonplaceholder.typicode.com/posts/1 (content-type: application/json)</li></li>
 *     <li>DELETE: https://jsonplaceholder.typicode.com/posts/1</li>
 * </ul>
 * @see <a href="https://jsonplaceholder.typicode.com/guide/">jsonplaceholder guide</a>
 * @see <a href="https://www.baeldung.com/java-9-http-client">baeldung - java 9 http client</a>
 * @see <a href="https://openjdk.org/groups/net/httpclient/recipes.html">openjdk - httpclient recipes</a>
 */
@Slf4j
public class JavaHttpClientTests {



    @Test
    @DisplayName("Retrieve data using HTTP GET method")
    void HttpClientGetMethodTest() {

        //Setting Http Request...
        HttpRequest getMethodRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                .GET()
                .headers(
                        "Content-Type", "application/json",
                        "Accept", "application/json;charset=UTF-8"
                )
                .timeout(Duration.ofSeconds(2))
                .version(HttpClient.Version.HTTP_2)
                .build();


        // remark: if you need to append complicate query string on url,
        // use Spring's UriComponentsBuilder!
        // ==> UriComponentsBuilder.fromUri(URI.create("https://jsonplaceholder.typicode.com/posts"))
        // ==>         .queryParam("id", 1)
        // ==>         .queryParam("userName", "toastBread")
        // ==>         .build().toUri();


        // Create a new Http Client using Http Request Object you just made
        HttpClient httpClient = HttpClient.newBuilder()
                .authenticator(new Authenticator() { // send basic authentication info (if you need to)
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("id", "password".toCharArray());
                    }
                })
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        try {
            HttpResponse<String> httpResponse = httpClient.send(getMethodRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            assertThat(httpResponse.statusCode()).isEqualTo(200);
            String body = httpResponse.body();
            log.info("http response body:\n {}", body);
            assertThat(body.contains("userId")).isTrue();
            assertThat(body.contains("id")).isTrue();
            assertThat(body.contains("title")).isTrue();
            assertThat(body.contains("body")).isTrue();
        } catch (IOException | InterruptedException e) {
            fail(e.getMessage());
        }

    }

    @Test
    @DisplayName("Send data using HTTP POST method")
    void HttpClientPostMethodTest() {
        log.info("Working in progress...");
        //Setting Http Request...



        // Create a new Http Client using Http Request Object you just made


        // send http request and retrieve the data from the web api
    }

    @Test
    @DisplayName("Send data using HTTP PUT method")
    void HttpClientPutMethodTest() {
        log.info("Working in progress...");
        //Setting Http Request...


        // Create a new Http Client using Http Request Object you just made


        // send http request and retrieve the data from the web api
    }

    @Test
    @DisplayName("Send HTTP DELETE request")
    void HttpClientDeleteMethodTest() {
        log.info("Working in progress...");
        //Setting Http Request...


        // Create a new Http Client using Http Request Object you just made


        // send http request and retrieve the data from the web api
    }
}
