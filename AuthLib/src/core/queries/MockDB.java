package core.queries;

import core.models.LoginRequest;

public class MockDB implements QueryLayer {
	
	@Override
	public int getNumFailedConnsecutiveByUser(String userName) {
		return 3;
	}

	@Override
	public int getNumFailedByRequestInLastXMins(LoginRequest signature, int xMins) {
		return 13;
	}

	@Override
	public boolean isSignaturetInBlockList(LoginRequest signature) {
		return false;
	}

	@Override
	public boolean isUserNameLocked(String userName) {
		return false;
	}

}
