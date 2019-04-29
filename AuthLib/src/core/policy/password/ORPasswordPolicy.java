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
	
	public boolean equals(Object o) {
		if(o instanceof ORPasswordPolicy) {
			ORPasswordPolicy other = (ORPasswordPolicy)o;
			int len = list.size();
			for(int i =0; i < len; i++) {
				if(!list.get(i).equals(other.list.get(i)))
					return false;
			}
			return true;
		}
		return false;
	}
}
