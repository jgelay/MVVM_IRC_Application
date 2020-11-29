package main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {

	public static void main(String[] args) throws IOException {
		int PORT = 11009;
		Server server = new Server(PORT, 1);
		server.accept_clients();
		
	}
	


}
