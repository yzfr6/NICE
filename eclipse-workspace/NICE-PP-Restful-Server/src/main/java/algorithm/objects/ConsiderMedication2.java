package algorithm.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsiderMedication2 {

	public String medication_code;
	public int id;
	

	public ConsiderMedication2(int id, EntityType type, String medication_code) {
		//super("algorithm_consider_medication_2");
		
		this.medication_code = medication_code;
		this.id = id;
	}



	public ConsiderMedication2(Connection c, int entity_id) throws SQLException {
		//super("algorithm_consider_medication_2");
		//this.algorithm_entity_id = entity_id;
		PreparedStatement ps1 = c.prepareStatement("select * from algorithm_consider_medication_2 where algorithm_entity_id = ?");
		ps1.setInt(1, entity_id);
		
		ResultSet rs1 = ps1.executeQuery();
		
		rs1.next();
		
		this.id = rs1.getInt("id");
		this.medication_code = rs1.getString("code");
		
		
	}
	
	
	
	
}
