package br.ucam.kuabaSubsystem.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.org.apache.xpath.internal.functions.FuncId;



import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.abstractTestCase.FunctionalTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.ReasoningElement;

public class KuabaHelperTest extends FunctionalTestCase {
	private Idea emailDomainIdea;
	private Idea attributeDesignIdea;
	private Question howModelEmailQuestion;
	private Idea classDesignIdea;
	private Question ownerQuestion;
	
	private int day;
	private int month;
	private int year;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.emailDomainIdea = this.repo.getIdea("email"); 
		this.classDesignIdea = this.repo.getIdea("class");
		this.ownerQuestion = this.repo.getQuestion("email_owner");
		this.attributeDesignIdea = this.repo.getIdea("attribute");
		this.howModelEmailQuestion = this.repo.getQuestion("how_model_email");
		Decision modelEmailAsAttributeDecision = 
			this.factory.createDecision("model_email_as_attribute");
		modelEmailAsAttributeDecision.setIsAccepted(true);
		modelEmailAsAttributeDecision.setHasDate(new GregorianCalendar());
		modelEmailAsAttributeDecision.setConcludes(this.attributeDesignIdea);
                this.attributeDesignIdea.addIsConcludedBy(modelEmailAsAttributeDecision);               
		this.howModelEmailQuestion.addHasDecision(modelEmailAsAttributeDecision);
		Decision personOwnerOfemailDecision = this.factory.createDecision("person_owner_of_email");
		ownerQuestion.addIsAddressedBy(classDesignIdea);
                classDesignIdea.addAddress(ownerQuestion);               
		personOwnerOfemailDecision.setIsAccepted(true);
		personOwnerOfemailDecision.setHasDate(new GregorianCalendar());		
		personOwnerOfemailDecision.setConcludes(classDesignIdea);
                classDesignIdea.addIsConcludedBy(personOwnerOfemailDecision);
		ownerQuestion.addHasDecision(personOwnerOfemailDecision);
		
		GregorianCalendar todayCalendar = new GregorianCalendar();
		this.day = todayCalendar.get(GregorianCalendar.DAY_OF_MONTH);
		this.month = todayCalendar.get(GregorianCalendar.MONTH);
		this.year = todayCalendar.get(GregorianCalendar.YEAR);
	}
	
	/**
	 * tests {@link KuabaHelper#makeDecisionForAllIdeasInSubtree(Idea, boolean)}}
	 */
	public void testRejectAllIdeasFromSubtree(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		KuabaHelper.makeDecisionForAllIdeasInSubtree(this.emailDomainIdea, false);
		List<Decision> emailIsModelElementDecisionList = (List<Decision>) this.emailDomainIdea.getIsConcludedBy();		
		Comparator<Decision> decisionByTimeComparator = new Comparator<Decision>(){
			@Override
			public int compare(Decision o1, Decision o2) {
				return o1.getHasDate().compareTo(o2.getHasDate());
			}
		};
		Collections.sort(emailIsModelElementDecisionList, decisionByTimeComparator);
		Decision rejectedEmailAsModelElementDecision = 
			emailIsModelElementDecisionList.get(emailIsModelElementDecisionList.size() -1);
		assertEquals(this.day, rejectedEmailAsModelElementDecision.getHasDate(
				).get(GregorianCalendar.DAY_OF_MONTH));
		assertEquals(this.month, rejectedEmailAsModelElementDecision.getHasDate(
				).get(GregorianCalendar.MONTH));
		assertEquals(this.year, rejectedEmailAsModelElementDecision.getHasDate(
				).get(GregorianCalendar.YEAR));
		assertFalse(rejectedEmailAsModelElementDecision.getIsAccepted());
		
		Question howModelEmailQuestion = (Question)this.emailDomainIdea.listSuggests().next();
		Idea attributeDesignIdea = (Idea)howModelEmailQuestion.listIsAddressedBy().next();
		
		List<Decision> attributeDecisionList = (List<Decision>) attributeDesignIdea.getIsConcludedBy();
		Collections.sort(attributeDecisionList, decisionByTimeComparator);
		Decision rejectModelEmailAsAttributeDecision = attributeDecisionList.get(attributeDecisionList.size()-1);
		assertEquals(this.day, rejectModelEmailAsAttributeDecision.getHasDate(
				).get(GregorianCalendar.DAY_OF_MONTH));
		assertEquals(this.month, rejectModelEmailAsAttributeDecision.getHasDate(
				).get(GregorianCalendar.MONTH));
		assertEquals(this.year, rejectModelEmailAsAttributeDecision.getHasDate(
				).get(GregorianCalendar.YEAR));
		assertFalse(rejectModelEmailAsAttributeDecision.getIsAccepted());
		
		Question ownerOfEmailQuestion = this.repo.getQuestion("email_owner");
		Idea ownerOfEmailIdea = (Idea)ownerOfEmailQuestion.listIsAddressedBy().next();
		List<Decision> classDecisionList = (List<Decision>) ownerOfEmailIdea.getIsConcludedBy();
		
		Collections.sort(classDecisionList, decisionByTimeComparator);
		
		Decision rejectClassPersonOwnerOfEmailDecision = classDecisionList.get(classDecisionList.size()-1);
		
		assertEquals(this.day, rejectClassPersonOwnerOfEmailDecision.getHasDate(
				).get(GregorianCalendar.DAY_OF_MONTH));
		assertEquals(this.month, rejectClassPersonOwnerOfEmailDecision.getHasDate(
				).get(GregorianCalendar.MONTH));
		assertEquals(this.year, rejectClassPersonOwnerOfEmailDecision.getHasDate(
				).get(GregorianCalendar.YEAR));
		assertFalse(rejectClassPersonOwnerOfEmailDecision.getIsAccepted());		
	}
	
	/**
	 * tests {@link KuabaHelper#makeDecisionForAllIdeasInSubtree(Idea, boolean)}}
	 */
	public void testAcceptAllIdeaInSubtree(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		KuabaHelper.makeDecisionForAllIdeasInSubtree(this.emailDomainIdea, false);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		KuabaHelper.makeDecisionForAllIdeasInSubtree(this.emailDomainIdea, true);
		List<Decision> emailIsModelElementDecisionList = (List<Decision>) this.emailDomainIdea.getIsConcludedBy();
		Comparator decisionByTimeComparator = new DecisionByDateComparator();
		Collections.sort(emailIsModelElementDecisionList, decisionByTimeComparator);
		
//		Decision rejectedEmailAsModelElementDecision = emailIsModelElementDecisionList.get(emailIsModelElementDecisionList.size() -1);
//		
//		assertTrue(rejectedEmailAsModelElementDecision.getIsAccepted());
		
		Question howModelEmailQuestion = (Question)this.emailDomainIdea.listSuggests().next();
		Idea attributeDesignIdea = (Idea)howModelEmailQuestion.listIsAddressedBy().next();
		
		List<Decision> attributeDecisionList = (List<Decision>) attributeDesignIdea.getIsConcludedBy();
		Collections.sort(attributeDecisionList, decisionByTimeComparator);
		Decision rejectModelEmailAsAttributeDecision = attributeDecisionList.get(attributeDecisionList.size()-1);
		
		assertTrue(rejectModelEmailAsAttributeDecision.getIsAccepted());
		
		Question ownerOfEmailQuestion = this.repo.getQuestion("email_owner");
		Idea ownerOfEmailIdea = (Idea)ownerOfEmailQuestion.listIsAddressedBy().next();
		List<Decision> classDecisionList = (List<Decision>) ownerOfEmailIdea.getIsConcludedBy();
		
		Collections.sort(classDecisionList, decisionByTimeComparator);
		
		Decision rejectClassPersonOwnerOfEmailDecision = classDecisionList.get(classDecisionList.size()-1);
		
		
		assertTrue(rejectClassPersonOwnerOfEmailDecision.getIsAccepted());		
		
		
	}
	
	/**
	 * tests {@link KuabaHelper#makeDecisionForAllIdeasInSubtree(Idea, boolean)}}
	 */
	public void testAcceptCertainIdeasInSubtree(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		KuabaHelper.makeDecisionForAllIdeasInSubtree(this.emailDomainIdea, false);
		Decision notModelEmailAsAttributeDecision = 
			this.factory.createDecision("not_model_email_as_attribute");
		notModelEmailAsAttributeDecision.setConcludes(this.attributeDesignIdea);
		notModelEmailAsAttributeDecision.setIsAccepted(false);
		notModelEmailAsAttributeDecision.setHasDate(new GregorianCalendar());
		this.howModelEmailQuestion.addHasDecision(notModelEmailAsAttributeDecision);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		KuabaHelper.makeDecisionForAllIdeasInSubtree(this.emailDomainIdea, true);
		Question howModelEmailQuestion = (Question)this.emailDomainIdea.listSuggests().next();
		Idea attributeDesignIdea = (Idea)howModelEmailQuestion.listIsAddressedBy().next();
		
		List<Decision> attributeDecisionList = (List<Decision>) attributeDesignIdea.getIsConcludedBy();
		Collections.sort(attributeDecisionList, new DecisionByDateComparator());
		Decision rejectModelEmailAsAttributeDecision = attributeDecisionList.get(attributeDecisionList.size()-1);
		System.out.println(attributeDecisionList);
		assertFalse(rejectModelEmailAsAttributeDecision.getIsAccepted());
	}
	
	/**
	 * tests {@link KuabaHelper#makeDecisionForAllIdeasInSubtree(Idea, boolean)}}
	 */
	public void testMakeDecisionForIdeasInSubtreeWithoutAnyDecision(){
		Question rootQuestion = this.factory.createQuestion("root_question");
		Idea thingDomainIdea = this.factory.createIdea("thing");
		Question howModelThingQuestion = this.factory.createQuestion("how_model_thing");
		Idea thingDesignIdea = this.factory.createIdea("thing_design_idea");
		
		rootQuestion.addIsAddressedBy(thingDomainIdea);
                thingDomainIdea.addAddress(rootQuestion);
                
		thingDomainIdea.addSuggests(howModelThingQuestion);
                howModelThingQuestion.addIsSuggestedBy(thingDomainIdea);
                
		howModelThingQuestion.addIsAddressedBy(thingDesignIdea);
                thingDesignIdea.addAddress(howModelThingQuestion);
		
		KuabaHelper.makeDecisionForAllIdeasInSubtree(thingDomainIdea, true);
		List<Decision> rootQuestionDecisionList = (List<Decision>) rootQuestion.getHasDecision();
		List<Decision> howModelThingDecisionList = (List<Decision>) howModelThingQuestion.getHasDecision();
		
		assertEquals(true, rootQuestionDecisionList.get(0).getIsAccepted());
		assertEquals(thingDomainIdea, rootQuestionDecisionList.get(0).getConcludes());
		
		assertEquals(true, howModelThingDecisionList.get(0).getIsAccepted());
		assertEquals(thingDesignIdea, howModelThingDecisionList.get(0).getConcludes());		
	}
	
	public void testDisconnectSubtree(){
		
		Map<Question, Idea> disconnectedElementMap = KuabaHelper.disconnectSubtree(this.emailDomainIdea);
		
		assertEquals(this.classDesignIdea, disconnectedElementMap.get(this.ownerQuestion));
		assertFalse(this.ownerQuestion.hasIsAddressedBy());
		assertFalse(this.classDesignIdea.getAddress().contains(this.ownerQuestion));
		
	}

}
