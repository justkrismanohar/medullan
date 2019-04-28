package core.queries;

import java.util.ArrayList;
import java.util.List;

import core.models.LoginDetails;
import core.models.LoginRequest;

public class MockDB implements QueryLayer {
	private static MockDB mockDB = null;
	
	public static QueryLayer getInstance() {
		if(mockDB == null)
			mockDB = new MockDB();
		return mockDB;
	}
	
	/*
	 * So JUnitTests can configure particular MockDB
	 */
	public static void setMockDB(MockDB m) {
		mockDB = m;
	}
	
	private List<LoginRequest> blockedSignatures;
	private List<String> blockedUsernames;
	
	public MockDB() {
		blockedSignatures = new ArrayList<LoginRequest>();
		blockedUsernames = new ArrayList<String>();
	}
	
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
		for(LoginRequest r : blockedSignatures)
			if(r.equals(signature))
				return true;
		return false;
	}

	@Override
	public boolean isUserNameLocked(String userName) {
		for(String s : blockedUsernames)
			if(s.equals(userName))
				return true;
		return false;
	}

	@Override
	public void blockSignature(LoginRequest signature) {
		blockedSignatures.add(signature);
	}
	

	@Override
	public void blockUser(String userName) {
		this.blockedUsernames.add(userName);
	}

	public void resetBlockSignatures() {
		this.blockedSignatures.clear();
	}
	
	public void resetBlockUsernames() {
		this.blockedUsernames.clear();
	}

	@Override
	public void unblockSignature(LoginRequest signature) {
		this.blockedSignatures.remove(signature);
	}

	@Override
	public boolean verifyLoginDetails(LoginDetails details) {
		return details.userName.equals(details.encryptedPassword);
	}
}
