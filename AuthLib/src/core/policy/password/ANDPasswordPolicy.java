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
	
	public boolean equals(Object o) {
		if(o instanceof ANDPasswordPolicy) {
			ANDPasswordPolicy other = (ANDPasswordPolicy)o;
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
