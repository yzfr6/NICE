package OWL;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.util.ShortFormProvider;

public class Ontology_AxiomRetriever {

	public static Set<OWLSubClassOfAxiom> getSubCLASSAxiomsWithSuperclass(Stream<OWLOntology> onts, OWLClass c) {
		
		Set<OWLSubClassOfAxiom> returnSet = new HashSet<>();
		for (OWLSubClassOfAxiom ax : getSubCLASSAxioms(onts)) {
			if (ax.getSuperClass().equals(c)) {
				returnSet.add(ax);
			}
		}
		return returnSet;
	}

	public static Stream<OWLSubClassOfAxiom> getSubCLASSAxiomsWithSubclass(Stream<OWLOntology> onts, OWLClass c) {
		Set<OWLSubClassOfAxiom> returnSet = new HashSet<>();
		for (OWLSubClassOfAxiom ax : getSubCLASSAxioms(onts)) {
			if (ax.getSubClass().equals(c)) {
				returnSet.add(ax);
			}
		}
		return returnSet.stream();
	}

	public static Set<OWLSubClassOfAxiom> getSubclassAxiomsWithSubclass(Stream<OWLOntology> onts, OWLClass c) {
		Set<OWLSubClassOfAxiom> returnSet = new HashSet<>();
		for (OWLSubClassOfAxiom ax : getSubclassAxioms(onts)) {
			if (ax.getSubClass().equals(c)) {
				returnSet.add(ax);
			}
		}
		return returnSet;
	}

	public static Set<OWLSubClassOfAxiom> getSubclassAxioms(Stream<OWLOntology> onts) {

		Set<OWLSubClassOfAxiom> set = new HashSet<>();

		onts.forEach(o1 -> set.addAll(o1.logicalAxioms().filter(OWLSubClassOfAxiom.class::isInstance)
				.map(ax -> (OWLSubClassOfAxiom) ax).collect(Collectors.toSet())));

		return set;
	}

	public static Set<OWLSubClassOfAxiom> getSubCLASSAxioms(Stream<OWLOntology> onts) {

		Set<OWLSubClassOfAxiom> set = new HashSet<>();

		onts.forEach(o1 -> o1.logicalAxioms().forEach(axiom1 -> {

			if (axiom1 instanceof OWLSubClassOfAxiom) {
				OWLSubClassOfAxiom axiom2 = (OWLSubClassOfAxiom) axiom1;

				OWLClassExpression subCE = axiom2.getSubClass();
				OWLClassExpression supCE = axiom2.getSuperClass();

				if (subCE instanceof OWLClass && supCE instanceof OWLClass) {
					set.add(axiom2);
				}
			}
		}));

		return set;
	}


	public static String prettyPrint(OWLAxiom e, Stream<OWLOntology> onts, OWLDataFactory df) {
		OWLObjectRenderer op = new DLSyntaxObjectRenderer();

		op.setShortFormProvider(new ShortFormProvider() {

			@Override
			public String getShortForm(OWLEntity entity) {
				return Ontology_EntityRetriever.getRDFSLabel(onts, entity, df).orElse("");

			}
		});
		return op.render(e);
	}
}
