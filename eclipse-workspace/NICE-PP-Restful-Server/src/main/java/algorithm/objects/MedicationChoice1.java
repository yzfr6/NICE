package algorithm.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.Medication;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import FHIRResourceHelper.ResourceCreator;

public class MedicationChoice1{

	public String medication_code;
	public List<JSONObject> medications;
	public int id;
	

	public MedicationChoice1(int id, EntityType type, String medication_code, List<JSONObject> medications) {
		//super("algorithm_medication_choice_1");
		this.medication_code = medication_code;
		this.medications = medications;
		this.id = id;
	}
	
	public MedicationChoice1(Connection c, int entity_id) throws SQLException, ParseException {
		//super("algorithm_medication_choice_1");
		//this.algorithm_entity_id = entity_id;
		PreparedStatement ps1 = c.prepareStatement("select * from algorithm_medication_choice_1 where algorithm_entity_id = ?");
		ps1.setInt(1, entity_id);
		
		ResultSet rs1 = ps1.executeQuery();
		
		rs1.next();
		
		this.id = rs1.getInt("id");
		this.medication_code = rs1.getString("code");
		
		PreparedStatement ps2 = c.prepareStatement("select * from algorithm_medication_choices_1 amc1 inner join drug d on d.id = amc1.drug_id where amc1.algorithm_medication_choice_1_id = ?");
		ps2.setInt(1, this.id);
		
		ResultSet rs2 = ps2.executeQuery();
		
		medications = new ArrayList<>();
		while (rs2.next()) {
			Medication medication = new Medication();
			medication.setCode(ResourceCreator.getCodeableConcept(rs2.getString("d.bnf_name"), ResourceCreator.SNOMED_SYSTEM, rs2.getString("d.snomed_code")));
			medication.setId(rs2.getInt("d.id")+"");
			medications.add(ResourceCreator.serialize(medication));
		}
		
	}
	
	
	
}
