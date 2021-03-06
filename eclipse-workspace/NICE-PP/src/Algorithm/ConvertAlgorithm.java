package Algorithm;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import Database.BNFConnector;
import JSON.JSONGetter;

public class ConvertAlgorithm {

	public static void main (String [] args) throws IOException, ParseException, SQLException {
		
		File f = new File("/Users/nice/Developer/eclipse-workspace/NICE-PP/resources/algorithm_entities/algorithm.json");
		
		JSONObject algorithm = JSONGetter.loadJSONObject(f);
		
		Connection c = BNFConnector.getConnection().orElseThrow();
		
		c.setAutoCommit(false);
		c.prepareStatement("SET FOREIGN_KEY_CHECKS=0").execute();
		c.prepareStatement("truncate algorithm").executeUpdate();
		c.prepareStatement("truncate node").executeUpdate();
		c.prepareStatement("truncate entity_atomic").executeUpdate();
		c.prepareStatement("truncate entity_conjunction").executeUpdate();
		c.prepareStatement("truncate entity_disjunction").executeUpdate();
		
		c.prepareStatement("SET FOREIGN_KEY_CHECKS=1").execute();
		
		PreparedStatement ps = c.prepareStatement("insert into algorithm (name) values (?)", Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, (String)algorithm.get("name"));
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		JSONArray nodes = (JSONArray)algorithm.get("nodes");
		for (Object obj1 : nodes) {
			
			JSONObject node = (JSONObject)obj1;
			String object_id = (String)node.get("id");
			String text = (String)node.get("text");
			String node_type = (String)node.get("node_type");
			JSONObject entities = (JSONObject)node.get("entities");
			String content_type = null;
			if (entities.containsKey("single")) {
				content_type = "atomic";
			} else if (entities.containsKey("and")) {
				content_type = "conjunction";
			} else if (entities.containsKey("or")) {
				content_type = "disjunction";
			}
			PreparedStatement ps1 = c.prepareStatement("insert into node (object_id, text, type, content_type, algorithm_id) values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			ps1.setString(1, object_id);
			ps1.setString(2, text);
			ps1.setString(3, node_type);
			ps1.setString(4, content_type);
			ps1.setInt(5, rs.getInt(1));
			ps1.executeUpdate();
			ResultSet rs1 = ps1.getGeneratedKeys();
			rs1.next();
			int node_id = rs1.getInt(1);
			
			
			
			
			if (entities.containsKey("single")) {
				
				JSONObject single = (JSONObject)entities.get("single");
				int algorithm_entity_id = insertEntity(single, node_id, c);
				
				
				if (algorithm_entity_id != -1) {
					PreparedStatement ps2 = c.prepareStatement("insert into entity_atomic (algorithm_entity_id, node_id) values (?, ?)");
					ps2.setInt(1,algorithm_entity_id);
					ps2.setInt(2, node_id);
					ps2.executeUpdate();
				}
				
				
			} else if (entities.containsKey("and")) {
				
				JSONArray and = (JSONArray)entities.get("and");
				for (Object a : and) {
					int algorithm_entity_id = insertEntity((JSONObject)a, node_id, c);
					
					
					if (algorithm_entity_id != -1) {
						PreparedStatement ps2 = c.prepareStatement("insert into entity_conjunction (algorithm_entity_id, node_id) values (?, ?)");
						ps2.setInt(1,algorithm_entity_id);
						ps2.setInt(2, node_id);
						ps2.executeUpdate();
					}
				}
				
				
			} else if (entities.containsKey("or")) {
				JSONArray and = (JSONArray)entities.get("or");
				for (Object a : and) {
					int algorithm_entity_id = insertEntity((JSONObject)a, node_id, c);
					
					
					if (algorithm_entity_id != -1) {
						PreparedStatement ps2 = c.prepareStatement("insert into entity_disjunction (algorithm_entity_id, node_id) values (?, ?)");
						ps2.setInt(1,algorithm_entity_id);
						ps2.setInt(2, node_id);
						ps2.executeUpdate();
					}
				}
			}
			
		}
		
		
		c.commit();
		c.setAutoCommit(true);
		
		c.close();
		
		
	}
	
	
	private static int insertEntity(JSONObject entity, int node_id, Connection c) throws SQLException {
	
		String type  = (String)entity.get("type");
	
		System.out.println(type);
		
		if (entity.containsKey("id")) {
		
		long id = (long)entity.get("id");
		PreparedStatement ps1 = c.prepareStatement("select algorithm_entity_id from "+type+" where id = ?");

		ps1.setLong(1, id);
		ResultSet rs1= ps1.executeQuery();
		rs1.next();
		return rs1.getInt("algorithm_entity_id");
		} else if (entity.containsKey("code")) {
			
			String code = (String)entity.get("code");
			PreparedStatement ps1 = c.prepareStatement("select algorithm_entity_id from "+type+" where code = ?");

			ps1.setString(1, code);
			ResultSet rs1= ps1.executeQuery();
			rs1.next();
			return rs1.getInt("algorithm_entity_id");
			
		} else {
			System.out.println(entity.toJSONString());
			throw new IllegalArgumentException();
		}
	}
}
