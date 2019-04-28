package core.api;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XMLConfig {
	
	
	public static void main(String[] args) {
		XMLConfig test = new XMLConfig("config.xml");
	}
	
	public XMLConfig(String filePath) {

		try {
			File configXML = new File(filePath);
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = f.newDocumentBuilder();
			Document config = builder.parse(configXML);
			config.getDocumentElement().normalize();
			
			NodeList l = config.getElementsByTagName("Policies");
			printList(l);
			
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
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
