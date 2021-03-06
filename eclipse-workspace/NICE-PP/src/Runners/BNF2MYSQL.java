package Runners;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import Database.BNFQueries;
import URL.JSONGetter;
import responses.BNFConnector;

public class BNF2MYSQL {

	public static final String RESOURCE_PATH = "/Users/nice/Developer/eclipse-workspace/NICE-BNF-PP/resources/";
	
	public static void main(String[] args) {

		Connection conn = null;
		try {
			
			List<String> d2Snomed = FileUtils.readLines(new File(RESOURCE_PATH+"Drug2SNOMED"));
			Map<String,String> d2SnomedMap = new HashMap<>();
			for (String line : d2Snomed) {
				String [] split = line.split(",");
				d2SnomedMap.put(split[0], split[1]);
			}
			
			List<String> se2Snomed = FileUtils.readLines(new File(RESOURCE_PATH+"SideEffect2SNOMED"));
			Map<String,String> se2SnomedMap = new HashMap<>();
			for (String line : se2Snomed) {
				String [] split = line.split(",");
				se2SnomedMap.put(split[0], split[1]);
			}
			
			List<String> ci2Snomed = FileUtils.readLines(new File(RESOURCE_PATH+"/Contraindication2SNOMED"));
			Map<String,String> ci2SnomedMap = new HashMap<>();
			for (String line : ci2Snomed) {
				String [] split = line.split(",");
				ci2SnomedMap.put(split[0], split[1]);
			}
			
			
			conn = BNFConnector.getConnection().orElseThrow();
			conn.setAutoCommit(false);

			conn.prepareStatement("SET FOREIGN_KEY_CHECKS=0").execute();
			conn.prepareStatement("truncate snomed_class").executeUpdate();
			conn.prepareStatement("truncate  drug_has_contraindication").executeUpdate();
			conn.prepareStatement("truncate drug_has_side_effect").executeUpdate();
			conn.prepareStatement("SET FOREIGN_KEY_CHECKS=1").execute();

			List<String> urls = FileUtils.readLines(new File(RESOURCE_PATH+"/MedicationURLs"));

			for (String url : urls) {

				JSONObject json = JSONGetter.readJsonFromUrl(url).orElseThrow();

				JSONObject graph = (JSONObject) ((JSONArray) json.get("@graph")).get(0);

				String uri = (String) graph.get("@id");
				System.out.println(uri);
				String rdfs_label = (String) graph.get("rdfs:label");
				String snomed_code = d2SnomedMap.getOrDefault(rdfs_label, "unknown");
				int drug_db_id = BNFQueries.getSnomedClass(rdfs_label, uri, "drug", snomed_code, conn);

				JSONArray contraindicationGroup = (JSONArray) graph.get("hasContraindicationsGroup");
				if (contraindicationGroup != null) {
					for (Object ciog : contraindicationGroup) {
						JSONObject cig = (JSONObject) ciog;
						JSONArray cia = (JSONArray) cig.get("hasContraindication");
						for (Object cio : cia) {
							JSONObject ci = (JSONObject) cio;
							String name = (String) ci.get("rdfs:label");
							String id = (String) ci.get("@id");
							String snomed_code_1 = ci2SnomedMap.getOrDefault(name, "unknown");
							int contraindication_db_id = BNFQueries.getSnomedClass(name, id, "contraindication", snomed_code_1, conn);

							conn.prepareStatement(
									"insert into drug_has_contraindication (source_id, target_id) values ( "
											+ drug_db_id + ", " + contraindication_db_id + " )")
									.executeUpdate();

						}
					}
				}

				JSONArray sideEffects = (JSONArray) graph.get("hasSideEffectsGroup");

				for (Object seog : sideEffects) {
					
					JSONObject seg = (JSONObject) seog;
					JSONObject hasFrequency = (JSONObject) seg.get("hasFrequency");
					String frequency = (String)hasFrequency.get("rdfs:label");
					int frequency_id = BNFQueries.getSideEffectFrequency(frequency, conn);
					JSONArray sea = (JSONArray) seg.get("hasSideEffect");
					for (Object seo : sea) {
						JSONObject se = (JSONObject) seo;
						String name = (String) se.get("rdfs:label");
						String id = (String) se.get("@id");
						String snomed_code_1 = se2SnomedMap.getOrDefault(name, "unknown");
						int side_effect_id = BNFQueries.getSnomedClass(name,id, "sideEffect", snomed_code_1, conn);
						
						conn.prepareStatement("insert into drug_has_side_effect (source_id, target_id, side_effect_frequency_id) values ( "
								+ drug_db_id + ", " + side_effect_id +  ", " + frequency_id + " )").executeUpdate();
					}
				}
				conn.commit();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}

		/**
		 * Medication m = new Medication();
		 * 
		 * Identifier m_id = new Identifier(); m_id.setSystem(system);
		 * m_id.setValue(rdfs_label); m.addIdentifier(m_id);
		 * 
		 * m.setCode(ResourceCreator.getCodeableConcept(rdfs_label,
		 * ResourceCreator.SNOMED_SYSTEM, "INSERT CODE"));
		 * 
		 * 
		 * 
		 * 
		 * List<MedicinalProductContraindication> m_contraindications = new
		 * ArrayList<>(); //get all contraindications JSONArray contraindicationGroup =
		 * (JSONArray)graph.get("hasContraindicationsGroup");
		 * 
		 * for (Object ciog : contraindicationGroup) { JSONObject cig = (JSONObject)
		 * ciog;
		 * 
		 * JSONArray cia = (JSONArray)cig.get("hasContraindication"); for (Object cio :
		 * cia) { JSONObject ci = (JSONObject) cio; String id = (String) ci.get("@id");
		 * 
		 * MedicinalProductContraindication mpci = new
		 * MedicinalProductContraindication(); mpci.addSubject(new Reference(m));
		 * mpci.setDisease(ResourceCreator.getCodeableConcept("CHANGE - " + id,
		 * ResourceCreator.SNOMED_SYSTEM, "INSERT CODE"));
		 * m_contraindications.add(mpci); } }
		 * 
		 * List<MedicinalProductUndesirableEffect> m_sideEffects = new ArrayList<>();
		 * //get all sideEffects JSONArray sideEffects =
		 * (JSONArray)graph.get("hasSideEffectsGroup");
		 * 
		 * for (Object seog : sideEffects) { JSONObject seg = (JSONObject) seog;
		 * 
		 * JSONArray sea = (JSONArray)seg.get("hasSideEffect"); for (Object seo : sea) {
		 * JSONObject se = (JSONObject) seo; String id = (String) se.get("@id");
		 * 
		 * MedicinalProductUndesirableEffect mpue = new
		 * MedicinalProductUndesirableEffect(); mpue.addSubject(new Reference(m));
		 * mpue.setSymptomConditionEffect(ResourceCreator.getCodeableConcept("CHANGE - "
		 * + id, ResourceCreator.SNOMED_SYSTEM, "INSERT CODE"));
		 * m_sideEffects.add(mpue); } }
		 * 
		 * //FhirContext ctx = FhirContext.forR4(); IParser p =
		 * FhirContext.forR4().newJsonParser();
		 * 
		 * System.out.println(p.encodeResourceToString(m)); for
		 * (MedicinalProductContraindication mpci : m_contraindications) {
		 * System.out.println(p.encodeResourceToString(mpci)); } for
		 * (MedicinalProductUndesirableEffect mpci : m_sideEffects) {
		 * System.out.println(p.encodeResourceToString(mpci)); }
		 **/
	}
}

// 08001385385 - ??