package core.policy.password;

import java.util.ArrayList;

public abstract class CompositePasswordPolicy implements PasswordPolicy {
	
	
	public static CompositePasswordPolicy getInstanceOf(String type) {
		if(type.equals("OR"))
			return new ORPasswordPolicy();
		
		//Default maybe throw and error later
		return new ANDPasswordPolicy();
	}
	
	protected ArrayList<PasswordPolicy> list;
	
	public CompositePasswordPolicy() {
		list = new ArrayList<PasswordPolicy>();
	}
	
	public void add(PasswordPolicy p) {
		if(p!=null)
			list.add(p);
	}
	
	public boolean equals(Object o) {
		if(o instanceof CompositePasswordPolicy) {
			CompositePasswordPolicy other = (CompositePasswordPolicy)o;
			int len = list.size();
			if(len == other.list.size()) {
				for(int i =0; i < len; i++) {
					if(!list.get(i).equals(other.list.get(i)))
						return false;
				}
				return true;
			}
		}
		return false;
	}

}
