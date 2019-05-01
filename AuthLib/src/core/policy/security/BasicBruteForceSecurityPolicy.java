package core.policy.security;

import core.models.LoginRequest;
import core.queries.QueryLayer;
import core.queries.QueryLayerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BasicBruteForceSecurityPolicy implements SecurityPolicy {
	public static final Logger log = LogManager.getLogger(BasicBruteForceSecurityPolicy.class);
	
	private int xMins;
	private int threshold;
	
	public BasicBruteForceSecurityPolicy(int xMins, int threshold) {
		this.xMins = xMins;
		this.threshold = threshold;
	}
	
	@Override
	public boolean handleRequest(LoginRequest req) {
		QueryLayer q = QueryLayerFactory.getInstance();
		int numFailed = q.getNumFailedByRequestInLastXMins(req, xMins);
		if( numFailed >= threshold && !q.isSignaturetInBlockList(req)) {
			log.info("Brute Force Detected - Blocking Signature - {} - {} - {} - {}" ,req.requestID,req.address,req.userAgent,req.cookie);
			q.blockSignature(req);
			return false;
		}
		
		return true;
	}
	
	public boolean equals(Object o) {
		return o instanceof BasicBruteForceSecurityPolicy;
	}

}
