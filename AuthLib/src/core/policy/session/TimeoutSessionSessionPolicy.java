package core.policy.session;

import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

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
		
		Instant validTime = Instant.now().minus(timeout,ChronoUnit.MINUTES);
		
		if(validTime.compareTo(current.creationTime) < 0)
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
