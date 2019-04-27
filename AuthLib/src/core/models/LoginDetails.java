package core.models;

public class LoginDetails {
	public String encryptedPassword;
	public String userName;
	
	public LoginDetails(String un, String pwd) {
		userName = un; encryptedPassword = pwd;
	}
}
