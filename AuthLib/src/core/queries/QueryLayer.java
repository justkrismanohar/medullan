package core.queries;


import core.models.LoginDetails;
import core.models.LoginRequest;

public interface QueryLayer {
	
	public boolean registerUser(LoginRequest details);
	public boolean verifyLoginDetails(LoginRequest details);
	
	public void blockUser(String userName);
	public void blockSignature(LoginRequest signature);
	
	public void unblockSignature(LoginRequest signature);
	
	public boolean isSignaturetInBlockList(LoginRequest signature);
	public boolean isUserNameLocked(String userName);
	public int getNumFailedConnsecutiveByUser(String userName);
	public int getNumFailedByRequestInLastXMins(LoginRequest signature, int xMins);
}
