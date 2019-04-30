package core.policy.security;

import core.models.LoginRequest;
import core.queries.QueryLayer;
import core.queries.QueryLayerFactory;

public class NConsecutiveFailedLoginsSecurityPolicy implements SecurityPolicy{

	private int maxFailures;
	
	public NConsecutiveFailedLoginsSecurityPolicy(int maxFailures) {
		this.maxFailures = maxFailures;
	}
	
	@Override
	public boolean handleRequest(LoginRequest req) {
		QueryLayer q = QueryLayerFactory.getInstance();
		String userName = req.loginDetails.userName;
		int failures = q.getNumFailedConnsecutiveByUser(userName);
		
		if(failures >= maxFailures) {
			q.blockUser(userName);
			return false;
		}
		
		return true;
	}
	
	public boolean equals(Object o) {
		if(o instanceof NConsecutiveFailedLoginsSecurityPolicy) {
			NConsecutiveFailedLoginsSecurityPolicy other = (NConsecutiveFailedLoginsSecurityPolicy)o;
			return other.maxFailures == this.maxFailures;
		}
		return false;
	}

}
