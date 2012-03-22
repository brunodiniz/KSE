package br.ucam.kuabaSubsystem.util.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.ReasoningElement;
import br.ucam.kuabaSubsystem.graph.util.KuabaGraphUtil;

public class KuabaGraphUtilTest extends AbstractKuabaTestCase {
	private Question whatElementsQuestion;
	private Idea personDomainIdea;
	private Question howModelPersonQuestion;
	private Idea classDesignIdea;
	private Idea privateVisibilityIdea;
	private Question visibilityQuestion;
	private Question isAbstractQuestion;
	private Idea trueIdea;
	
	@Override
	protected void setUp() throws Exception {
                System.out.println("setup started");
		super.setUp();
		this.whatElementsQuestion = this.repo.getQuestion(Question.ROOT_QUESTION_ID);
		this.personDomainIdea = this.repo.getIdea("person");
		this.howModelPersonQuestion = this.repo.getQuestion("how_model_person");
		this.classDesignIdea = this.repo.getIdea("class");
		this.visibilityQuestion = this.factory.createQuestion("visibility");
		this.privateVisibilityIdea = this.factory.createIdea("private");
		this.isAbstractQuestion = this.factory.createQuestion("isAbstract");
		this.trueIdea = this.factory.createIdea("true");
		classDesignIdea.addSuggests(this.isAbstractQuestion);
		this.isAbstractQuestion.addIsAddressedBy(this.trueIdea);
		classDesignIdea.addSuggests(visibilityQuestion);
		visibilityQuestion.addIsAddressedBy(privateVisibilityIdea);		
		System.out.println("setup finished");
	}
	
	public void testGetReasoningElementStageMap(){
            System.out.println("testGetReasoningElementStageMap started");
		HashMap<ReasoningElement, Integer> reasoningElementStageMap = 
			KuabaGraphUtil.getReasoningElementStageMap(this.whatElementsQuestion);
		
		int stage = reasoningElementStageMap.get(this.whatElementsQuestion);
		assertEquals(1, stage);
		stage = reasoningElementStageMap.get(this.classDesignIdea);
		assertEquals(4, stage);		
		stage = reasoningElementStageMap.get(this.visibilityQuestion);
		assertEquals(5, stage);
		stage = reasoningElementStageMap.get(this.isAbstractQuestion);
		assertEquals(5, stage);
		stage = reasoningElementStageMap.get(this.privateVisibilityIdea);
		assertEquals(6, stage);
		stage = reasoningElementStageMap.get(this.trueIdea);
		assertEquals(6, stage);
            System.out.println("testGetReasoningElementStageMap finished");
	}
	
 	public void testGetConnectionIdeaMap(){
            System.out.println("testGetConnectionIdeaMap started");
		Idea emailDomainIdea = this.repo.getIdea("email");
		Question emailOwnerQuestion = this.repo.getQuestion("email_owner");
		emailOwnerQuestion.addIsAddressedBy(this.classDesignIdea);
		HashMap<Question, List<Idea>> resultMap = new HashMap<Question, List<Idea>>();
		List<Idea> resultIdeaList = new ArrayList<Idea>();
		resultIdeaList.add(this.classDesignIdea);
		resultMap.put(emailOwnerQuestion, resultIdeaList);
		HashMap<Question, List<Idea>> connectionQuestionIdeaMap = KuabaGraphUtil.getSubtreeConnectionIdeaMap(emailDomainIdea);
		
		assertEquals(resultMap, connectionQuestionIdeaMap);
            System.out.println("testGetConnectionIdeaMap finished");
	}
 	
	public void testGetConnectionIdeaMapWithTwoIdeasForQuestion(){
            System.out.println("testGetConnectionIdeaMapWithTwoIdeasForQuestion started");
		Idea emailDomainIdea = this.repo.getIdea("email");
		Question emailOwnerQuestion = this.repo.getQuestion("email_owner");
		emailOwnerQuestion.addIsAddressedBy(this.classDesignIdea);
		emailOwnerQuestion.addIsAddressedBy(this.personDomainIdea);
		HashMap<Question, List<Idea>> resultMap = new HashMap<Question, List<Idea>>();
		List<Idea> resultIdeaList = new ArrayList<Idea>();		
		resultIdeaList.add(this.classDesignIdea);
                resultIdeaList.add(this.personDomainIdea);
		resultMap.put(emailOwnerQuestion, resultIdeaList);
		HashMap<Question, List<Idea>> connectionQuestionIdeaMap = KuabaGraphUtil.getSubtreeConnectionIdeaMap(emailDomainIdea);
		
                for (Question q : connectionQuestionIdeaMap.keySet()) {
                    for (Idea i : connectionQuestionIdeaMap.get(q)) System.out.println(q.getId()+" -- "+i.getId());
                }
                System.out.println("");
                for (Question q : resultMap.keySet()) {
                    for (Idea i : resultMap.get(q)) System.out.println(q.getId()+" -- "+i.getId());
                }
                
		assertEquals(resultMap, connectionQuestionIdeaMap);
            System.out.println("testGetConnectionIdeaMapWithTwoIdeasForQuestion finished");
        }
}
