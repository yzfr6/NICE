package resource_providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.MedicinalProductUndesirableEffect;

import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import database.BNFConnector;
import resource_creators.ResourceCreator;

public class MedicinalProductUndesirableEffect_ResourceProvider implements IResourceProvider {

	@Override
	public Class<MedicinalProductUndesirableEffect> getResourceType() {
		return MedicinalProductUndesirableEffect.class;
	}

	@Search()
	public List<MedicinalProductUndesirableEffect> searchByMedication(
			@RequiredParam(name = "medication_identifier") StringParam m_id){
		List<MedicinalProductUndesirableEffect> list = new ArrayList<>();
		Connection conn = BNFConnector.getConnection().orElseThrow();

		try {
			PreparedStatement ps = conn.prepareStatement(
					"select * from snomed_class target "
					+ "inner join drug_has_side_effect dhse on dhse.target_id = target.id "
					+ "inner join snomed_class source on source.id = dhse.source_id "
					+ "inner join side_effect_frequency sef on dhse.side_effect_frequency_id = sef.id "
					+ "where source.id = "+ m_id.getValue());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				MedicinalProductUndesirableEffect se = new MedicinalProductUndesirableEffect();
				se.setSymptomConditionEffect(ResourceCreator.getCodeableConcept(rs.getString("target.bnf_name"),
						ResourceCreator.SNOMED_SYSTEM, rs.getString("target.snomed_code")))
						.setFrequencyOfOccurrence(ResourceCreator.getCodeableConcept(rs.getString("sef.name"),
								ResourceCreator.BNF_SYSTEM, rs.getInt("sef.id") + "")).setId(rs.getInt("target.id")+"");
				list.add(se);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
};