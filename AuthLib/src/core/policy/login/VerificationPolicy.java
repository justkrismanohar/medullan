package core.policy.login;

import core.models.LoginRequest;

public interface VerificationPolicy {
	public boolean verifyLoginDetails(LoginRequest req);
}
