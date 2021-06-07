package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BNFQueries {

public static int getSnomedClass(String bnf_name, String bnf_uri, String type, String snomed_code, Connection conn) throws SQLException {
	PreparedStatement ps = conn.prepareStatement("select * from snomed_class where bnf_name = ?");
	ps.setString(1, bnf_name);
	ResultSet rs = ps.executeQuery();
	
	if (rs.next()) {
		return rs.getInt("id");
	} else {	
	PreparedStatement ps1 = conn.prepareStatement("insert into snomed_class (snomed_code, type, bnf_name, bnf_uri) values (?, ?, ?, ?)",
			PreparedStatement.RETURN_GENERATED_KEYS);
	ps1.setString(1, snomed_code);
	ps1.setString(2, type);
	ps1.setString(3, bnf_name);
	ps1.setString(4, bnf_uri);
	ps1.executeUpdate();
	ResultSet rs1 = ps1.getGeneratedKeys();
	rs1.next();
	return rs1.getInt(1);
	}
		
	}
	
	
public static int getSideEffectFrequency(String name, Connection conn) throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement("select * from side_effect_frequency where name = ?");
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		
		if (rs.next()) {
			return rs.getInt("id");
		} else {
			PreparedStatement ps1 = conn.prepareStatement("insert into side_effect_frequency (name) values (?)", PreparedStatement.RETURN_GENERATED_KEYS);
			ps1.setString(1, name);
			ps1.executeUpdate();
			ResultSet rs1 = ps1.getGeneratedKeys();
			rs1.next();
			return rs1.getInt(1);
		}	
	}
	
}
