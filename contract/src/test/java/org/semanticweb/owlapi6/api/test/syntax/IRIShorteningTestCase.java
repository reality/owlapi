package org.semanticweb.owlapi6.api.test.syntax;

import static org.junit.Assert.assertTrue;
import static org.semanticweb.owlapi6.apibinding.OWLFunctionalSyntaxFactory.IRI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.semanticweb.owlapi6.api.test.baseclasses.TestBase;
import org.semanticweb.owlapi6.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi6.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi6.model.OWLNamedIndividual;
import org.semanticweb.owlapi6.model.OWLOntology;
import org.semanticweb.owlapi6.model.OWLOntologyCreationException;
import org.semanticweb.owlapi6.model.OWLOntologyStorageException;
import org.semanticweb.owlapi6.vocab.Namespaces;
import org.semanticweb.owlapi6.vocab.OWLRDFVocabulary;

/**
 * Created by ses on 6/23/14.
 */
public class IRIShorteningTestCase extends TestBase {

    @Test
    public void testIriEqualToPrefixNotShortenedInFSS() throws Exception {
        OWLOntology o = createTestOntology();
        String output = saveOntology(o, new FunctionalSyntaxDocumentFormat()).toString();
        matchExact(output, "NamedIndividual(rdf:)", false);
        matchExact(output, "NamedIndividual(rdf:type)", true);
    }

    public void matchExact(String output, String text, boolean expected) {
        String message = "should " + (expected ? "" : "not ") + "contain" + text + " - " + output;
        assertTrue(message, expected == output.contains(text));
    }

    @Test
    public void testIriEqualToPrefixShortenedInTurtle() throws Exception {
        OWLOntology o = createTestOntology();
        String output = saveOntology(o, new TurtleDocumentFormat()).toString();
        matchRegex(output, "rdf:\\s+rdf:type\\s+owl:NamedIndividual");
        matchRegex(output, "rdf:type\\s+rdf:type\\s+owl:NamedIndividual");
    }

    public void matchRegex(String output, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(output);
        boolean found = matcher.find();
        assertTrue("should  contain " + regex + " - " + output, found);
    }

    private OWLOntology createTestOntology() {
        OWLOntology o = getOWLOntology();
        OWLNamedIndividual i = df.getOWLNamedIndividual(IRI(Namespaces.RDF.getPrefixIRI(), ""));
        o.add(df.getOWLDeclarationAxiom(i));
        i = df.getOWLNamedIndividual(OWLRDFVocabulary.RDF_TYPE);
        o.add(df.getOWLDeclarationAxiom(i));
        return o;
    }

    @Test
    public void shouldOutputURNsCorrectly()
                    throws OWLOntologyCreationException, OWLOntologyStorageException {
        OWLOntology o = m.createOntology(df.getIRI("urn:ontology:", "test"));
        o.add(df.getOWLObjectPropertyAssertionAxiom(df.getOWLObjectProperty("urn:test#", "p"),
                        df.getOWLNamedIndividual("urn:test#", "test"),
                        df.getOWLNamedIndividual("urn:other:", "test")));
        equal(o, roundTrip(o));
    }
}