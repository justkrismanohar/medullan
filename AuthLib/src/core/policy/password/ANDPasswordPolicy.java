package core.policy.password;

import java.util.ArrayList;

public class ANDPasswordPolicy implements PasswordPolicy{
	
	private ArrayList<PasswordPolicy> list;
	
	public ANDPasswordPolicy() {
		list = new ArrayList<PasswordPolicy>();
	}
	
	public void add(PasswordPolicy p) {
		if(p!=null)
			list.add(p);
	}
	
	@Override
	public boolean evaluatePassword(String password) {
		for(PasswordPolicy p : list)
			if(!p.evaluatePassword(password))
				return false;
		return true;
	}

}
