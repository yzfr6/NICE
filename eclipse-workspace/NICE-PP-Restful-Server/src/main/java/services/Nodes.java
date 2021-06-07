 package services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.parser.ParseException;

import algorithm.objects.Node;
import database.BNFConnector;


@Path("/node/")
public class Nodes {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response get(@PathParam("id") int db_id) {

		Optional<Connection> c = null;
		try {
			c = BNFConnector.getConnection();
			if (!c.isPresent()) {
				return ResponseHelper.get500("Could not connect to the DB");
			}

			Node j = NodeQueries.getNode(db_id, c.get());

			return ResponseHelper.get200(j, MediaType.APPLICATION_JSON_TYPE);

		} catch (SQLException se) {
			se.printStackTrace();
			return ResponseHelper.get500("SQL exception thrown\n" + se.getMessage());
		} catch (Exception e) {

			e.printStackTrace();
			return ResponseHelper.get500("Exception thrown \n" + e.getMessage());
		} finally {
			if (c != null && c.isPresent()) {
				try {
					c.get().close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

class NodeQueries {
	
	public static Node getNode(int db_id, Connection c) throws SQLException, ParseException {
		return new Node(db_id, c);
	}
}
