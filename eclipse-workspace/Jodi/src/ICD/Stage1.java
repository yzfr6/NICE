package ICD;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import OWL.IRI_Creator;
import OWL.Ontology_EntityRetriever;
import OWL.Ontology_LoaderSaver;
import XML.XMLContentRetriever;
import XML.XMLLoader;

/**
 * This class reads the icd10cm_tabular_2021 formatted XML file and outputs an
 * owl ontology version of the file
 * 
 * @author jaredleo
 *
 */
// /Users/jaredleo/Developer/eclipse_workspace/Jodi/resources/icd10cm_neoplasm_2021.xml
public class Stage1 {

	/*
	 * The ontology IRI
	 */
	static String prefix = "http://ICD10.com";

	/*
	 * The ontolgoy entity prefix
	 */
	static String entityPrefix = prefix + "#";

	/*
	 * The ICD ontology object that will be filled
	 */
	static OWLOntology o;

	/*
	 * A manager for the ontology to add axioms to the ontology
	 */
	static OWLOntologyManager man;

	/**
	 * The ontology's data factory to help building axioms
	 */
	static OWLDataFactory df;

	/*
	 * An annotation property to store the descriptions
	 */
	static OWLAnnotationProperty description;

	static IRI_Creator iric;

	/**
	 * Returns an entity IRI of the form entityPrefix + suffix
	 * 
	 * @param suffix
	 * @return an IRI object
	 */
	public static IRI getIRI(String suffix) {

		return IRI.create(new String(entityPrefix + suffix).replaceAll(" ", ""));
	}

	/**
	 * Returns a default IRI built from the iric
	 * 
	 * @return
	 */
	public static IRI getIRI() {

		return iric.getNextIRI();

	}

	/**
	 * Converts the given xml file to an owl ontology, saved in the output file
	 * given
	 * 
	 * @param pathToXML the icd10cm_tabular_2021 xml file path
	 * @param output    the output ontology file path
	 */
	public static void convert(File pathToXML, File output) {
		try {

			/**
			 * The top element in the xml file
			 */
			Element e = XMLLoader.loadXMLDocument(pathToXML);

			/*
			 * Initialise the ontology and its helpers
			 */
			man = OWLManager.createOWLOntologyManager();
			o = man.createOntology(IRI.create(prefix));
			df = man.getOWLDataFactory();
			iric = new IRI_Creator(o, entityPrefix);

			OWLClass icd10Tabular = Ontology_EntityRetriever.addOWLClassWithLabel(o, getIRI("ICD10Tabular"),
					"ICD10Tabular");
			/*
			 * Declare class and properties that will be used later
			 */
			OWLClass chapter = Ontology_EntityRetriever.addOWLClassWithLabel(o, getIRI(), "Chapter");
			man.addAxiom(o, df.getOWLSubClassOfAxiom(chapter, icd10Tabular));

			description = Ontology_EntityRetriever.addOWLAnnotationPropertyWithLabel(o, getIRI("icd10Description"),
					"icd10Description");
			Ontology_EntityRetriever.addAnnotationToClass(o, chapter, "Chapter", description);

			/*
			 * Add classes and object properties to the ontology
			 */
			/*OWLClass c1 = Ontology_EntityRetriever.addOWLClassWithLabel(o, getIRI("InclusionTerm"), "InclusionTerm");

			OWLClass c2 = Ontology_EntityRetriever.addOWLClassWithLabel(o, getIRI("ExclusionTerm1"), "ExclusionTerm1");
			OWLClass c3 = Ontology_EntityRetriever.addOWLClassWithLabel(o, getIRI("ExclusionTerm2"), "ExclusionTerm2");
			man.addAxiom(o, df.getOWLSubClassOfAxiom(c1, icd10Tabular));
			man.addAxiom(o, df.getOWLSubClassOfAxiom(c2, icd10Tabular));
			man.addAxiom(o, df.getOWLSubClassOfAxiom(c3, icd10Tabular));

			Ontology_EntityRetriever.addOWLObjectPropertyWithLabel(o, getIRI("hasInclusionTerm"), "hasInclusionTerm");
			Ontology_EntityRetriever.addOWLObjectPropertyWithLabel(o, getIRI("hasExclusionTerm1"), "hasExclusionTerm1");
			Ontology_EntityRetriever.addOWLObjectPropertyWithLabel(o, getIRI("hasExclusionTerm2"), "hasExclusionTerm2");

			Ontology_EntityRetriever.addAnnotationToClass(o, c1, "InclusionTerm", description);
			Ontology_EntityRetriever.addAnnotationToClass(o, c2, "ExclusionTerm1", description);
			Ontology_EntityRetriever.addAnnotationToClass(o, c3, "ExclusionTerm2", description);*/
			/**
			 * Store all the axioms that will be added to the ontology in a set so they can
			 * be added in bulk at the end to reduce time
			 */
			Set<OWLAxiom> axiomsToAdd = new HashSet<>();

			/**
			 * A set of disjoint chapters
			 */
			// Set<OWLClass> disjointChapters = new HashSet<>();

			/**
			 * For each element called 'chapter' in the xml file
			 */
			for (Element e1 : XMLContentRetriever.getDirectChildElements(e, "chapter")) {

				/*
				 * Get the chapter's name
				 */
				String chapterName = "Chapter "
						+ XMLContentRetriever.getDirectChildElements(e1, "name").get(0).getTextContent();

				/*
				 * Get the chapter's description
				 */
				String desc = XMLContentRetriever.getDirectChildElements(e1, "desc").get(0).getTextContent();

				/*
				 * Create the class to represent the chapter
				 */
				OWLClass chapterSub = Ontology_EntityRetriever.addOWLClassWithLabel(o, getIRI(), chapterName);
				Ontology_EntityRetriever.addAnnotationToClass(o, chapterSub, desc, description);
				/**
				 * add it to the disjoint chapters set (each class in this set will be disjoint
				 * from eachother)
				 */
				// disjointChapters.add(chapterSub);

				/*
				 * Declare this class as a sub class of the main chapter class defined above
				 */
				axiomsToAdd.add(df.getOWLSubClassOfAxiom(chapterSub, chapter));

				/*
				 * A set to store the chapters disjoint sections
				 */
				// Set<OWLClass> disjointSections = new HashSet<>();

				/*
				 * For each section within the chapter
				 */
				for (Element e2 : XMLContentRetriever.getDirectChildElements(e1, "section")) {

					/*
					 * Get the section's ID
					 */
					String id = e2.getAttribute("id");

					/*
					 * Get the section's description
					 */
					String desc2 = XMLContentRetriever.getDirectChildElements(e2, "desc").get(0).getTextContent();

					/*
					 * Create the class to represent the section
					 */
					OWLClass sectionSub = Ontology_EntityRetriever.addOWLClassWithLabel(o, getIRI(), id);
					// Ontology_EntityRetriever.addAnnotationToClass(o, sectionSub, id, description
					// );
					/*
					 * Add the description as an annotation to the class
					 */
					Ontology_EntityRetriever.addAnnotationToClass(o, sectionSub, desc2, description);

					/*
					 * Declare this class as a sub class of the chapter class defined above
					 */
					axiomsToAdd.add(df.getOWLSubClassOfAxiom(sectionSub, chapterSub));

					/**
					 * add it to the disjoint sections set (each class in this set will be disjoint
					 * from eachother)
					 */
					// disjointSections.add(sectionSub);

					/*
					 * retrieve all diags from the section
					 */
					// getDiags2(e2, sectionSub, axiomsToAdd);

					for (Element diagSC : XMLContentRetriever.getDirectChildElements(e2, "diag")) {
						getDiags2(diagSC, sectionSub, axiomsToAdd);
						// System.out.println("Here");

					}

				}

				/*
				 * declare all the sections to be disjoint
				 */
				// if (!disjointSections.isEmpty()) {
				// man.addAxiom(o, df.getOWLDisjointClassesAxiom(disjointSections));
				// }

			}

			/*
			 * Add all the axioms to the ontology
			 */
			man.addAxioms(o, axiomsToAdd);

			/*
			 * Declare all chapters to be disjoint
			 */
			// if (!disjointChapters.isEmpty()) {
			// man.addAxiom(o, df.getOWLDisjointClassesAxiom(disjointChapters));
			// }

			/**
			 * Save the ontology to a file
			 */
			Ontology_LoaderSaver.saveOntologyOWLXML(o, output);

			System.out.println("Complete");

		} catch (OWLOntologyStorageException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (ParserConfigurationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (SAXException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (OWLOntologyCreationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	public static OWLClass getDiags2(Element diag, OWLClass superclass, Set<OWLAxiom> axiomsToAdd) {
		String name = XMLContentRetriever.getDirectChildElements(diag, "name").get(0).getTextContent();
		String desc = XMLContentRetriever.getDirectChildElements(diag, "desc").get(0).getTextContent();
		OWLClass diagClass = Ontology_EntityRetriever.addOWLClassWithLabel(o, getIRI(name), name);
		Ontology_EntityRetriever.addAnnotationToClass(o, diagClass, desc, description);
		axiomsToAdd.add(df.getOWLSubClassOfAxiom(diagClass, superclass));
		// Set<OWLClass> disjointDiags = new HashSet<>();
		/*for (Element diagSC : XMLContentRetriever.getDirectChildElements(diag, "diag")) {
			OWLClass diagSub = (getDiags2(diagSC, diagClass, axiomsToAdd));
			//disjointDiags.add(diagSub);

			for (Element incTerm : XMLContentRetriever.getDirectChildElements(diagSC, "inclusionTerm")) {
				String note = XMLContentRetriever.getDirectChildElements(incTerm, "note").get(0).getTextContent();
				OWLClass it = Ontology_EntityRetriever.addOWLClassWithLabel(o, getIRI(), note);
				Ontology_EntityRetriever.addAnnotationToClass(o, it, note, description);
				axiomsToAdd.add(df.getOWLSubClassOfAxiom(it,
						Ontology_EntityRetriever.getOWLClassFromLabel(Stream.of(o), "InclusionTerm", df).get()));
				axiomsToAdd
						.add(df.getOWLSubClassOfAxiom(diagSub,
								df.getOWLObjectSomeValuesFrom(Ontology_EntityRetriever
										.getOWLObjectPropertyFromLabel(Stream.of(o), "hasInclusionTerm", df).get(),
										it)));
			}
			for (Element excludes1 : XMLContentRetriever.getDirectChildElements(diagSC, "excludes1")) {
				String note = XMLContentRetriever.getDirectChildElements(excludes1, "note").get(0).getTextContent();
				OWLClass it = Ontology_EntityRetriever.addOWLClassWithLabel(o, getIRI(), note);
				Ontology_EntityRetriever.addAnnotationToClass(o, it, note, description);
				axiomsToAdd.add(df.getOWLSubClassOfAxiom(it,
						Ontology_EntityRetriever.getOWLClassFromLabel(Stream.of(o), "ExclusionTerm1", df).get()));
				axiomsToAdd
						.add(df.getOWLSubClassOfAxiom(diagSub,
								df.getOWLObjectSomeValuesFrom(Ontology_EntityRetriever
										.getOWLObjectPropertyFromLabel(Stream.of(o), "hasExclusionTerm1", df).get(),
										it)));
			}
			for (Element excludes2 : XMLContentRetriever.getDirectChildElements(diagSC, "excludes2")) {
				String note = XMLContentRetriever.getDirectChildElements(excludes2, "note").get(0).getTextContent();
				OWLClass it = Ontology_EntityRetriever.addOWLClassWithLabel(o, getIRI(), note);
				Ontology_EntityRetriever.addAnnotationToClass(o, it, note, description);
				axiomsToAdd.add(df.getOWLSubClassOfAxiom(it,
						Ontology_EntityRetriever.getOWLClassFromLabel(Stream.of(o), "ExclusionTerm2", df).get()));
				axiomsToAdd
						.add(df.getOWLSubClassOfAxiom(diagSub,
								df.getOWLObjectSomeValuesFrom(Ontology_EntityRetriever
										.getOWLObjectPropertyFromLabel(Stream.of(o), "hasExclusionTerm2", df).get(),
										it)));
			}
		}*/
//if (!disjointDiags.isEmpty()) {
		// axiomsToAdd.add(df.getOWLDisjointClassesAxiom(disjointDiags));
//}
		return diagClass;
	}

}
