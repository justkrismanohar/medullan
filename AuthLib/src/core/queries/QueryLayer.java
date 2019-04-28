package core.queries;


import core.models.LoginDetails;
import core.models.LoginRequest;
import core.models.Session;

public interface QueryLayer {
	
	public boolean registerUser(LoginRequest details);
	//public boolean verifyLoginDetails(LoginRequest details);//maybe put this one level of abastraction up...
	
	public boolean isUserRegistered(String username);
	public String getPasswordHash(String username);
	public void resetConsecutiveFailedByUser(String username);
	public void recordConsecutiveFailedLogonForUser(String username);
	public void recordFailedLogonFromSignature(LoginRequest signature);
	
	public void blockUser(String userName);
	public void blockSignature(LoginRequest signature);
	
	public void unblockSignature(LoginRequest signature);
	
	public boolean isSignaturetInBlockList(LoginRequest signature);
	public boolean isUserNameLocked(String userName);
	public int getNumFailedConnsecutiveByUser(String userName);
	public int getNumFailedByRequestInLastXMins(LoginRequest signature, int xMins);
	
	public Session getUserSession(String username);
	public boolean createSession(LoginRequest req);
	
}
