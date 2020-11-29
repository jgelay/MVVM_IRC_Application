package main;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientHandlerTest {
	private ClientHandler c;
	private ChannelManager cm = new ChannelManager();
	
	@BeforeEach
	void setup() {
		c = new ClientHandler(null,cm);
	}
	@Test
	void test() {
		assertEquals(c.client_Registration("TESTING Joe John\r\n"),"001");
	}

}
