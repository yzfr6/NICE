package XML;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLContentRetriever {

	public static List<Element> getAllElementsInDocument(Document d, String name) {

		NodeList nl = d.getElementsByTagName(name);
		List<Element> e = new ArrayList<>();
		for (int i = 0; i < nl.getLength(); i++) {
			if (!nl.item(i).getNodeName().equals("#text")) {
				e.add((Element) nl.item(i));
			}
		}

		return e;
	}

	public static List<Element> getAllChildElements(Element d, String name) {

		NodeList nl = d.getElementsByTagName(name);
		List<Element> e = new ArrayList<>();
		for (int i = 0; i < nl.getLength(); i++) {
			if (!nl.item(i).getNodeName().equals("#text")) {
				e.add((Element) nl.item(i));
			}
		}

		return e;
	}
	
	public static List<Element> getDirectChildElements(Element d, String name) {

		NodeList nl = d.getElementsByTagName(name);
		List<Element> e = new ArrayList<>();
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getParentNode().equals(d) && !nl.item(i).getNodeName().equals("#text")) {
				e.add((Element) nl.item(i));
			}
		}

		return e;
	}

	public static List<Element> getDirectElementChildren(Element e) {

		NodeList nl = e.getChildNodes();
		List<Element> el = new ArrayList<>();
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getParentNode().equals(e) && !nl.item(i).getNodeName().equals("#text")) {
				el.add((Element) nl.item(i));
			}
		}
		return el;
	}


}
