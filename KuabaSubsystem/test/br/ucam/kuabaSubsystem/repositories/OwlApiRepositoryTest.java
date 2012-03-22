/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucam.kuabaSubsystem.repositories;

import br.ucam.kuabaSubsystem.abstractTestCase.AbstractOwlApiKuabaTestCase;
import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.FormalModel;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import com.hp.hpl.jena.assembler.acceptance.AllAccept.SetupDatabase;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 *
 * @author Bruno
 */
public class OwlApiRepositoryTest extends AbstractOwlApiKuabaTestCase {
    
    private KuabaRepository owlRepository;

    public OwlApiRepositoryTest() {
        try {
            setUp();
        } catch (Exception ex) {
            Logger.getLogger(OwlApiRepositoryTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    protected void setUp() throws Exception {
            super.setUp();
            this.owlRepository = this.repo;
    }

    public void testFindAllQuestions(){
            Question q1 = this.owlRepository.getQuestion("how_model_cd");
            Question q2 = this.owlRepository.getQuestion(Question.ROOT_QUESTION_ID);
            Question q3 = this.owlRepository.getQuestion("how_model_athlete");
//            q1.setId("how_model_cd9000");
//            Question qq = this.owlRepository.getQuestion("how_model_cd9000");
//            assertEquals(q1,qq);

            List<Question> questions = this.owlRepository.getAllQuestions();

            for (Question q : questions) System.out.println("teste "+q.getId());
            
            assertEquals(3, questions.size());
            assertTrue(questions.contains(q1));
            assertTrue(questions.contains(q2));
            assertTrue(questions.contains(q3));
    }

    public void testFindFormalModelQuestions(){
            Question q1 = this.owlRepository.getQuestion("how_model_cd");
            Question q2 = this.owlRepository.getQuestion(Question.ROOT_QUESTION_ID);

            FormalModel formalModel = this.owlRepository.getFormalModel("UML");

            assertEquals(formalModel, q1.getIsDefinedBy());
            assertEquals(formalModel, q2.getIsDefinedBy());

            List<Question> questions = this.owlRepository.getQuestions(formalModel);

            assertEquals(2, questions.size());
            assertTrue(questions.contains(q1));
            assertTrue(questions.contains(q2));
    }

    public void testFindAllKuabaElement_Decision(){
            OWLClass argumentClass = ((OWLOntology)this.repo.getModel()).getOWLOntologyManager().getOWLDataFactory().getOWLClass(IRI.create(KuabaSubsystem.ONTOLOGY_URL+"#Decision"));       
            Collection<OWLIndividual> instances = argumentClass.getIndividuals(((OWLOntology)this.repo.getModel()));
            
            Decision dec1 = this.owlRepository.getDecision("model_cd_as_class");
            Decision dec2 = this.owlRepository.getDecision("model_cd_as_attribute");
            List<Decision> decisionList = this.owlRepository.getAllDecisions();
            
            assertTrue(decisionList.contains(dec1));
            assertTrue(decisionList.contains(dec2));
            assertTrue(instances.size() == decisionList.size());
//            assertTrue(decisionList.containsAll(instances));


    }

    public void testFindAllKuabaElement_Argument(){
            OWLClass argumentClass = ((OWLOntology)this.repo.getModel()).getOWLOntologyManager().getOWLDataFactory().getOWLClass(IRI.create(KuabaSubsystem.ONTOLOGY_URL+"#Argument"));       
            Collection<OWLIndividual> instances = argumentClass.getIndividuals(((OWLOntology)this.repo.getModel()));
            
            Argument arg1 = this.owlRepository.getArgument("model_athlete_as_class");		
            List<Argument> argumentList = this.owlRepository.getAllArguments();
            
            assertTrue(argumentList.contains(arg1));
            assertTrue(instances.size() == argumentList.size());
//            assertTrue(argumentList.containsAll(instances));

    }

    public void testFindAllKuabaElement_Question(){
            OWLClass argumentClass = ((OWLOntology)this.repo.getModel()).getOWLOntologyManager().getOWLDataFactory().getOWLClass(IRI.create(KuabaSubsystem.ONTOLOGY_URL+"#Question"));       
            Collection<OWLIndividual> instances = argumentClass.getIndividuals(((OWLOntology)this.repo.getModel()));
            
            List<Question> questionList = this.owlRepository.getAllQuestions();
            
//            assertTrue(questionList.containsAll(instances));
            assertTrue(instances.size() == questionList.size());		
    }

    public void testFindAllKuabaElement_Idea(){
            OWLClass argumentClass = ((OWLOntology)this.repo.getModel()).getOWLOntologyManager().getOWLDataFactory().getOWLClass(IRI.create(KuabaSubsystem.ONTOLOGY_URL+"#Idea"));       
            Collection<OWLIndividual> instances = argumentClass.getIndividuals(((OWLOntology)this.repo.getModel()));
            
            List<Idea> ideaList = this.owlRepository.getAllIdeas();
            
//            assertTrue(ideaList.containsAll(instances));
            assertTrue(instances.size() == ideaList.size());		
    }
    
    public static void main(String[] args) {
	OwlApiRepositoryTest t = new OwlApiRepositoryTest();
        t.testFindAllQuestions();
//        t.testFindFormalModelQuestions();
//        t.testFindAllKuabaElement_Decision();
//        t.testFindAllKuabaElement_Argument();
//        t.testFindAllKuabaElement_Question();
//        t.testFindAllKuabaElement_Idea();
    }
}
