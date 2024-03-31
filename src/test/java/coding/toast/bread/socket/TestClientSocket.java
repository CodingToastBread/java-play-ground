package coding.toast.bread.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class TestClientSocket {
	public static void main(String[] args) {
		
		try (
			Socket socket = new Socket("127.0.0.1", 9000);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))
		) {
			// User Input
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String userInput;
			
			// stdIn will be blocked unless you send some text to this program console
			while ((userInput = stdIn.readLine()) != null) {
				if ("exit".equalsIgnoreCase(userInput)) {
					System.out.println("exit client Socket Program");
					System.exit(0);
				}
				
				// send User Input To Server Socket
				out.println(userInput);
				
				// get Response From Server Socket
				System.out.println("echo: " + in.readLine());
			}
		} catch (UnknownHostException e) {
			System.err.println("No Host Found");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("IO Connection Failed!");
			System.exit(1);
		}
	}
}
