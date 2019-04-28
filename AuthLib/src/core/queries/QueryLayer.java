package core.queries;

import core.models.LoginRequest;

public interface QueryLayer {
	
	public void blockUser(String userName);
	public void blockSignature(LoginRequest signature);
	
	public void unblockSignature(LoginRequest signature);
	
	public boolean isSignaturetInBlockList(LoginRequest signature);
	public boolean isUserNameLocked(String userName);
	public int getNumFailedConnsecutiveByUser(String userName);
	public int getNumFailedByRequestInLastXMins(LoginRequest signature, int xMins);
}
