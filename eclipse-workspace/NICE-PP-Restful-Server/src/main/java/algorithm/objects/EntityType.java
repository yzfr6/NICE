package algorithm.objects;

public enum EntityType {

	CLINICIAN_DECISION_1("algorithm_clinician_decision_1"),
	CONDITION_DECISION_1("algorithm_condition_decision_1"),
	CONSIDER_MEDICATION_1("algorithm_consider_medication_1"),
	CONSIDER_MEDICATION_2("algorithm_consider_medication_2"),
	MEDICATION_CHOICE_1("algorithm_medication_choice_1"),
	MEDICATION_CONTRAINDICATION_DECISION_1("algorithm_medication_contraindication_decision_1"),
	MEDICATION_CONTRAINDICATION_DECISION_2("algorithm_medication_contraindication_decision_2"),
	MEDICATION_NOT_TOLERATED_1("algorithm_medication_not_tolerated_decision_1"),
	MEDICATION_NOT_TOLERATED_2("algorithm_medication_not_tolerated_decision_2"),
	OBSERVATION_DECISION_1("algorithm_observation_decision_1"),
	OFFER_MEDICATION_1("algorithm_offer_medication_1"),
	SET_ALERT_1("algorithm_set_alert_1"),
	SET_ALERT_2("algorithm_set_alert_2"),
	SET_GOAL_1("algorithm_set_goal_1"),
	STOP_MEDICATION_1("algorithm_stop_medication_1");
	
	private final String value;

	EntityType(String s) {
		value = s;
    } 
	
	public  String getValue() {
		return this.value;
	}
	
	public static EntityType getType(String s) {
		for (EntityType e : EntityType.values()) {
			if (e.getValue().equals(s)) {
				return e;
			}
		}
		throw new IllegalArgumentException();
	}
}
