package algorithm.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.parser.ParseException;

public class Node {

	public int id;
	public String code;
	public String text;
	public String type;

	public String content_type;
	public List<Entity> entities;

	public Node(int id, String code, String text, String type, String contentType, List<Entity> entities) {
		super();
		this.id = id;
		this.code = code;
		this.text = text;
		this.type = type;
		this.content_type = contentType;
		this.entities = entities;
	}

	public Node(int id, Connection c) throws SQLException, ParseException {

		PreparedStatement ps = c.prepareStatement("select * from node where id = ?");
		ps.setInt(1, id);

		ResultSet rs = ps.executeQuery();
		rs.next();

		this.code = rs.getString("object_id");
		this.text = rs.getString("text");
		this.type = rs.getString("type");
		this.entities = new ArrayList<>();
		this.id = id;
		String content_type = rs.getString("content_type");
		this.content_type = content_type;

		switch (content_type) {
		case "atomic": {
			PreparedStatement ps1 = c
					.prepareStatement("select algorithm_entity_id from entity_atomic where node_id = ?");
			ps1.setInt(1, id);
			ResultSet rs1 = ps1.executeQuery();
			rs1.next();

			this.entities.add(Entity.create(rs1.getInt("algorithm_entity_id"), c));
			break;
		}
		case "conjunction": {
			PreparedStatement ps2 = c
					.prepareStatement("select algorithm_entity_id from entity_conjunction where node_id = ?");
			ps2.setInt(1, id);
			ResultSet rs2 = ps2.executeQuery();
			while (rs2.next()) {

				this.entities.add(Entity.create(rs2.getInt("algorithm_entity_id"), c));
			}
			break;
		}
		case "disjunction": {
			PreparedStatement ps3 = c
					.prepareStatement("select algorithm_entity_id from entity_disjunction where node_id = ?");
			ps3.setInt(1, id);
			ResultSet rs3 = ps3.executeQuery();
			while (rs3.next()) {

				this.entities.add(Entity.create(rs3.getInt("algorithm_entity_id"), c));
			}
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + content_type);
		}

	}
}
