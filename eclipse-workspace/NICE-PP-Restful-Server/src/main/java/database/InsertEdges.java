package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class InsertEdges {

	public static void main (String [] args) throws SQLException {
		String edges = "1.1,1.2,1\n"
				+ "1.2,1.3,2\n"
				+ "1.3,1.4,0\n"
				+ "1.3,1.20,1\n"
				+ "1.4,1.5,2\n"
				+ "1.5,1.6,2\n"
				+ "1.5,1.11,2\n"
				+ "1.5,1.12,2\n"
				+ "1.5,1.13,2\n"
				+ "1.6,1.7,2\n"
				+ "1.7,1.8,2\n"
				+ "1.8,1.9,2\n"
				+ "1.9,1.10,1\n"
				+ "1.9,1.13,0\n"
				+ "1.10,1.15,2\n"
				+ "1.10,1.20,2\n"
				+ "1.10,1.32,2\n"
				+ "1.11,1.10,1\n"
				+ "1.12,1.10,1\n"
				+ "1.13,1.14,1\n"
				+ "1.14,2.1,2\n"
				+ "1.14,2.7,2\n"
				+ "1.14,2.13,2\n"
				+ "1.14,2.18,2\n"
				+ "1.15,1.16,2\n"
				+ "1.16,1.17,2\n"
				+ "1.17,1.18,2\n"
				+ "1.18,1.19,2\n"
				+ "1.19,2.24,2\n"
				+ "1.19,2.38,2\n"
				+ "1.20,1.21,1\n"
				+ "1.20,1.26,0\n"
				+ "1.21,1.22,0\n"
				+ "1.22,1.23,2\n"
				+ "1.23,1.24,0\n"
				+ "1.24,1.25,2\n"
				+ "1.26,1.27,0\n"
				+ "1.27,1.28,2\n"
				+ "1.28,1.29,0\n"
				+ "1.29,1.30,2\n"
				+ "1.30,1.31,1\n"
				+ "1.31,2.29,2\n"
				+ "1.31,2.33,2\n"
				+ "1.32,1.33,0\n"
				+ "1.33,1.34,2\n"
				+ "1.34,1.35,0\n"
				+ "1.35,1.36,2\n"
				+ "1.36,1.37,1\n"
				+ "1.37,2.43,2\n"
				+ "2.1,2.2,0\n"
				+ "2.2,2.3,2\n"
				+ "2.3,2.4,0\n"
				+ "2.4,2.5,2\n"
				+ "2.5,2.6,1\n"
				+ "2.6,3.1,2\n"
				+ "2.6,3.7,2\n"
				+ "2.7,2.8,0\n"
				+ "2.8,2.9,2\n"
				+ "2.9,2.10,0\n"
				+ "2.10,2.11,2\n"
				+ "2.11,2.12,1\n"
				+ "2.12,3.14,2\n"
				+ "2.13,2.14,0\n"
				+ "2.14,2.15,2\n"
				+ "2.15,2.16,0\n"
				+ "2.16,2.17,1\n"
				+ "2.17,3.21,2\n"
				+ "2.18,2.19,0\n"
				+ "2.19,2.20,2\n"
				+ "2.20,2.21,0\n"
				+ "2.21,2.22,2\n"
				+ "2.22,2.23,1\n"
				+ "2.23,3.28,2\n"
				+ "2.24,2.25,0\n"
				+ "2.25,2.26,2\n"
				+ "2.26,2.27,0\n"
				+ "2.27,2.28,2\n"
				+ "2.29,2.30,0\n"
				+ "2.30,2.31,2\n"
				+ "2.31,2.32,2\n"
				+ "2.33,2.34,0\n"
				+ "2.34,2.35,2\n"
				+ "2.35,2.36,0\n"
				+ "2.36,2.37,2\n"
				+ "2.38,2.39,0\n"
				+ "2.39,2.40,2\n"
				+ "2.40,2.41,0\n"
				+ "2.41,2.42,2\n"
				+ "2.43,2.44,0\n"
				+ "2.44,2.45,2\n"
				+ "2.45,2.46,0\n"
				+ "2.46,2.47,2";
		
		Connection c = BNFConnector.getConnection().orElseThrow();
		
		 for(String edge : edges.split("\n")) {
			 
			String [] components = edge.split(",");
			System.out.println(Arrays.toString(components));
			PreparedStatement ps1 = c.prepareStatement("insert into edge (source_id, target_id, type, algorithm_id) values (?,?,?,1)");
			ps1.setInt(1, getNodeId(components[0],c));
			ps1.setInt(2, getNodeId(components[1],c));
			ps1.setInt(3, Integer.parseInt(components[2]));
			
			ps1.executeUpdate();
		}
	}
	
	public static int getNodeId(String code, Connection c) throws SQLException {
		PreparedStatement ps = c.prepareStatement("select id from node where object_id = ?");
		ps.setString(1, code);
		ResultSet rs = ps.executeQuery();
		rs.next();
		return rs.getInt("id");
		
	}
	
}
