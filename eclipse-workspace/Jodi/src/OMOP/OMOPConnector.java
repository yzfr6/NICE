package OMOP;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class OMOPConnector {
	
	public static Optional<Connection> getConnection() {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Properties pro = new Properties();
			pro.load(new FileInputStream(new File("/Users/nice/Developer/databases/OMOPConnection.properties")));
			
			Connection conn;
			conn = DriverManager.getConnection(pro.getProperty("url"), pro.getProperty("user"), pro.getProperty("password"));
			return Optional.of(conn);
		} catch (SQLException | IOException | ClassNotFoundException e) {
			
			e.printStackTrace();
			return Optional.empty();
		}
		
		
	}
	
}
