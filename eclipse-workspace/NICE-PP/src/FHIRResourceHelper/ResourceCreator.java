package FHIRResourceHelper;

import java.util.List;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;

public class ResourceCreator {

	public static final String SNOMED_SYSTEM = "http://snomed.info/sct";
	public static CodeableConcept getCodeableConcept(String text, String coding_system, String coding_code) {
		return new CodeableConcept().setText(text)
				.setCoding(List.of(new Coding().setCode(coding_code).setSystem(coding_system)));
	}
	


}
