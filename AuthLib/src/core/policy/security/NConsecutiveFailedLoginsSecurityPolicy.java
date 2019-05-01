package core.policy.security;

import core.models.LoginRequest;
import core.queries.QueryLayer;
import core.queries.QueryLayerFactory;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NConsecutiveFailedLoginsSecurityPolicy implements SecurityPolicy{
	public static final Logger log = LogManager.getLogger(NConsecutiveFailedLoginsSecurityPolicy.class);
	private int maxFailures;
	
	public NConsecutiveFailedLoginsSecurityPolicy(int maxFailures) {
		this.maxFailures = maxFailures;
	}
	
	@Override
	public boolean handleRequest(LoginRequest req) {
		QueryLayer q = QueryLayerFactory.getInstance();
		String userName = req.loginDetails.userName;
		int failures = q.getNumFailedConnsecutiveByUser(userName);
		
		if(!q.isUserNameLocked(userName) && failures >= maxFailures) {
			log.info("{} consecutive failed logins for {}. Locking account - {} - {} - {} - {}" ,maxFailures,userName,req.requestID,req.address,req.userAgent,req.cookie);
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
