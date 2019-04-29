package core.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import core.policy.login.SessionPolicy;
import core.policy.login.SessionPolicyFactory;
import core.policy.login.VerificationPolicy;
import core.policy.login.VerificationPolicyFactory;
import core.policy.password.ANDPasswordPolicy;
import core.policy.password.PasswordPolicy;
import core.policy.password.PasswordPolicyFactory;
import core.policy.security.ANDSecurityPolicy;
import core.policy.security.SecurityPolicy;
import core.policy.security.SecurityPolicyFactory;
import core.policy.username.UsernamePolicy;
import core.policy.username.UsernamePolicyFactory;

/*
 * This class is a bit messy. XML was chosen over .properties since 
 * can model complex relationships later (the API supports composition
 * so it can get very complicated). While not necessary for the current
 * client, the potential for reuse and extending is baked in...which 
 * will save time in working with future clients...
 * 
 * The loadXPolicies calls can be abstracted out to be less repetitive. 
 * Something for later I guess....
 * 
 */
public class XMLConfig {
	
	
	public static void main(String[] args) {
		XMLConfig test = new XMLConfig("config.xml");
	}
	
	private Document config;
	
	public XMLConfig(String filePath) {

		try {
			File configXML = new File(filePath);
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = f.newDocumentBuilder();
			config = builder.parse(configXML);
			config.getDocumentElement().normalize();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	 
	}
	
	public Policies parsePolicies() {
		return loadPolicies(config);
	}
	
	private Policies loadPolicies(Document config) {
		Policies p = new Policies();
		
		//load session
		p.timeoutSession = loadSession(config.getElementsByTagName("Session"));
		//load prelogin
		p.preLoginPolicies = loadLoginSecurityPolicies(config.getElementsByTagName("PreLogin"));
		//load login
		p.basicVerification = loadVerification(config.getElementsByTagName("Verification"));
		p.passwordPolicy = loadPasswordPolicies(config.getElementsByTagName("Password"));
		p.usernamePolicy = loadUsernamePolicies(config.getElementsByTagName("Username"));
		
		//load postlogin
		p.postLoginPolicies = loadLoginSecurityPolicies(config.getElementsByTagName("PostLogin"));
		
		return p;
	}
	
	
	private HashMap<String,String> extractAttributes(Node n){
		HashMap<String,String> out = new HashMap<String,String>();
		NamedNodeMap m = n.getAttributes();
		for(int j = 0; j < m.getLength(); j++) {
			Node att = m.item(j);
			out.put(att.getNodeName(),att.getNodeValue());
		}
		return out;
	}
	
	
	private UsernamePolicy loadUsernamePolicies(NodeList l) {
		Node n = l.item(0);
		return UsernamePolicyFactory.getInstance(n.getNodeName(),extractAttributes(n));
	}
	
	private VerificationPolicy loadVerification(NodeList l) {
		Node n = l.item(0);
		return VerificationPolicyFactory.getInstance(n.getNodeName(),extractAttributes(n));
	}
	
	private PasswordPolicy loadPasswordPolicies(NodeList l) {
		ANDPasswordPolicy out = new ANDPasswordPolicy();//This can be configured as well later
		
		l = l.item(0).getChildNodes();
		for(int i=0; i < l.getLength(); i ++) {
			Node n = l.item(i);
			
			if(n.getNodeType() == Node.ELEMENT_NODE) {
				out.add(PasswordPolicyFactory.getInstance(n.getNodeName(),extractAttributes(n)));
			}
		}
		
		return out;
	}
	private SessionPolicy loadSession(NodeList l) {
		l = l.item(0).getChildNodes();
		for(int i=0; i < l.getLength(); i ++) {
			Node n = l.item(i);
			
			if(n.getNodeType() == Node.ELEMENT_NODE) {
				return SessionPolicyFactory.getInstance(n.getNodeName(),extractAttributes(n));
			}
		}
		
		return null;
	}
	
	private SecurityPolicy loadLoginSecurityPolicies(NodeList l) {
		ANDSecurityPolicy out = new ANDSecurityPolicy();//This can be configured as well later
		
		l = l.item(0).getChildNodes();
		for(int i=0; i < l.getLength(); i ++) {
			Node n = l.item(i);
			
			if(n.getNodeType() == Node.ELEMENT_NODE) {
				out.add(SecurityPolicyFactory.getInstance(n.getNodeName(),extractAttributes(n)));
			}
		}
		
		return out;
	}
	
	public void printList(NodeList l) {
		for(int i=0; i < l.getLength(); i ++) {
			Node n = l.item(i);
			
			if(n.getNodeType() == Node.ELEMENT_NODE) {
				System.out.print(n.getNodeName());
				
				if(n.hasAttributes()) {
					NamedNodeMap m = n.getAttributes();
					for(int j = 0; j < m.getLength(); j++) {
						Node att = m.item(j);
						System.out.print(" "+att.getNodeName() +"="+att.getNodeValue());
					}
				}
				
				System.out.println();
			}
			
			if(n.hasChildNodes())
				printList(n.getChildNodes());
			
		}
	}
}
