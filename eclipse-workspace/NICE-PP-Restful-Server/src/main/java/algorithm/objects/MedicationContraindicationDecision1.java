package algorithm.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicinalProductContraindication;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import FHIRResourceHelper.ResourceCreator;

public class MedicationContraindicationDecision1  {

	public JSONObject medication;
	public List<JSONObject> contraindications;
	public int id;
	
	public MedicationContraindicationDecision1(int id, JSONObject medication, List<JSONObject> contraindications) {
		//super("algorithm_medication_contraindication_decision_1");
		this.medication = medication;
		this.id = id;
		this.contraindications = new ArrayList<>();
	}
	
	public MedicationContraindicationDecision1(Connection c, int entity_id) throws SQLException, ParseException {
		//super("algorithm_medication_contraindication_decision_1");
		//this.algorithm_entity_id = entity_id;
		PreparedStatement ps1 = c.prepareStatement("select * from algorithm_medication_contraindication_decision_1 acm1 inner join drug d on d.id = acm1.drug_id where acm1.algorithm_entity_id = ?");
		ps1.setInt(1, entity_id);
		
		ResultSet rs1 = ps1.executeQuery();
		
		rs1.next();
		
		this.id = rs1.getInt("acm1.id");
		
		Medication medication = new Medication();
		medication.setCode(ResourceCreator.getCodeableConcept(rs1.getString("d.bnf_name"), ResourceCreator.SNOMED_SYSTEM, rs1.getString("d.snomed_code")));
		medication.setId(rs1.getInt("d.id")+"");
		
		PreparedStatement ps2 = c.prepareStatement("select * from drug d "
				+ "inner join drug_has_contraindication dhc on dhc.drug_id = d.id "
				+ "inner join conditionn c on dhc.condition_id = c.id "
				+ "where d.id = ?");
		ps2.setInt(1, rs1.getInt("acm1.drug_id"));
		
		this.contraindications = new ArrayList<>();
		ResultSet rs2 = ps2.executeQuery();
		while (rs2.next()) {
			MedicinalProductContraindication mpc = new MedicinalProductContraindication();
			mpc.setDisease(ResourceCreator.getCodeableConcept(rs2.getString("c.bnf_name"), ResourceCreator.SNOMED_SYSTEM, rs2.getString("c.snomed_code")));
			mpc.setId(rs2.getInt("c.id")+"");
			this.contraindications.add(ResourceCreator.serialize(mpc));
		}
		
		this.medication = ResourceCreator.serialize(medication);
		
	}
	
	public MedicationContraindicationDecision1(int id,Connection c) throws SQLException, ParseException {
		//super("algorithm_medication_contraindication_decision_1");
		PreparedStatement ps1 = c.prepareStatement("select * from algorithm_medication_contraindication_decision_1 acm1 inner join drug d on d.id = acm1.drug_id where acm1.id = ?");
		ps1.setInt(1, id);
		
		ResultSet rs1 = ps1.executeQuery();
		
		rs1.next();
		
		this.id = rs1.getInt("acm1.id");
		Medication medication = new Medication();
		medication.setCode(ResourceCreator.getCodeableConcept(rs1.getString("d.bnf_name"), ResourceCreator.SNOMED_SYSTEM, rs1.getString("d.snomed_code")));
		medication.setId(rs1.getInt("d.id")+"");
		
		this.medication = ResourceCreator.serialize(medication);
		
		PreparedStatement ps2 = c.prepareStatement("select * from drug d "
				+ "inner join drug_has_contraindication dhc on dhc.drug_id = d.id "
				+ "inner join conditionn c on dhc.condition_id = c.id "
				+ "where d.id = ?");
		ps2.setInt(1, rs1.getInt("d.id"));
		this.contraindications = new ArrayList<>();
		ResultSet rs2 = ps2.executeQuery();
		while (rs2.next()) {
			MedicinalProductContraindication mpc = new MedicinalProductContraindication();
			mpc.setDisease(ResourceCreator.getCodeableConcept(rs2.getString("c.bnf_name"), ResourceCreator.SNOMED_SYSTEM, rs2.getString("c.snomed_code")));
			mpc.setId(rs2.getInt("c.id")+"");
			this.contraindications.add(ResourceCreator.serialize(mpc));
		}
		
	}
	
}
