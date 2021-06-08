package algorithm.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.ParseException;

public class Algorithm {

	public int id;
	public String name;
	public List<Node> nodes;
	public List<Edge> edges;
	
	public Algorithm(int id, String name, List<Node> nodes, List<Edge> edges) {
		super();
		this.id = id;
		this.name = name;
		this.nodes = nodes;
		this.edges = edges;
	}
	
	public Algorithm(int id, Connection c) throws SQLException, ParseException {

		PreparedStatement ps = c.prepareStatement("select * from algorithm where id = ?");
		ps.setInt(1, id);

		ResultSet rs = ps.executeQuery();
		rs.next();

		this.name = rs.getString("name");
		this.nodes = new ArrayList<>();
		this.edges = new ArrayList<>();
			PreparedStatement ps1 = c
					.prepareStatement("select id from node where algorithm_id = ?");
			ps1.setInt(1, id);
			ResultSet rs1 = ps1.executeQuery();
			while (rs1.next()) {
				this.nodes.add(new Node(rs1.getInt("id"), c));
			
			}
			
			PreparedStatement ps2 = c
					.prepareStatement("select * from edge where algorithm_id = ?");
			ps2.setInt(1, id);
			ResultSet rs2 = ps2.executeQuery();
			while (rs2.next()) {
				Node source = new Node(rs2.getInt("source_id"),c);
				Node target = new Node(rs2.getInt("target_id"),c);
				this.edges.add(new Edge(rs2.getInt("id"), source, target, rs2.getInt("type")));
			
			}


	}
	
	
}
