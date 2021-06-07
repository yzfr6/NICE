package resource_providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.MedicinalProductContraindication;

import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import database.BNFConnector;
import resource_creators.ResourceCreator;

public class MedicinalProductContraindication_ResourceProvider implements IResourceProvider {

	@Override
	public Class<MedicinalProductContraindication> getResourceType() {
		return MedicinalProductContraindication.class;
	}

	@Search()
	public List<MedicinalProductContraindication> searchByMedication (
			@RequiredParam(name = "medication_identifier") StringParam m_id){
		List<MedicinalProductContraindication> list = new ArrayList<>();
		Connection conn = BNFConnector.getConnection().orElseThrow();

		try {
			PreparedStatement ps = conn.prepareStatement(
					"select * from snomed_class c "
					+ "inner join drug_has_contraindication dhc on dhc.target_id = c.id "
					+ "inner join snomed_class d on d.id = dhc.source_id "
					+ "where d.id = "+ m_id.getValue());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				MedicinalProductContraindication se = new MedicinalProductContraindication();
				se.setDisease(ResourceCreator.getCodeableConcept(rs.getString("c.bnf_name"),
						ResourceCreator.SNOMED_SYSTEM, rs.getString("c.snomed_code"))).setId(rs.getInt("c.id")+"");
				list.add(se);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
};