package XML;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class XMLLoader {

	
	public static Element loadXMLDocument(File f) throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(f);
		return doc.getDocumentElement();
		
	}
	
	
}
