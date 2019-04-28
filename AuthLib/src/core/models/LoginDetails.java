package core.models;

import core.queries.QueryLayerFactory;

public class LoginDetails {
	public String encryptedPassword;
	public String userName;
	
	
	public static boolean verifyLoginDetails(LoginDetails details) {
		return QueryLayerFactory.getInstance().verifyLoginDetails(details);
	}
	
	public LoginDetails(String un, String pwd) {
		userName = un; encryptedPassword = pwd;
	}
}
