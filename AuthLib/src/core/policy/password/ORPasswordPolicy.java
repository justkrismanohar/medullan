package core.policy.password;

import java.util.ArrayList;

public class ORPasswordPolicy implements PasswordPolicy{
	
	private ArrayList<PasswordPolicy> list;
	
	public ORPasswordPolicy() {
		list = new ArrayList<PasswordPolicy>();
	}
	
	public void add(PasswordPolicy p) {
		if(p!=null)
			list.add(p);
	}
	
	@Override
	public boolean evaluatePassword(String password) {
		for(PasswordPolicy p : list)
			if(p.evaluatePassword(password))
				return true;
		return false;
	}

}
