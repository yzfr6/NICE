package XML;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import OWL.IRI_Creator;
import OWL.Ontology_EntityRetriever;

public class BNF2OWL {

	private static File BNF_XML = new File("/Users/nice/Developer/eclipse-workspace/NICE-PP/resources/BNF.xml");
	private static File BNF_OWL = new File("/Users/nice/Developer/eclipse-workspace/NICE-PP/resources/BNF.owl");
	private static File SNOMED_OWL = new File("/Users/nice/Developer/eclipse-workspace/NICE-PP/resources/snomed-ct.owl");
	
	private static String BNF_IRI = "https://bnf.nice.org.uk";

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException,
			OWLOntologyStorageException, OWLOntologyCreationException {

		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		OWLDataFactory df = man.getOWLDataFactory();
		OWLOntology bnf_owl = man.createOntology(IRI.create(BNF_IRI));
		
		log("loading snomed");
		OWLOntology snomed = man.loadOntologyFromOntologyDocument(SNOMED_OWL);
		
		OWLImportsDeclaration importDeclaration= man.getOWLDataFactory().getOWLImportsDeclaration(IRI.create(SNOMED_OWL));
		man.applyChange(new AddImport(bnf_owl, importDeclaration));
		
		Set<OWLAxiom> axiomsToAdd = new HashSet<>();

		
		OWLClass findings = df.getOWLClass(IRI.create("http://snomed.info/id/404684003"));
		OWLAnnotationProperty skosPrefLabel = df.getOWLAnnotationProperty(IRI.create("http://www.w3.org/2004/02/skos/core#prefLabel"));
		
		log("classifying snomed");
		OWLReasoner reasoner = new StructuralReasonerFactory().createReasoner(snomed);
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		
		
		Map<String,IRI> labelMap = new HashMap<>();
		
		log("creating findings map");
		for (OWLClass finding : reasoner.getSubClasses(findings).getFlattened()) {
			for (String rdfsLabel : Ontology_EntityRetriever.getAnnotationValues(snomed, finding, df.getRDFSLabel()).toArray(String[]::new) ) {
				labelMap.put(rdfsLabel.toLowerCase().trim(), finding.getIRI());
			}
			
			for (String skosPrefLabelString : Ontology_EntityRetriever.getAnnotationValues(snomed, finding, skosPrefLabel).toArray(String[]::new) ) {
				labelMap.put(skosPrefLabelString.toLowerCase().trim(), finding.getIRI());
			}
			
		}
		
		
		
		IRI_Creator iric = new IRI_Creator(bnf_owl, BNF_IRI);

		OWLObjectProperty hasContraindication = Ontology_EntityRetriever.addOWLObjectPropertyWithLabel(bnf_owl,
				iric.getNextIRI(), "has_contraindication");
		OWLObjectProperty hasCaution = Ontology_EntityRetriever.addOWLObjectPropertyWithLabel(bnf_owl,
				iric.getNextIRI(), "has_caution");

		OWLObjectProperty hasSideEffect = Ontology_EntityRetriever.addOWLObjectPropertyWithLabel(bnf_owl,
				iric.getNextIRI(), "has_side_effect");
		OWLObjectProperty hasCommonOrVeryCommonSideEffect = Ontology_EntityRetriever
				.addOWLObjectPropertyWithLabel(bnf_owl, iric.getNextIRI(), "has_common_or_very_common_side_effect");
		OWLObjectProperty hasUncommonSideEffect = Ontology_EntityRetriever.addOWLObjectPropertyWithLabel(bnf_owl,
				iric.getNextIRI(), "has_uncommon_side_effect");
		OWLObjectProperty hasRareOrVeryRareSideEffect = Ontology_EntityRetriever.addOWLObjectPropertyWithLabel(bnf_owl,
				iric.getNextIRI(), "has_rare_or_very_rare_side_effect");
		OWLObjectProperty hasFrequencyNotKnownSideEffect = Ontology_EntityRetriever
				.addOWLObjectPropertyWithLabel(bnf_owl, iric.getNextIRI(), "has_frequency_not_known_side_effect");

		axiomsToAdd.add(df.getOWLSubObjectPropertyOfAxiom(hasCommonOrVeryCommonSideEffect, hasSideEffect));
		axiomsToAdd.add(df.getOWLSubObjectPropertyOfAxiom(hasUncommonSideEffect, hasSideEffect));
		axiomsToAdd.add(df.getOWLSubObjectPropertyOfAxiom(hasRareOrVeryRareSideEffect, hasSideEffect));
		axiomsToAdd.add(df.getOWLSubObjectPropertyOfAxiom(hasFrequencyNotKnownSideEffect, hasSideEffect));

		OWLClass contraindications_class = Ontology_EntityRetriever.getOWLClassFromLabelOrAdd(Stream.of(bnf_owl),
				"Contraindications", df, iric.getNextIRI());
		OWLClass side_effects_class = Ontology_EntityRetriever.getOWLClassFromLabelOrAdd(Stream.of(bnf_owl),
				"Side Effects", df, iric.getNextIRI());
		OWLClass cautions_class = Ontology_EntityRetriever.getOWLClassFromLabelOrAdd(Stream.of(bnf_owl), "Cautions", df,
				iric.getNextIRI());
		OWLClass drugs_class = Ontology_EntityRetriever.getOWLClassFromLabelOrAdd(Stream.of(bnf_owl), "Drugs", df,
				iric.getNextIRI());

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		;
		Document doc = builder.parse(BNF_XML);
		// Do something with the document here.
		Element root = doc.getDocumentElement();

		Element drugs = getElementWithIDandName(root, "topic", "drugs");

		log("creating drugs");
		int caution_matches = 0;
		int caution_misses = 0;
		
		int contraindication_matches = 0;
		int contraindication_misses = 0;
		
		int se_matches = 0;
		int se_misses = 0;
		
		for (Element drug : getChildredWithName(drugs, "topic", true)) {

			String drug_title = getChildredWithName(drug, "title", true).get(0).getTextContent().replace("\n", "")
					.replace("\r", "").replace(" +", " ").trim();

			OWLClass drug_class = Ontology_EntityRetriever.addOWLClassWithLabel(bnf_owl,
					getIRI(drug.getAttribute("id")), drug_title);

			axiomsToAdd.add(df.getOWLSubClassOfAxiom(drug_class, drugs_class));

			//log("creating contraindications");
			// contraindications
			for (Element contraindication : getElementsWithAttributesandName(drug, "ph", "outputclass",
					"contraindication")) {
				String contraindication_title = contraindication.getTextContent().replace("\n", "").replace("\r", "")
						.replace(" +", " ").trim();
				
				OWLClass newClass;
				if (labelMap.containsKey(contraindication_title.toLowerCase().trim())){
					contraindication_matches++;
					newClass = df.getOWLClass(labelMap.get(contraindication_title.toLowerCase().trim()));
				} else {
					contraindication_misses--;
					newClass = Ontology_EntityRetriever.getOWLClassFromLabelOrAdd(Stream.of(bnf_owl),
							contraindication_title, df, iric.getNextIRI());
				}
				axiomsToAdd.add(df.getOWLSubClassOfAxiom(newClass, contraindications_class));
				axiomsToAdd.add(df.getOWLSubClassOfAxiom(drug_class,
						df.getOWLObjectSomeValuesFrom(hasContraindication, newClass)));

			}

			//log("creating cautions");
			// cautions
			for (Element caution : getElementsWithAttributesandName(drug, "ph", "outputclass", "caution")) {
				String caution_title = caution.getTextContent().replace("\n", "").replace("\r", "").replace(" +", " ")
						.trim();

				OWLClass newClass;
				if (labelMap.containsKey(caution_title.toLowerCase().trim())){
					caution_matches++;
					newClass = df.getOWLClass(labelMap.get(caution_title.toLowerCase().trim()));
				} else {
					caution_misses--;
					newClass = Ontology_EntityRetriever.getOWLClassFromLabelOrAdd(Stream.of(bnf_owl),
							caution_title, df, iric.getNextIRI());
				}
				axiomsToAdd.add(df.getOWLSubClassOfAxiom(newClass, cautions_class));
				axiomsToAdd.add(
						df.getOWLSubClassOfAxiom(drug_class, df.getOWLObjectSomeValuesFrom(hasCaution, newClass)));

			}

			//log("creating common or very common side effects");
			// common or very common side effects
			List<Element> covcse_list = getElementsWithAttributesandName(drug, "sectiondiv", "outputclass",
					"commonOrVeryCommon - "+ new SimpleDateFormat("HH:mm:ss").format(new Date()));

			if (!covcse_list.isEmpty()) {
				Element covcse = covcse_list.get(0);
				// System.out.println("here");
				for (Element se : getElementsWithAttributesandName(covcse, "ph", "outputclass", "sideEffect")) {
					String se_title = se.getTextContent().replace("\n", "").replace("\r", "").replaceAll(" +", " ")
							.trim();

					OWLClass newClass;
					if (labelMap.containsKey(se_title.toLowerCase().trim())){
						se_matches++;
						newClass = df.getOWLClass(labelMap.get(se_title.toLowerCase().trim()));
					} else {
						se_misses--;
						newClass = Ontology_EntityRetriever.getOWLClassFromLabelOrAdd(Stream.of(bnf_owl),
								se_title, df, iric.getNextIRI());
					}
					axiomsToAdd.add(df.getOWLSubClassOfAxiom(newClass, side_effects_class));
					axiomsToAdd.add(df.getOWLSubClassOfAxiom(drug_class,
							df.getOWLObjectSomeValuesFrom(hasCommonOrVeryCommonSideEffect, newClass)));

				}
			}

			//log("creating uncommon side effects");
			// uncommon side effects
			List<Element> use_list = getElementsWithAttributesandName(drug, "sectiondiv", "outputclass", "uncommon");

			if (!use_list.isEmpty()) {
				Element use = use_list.get(0);
				// System.out.println("here");
				for (Element se : getElementsWithAttributesandName(use, "ph", "outputclass", "sideEffect")) {
					String se_title = se.getTextContent().replace("\n", "").replace("\r", "").replaceAll(" +", " ")
							.trim();

					OWLClass newClass;
					if (labelMap.containsKey(se_title.toLowerCase().trim())){
						se_matches++;
						newClass = df.getOWLClass(labelMap.get(se_title.toLowerCase().trim()));
					} else {
						se_misses--;
						newClass = Ontology_EntityRetriever.getOWLClassFromLabelOrAdd(Stream.of(bnf_owl),
								se_title, df, iric.getNextIRI());
					}
					axiomsToAdd.add(df.getOWLSubClassOfAxiom(newClass, side_effects_class));
					axiomsToAdd.add(df.getOWLSubClassOfAxiom(drug_class,
							df.getOWLObjectSomeValuesFrom(hasUncommonSideEffect, newClass)));

				}
			}

			//log("creating rare or very rare side effects");
			// rare or very rare side effects
			List<Element> rovrse_list = getElementsWithAttributesandName(drug, "sectiondiv", "outputclass",
					"rareOrVeryRare");

			if (!rovrse_list.isEmpty()) {
				Element use = rovrse_list.get(0);
				// System.out.println("here");
				for (Element se : getElementsWithAttributesandName(use, "ph", "outputclass", "sideEffect")) {
					String se_title = se.getTextContent().replace("\n", "").replace("\r", "").replaceAll(" +", " ")
							.trim();

					OWLClass newClass;
					if (labelMap.containsKey(se_title.toLowerCase().trim())){
						se_matches++;
						newClass = df.getOWLClass(labelMap.get(se_title.toLowerCase().trim()));
					} else {
						se_misses--;
						newClass = Ontology_EntityRetriever.getOWLClassFromLabelOrAdd(Stream.of(bnf_owl),
								se_title, df, iric.getNextIRI());
					}
					axiomsToAdd.add(df.getOWLSubClassOfAxiom(newClass, side_effects_class));
					axiomsToAdd.add(df.getOWLSubClassOfAxiom(drug_class,
							df.getOWLObjectSomeValuesFrom(hasRareOrVeryRareSideEffect, newClass)));

				}
			}

			//log("creating unknown frequency side effect");
			// unknown frequency side effects
			List<Element> fnkse_list = getElementsWithAttributesandName(drug, "sectiondiv", "outputclass",
					"notKnown");

			if (!fnkse_list.isEmpty()) {
				Element use = fnkse_list.get(0);
				// System.out.println("here");
				for (Element se : getElementsWithAttributesandName(use, "ph", "outputclass", "sideEffect")) {
					String se_title = se.getTextContent().replace("\n", "").replace("\r", "").replaceAll(" +", " ")
							.trim();

					OWLClass newClass;
					if (labelMap.containsKey(se_title.toLowerCase().trim())){
						se_matches++;
						newClass = df.getOWLClass(labelMap.get(se_title.toLowerCase().trim()));
					} else {
						se_misses--;
						newClass = Ontology_EntityRetriever.getOWLClassFromLabelOrAdd(Stream.of(bnf_owl),
								se_title, df, iric.getNextIRI());
					}
					axiomsToAdd.add(df.getOWLSubClassOfAxiom(newClass, side_effects_class));
					axiomsToAdd.add(df.getOWLSubClassOfAxiom(drug_class,
							df.getOWLObjectSomeValuesFrom(hasFrequencyNotKnownSideEffect, newClass)));

				}
			}
		}

		man.addAxioms(bnf_owl, axiomsToAdd);

		man.saveOntology(bnf_owl, new OWLXMLDocumentFormat(), IRI.create(BNF_OWL));

		System.out.println("found " +caution_matches + " caution matches and missed " + caution_misses);
		
		System.out.println("found " +contraindication_matches + " contraindication matches and missed " + contraindication_misses);
		
		System.out.println("found " +se_matches + "se matches and missed " + se_misses);
	}

	private static IRI getIRI(String suffix) {
		return IRI.create(BNF_IRI + "#" + suffix);
	}

	private static Element getElementWithIDandName(Element parent, String name, String id) {
		NodeList nl = parent.getElementsByTagName(name);
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
			}
			Element e = (Element) nl.item(i);
			if (e.getAttribute("id").equals(id)) {
				return e;
			}
		}
		throw new RuntimeException("Could not find element with name " + name + " and id " + id + "");
	}

	private static List<Element> getElementsWithAttributesandName(Element parent, String name, String att_id,
			String att_value) {
		List<Element> children = new ArrayList<>();
		NodeList nl = parent.getElementsByTagName(name);
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) nl.item(i);
				if (e.getAttribute(att_id).equals(att_value)) {
					children.add(e);
				}
			}
		}
		return children;
	}

	private static List<Element> getChildredWithName(Element parent, String name, boolean direct) {

		List<Element> children = new ArrayList<>();
		NodeList nl = parent.getElementsByTagName(name);
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {

				Element e = (Element) nl.item(i);
				if (direct) {
					if (((Element) e.getParentNode()).equals(parent)) {
						children.add(e);
					}
				} else {
					children.add(e);
				}
			}
		}
		return children;

	}
	
	private static long currentTimeInMillis = System.currentTimeMillis();
	
	private static void log(String log) {
		long now = System.currentTimeMillis();
		System.out.println((now-currentTimeInMillis)/1000 + "s - " +log);
		currentTimeInMillis = System.currentTimeMillis();
	}

}
