package core.policy.session;

import java.time.LocalTime;

import core.models.Session;
import core.queries.QueryLayer;
import core.queries.QueryLayerFactory;

public class TimeoutSessionSessionPolicy implements SessionPolicy{

	private int timeout;
	
	public TimeoutSessionSessionPolicy(int timeout) {
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
		if(other instanceof TimeoutSessionSessionPolicy) {
			TimeoutSessionSessionPolicy t = (TimeoutSessionSessionPolicy )other;
			return t.timeout == this.timeout;
		}
		return false;
	}

}
