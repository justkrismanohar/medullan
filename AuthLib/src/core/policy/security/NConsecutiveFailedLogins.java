package core.policy.security;

import core.models.LoginRequest;
import core.queries.QueryLayer;
import core.queries.QueryLayerFactory;

public class NConsecutiveFailedLogins implements SecurityPolicy{

	private int maxFailures;
	
	public NConsecutiveFailedLogins(int maxFailures) {
		this.maxFailures = maxFailures;
	}
	
	@Override
	public void handleRequest(LoginRequest req) {
		QueryLayer q = QueryLayerFactory.getInstance();
		String userName = req.loginDetails.userName;
		int failures = q.getNumFailedConnsecutiveByUser(userName);
		
		if(failures >= maxFailures) {
			q.blockUser(userName);
		}
	}

}
