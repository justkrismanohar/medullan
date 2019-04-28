package core.policy.login;

import java.time.LocalTime;
import java.util.ArrayList;

import core.models.LoginRequest;
import core.queries.QueryLayer;
import core.queries.QueryLayerFactory;

public class BasicVerification implements VerificationPolicy{

	@Override
	public boolean verifyLoginDetails(LoginRequest req) {
		String username = req.loginDetails.userName;
		QueryLayer q = QueryLayerFactory.getInstance();
		
		boolean isRegistered = q.isUserRegistered(username);
		
		if(isRegistered) {
			String encryptedPassworedStored = q.getPasswordHash(username);
			if(req.loginDetails.encryptedPassword.equals(encryptedPassworedStored)) {
				q.resetConsecutiveFailedByUser(username);
				//register session
				return true;
			}
		}
		
		//update failure stats
		if(isRegistered) q.recordConsecutiveFailedLogonForUser(username);
		q.recordFailedLogonFromSignature(req);
		
		return false;
	}

}
