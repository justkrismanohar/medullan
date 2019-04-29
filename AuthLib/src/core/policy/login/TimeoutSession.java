package core.policy.login;

import java.time.LocalTime;

import core.models.Session;
import core.queries.QueryLayer;
import core.queries.QueryLayerFactory;

public class TimeoutSession implements SessionPolicy{

	private int timeout;
	
	public TimeoutSession(int timeout) {
		this.timeout = timeout;
	}
	
	@Override
	public boolean isValid(String username) {
		QueryLayer q = QueryLayerFactory.getInstance();
		Session current = q.getUserSession(username);
		if(current == null) return false;//no session for user
		
		LocalTime validTime = LocalTime.now().minusMinutes(timeout);
		
		if(validTime.compareTo(current.lastRequest.dateTime) < 0)
			return true;//There is still time for this session
		
		//Session expired 
		//remove it
		q.removeSession(username);
		return false;
	}
	
	public boolean equals(Object other) {
		if(other instanceof TimeoutSession) {
			TimeoutSession t = (TimeoutSession )other;
			return t.timeout == this.timeout;
		}
		return false;
	}

}
