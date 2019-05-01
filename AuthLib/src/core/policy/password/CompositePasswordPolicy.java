package core.policy.password;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




public abstract class CompositePasswordPolicy implements PasswordPolicy {
	public static final Logger log = LogManager.getLogger(CompositePasswordPolicy.class);
	
	public static CompositePasswordPolicy getInstanceOf(String type) {
		//These checks could be automated using java reflections and a file
		//or whatever to map String type to a class name
		//Not necessary right now for only AND and OR types
		if(type.equals("OR"))
			return new CompositeORPasswordPolicy();
		
		//Default maybe throw and error later
		return new CompositeANDPasswordPolicy();
	}
	
	protected ArrayList<PasswordPolicy> list;
	
	public CompositePasswordPolicy() {
		list = new ArrayList<PasswordPolicy>();
	}
	
	public void add(PasswordPolicy p) {
		if(p ==null) {
			log.error("Tried to add null PasswordPolicty");
			return;
		}
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
