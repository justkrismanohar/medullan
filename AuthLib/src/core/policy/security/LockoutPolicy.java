package core.policy.security;

import java.time.LocalTime;

import core.models.LoginRequest;
import core.queries.QueryLayer;
import core.queries.QueryLayerFactory;

public class LockoutPolicy implements SecurityPolicy{

	
	/*
	 * This implements an event based unlocking of previously block signatures.
	 * Simply get the time from the currently blocked signature and compare
	 */
	@Override
	public boolean handleRequest(LoginRequest req) {
		String userName = req.loginDetails.userName;
		QueryLayer q = QueryLayerFactory.getInstance();
		if(q.isUserNameLocked(userName)) {
			//Determine if to unblock
			boolean unblock = true;
			if(unblock)
				q.unBlockUser(userName);
		}
		
		
		return false;
	}
	
}
