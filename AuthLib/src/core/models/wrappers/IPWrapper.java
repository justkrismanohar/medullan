package core.models.wrappers;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author krism
 * A simple wrapper for IP address
 * In most cases will use the InetAddress under the hood,
 * but it can be swapped out for something else later, if necessary
 */

public class IPWrapper {
	
	public class IPCreationFailed extends Exception{
		public IPCreationFailed(String message) {
			super(message);
		}
	}
	
	private InetAddress address;
	
	public IPWrapper(String name) throws IPCreationFailed{
		
		try {
			address = InetAddress.getByName(name);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IPCreationFailed(t.getMessage());
		}
	}
	
	public boolean equals(IPWrapper other) {
		return other.address.getHostAddress().equals(this.address.getHostAddress());
	}
	
	public String toString() {
		return address.getHostAddress();
	}
}
