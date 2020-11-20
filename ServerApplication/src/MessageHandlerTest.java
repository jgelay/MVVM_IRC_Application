import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class MessageHandlerTest {
	private MessageHandler m;
	private ClientHandler c;
	
	@BeforeEach
	void setup() {
		m = new MessageHandler(c);
	}
	
	@Test
	void test() {
		//assertTrue(m.testMessage("PASS Test\r\n"));
		//assertTrue(m.testMessage("NICK Wiz\r\n"));
		//assertTrue(m.testMessage("USER guest 0 * :Ronnie Regan\r\n"));
		//assertTrue(m.testMessage("Hello World\r\n"));
		//assertTrue(m.testNick("JOSH"));
		//assertTrue(m.testPrefix("JOSH"));
		//assertTrue(m.testMessage(":JOSH PRIVMSG #default Test\r\n"));
		assertTrue(m.testChannel("#default"));
		//assertTrue(m.testChannelString("A"));
	}

}
