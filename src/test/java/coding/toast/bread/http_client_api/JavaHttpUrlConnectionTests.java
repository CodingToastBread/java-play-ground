package coding.toast.bread.http_client_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    // predefine Http Request body contents
    private static final Map<String, ?> httpBody = Map.of(
        "title", "toast bread",
        "body", "toast bread responseBody",
        "userId", 2314
    );
    
    private static void setDefaultUrlConnection(HttpURLConnection urlConnection, String method) throws IOException {
        urlConnection.setRequestMethod(method);
        urlConnection.setConnectTimeout(2000);
        urlConnection.setReadTimeout(2000);
        urlConnection.setRequestProperty("key", "val");

        // HttpUrlConnection supports only Http/1.1.
        // so, if you want to suppress network connection latency,
        // you need to add headers (Connection: keep-alive)
        urlConnection.setRequestProperty("Connection", "keep-alive");
        urlConnection.setInstanceFollowRedirects(true);
    }
    
    @Test
    void getMethodTest() throws IOException {
        log.info("working on it !!!");
        URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        setDefaultUrlConnection(urlConnection, "GET");
        
        
        int responseCode = urlConnection.getResponseCode();
    
        // You can explicitly establish a connection with an external server by calling the connect method,
        // but the following methods also implicitly attempt to establish a connection with an external server.
        //
        // - urlConnection.getContentLength();
        // - getResponseCode(),
        // - connect(),
        // - getInputStream()
        // - getOutputStream()
    
        System.out.println("responseCode = " + responseCode);
        
        // get response headers
        Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
    
        InputStream is;
        if (responseCode / 100 == 2) {
            is = urlConnection.getInputStream();
        } else {
            is = urlConnection.getErrorStream();
        }
    
        // get response body
        StringBuilder sb = new StringBuilder();
        try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader bfr = new BufferedReader(isr);
        ) {
            String line;
            while ((line = bfr.readLine()) != null) {
                sb.append(line);
            }
        }
    
        urlConnection.disconnect();

        // need to copy them to another? use StreamUtils.copy(inputStream, outputStream)
        // StreamUtils.copy()
        
        System.out.println("responseHeader = " + headerFields);
        System.out.println("responseBody = " + sb.toString());
        
    }
    
    @Test
    void getResponseStreamAndTransferToOutputStream() {
    
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
