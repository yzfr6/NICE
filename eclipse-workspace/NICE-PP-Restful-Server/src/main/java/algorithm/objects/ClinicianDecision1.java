package algorithm.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClinicianDecision1 {

	public String description;
	public int id;
	
	public ClinicianDecision1(int id, String description) {
		//super("algorithm_clinician_decision_1");
		this.description = description;
		this.id = id;
	}
	
	
	public ClinicianDecision1(Connection c, int entity_id) throws SQLException {
		//super("algorithm_clinician_decision_1");
		//this.algorithm_entity_id = entity_id;
		PreparedStatement ps1 = c.prepareStatement("select * from algorithm_clinician_decision_1 where algorithm_entity_id = ?");
		ps1.setInt(1, entity_id);
		
		ResultSet rs1 = ps1.executeQuery();
		
		rs1.next();
		
		this.description = rs1.getString("description");
		this.id = rs1.getInt("id");
		
	}
	
	
	
	
}
