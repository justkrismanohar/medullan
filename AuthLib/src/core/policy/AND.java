package core.policy;

import java.util.ArrayList;

public class AND implements PasswordPolicy{
	
	private ArrayList<PasswordPolicy> list;
	
	public AND() {
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
