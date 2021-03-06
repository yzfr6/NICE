package algorithm.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.MedicationRequest.MedicationRequestIntent;
import org.hl7.fhir.r4.model.MedicationRequest.MedicationRequestStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import FHIRResourceHelper.ResourceCreator;

public class StopMedication1 {

	public JSONObject medication_request;
	public int id;
	
	public StopMedication1(int id, JSONObject medication_request) {
		//super("algorithm_stop_medication_1");
		this.medication_request = medication_request;
		this.id = id;
	}
	public StopMedication1(int id, Connection c) throws SQLException, ParseException {
		//super("algorithm_stop_medication_1");
		
		PreparedStatement ps1 = c.prepareStatement("select * from algorithm_stop_medication_1 acm1 inner join drug d on d.id = acm1.drug_id where acm1.id = ?");
		ps1.setInt(1, id);
		
		ResultSet rs1 = ps1.executeQuery();
		
		rs1.next();
		
		this.id = rs1.getInt("acm1.id");
		
		MedicationRequest mr = new MedicationRequest();
		mr.setMedication(ResourceCreator.getCodeableConcept(rs1.getString("d.bnf_name"), ResourceCreator.SNOMED_SYSTEM, rs1.getString("d.snomed_code")));
		mr.setId(rs1.getInt("d.id")+"");
		mr.setIntent(MedicationRequestIntent.ORDER);
		mr.setStatus(MedicationRequestStatus.ACTIVE);
		mr.setDoNotPerform(true);
		mr.setSubject(new Reference(new Patient()));
		this.medication_request = ResourceCreator.serialize(mr);;
		
	}
	public StopMedication1(Connection c, int entity_id) throws SQLException, ParseException {
		//super("algorithm_stop_medication_1");
		//this.algorithm_entity_id = entity_id;
		PreparedStatement ps1 = c.prepareStatement("select * from algorithm_stop_medication_1 acm1 inner join drug d on d.id = acm1.drug_id where acm1.algorithm_entity_id = ?");
		ps1.setInt(1, entity_id);
		
		ResultSet rs1 = ps1.executeQuery();
		
		rs1.next();
		
		this.id = rs1.getInt("acm1.id");
		
		MedicationRequest mr = new MedicationRequest();
		mr.setMedication(ResourceCreator.getCodeableConcept(rs1.getString("d.bnf_name"), ResourceCreator.SNOMED_SYSTEM, rs1.getString("d.snomed_code")));
		mr.setId(rs1.getInt("d.id")+"");
		mr.setIntent(MedicationRequestIntent.ORDER);
		mr.setStatus(MedicationRequestStatus.ACTIVE);
		mr.setDoNotPerform(true);
		mr.setSubject(new Reference(new Patient()));
		this.medication_request = ResourceCreator.serialize(mr);;
		
	}
	
	
	
	
}
