package OMOP;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Triple;

/**
 * 11281008 59011009 870631005 11527006 480000 119202000 51833009 119554006
 * 23451007 280121000 280120004 45793000 56329008 9875009 297261005
 * 
 * @author nice
 *
 */
public class Runner1 {
	
	private static final String resources = "/Users/nice/Developer/eclipse-workspace/Jodi/resources/";
	
	public static void main(String[] args) throws IOException, SQLException {
		Connection conn = OMOPConnector.getConnection().orElseThrow();

		for (String snomed_code : FileUtils.readLines(
				new File(resources+"SNOMED_CONCEPTS_1.list"),
				Charset.defaultCharset())) {
			int id = getConceptIDFromCode(conn, snomed_code);
			Set<Integer> icd_codes = new HashSet<>();
			icd_codes.addAll(fullQuery(conn, id));
			createFile(conn, getConceptIDFromCode(conn, snomed_code), icd_codes);
		}

	}
	
	private static Set<Integer> fullQuery(Connection conn, int snomed_id) throws SQLException{
		PreparedStatement ps = conn.prepareStatement("SELECT distinct source.concept_id FROM concept source INNER JOIN concept_relationship rel ON rel.concept_id_1 = source.concept_id "
				+ "AND rel.relationship_id = 'Maps to' "
				+ "INNER JOIN concept target  ON target.concept_id = rel.concept_id_2 "
				+ "WHERE source.vocabulary_id = 'ICD10CM' "
				+ " AND target.vocabulary_id = 'SNOMED' "
				+ "AND target.concept_id in "
				+ "(select c.concept_id from concept c "
				+ "inner join concept_ancestor ca on ca.descendant_concept_id = c.concept_id "
				+ "where ca.ancestor_concept_id in "
				+ "(select cr.concept_id_1 from concept_relationship cr "
				+ "where cr.relationship_id = 'Has finding site' "
				+ "and cr.concept_id_2 = ? ))");
		ps.setInt(1, snomed_id);
		
		ResultSet rs = ps.executeQuery();
		Set<Integer> set = new HashSet<>();
		while (rs.next()) {
			int child = rs.getInt("source.concept_id");
			set.add(child);
			// System.out.println(child);
		}
		return set;
	}
	
	private static void createFile(Connection conn, int snomed_id, Set<Integer> icd_ids) throws SQLException, IOException {
		Triple<String,String, String> p1 = getConceptVocabName(conn, snomed_id);
		File f = new File(resources+"Runner1-"+getCSVStringFromConceptCSV(p1)+".csv");
		
		List<String> matches = new ArrayList<>();
		for (int icd : icd_ids) {
			Triple<String,String,String> p2 = getConceptVocabName(conn, icd);
			matches.add(getCSVStringFromConceptCSV(p2));
		}
		
		FileUtils.writeLines(f, matches, false);
		//FileUtils.writeStringToFile(f, sb.toString(), Charset.defaultCharset());
		
	}
	
	private static String getCSVStringFromConceptCSV(Triple<String,String,String> t) {
		return String.format("`%s`,`%s`,`%s`", t.getRight(), t.getLeft(), t.getMiddle());
	}
	
	private static Triple<String,String,String> getConceptVocabName(Connection conn, int id) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("select vocabulary_id, concept_name, concept_code from concept where concept_id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		rs.next();
		return Triple.of(rs.getString("vocabulary_id"), rs.getString("concept_name"), rs.getString("concept_code"));

	}
	
	private static int getConceptIDFromCode(Connection conn, String code) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("select concept_id from concept where concept_code = ?");
		ps.setString(1, code);
		ResultSet rs = ps.executeQuery();
		rs.next();
		return rs.getInt("concept_id");
	}

	private static Set<Integer> getSNOMEDSubChildren(Connection conn, String parent_snomed_id) throws SQLException {

		PreparedStatement ps = conn.prepareStatement("select des.concept_id from concept des "
				+ "inner join concept_ancestor ca on ca.descendant_concept_id = des.concept_id "
				+ "inner join concept ancs on ca.ancestor_concept_id = ancs.concept_id"
				+ " where ancs.concept_code = ?");
		ps.setString(1, parent_snomed_id);

		ResultSet rs = ps.executeQuery();
		Set<Integer> set = new HashSet<>();
		while (rs.next()) {
			int child = rs.getInt("des.concept_id");
			set.add(child);
			// System.out.println(child);
		}
		return set;

	}
	
	private static Set<Integer> getSNOMEDHasFindingSite(Connection conn, int range_concept_id) throws SQLException {

		PreparedStatement ps = conn.prepareStatement("select cr.concept_id_1 from concept_relationship cr where cr.relationship_id = 'Has finding site' and cr.concept_id_2 = ?");
		ps.setInt(1, range_concept_id);

		ResultSet rs = ps.executeQuery();
		Set<Integer> set = new HashSet<>();
		while (rs.next()) {
			int child = rs.getInt("cr.concept_id_1");
			set.add(child);
			// System.out.println(child);
		}
		return set;

	}
	
	private static Set<Integer> getICDMapsTo(Connection conn, int snomed_concept_id) throws SQLException{
PreparedStatement ps = conn.prepareStatement("SELECT distinct source.concept_id FROM concept source "
		+ "INNER JOIN concept_relationship rel ON rel.concept_id_1 = source.concept_id AND rel.relationship_id = 'Maps to' "
		+ "INNER JOIN concept target ON target.concept_id = rel.concept_id_2 "
		+ "WHERE source.vocabulary_id = 'ICD10CM' "
		+ "AND target.vocabulary_id = 'SNOMED' "
		+ "and target.concept_id = ?");
		ps.setInt(1, snomed_concept_id);
		
		ResultSet rs = ps.executeQuery();
		Set<Integer> set = new HashSet<>();
		while (rs.next()) {
			int child = rs.getInt("source.concept_id");
			set.add(child);
		}
		return set;

	}
}
