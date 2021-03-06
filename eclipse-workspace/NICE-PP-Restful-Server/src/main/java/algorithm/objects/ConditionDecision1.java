package algorithm.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import FHIRResourceHelper.ResourceCreator;

public class ConditionDecision1 {

	public JSONObject condition;
	public int id;
	
	public ConditionDecision1(int id, JSONObject condition) {
		//super("algorithm_condition_decision_1");
		this.condition = condition;
		this.id = id;
	}
	
	public ConditionDecision1(Connection c, int entity_id) throws SQLException, ParseException {
		//super("algorithm_condition_decision_1");
		//this.algorithm_entity_id = entity_id;
		PreparedStatement ps1 = c.prepareStatement("select * from algorithm_condition_decision_1 acd1 "
				+ "inner join conditionn c on c.id = acd1.condition_id "
				+ "where acd1.algorithm_entity_id = ?");
		ps1.setInt(1, entity_id);
		
		ResultSet rs1 = ps1.executeQuery();
		
		rs1.next();
		
		this.id = rs1.getInt("acd1.id");
		
		Condition condition = new Condition();
		condition.setCode(ResourceCreator.getCodeableConcept(rs1.getString("c.bnf_name"), ResourceCreator.SNOMED_SYSTEM, rs1.getString("c.snomed_code")));
		condition.setId(rs1.getInt("c.id")+"");
		condition.setSubject(new Reference(new Patient()));
		this.condition = ResourceCreator.serialize(condition);
		
	}
	
	
}
