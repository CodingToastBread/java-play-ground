package coding.toast.bread.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Very Simple Echo Server Class For Test
 */
public class TestServerSocket {
	
	public static void main(String[] args) {
		
		try {
			try (ServerSocket serverSocket = new ServerSocket(9000)) {
				// Keep listen to Server Socket!
				while (true) {
					// serverSocket.accept(); will be blocked,
					// unless you get a Request To This Server Socket.
					// after you get Request, then the Server Socket creates new
					// Socket to Communicate with the client Request
					System.out.println("Waiting For New Socket Connection!!");
					Socket clientCommunicateSocket = serverSocket.accept();
					System.out.println("Detect Request, Create New Communication Socket ");
					InetAddress inetAddress = clientCommunicateSocket.getInetAddress();
					System.out.println("inetAddress = " + inetAddress);
					
					// one Task => one Virtual Thread!
					Thread.ofVirtual().start(() -> {
						try (
							// read Request Strings
							BufferedReader in = new BufferedReader(new InputStreamReader(clientCommunicateSocket.getInputStream(), StandardCharsets.UTF_8));
							// write Response to Request Client
							PrintWriter out = new PrintWriter(clientCommunicateSocket.getOutputStream(), true, StandardCharsets.UTF_8)
						) {
							String readLine;
							// while the Client Socket is connected,
							// it will keep the connection. so if the connection socket exit,
							// this in.readLine will throw Exception. and break the while loop.
							while ((readLine = in.readLine()) != null) {
								System.out.printf("%s got message!%n" +
									"message => %s%n%n", Thread.currentThread(), readLine);
								out.println(readLine);
							}
						} catch (IOException e) {
							System.err.println("Server Socket Communication Exception Occured!\n" +
								"==> " + e.getMessage());
						}
					});
				}
			}
		} catch (IOException e) {
			System.err.println("The ServerSocket Creation Failed! " + e.getMessage());
			System.exit(-1);
		}
		
	}
}
