package Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class BNFConnector {
	
	public static Optional<Connection> getConnection() {
		
		try {
			
			Properties pro = new Properties();
			pro.load(new FileInputStream(new File("/Users/nice/Developer/databases/BNFConnection.properties")));
			
			Connection conn;
			conn = DriverManager.getConnection(pro.getProperty("url"), pro.getProperty("user"), pro.getProperty("password"));
			return Optional.of(conn);
		} catch (SQLException | IOException e) {
			
			e.printStackTrace();
			return Optional.empty();
		}
		
		
	}
	
}
