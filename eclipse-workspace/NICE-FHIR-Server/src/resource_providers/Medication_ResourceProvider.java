package resource_providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Medication;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import database.BNFConnector;
import resource_creators.ResourceCreator;

public class Medication_ResourceProvider implements IResourceProvider {

	@Override
	public Class<Medication> getResourceType() {
		return Medication.class;
	}

	@Read()
	public Medication getResourceById(@IdParam IdType theId) throws SQLException {
		System.out.println("REQUEST FOR MED - "+theId.getIdPart());
		Connection conn = BNFConnector.getConnection().orElseThrow();

		PreparedStatement ps = conn.prepareStatement("select * from snomed_class where id = " + theId.getIdPart());
		System.out.println(ps);
		ResultSet rs = ps.executeQuery();

		Medication drug = new Medication();
		if (rs.next()) {
			drug.setCode(ResourceCreator.getCodeableConcept(rs.getString("bnf_name"), ResourceCreator.SNOMED_SYSTEM,
					rs.getString("snomed_code")));
			drug.setId(theId);
		}
		return drug;
	}

}