package OWL;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;

public class IRI_Creator {

	OWLOntology o;
	String prefix;
	int currentCount = 0;
	
	public IRI_Creator(OWLOntology o, String prefix) {
		this.o = o;
		this.prefix = prefix;
		
	}
	
	public IRI getNextIRI() {
		
		while (true) {

			IRI iri = IRI.create(String.format("%s%010d", prefix, currentCount));
			// System.out.println(iri);
			if (o.containsEntityInSignature(iri) ) {
				currentCount++;
			} else {
				
				return iri;
			}
		}
	}


}
