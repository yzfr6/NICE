package RUNNER;

import java.io.FileNotFoundException;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import FILE.FILES;
import ICD.Stage1;
import ICD.Stage2;
import OWL.ONTOLOGIES;

public class Runner {

	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException {
		//Stage1.convert(FILES.ICD_TABULAR, ONTOLOGIES.ICD_10_OWL);
		Stage2.createChronicityOntology(ONTOLOGIES.CHRONICITY_OWL);
		
	}
}
