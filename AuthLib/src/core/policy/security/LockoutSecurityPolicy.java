package core.policy.security;

import java.time.LocalTime;

import core.models.LoginRequest;
import core.queries.QueryLayer;
import core.queries.QueryLayerFactory;

public class LockoutSecurityPolicy implements SecurityPolicy{

	
	private long duration;
	
	public LockoutSecurityPolicy(long duration) {
		this.duration = duration;
	}
	
	/*
	 * This implements an event based unlocking of previously block signatures.
	 * Simply get the time from the currently blocked signature and compare
	 */
	@Override
	public boolean handleRequest(LoginRequest req) {
		String userName = req.loginDetails.userName;
		QueryLayer q = QueryLayerFactory.getInstance();
	
		if(q.isSignaturetInBlockList(req)) {
			//Determine if to unblock
			LocalTime XMinutesAgo = LocalTime.now().minusMinutes(duration);
			if(XMinutesAgo.compareTo(req.dateTime) > 0) {//After minusing XMins if now is still bigger this means more than Xmins has passed
				q.unblockSignature(req);;//duration passed unblock user
				return true;
			}
			
			return false;//user is still blocked
		}
		
		return true;
	}
	
	
	public boolean equals(Object o) {
		if(o instanceof LockoutSecurityPolicy) {
			LockoutSecurityPolicy other = (LockoutSecurityPolicy)o;
			return other.duration == this.duration;
		}
		return false;
	}

	
	
}
