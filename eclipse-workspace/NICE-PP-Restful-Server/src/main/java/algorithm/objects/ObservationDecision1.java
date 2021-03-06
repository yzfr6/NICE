package algorithm.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Quantity.QuantityComparator;
import org.hl7.fhir.r4.model.codesystems.ObservationStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import FHIRResourceHelper.ResourceCreator;

public class ObservationDecision1  {

	public JSONObject observation;
	public int id;

	public ObservationDecision1(int id, JSONObject observation) {
		//super("algorithm_observation_decision_1");
		this.observation = observation;
		this.id = id;
	}

	public ObservationDecision1(int id, Connection c ) throws SQLException, ParseException {
		//super("algorithm_observation_decision_1");
		PreparedStatement ps1 = c.prepareStatement("select * from algorithm_observation_decision_1 acd1 "
				+ "inner join observation o on o.id = acd1.observation_id "
				+ "inner join comparator c on acd1.comparator_id = c.id " + "inner join unit u on acd1.unit_id = u.id "
				+ "where acd1.id = ?");
		
		ps1.setInt(1, id);
		
		ResultSet rs1 = ps1.executeQuery();

		rs1.next();
		this.id = rs1.getInt("acd1.id");

		Observation observation = new Observation();
		observation.setCode(ResourceCreator.getCodeableConcept(rs1.getString("o.name"), ResourceCreator.LOINC_SYSTEM,
				rs1.getString("o.code")));
		observation.setId(rs1.getInt("c.id") + "");
		observation.setStatus(Observation.ObservationStatus.FINAL);
		Quantity q = new Quantity();
		q.setComparator(QuantityComparator.fromCode(rs1.getString("c.name")));
		q.setUnit(rs1.getString("u.name"));
		q.setValue(rs1.getDouble("acd1.value"));
		observation.setValue(q);
		
		this.observation = ResourceCreator.serialize(observation);

	}
	
	public ObservationDecision1(Connection c, int entity_id) throws SQLException, ParseException {
		//super("algorithm_observation_decision_1");
		//this.algorithm_entity_id = entity_id;
		PreparedStatement ps1 = c.prepareStatement("select * from algorithm_observation_decision_1 acd1 "
				+ "inner join observation o on o.id = acd1.observation_id "
				+ "inner join comparator c on acd1.comparator_id = c.id " + "inner join unit u on acd1.unit_id = u.id "
				+ "where acd1.algorithm_entity_id = ?");
		ps1.setInt(1, entity_id);

		ResultSet rs1 = ps1.executeQuery();

		rs1.next();

		this.id = rs1.getInt("acd1.id");

		Observation observation = new Observation();
		observation.setCode(ResourceCreator.getCodeableConcept(rs1.getString("o.name"), ResourceCreator.LOINC_SYSTEM,
				rs1.getString("o.code")));
		observation.setId(rs1.getInt("o.id") + "");
		observation.setStatus(Observation.ObservationStatus.FINAL);
		Quantity q = new Quantity();
		q.setComparator(QuantityComparator.fromCode(rs1.getString("c.name")));
		q.setUnit(rs1.getString("u.name"));
		q.setValue(rs1.getDouble("acd1.value"));
		observation.setValue(q);
		
		this.observation = ResourceCreator.serialize(observation);

	}

}
