package br.ucam.kuabaSubsystem.repositories;

import java.util.Collection;
import java.util.List;


import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.FormalModel;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;

public class OwlRepositoryTest extends AbstractKuabaTestCase {
	private KuabaRepository owlRepository;
	protected void setUp() throws Exception {
		super.setUp();
		this.owlRepository = this.repo;
		
	}
	
	public void testFindAllQuestions(){
		Question q1 = this.repo.getQuestion("how_model_cd");
		Question q2 = this.repo.getQuestion(Question.ROOT_QUESTION_ID);
		Question q3 = this.repo.getQuestion("how_model_athlete");
		
		List<Question> questions = this.owlRepository.getAllQuestions();
		
		assertEquals(6, questions.size());
		assertTrue(questions.contains(q1));
		assertTrue(questions.contains(q2));
		assertTrue(questions.contains(q3));
	}
	
	public void testFindFormalModelQuestions(){
		Question q1 = this.repo.getQuestion("how_model_cd");
		Question q2 = this.repo.getQuestion(Question.ROOT_QUESTION_ID);
		
		FormalModel formalModel = this.repo.getFormalModel("UML");
		
		assertEquals(formalModel, q1.getIsDefinedBy());
		assertEquals(formalModel, q2.getIsDefinedBy());
		
		List<Question> questions = this.owlRepository.getQuestions(formalModel);
		
		assertEquals(2, questions.size());
		assertTrue(questions.contains(q1));
		assertTrue(questions.contains(q2));
	}
	
//	public void testFindAllKuabaElement_Decision(){
//		OWLNamedClass argumentClass = this.model.getOWLNamedClass("k:Decision");
//		Collection<RDFIndividual> instances = argumentClass.getInstances(true);	
//		Decision dec1 = this.repo.getDecision("model_cd_as_class");
//		Decision dec2 = this.repo.getDecision("model_cd_as_attribute");
//		List<Decision> decisionList = 
//			this.owlRepository.getAllKuabaElement(Decision.class);
//		assertTrue(decisionList.contains(dec1));
//		assertTrue(decisionList.contains(dec2));
//		assertTrue(instances.size() == decisionList.size());
//		assertTrue(decisionList.containsAll(instances));
//	
//		
//	}
	
//	public void testFindAllKuabaElement_Argument(){
//		OWLNamedClass argumentClass = this.model.getOWLNamedClass("k:Argument");
//		Collection<RDFIndividual> instances = argumentClass.getInstances(true);		
//		Argument arg1 = this.factory.getArgument("model_athlete_as_class");		
//		List<Argument> argumentList = 
//			this.owlRepository.findAllKuabaElement(Argument.class);
//		assertTrue(argumentList.contains(arg1));
//		assertTrue(instances.size() == argumentList.size());
//		assertTrue(argumentList.containsAll(instances));
//		
//	}
//	
//	public void testFindAllKuabaElement_Question(){
//		OWLNamedClass argumentClass = this.model.getOWLNamedClass("k:Question");
//		Collection<RDFIndividual> instances = argumentClass.getInstances(true);			
//		List<Question> questionList = 
//			this.owlRepository.findAllKuabaElement(Question.class);
//		assertTrue(questionList.containsAll(instances));
//		assertTrue(instances.size() == questionList.size());		
//	}
//	
//	public void testFindAllKuabaElement_Idea(){
//		OWLNamedClass ideaClass = this.model.getOWLNamedClass("k:Idea");
//		Collection<RDFIndividual> instances = ideaClass.getInstances(true);			
//		List<Idea> ideaList = 
//			this.owlRepository.findAllKuabaElement(Idea.class);
//		assertTrue(ideaList.containsAll(instances));
//		assertTrue(instances.size() == ideaList.size());		
//	}
	
	

}
