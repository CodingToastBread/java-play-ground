package coding.toast.bread.http_client_api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * This is a test class that tests the Java built-in Class, HttpUrlConnection class.
 * The endpoints used here are all provided by <a href="https://jsonplaceholder.typicode.com/">jsonplaceholder</a>.<br><br>
 * Here are the endpoints I used:
 * <ul>
 *     <li>GET: https://jsonplaceholder.typicode.com/posts/1</li>
 *     <li>POST: https://jsonplaceholder.typicode.com/posts (content-type: application/json)</li>
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
    void postMethodTest() throws IOException {
    
        URL url = new URL("https://jsonplaceholder.typicode.com/posts/");
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        setDefaultUrlConnection(urlConnection, "POST");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");
        
        // ensure the connection will send Content
        urlConnection.setDoOutput(true);
        
        
        // connection occurs at this moment!
        try (DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream())) {
            String content = mapper.writeValueAsString(httpBody);
            System.out.println("content = " + content);
            wr.write(content.getBytes(StandardCharsets.UTF_8));
        }
        
        // actually, you can just write content using only ObjectMapper.
        // mapper.writeValue(urlConnection.getOutputStream(), httpBody);
    
        // Choice 1: Read Content without ObjectMapper
        /*
        StringBuilder bodyBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {

            String readLine = "";
            while ((readLine = br.readLine()) != null) {
                bodyBuilder.append(readLine);
            }
        }

        String responseBody = bodyBuilder.toString();
        */
    
        // Choice 2: Read Content with ObjectMapper
        JsonNode responseBody = mapper.readValue(urlConnection.getInputStream(), JsonNode.class);
        log.info("responseBody : {}", responseBody);
    
        urlConnection.disconnect();
        
    }

    // I think it is too verbose coding Put And Delete.
    // I'll just skip it! 😋
}
