package algorithm.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.parser.ParseException;

public class SetAlert1  {

	public ObservationDecision1 observation_decision_1;
	public StopMedication1 stop_medication_1;
	public int id;
	public SetAlert1(int id, ObservationDecision1 observationDecision1, StopMedication1 stopMedication1) {
		//super("algorithm_set_alert_1");
		this.observation_decision_1 = observationDecision1;
		this.stop_medication_1 = stopMedication1;
		this.id = id;
	}
	
	public SetAlert1(Connection c, int entity_id) throws SQLException, ParseException {
		//super("algorithm_set_alert_1");
		//this.algorithm_entity_id = entity_id;
		PreparedStatement ps1 = c.prepareStatement("select * from algorithm_set_alert_1 where algorithm_entity_id = ?");
		ps1.setInt(1, entity_id);
		
		ResultSet rs1 = ps1.executeQuery();
		
		rs1.next();
		
		this.id = rs1.getInt("id");
		this.observation_decision_1 = new ObservationDecision1(rs1.getInt("algorithm_observation_decision_1_id"), c);
		this.stop_medication_1 = new StopMedication1(rs1.getInt("algorithm_stop_medication_1_id"), c);
		
	}
	
}
