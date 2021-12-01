package OMOP;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import XML.XMLContentRetriever;
import XML.XMLLoader;

/**
 * 11281008 59011009 870631005 11527006 480000 119202000 51833009 119554006
 * 23451007 280121000 280120004 45793000 56329008 9875009 297261005
 * 
 * @author nice
 *
 */
public class Runner2 {
	
	private static final String resources = "/Users/nice/Developer/eclipse-workspace/Jodi/resources/";
	
	private static Element ICD_10 = null;
	
	private static Set<String> mesh_terms = null;
	
	private static Map<String,String> codes = null;
	
	public static void main(String[] args) throws IOException, SQLException, ParserConfigurationException, SAXException {

		ICD_10 = XMLLoader.loadXMLDocument(new File(resources+"icd10cm_tabular_2021.xml"));
		mesh_terms = new HashSet<>();
		
		for (String mesh_term : FileUtils.readLines(
				new File(resources+"MESH_PRECEREBRAL_ARTERY_TERMS.list"),
				Charset.defaultCharset())) {
			mesh_terms.add(mesh_term.replaceAll("[^A-Za-z0-9]","").toLowerCase().trim());
		}
		
		codes = new HashMap<>();
		
		getICDCodes();
		
		
		File omop_icd_codes_file = new File(resources+"Runner1-`11281008`,`SNOMED`,`Precerebral artery`.csv");
		
		
		Map<String,String> inOMOPAndMESH = new HashMap<>();
		Map<String,String> inOMOPButNotMEsH = new HashMap<>();
		Map<String,String> inMESHbutnotOMOP = new HashMap<>();
		
		Set<String> omop_icd_codes = new HashSet<>();
		for (String s : FileUtils.readLines(omop_icd_codes_file)) {
		
			String[] line = s.split(",");
			String code = line[0];
			code = code.replaceAll("`","").trim();
			omop_icd_codes.add(code);
			//System.out.println(code);
			if (codes.keySet().contains(code)) {
				inOMOPAndMESH.put(code, codes.get(code));
			} else {
				//System.out.println(code + " is not in codes" );
				List<String> newarray = new ArrayList(Arrays.asList(line));
				newarray.remove(0);
				newarray.remove(0);
				inOMOPButNotMEsH.put(code,  newarray.toString());
			}
		}
		
		for (String code : codes.keySet()) {
			if (!omop_icd_codes.contains(code)) {
				inMESHbutnotOMOP.put(code, codes.get(code));
			}
		}
		
		
		System.out.println("IN OMOP and MESH");
		for (Entry<String, String> entry : inOMOPAndMESH.entrySet()) {
			System.out.println(String.format("%s - %s", entry.getKey(), entry.getValue()));
		}
		
		System.out.println("\n\n\nIN OMOP BUT NOT IN MESH");
		for (Entry<String, String> entry : inOMOPButNotMEsH.entrySet()) {
			System.out.println(String.format("%s - %s", entry.getKey(), entry.getValue()));
		}
		
		System.out.println("\n\n\nIN MESH BUT NOT IN OMOP");
		for (Entry<String, String> entry : inMESHbutnotOMOP.entrySet()) {
			System.out.println(String.format("%s - %s", entry.getKey(), entry.getValue()));
		}
		
		
	
	}
	
	private static void getICDCodes() {
		for (Element diag : XMLContentRetriever.getAllChildElements(ICD_10, "diag")) {
			
			String id = XMLContentRetriever
					.getDirectChildElements(diag, "name").get(0).getTextContent().trim();
			String name = XMLContentRetriever
					.getDirectChildElements(diag, "desc").get(0).getTextContent().trim();
			String trimmed_name = name.replaceAll("[^A-Za-z0-9]","").toLowerCase().trim();
			
			if (mesh_terms.contains(trimmed_name)) {
				codes.put(id, name);
			}
			else {
				for (String mesh_term : mesh_terms) {
					if (trimmed_name.contains(mesh_term)) {
						codes.put(id, name);
					}
				}
			}
			
			for (Element inclusionTerm : XMLContentRetriever.getDirectChildElements(diag, "inclusionTerm")) {
				for (Element note : XMLContentRetriever.getDirectChildElements(inclusionTerm, "note")) {
					String note_name = note.getTextContent().replaceAll("[^A-Za-z0-9]","").toLowerCase().trim();
					if (mesh_terms.contains(note_name)) {
						codes.put(id, name);
					}
					else {
						for (String mesh_term : mesh_terms) {
							if (note_name.contains(mesh_term)) {
								codes.put(id, name);
							}
						}
					}
				}
			}
		}
	}

	
	private static void createFilexxx() throws IOException  {
		File f = new File(resources+"Runner2-BREAST-MESH.csv");
		List<String> matches = new ArrayList<>();
		for (String icd : codes.keySet()) {
			matches.add(String.format("`%s`,`%s`,`%s`", icd, "ICD10CM",  codes.get(icd) ));
		}
		
		FileUtils.writeLines(f, matches, false);
	}
}
