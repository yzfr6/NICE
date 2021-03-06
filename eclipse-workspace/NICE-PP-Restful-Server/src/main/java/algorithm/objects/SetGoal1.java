package algorithm.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hl7.fhir.r4.model.Goal;
import org.hl7.fhir.r4.model.Goal.GoalLifecycleStatus;
import org.hl7.fhir.r4.model.Goal.GoalTargetComponent;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Quantity.QuantityComparator;
import org.hl7.fhir.r4.model.Reference;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import FHIRResourceHelper.ResourceCreator;

public class SetGoal1 {

	public JSONObject goal;
	public int id;

	public SetGoal1(int id, JSONObject goal) {
		// super("algorithm_set_goal_1");
		this.goal = goal;
		this.id = id;
	}

	public SetGoal1(Connection c, int entity_id) throws SQLException, ParseException {
		// super("algorithm_set_goal_1");
		// this.algorithm_entity_id = entity_id;
		PreparedStatement ps1 = c.prepareStatement(
				"select * from algorithm_set_goal_1 acd1 " + "inner join observation o on o.id = acd1.observation_id "
						+ "inner join comparator c on acd1.comparator_id = c.id "
						+ "inner join unit u on acd1.unit_id = u.id " + "where acd1.algorithm_entity_id = ?");
		ps1.setInt(1, entity_id);

		ResultSet rs1 = ps1.executeQuery();

		rs1.next();

		this.id = rs1.getInt("acd1.id");

		Goal.GoalTargetComponent gt = new GoalTargetComponent();
		gt.setMeasure(ResourceCreator.getCodeableConcept(rs1.getString("o.name"), ResourceCreator.LOINC_SYSTEM,
				rs1.getString("o.code")));
		gt.setId(rs1.getInt("c.id") + "");
		Quantity q = new Quantity();
		q.setComparator(QuantityComparator.fromCode(rs1.getString("c.name")));
		q.setUnit(rs1.getString("u.name"));
		q.setValue(rs1.getDouble("acd1.value"));
		gt.setDetail(q);

		Goal g = new Goal();
		g.setId(rs1.getInt("c.id") + "");
		g.setTarget(List.of(gt));
		g.setDescription(ResourceCreator.getCodeableConcept(rs1.getString("acd1.description"),
				"https://nice.org.uk", "0000000"));
		g.setSubject(new Reference(new Patient()));
		g.setLifecycleStatus(GoalLifecycleStatus.ACTIVE);

		this.goal = ResourceCreator.serialize(g);

	}

}
