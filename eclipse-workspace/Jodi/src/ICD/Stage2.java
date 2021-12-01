package ICD;

import java.io.File;
import java.io.FileNotFoundException;

import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import OWL.IRI_Creator;
import OWL.ONTOLOGIES;
import OWL.Ontology_LoaderSaver;

public class Stage2 {

	
	public static void createChronicityOntology(File outputOntologyFile) throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException {
		
		OWLOntology SNOMED = Ontology_LoaderSaver.loadOntology(ONTOLOGIES.SNOMED_OWL);
		
		OWLOntologyManager man = SNOMED.getOWLOntologyManager();
		OWLDataFactory df = man.getOWLDataFactory();
		final String prefix = "https://SNOMED-ICD-CHRONICITY-ONTOLOGY.com";
		IRI chronIRI = IRI.create(prefix);
		
		OWLOntology chronicityOntology = man.createOntology(chronIRI);
		
		IRI_Creator iric = new IRI_Creator(chronicityOntology, prefix+"#");
		
		
		
		OWLClass acuteDisease = df.getOWLClass(IRI.create("http://snomed.info/id/2704003"));
		
		
		
		
		
		
		Ontology_LoaderSaver.saveOntology(chronicityOntology, new OWLXMLDocumentFormat(), outputOntologyFile);
		
		
		
		
	}
	
}
