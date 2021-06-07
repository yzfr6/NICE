package FHIRResourceHelper;

import java.util.List;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Resource;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

public class ResourceCreator {

	public static final String SNOMED_SYSTEM = "http://snomed.info/sct";
	public static final String LOINC_SYSTEM = "http://loinc.org";
	
	public static CodeableConcept getCodeableConcept(String text, String coding_system, String coding_code) {
		return new CodeableConcept().setText(text)
				.setCoding(List.of(new Coding().setCode(coding_code).setSystem(coding_system)));
	}
	
	
	public static JSONObject serialize(Resource r) throws ParseException {
		// Create a FHIR context
		FhirContext ctx = FhirContext.forR4();

		// Instantiate a new JSON parser
		IParser parser = ctx.newJsonParser();

		// Serialize it
		String serialized = parser.encodeResourceToString(r);
		JSONParser p = new JSONParser();  
		JSONObject json = (JSONObject) p.parse(serialized);  
		
		return json;
		

	}


}
