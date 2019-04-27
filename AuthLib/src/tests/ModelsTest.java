package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;

import core.models.IP;
import core.models.IP.IPCreationFailed;

class ModelsTest {

	@Test
	void testIPCreation() throws IPCreationFailed {
		IP other = new IP("127.0.0.1");
		IP address = new IP("127.0.0.1");
		assertTrue(address.equals(other));
	}

}
