package core.policy.security;

import core.models.LoginRequest;
import core.queries.QueryLayer;
import core.queries.QueryLayerFactory;

public class BasicBruteForce implements SecurityPolicy {

	private int xMins;
	private int threshold;
	
	public BasicBruteForce(int xMins, int threshold) {
		this.xMins = xMins;
		this.threshold = threshold;
	}
	
	@Override
	public void handleRequest(LoginRequest req) {
		QueryLayer q = QueryLayerFactory.getInstance();
		int numFailed = q.getNumFailedByRequestInLastXMins(req, xMins);
		if( numFailed >= threshold) {
			q.blockSignature(req);
		}
	}

}