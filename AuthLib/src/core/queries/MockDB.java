package core.queries;

import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import core.models.LoginDetails;
import core.models.LoginRequest;
import core.models.Session;

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
	
	protected List<LoginRequest> blockedSignatures;
	protected List<String> blockedUsernames;
	
	protected HashMap<LoginRequest, List<Instant>> failedBySignature;
	protected HashMap<String,Integer> consecutiveFailedByUser;
	protected HashMap<String,String> registeredUsers;
	protected HashMap<String,Session> userSessions;
	
	
	public MockDB() {
		blockedSignatures = new ArrayList<LoginRequest>();
		blockedUsernames = new ArrayList<String>();
		failedBySignature = new HashMap<LoginRequest,List<Instant>>();
		consecutiveFailedByUser = new HashMap<String,Integer>();
		registeredUsers = new HashMap<String,String>();
		userSessions = new HashMap<String,Session>();
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
		
		List<Instant> fails = failedBySignature.get(signature);
		Instant now = Instant.now().minus(xMins,ChronoUnit.MINUTES);
		int count = 0;
		for(Instant then : fails) {
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
	public void removeSession(String username) {
		userSessions.remove(username);
	}

	@Override
	public boolean registerUser(LoginRequest req) {
		LoginDetails details = req.loginDetails;
		this.registeredUsers.put(details.userName, details.encryptedPassword);
		return true;// boolean is place holder for exceptions etc later on...
	}

	@Override
	public Session getUserSession(String username) {
		if(userSessions.containsKey(username))
			return userSessions.get(username);
		return null;//should really be null object...
	}

	@Override
	public Session createSession(LoginRequest req) {
		//Assumes this is called only after verifying login details
		Session s = new Session(req);
		userSessions.put(req.loginDetails.userName,s);
		return s;// boolean is place holder for exceptions etc later on...
	}

	@Override
	public boolean isUserRegistered(String username) {
		return registeredUsers.containsKey(username);
	}

	@Override
	public String getPasswordHash(String username) {
		return registeredUsers.get(username);
	}

	@Override
	public void resetConsecutiveFailedByUser(String username) {
		if(consecutiveFailedByUser.containsKey(username)) {
			consecutiveFailedByUser.put(username, 0);
		}
	}

	@Override
	public void recordConsecutiveFailedLogonForUser(String username) {
		if(consecutiveFailedByUser.containsKey(username)) {
			consecutiveFailedByUser.put(username, consecutiveFailedByUser.get(username).intValue() +1);
		}
		else{
			consecutiveFailedByUser.put(username, 0);
		}
	}

	@Override
	public void recordFailedLogonFromSignature(LoginRequest signature) {
		if(failedBySignature.containsKey(signature)) {
			failedBySignature.get(signature).add(signature.dateTime);
		}
		else {
			failedBySignature.put(signature, new ArrayList<Instant>());
		}
	}
	
	
}
