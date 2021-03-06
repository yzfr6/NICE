package algorithm.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.parser.ParseException;

public class Entity {

	//public int entity_id;
	public String type;
	public int entity_id;

	public ClinicianDecision1 clinician_decision_1;
	public ConditionDecision1  condition_decision_1;
	public ConsiderMedication1 consider_medication_1;
	public ConsiderMedication2 consider_medication_2;
	public MedicationChoice1 medication_choice_1;
	public MedicationContraindicationDecision1 medication_contraindication_decision_1;
	public MedicationContraindicationDecision2 medication_contraindication_decision_2;
	public MedicationNotToleratedDecision1 medication_not_tolerated_decision_1;
	public MedicationNotToleratedDecision2 medication_not_tolerated_decision_2;
	public ObservationDecision1 observation_decision_1;
	public OfferMedication1 offer_medication_1;
	public SetAlert1 set_alert_1;
	public SetAlert2 set_alert_2;
	public SetGoal1 set_goal_1;
	public StopMedication1 stop_medication_1;
	
	public Entity(String type, int entity_id) {
		super();
		this.type = type;
		this.entity_id = entity_id;
	}

	public static Entity create(int entity_id, Connection c) throws SQLException, ParseException {
		
		PreparedStatement ps = c.prepareStatement("select * from algorithm_entity where id = ?");
		ps.setInt(1, entity_id);

		ResultSet rs = ps.executeQuery();
		rs.next();

		EntityType type = EntityType.getType(rs.getString("type"));
		Entity entity = new Entity(type.getValue(), entity_id);
	
		switch (type) {
		case CLINICIAN_DECISION_1: {

			entity.clinician_decision_1 = new ClinicianDecision1(c, entity_id);
			//entity.id =entity.clinician_decision_1.id;
			break;
		}
		case CONDITION_DECISION_1: {

			entity.condition_decision_1 = new ConditionDecision1(c, entity_id);
			//entity.id =entity.condition_decision_1.id;
			break;
		}
		case CONSIDER_MEDICATION_1: {

			entity.consider_medication_1 = new ConsiderMedication1(c, entity_id);
			//entity.id =entity.consider_medication_1.id;
			break;
		}
		case CONSIDER_MEDICATION_2: {

			entity.consider_medication_2 = new ConsiderMedication2(c, entity_id);
			//entity.id =entity.consider_medication_2.id;
			break;
		}
		case MEDICATION_CHOICE_1: {

			entity.medication_choice_1 = new MedicationChoice1(c, entity_id);
			//entity.id =entity.medication_choice_1.id;
			break;
		}
		case MEDICATION_CONTRAINDICATION_DECISION_1: {

			entity.medication_contraindication_decision_1 = new MedicationContraindicationDecision1(c, entity_id);
			//entity.id =entity.medication_contraindication_decision_1.id;
			break;
		}
		case MEDICATION_CONTRAINDICATION_DECISION_2: {

			entity.medication_contraindication_decision_2 = new MedicationContraindicationDecision2(c, entity_id);
			//entity.id =entity.medication_contraindication_decision_2.id;
			break;
		}
		case MEDICATION_NOT_TOLERATED_1: {

			entity.medication_not_tolerated_decision_1 = new MedicationNotToleratedDecision1(c, entity_id);
			//entity.id =entity.medication_not_tolerated_decision_1.id;
			break;
		}
		case MEDICATION_NOT_TOLERATED_2: {

			entity.medication_not_tolerated_decision_2 = new MedicationNotToleratedDecision2(c, entity_id);
			//entity.id =entity.medication_not_tolerated_decision_2.id;
			break;
		}
		case OBSERVATION_DECISION_1: {

			entity.observation_decision_1 = new ObservationDecision1(c, entity_id);
			//entity.id =entity.observation_decision_1.id;
			break;
		}
		case OFFER_MEDICATION_1: {

			entity.offer_medication_1 = new OfferMedication1(c, entity_id);
			//entity.id =entity.offer_medication_1.id;
			break;
		}
		case SET_ALERT_1: {

			entity.set_alert_1 = new SetAlert1(c, entity_id);
			//entity.id =entity.set_alert_1.id;
			break;
		}
		case SET_ALERT_2: {

			entity.set_alert_2 = new SetAlert2(c, entity_id);
			//entity.id =entity.set_alert_2.id;
			break;
		}
		case SET_GOAL_1: {

			entity.set_goal_1 = new SetGoal1(c, entity_id);
			//entity.id =entity.set_goal_1.id;
			break;
		}
		case STOP_MEDICATION_1: {
			entity.stop_medication_1 = new StopMedication1(c, entity_id);
			//entity.id =entity.stop_medication_1.id;
			break;
		}

		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
		
		return entity;

	}

}
