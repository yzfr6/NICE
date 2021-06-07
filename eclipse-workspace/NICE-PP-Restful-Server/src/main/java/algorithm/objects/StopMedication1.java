package algorithm.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hl7.fhir.r4.model.Medication;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import FHIRResourceHelper.ResourceCreator;

public class StopMedication1 {

	public JSONObject medication;
	public int id;
	
	public StopMedication1(int id, JSONObject medication) {
		//super("algorithm_stop_medication_1");
		this.medication = medication;
		this.id = id;
	}
	public StopMedication1(int id, Connection c) throws SQLException, ParseException {
		//super("algorithm_stop_medication_1");
		
		PreparedStatement ps1 = c.prepareStatement("select * from algorithm_stop_medication_1 acm1 inner join drug d on d.id = acm1.drug_id where acm1.id = ?");
		ps1.setInt(1, id);
		
		ResultSet rs1 = ps1.executeQuery();
		
		rs1.next();
		
		this.id = rs1.getInt("acm1.id");
		
		Medication medication = new Medication();
		medication.setCode(ResourceCreator.getCodeableConcept(rs1.getString("d.bnf_name"), ResourceCreator.SNOMED_SYSTEM, rs1.getString("d.snomed_code")));
		medication.setId(rs1.getInt("d.id")+"");
		this.medication = ResourceCreator.serialize(medication);
		
	}
	public StopMedication1(Connection c, int entity_id) throws SQLException, ParseException {
		//super("algorithm_stop_medication_1");
		//this.algorithm_entity_id = entity_id;
		PreparedStatement ps1 = c.prepareStatement("select * from algorithm_stop_medication_1 acm1 inner join drug d on d.id = acm1.drug_id where acm1.algorithm_entity_id = ?");
		ps1.setInt(1, entity_id);
		
		ResultSet rs1 = ps1.executeQuery();
		
		rs1.next();
		
		this.id = rs1.getInt("acm1.id");
		
		Medication medication = new Medication();
		medication.setCode(ResourceCreator.getCodeableConcept(rs1.getString("d.bnf_name"), ResourceCreator.SNOMED_SYSTEM, rs1.getString("d.snomed_code")));
		medication.setId(rs1.getInt("d.id")+"");
		this.medication = ResourceCreator.serialize(medication);
		
	}
	
	
	
	
}