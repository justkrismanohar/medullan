package core.queries;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
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
	
	private HashMap<LoginRequest, List<LocalTime>> failedBySignature;
	private HashMap<String,Integer> consecutiveFailedByUser;
	private HashMap<String,String> registeredUsers;
	
	
	public MockDB() {
		blockedSignatures = new ArrayList<LoginRequest>();
		blockedUsernames = new ArrayList<String>();
		failedBySignature = new HashMap<LoginRequest,List<LocalTime>>();
		consecutiveFailedByUser = new HashMap<String,Integer>();
		registeredUsers = new HashMap<String,String>();
	}
	
	@Override
	public int getNumFailedConnsecutiveByUser(String userName) {
		try{
			return consecutiveFailedByUser.get(userName).intValue();
		}
		catch(Throwable t) {
			return 0;//not in hashmap
		}
	}

	@Override
	public int getNumFailedByRequestInLastXMins(LoginRequest signature, int xMins) {
		if(!failedBySignature.containsKey(signature))
			return 0;
		
		List<LocalTime> fails = failedBySignature.get(signature);
		LocalTime now = LocalTime.now().minusMinutes(xMins);
		int count = 0;
		for(LocalTime then : fails) {
			if(now.compareTo(then) < 0)//after minusing if now occurs before then, it means then is within xMins of now
				count++;
		}
		return count;
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
		if(!blockedSignatures.contains(signature)) 
				blockedSignatures.add(signature);
	}
	

	@Override
	public void blockUser(String userName) {
		if(registeredUsers.containsKey(userName) && !blockedUsernames.contains(userName))
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
	public boolean verifyLoginDetails(LoginRequest req) {
		String un = req.loginDetails.userName;
		boolean isRegistered = registeredUsers.containsKey(un);
		if(isRegistered) {
			String encryptedPassworedStored = registeredUsers.get(un);
			if(req.loginDetails.encryptedPassword.equals(encryptedPassworedStored)) {
				if(consecutiveFailedByUser.containsKey(un)) {
					consecutiveFailedByUser.put(un, 0);
				}
				return true;
			}
		}

		
		//update failure stats
		if(isRegistered) {
			if(consecutiveFailedByUser.containsKey(un)) {
				consecutiveFailedByUser.put(un, consecutiveFailedByUser.get(un).intValue() +1);
			}
			else{
				consecutiveFailedByUser.put(un, 0);
			}
		}
		
		if(failedBySignature.containsKey(req)) {
			failedBySignature.get(req).add(req.dateTime);
		}
		else {
			failedBySignature.put(req, new ArrayList<LocalTime>());
		}
		
		return false;
		
	}

	@Override
	public boolean registerUser(LoginDetails details) {
		this.registeredUsers.put(details.userName, details.encryptedPassword);
		return true;// boolean is place holder for exceptions etc later on...
	}
}
