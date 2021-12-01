package OWL;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.ShortFormProvider;

import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImplString;

public class Ontology_EntityRetriever {

	public static Set<OWLClass> getDirectSubclassesSetExcludingBottom(OWLClass c, OWLReasoner r) {
		return getDirectSubclasses(c, r).filter(c1-> !c1.isOWLNothing()).collect(Collectors.toSet());
	}
	
	public static Stream<OWLClass> getDirectSubclasses(OWLClass c, OWLReasoner r) {
		return r.getSubClasses(c, true).entities();
	}
	
	public static Stream<OWLClass> getSubclasses(OWLClass c, OWLReasoner r) {
		return r.getSubClasses(c, false).entities();
	}
	
	public static Stream<OWLClass> getDirectSuperclasses(OWLClass c, OWLReasoner r) {
		return r.getSuperClasses(c, true).entities();
	}
	public static Stream<OWLClass> getSuperclasses(OWLClass c, OWLReasoner r) {
		return r.getSuperClasses(c, false).entities();
	}

	public static Stream<OWLObjectIntersectionOf> getSuperClassOWLObjectIntersectionOfs(OWLClass c,
			Stream<OWLOntology> onts) {

		return Ontology_AxiomRetriever.getSubclassAxiomsWithSubclass(onts, c).stream().map(ax -> ax.getSuperClass())
				.filter(ce -> ce instanceof OWLObjectIntersectionOf).map(ce -> (OWLObjectIntersectionOf) ce);

	}

	public static OWLObjectProperty addOWLObjectPropertyWithLabel(OWLOntology o, IRI iri, String label) {
		OWLDataFactory df = o.getOWLOntologyManager().getOWLDataFactory();
		OWLOntologyManager man = o.getOWLOntologyManager();
		OWLObjectProperty c = df.getOWLObjectProperty(iri);
		man.addAxiom(o, df.getOWLDeclarationAxiom(c));
		man.addAxiom(o,
				df.getOWLAnnotationAssertionAxiom(df.getRDFSLabel(), c.getIRI(), new OWLLiteralImplString(label)));

		return c;

	}
	
	public static OWLAnnotationProperty addOWLAnnotationPropertyWithLabel(OWLOntology o, IRI iri, String label) {
		OWLDataFactory df = o.getOWLOntologyManager().getOWLDataFactory();
		OWLOntologyManager man = o.getOWLOntologyManager();
		OWLAnnotationProperty c = df.getOWLAnnotationProperty(iri);
		man.addAxiom(o, df.getOWLDeclarationAxiom(c));
		man.addAxiom(o,
				df.getOWLAnnotationAssertionAxiom(df.getRDFSLabel(), c.getIRI(), new OWLLiteralImplString(label)));

		return c;

	}

	public static OWLDataProperty addOWLDataPropertyWithLabel(OWLOntology o, IRI iri, String label) {
		OWLDataFactory df = o.getOWLOntologyManager().getOWLDataFactory();
		OWLOntologyManager man = o.getOWLOntologyManager();
		OWLDataProperty c = df.getOWLDataProperty(iri);
		man.addAxiom(o, df.getOWLDeclarationAxiom(c));
		man.addAxiom(o,
				df.getOWLAnnotationAssertionAxiom(df.getRDFSLabel(), c.getIRI(), new OWLLiteralImplString(label)));

		return c;

	}

	public static OWLNamedIndividual addOWLIndividualWithLabel(OWLOntology o, IRI iri, String label) {
		OWLDataFactory df = o.getOWLOntologyManager().getOWLDataFactory();
		OWLOntologyManager man = o.getOWLOntologyManager();
		OWLNamedIndividual c = df.getOWLNamedIndividual(iri);
		man.addAxiom(o, df.getOWLDeclarationAxiom(c));
		man.addAxiom(o,
				df.getOWLAnnotationAssertionAxiom(df.getRDFSLabel(), c.getIRI(), new OWLLiteralImplString(label)));

		return c;
	}

	public static OWLAxiom getAnnotatedAxiom(OWLAxiom ax, OWLOntology o, String annotationValue,
			OWLAnnotationProperty annotationProperty) {
		OWLDataFactory df = o.getOWLOntologyManager().getOWLDataFactory();
		return ax.getAnnotatedAxiom(
				Collections.singleton(df.getOWLAnnotation(annotationProperty, df.getOWLLiteral(annotationValue))));
	}

	public static OWLClass addOWLClassWithLabel(OWLOntology o, IRI iri, String label) {
		OWLDataFactory df = o.getOWLOntologyManager().getOWLDataFactory();
		OWLOntologyManager man = o.getOWLOntologyManager();
		OWLClass c = df.getOWLClass(iri);
		man.addAxiom(o, df.getOWLDeclarationAxiom(c));
		man.addAxiom(o, df.getOWLAnnotationAssertionAxiom(df.getRDFSLabel(), iri, new OWLLiteralImplString(label)));

		return c;
	}
	
	
	public static Pair<OWLClass,Set<OWLAxiom>> addOWLClassWithLabelNoDecl(OWLOntology o, IRI iri, String label) {
		OWLDataFactory df = o.getOWLOntologyManager().getOWLDataFactory();
		OWLOntologyManager man = o.getOWLOntologyManager();
		OWLClass c = df.getOWLClass(iri);
		Set<OWLAxiom> set = new HashSet<>();
		set.add(df.getOWLDeclarationAxiom(c));
		set.add(df.getOWLAnnotationAssertionAxiom(df.getRDFSLabel(), iri, new OWLLiteralImplString(label)));

		return Pair.of(c,set);
	}
	
	public static void addAnnotationToClass(OWLOntology o, OWLClass c, String label, OWLAnnotationProperty ap) {
		OWLDataFactory df = o.getOWLOntologyManager().getOWLDataFactory();
		OWLOntologyManager man = o.getOWLOntologyManager();
		
		man.addAxiom(o, df.getOWLAnnotationAssertionAxiom(ap, c.getIRI(), new OWLLiteralImplString(label)));

	}

	public static Optional<String> getRDFSLabel(Stream<OWLOntology> onts, OWLEntity e, OWLDataFactory df) {

		Optional<OWLAnnotation> oa;

		oa = EntitySearcher.getAnnotations(e, onts, df.getRDFSLabel()).filter(a -> a.getValue() instanceof OWLLiteral)
				.findFirst();

		if (oa.isPresent()) {
			return Optional.of(((OWLLiteral) oa.get().getValue()).getLiteral());
		} else {
			return Optional.empty();
		}

	}

	public static Stream<String> getAnnotationValues(Stream<OWLOntology> o, OWLEntity e, OWLAnnotationProperty a) {
		return EntitySearcher.getAnnotations(e, o, a).filter(oa -> oa.getValue() instanceof OWLLiteral)
				.map(oa -> ((OWLLiteral) oa.getValue()).getLiteral());
	}
	
	public static Stream<String> getAnnotationValues(OWLOntology o, OWLEntity e, OWLAnnotationProperty a) {
		return EntitySearcher.getAnnotations(e, o, a).filter(oa -> oa.getValue() instanceof OWLLiteral)
				.map(oa -> ((OWLLiteral) oa.getValue()).getLiteral());
	}
	
	public static Stream<OWLAnnotationValue> getOWLAnnotationValues(OWLOntology o, OWLEntity e, OWLAnnotationProperty a) {
		
		return EntitySearcher.getAnnotations(e, o, a)
				.map(oa -> oa.getValue());
	}

	public static Stream<String> getAnnotationValues(OWLOntology o, OWLEntity e, Stream<OWLAnnotationProperty> as) {

		Set<String> returnSet = new HashSet<>();
		as.forEach(a -> {
			EntitySearcher.getAnnotations(e, o, a).forEach(oa -> {
				if (oa.getValue() instanceof OWLLiteral) {
					returnSet.add(((OWLLiteral) oa.getValue()).getLiteral());
				}
			});
		});
		return returnSet.stream();

	}

	public static Optional<OWLClass> getOWLClassFromLabel(Stream<OWLOntology> onts, OWLAnnotationProperty oap, String l, OWLDataFactory df) {

		for (OWLOntology o : onts.toArray(OWLOntology[]::new)) {
			for (OWLClass c : o.classesInSignature().collect(Collectors.toSet())) {
				if (!c.isOWLThing()) {
					Optional<String> label = getAnnotationValues(Stream.of(o), c, oap).findFirst();

					if (label.isPresent() && label.get().equals(l)) {
						return Optional.of(c);
					}
				}
			}
		}
		return Optional.empty();
	}
	
	public static Optional<OWLClass> getOWLClassFromLabel(Stream<OWLOntology> onts, String l, OWLDataFactory df) {

		for (OWLOntology o : onts.toArray(OWLOntology[]::new)) {
			for (OWLClass c : o.classesInSignature().collect(Collectors.toSet())) {
				if (!c.isOWLThing()) {
					Optional<String> label = getRDFSLabel(Stream.of(o), c, df);

					if (label.isPresent() && label.get().equals(l)) {
						return Optional.of(c);
					}
				}
			}
		}
		return Optional.empty();
	}
	
	public static OWLClass getOWLClassFromLabelOrAdd(Stream<OWLOntology> onts, String l, OWLDataFactory df, IRI iri) {

		OWLOntology o1 = null;
		boolean first = true;
		for (OWLOntology o : onts.toArray(OWLOntology[]::new)) {
			if (first) {
				o1 = o;
				first = false;
			}
			for (OWLClass c : o.classesInSignature().collect(Collectors.toSet())) {
				if (!c.isOWLThing()) {
					Optional<String> label = getRDFSLabel(Stream.of(o), c, df);

					if (label.isPresent() && label.get().equals(l)) {
						return c;
					}
				}
			}
		}
		return addOWLClassWithLabel(o1, iri, l);
	}

	public static Optional<OWLObjectProperty> getOWLObjectPropertyFromLabel(Stream<OWLOntology> onts, String l,
			OWLDataFactory df)  {

		for (OWLOntology o : onts.toArray(OWLOntology[]::new)) {
			for (OWLObjectProperty c : o.objectPropertiesInSignature().collect(Collectors.toSet())) {
				if (!c.isOWLTopObjectProperty()) {
					Optional<String> label = getRDFSLabel(Stream.of(o), c, df);

					if (label.isPresent() && label.get().equals(l)) {
						return Optional.of(c);
					}
				}
			}
		}
		return Optional.empty();

	}

	public static Optional<OWLDataProperty> getOWLDataPropertyFromLabel(Stream<OWLOntology> onts, String l,
			OWLDataFactory df){

		for (OWLOntology o : onts.toArray(OWLOntology[]::new)) {
			for (OWLDataProperty c : o.dataPropertiesInSignature().collect(Collectors.toSet())) {
				if (!c.isOWLTopDataProperty()) {
					Optional<String> label = getRDFSLabel(Stream.of(o), c, df);

					if (label.isPresent() && label.get().equals(l)) {
						return Optional.of(c);
					}
				}
			}
		}
		return Optional.empty();
	}

	public static String prettyPrint(OWLObject e, OWLOntology o, OWLDataFactory df, Imports imp) {
		OWLOntologyManager man = o.getOWLOntologyManager();
		Stream<OWLOntology> onts = imp.equals(Imports.INCLUDED) ? man.ontologies() : Stream.of(o);

		OWLObjectRenderer op = new DLSyntaxObjectRenderer();
		op.setShortFormProvider(new ShortFormProvider() {

			@Override
			public String getShortForm(OWLEntity entity) {
				return getRDFSLabel(onts, entity, df).orElse("");

			}
		});
		return op.render(e);
	}

	public static OWLObjectRenderer getDLSyntaxRenderer(OWLOntologyManager man) {
		OWLObjectRenderer op = new DLSyntaxObjectRenderer();
		op.setShortFormProvider(new ShortFormProvider() {

			@Override
			public String getShortForm(OWLEntity entity) {
				return Ontology_EntityRetriever.getRDFSLabel(man.ontologies(), entity, man.getOWLDataFactory())
						.orElse(entity.getIRI().toString());
			}
		});
		return op;
	}

	public static OWLObjectRenderer getManchesterSyntaxRenderer(OWLOntologyManager man) {
		OWLObjectRenderer op = new ManchesterOWLSyntaxOWLObjectRendererImpl();
		op.setShortFormProvider(new ShortFormProvider() {

			@Override
			public String getShortForm(OWLEntity entity) {
				return Ontology_EntityRetriever.getRDFSLabel(man.ontologies(), entity, man.getOWLDataFactory())
						.orElse(entity.getIRI().toString());
			}
		});
		return op;
	}

}
