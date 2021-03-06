package algorithm.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.parser.ParseException;

public class SetAlert2 {

	public MedicationContraindicationDecision1 medication_contraindication_decision_1;
	public StopMedication1 stop_medication_1;
	public int id;
	
	public SetAlert2(int id, MedicationContraindicationDecision1 medicationContraindicationDecision1, StopMedication1 stopMedication1) {
		//super("algorithm_set_alert_2");
		this.medication_contraindication_decision_1 = medicationContraindicationDecision1;
		this.stop_medication_1 = stopMedication1;
		this.id = id;
	}
	
	public SetAlert2(Connection c, int entity_id) throws SQLException, ParseException {
		//super("algorithm_set_alert_2");
		//this.algorithm_entity_id = entity_id;
		PreparedStatement ps1 = c.prepareStatement("select * from algorithm_set_alert_2 where algorithm_entity_id = ?");
		ps1.setInt(1, entity_id);
		
		ResultSet rs1 = ps1.executeQuery();
		
		rs1.next();
		
		this.id = rs1.getInt("id");
		this.medication_contraindication_decision_1 = new MedicationContraindicationDecision1(rs1.getInt("algorithm_medication_contraindication_decision_1"), c);
		this.stop_medication_1 = new StopMedication1(rs1.getInt("algorithm_stop_medication_1_id"), c);
		
	}
	
}
