package OWL;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class Ontology_LoaderSaver {

	public static OWLOntology loadOntology(File ont) throws OWLOntologyCreationException {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		// OWLDataFactory df = man.getOWLDataFactory();
		OWLOntology o;

		o = man.loadOntologyFromOntologyDocument(ont);
		return o;

	}

	public static void saveOntologyOWLXML(OWLOntology o, File output)
			throws OWLOntologyStorageException, FileNotFoundException {

		o.getOWLOntologyManager().saveOntology(o, new OWLXMLDocumentFormat(), new FileOutputStream(output));

	}

	public static void saveOntology(OWLOntology o, OWLDocumentFormat format, File output)
			throws OWLOntologyStorageException, FileNotFoundException {

		o.getOWLOntologyManager().saveOntology(o, format, new FileOutputStream(output));

	}

}
