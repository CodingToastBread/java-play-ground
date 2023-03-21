package coding.toast.bread.http_client_api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriBuilder;

/**
 * This is a test class that tests the Java built-in Class, HttpUrlConnection class.
 * The endpoints used here are all provided by <a href="https://jsonplaceholder.typicode.com/">jsonplaceholder</a>.<br><br>
 * Here are the endpoints I used:
 * <ul>
 *     <li>GET: https://jsonplaceholder.typicode.com/posts/1</li>
 *     <li>POST: https://jsonplaceholder.typicode.com/posts (content-type: application/json)</li>
 *     <li>PUT: https://jsonplaceholder.typicode.com/posts/1 (content-type: application/json)</li></li>
 *     <li>DELETE: https://jsonplaceholder.typicode.com/posts/1</li>
 * </ul>
 * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/net/URLConnection.html#connect()">URLConnection</a>
 */
@Slf4j
public class JavaHttpUrlConnectionTests {

    @Test
    void getMethodTest() {
        log.info("working on it !!!");

    }

    @Test
    void postMethodTest() {
        log.info("working on it !!!");

    }

    @Test
    void putMethodTest() {
        log.info("working on it !!!");

    }

    @Test
    void deleteMethodTest() {
        log.info("working on it !!!");

    }
}
