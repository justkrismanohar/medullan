package core.queries;

import core.models.LoginRequest;

public interface QueryLayer {
	
	public void blockSignature(LoginRequest signature);
	/*
	 * This method check should implement an event based unlocking of previously block signatures.
	 * Simply get the time from the currently blocked signature and compare
	 */
	public boolean isSignaturetInBlockList(LoginRequest signature);
	public boolean isUserNameLocked(String userName);
	public int getNumFailedConnsecutiveByUser(String userName);
	public int getNumFailedByRequestInLastXMins(LoginRequest signature, int xMins);
}
