package br.ucam.kuabaSubsystem.action;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.jmi.reflect.RefObject;

import br.ucam.kuabaSubsystem.abstractTestCase.FunctionalTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.util.DecisionByDateComparator;

public class DesignElementNameChangedActionTest extends FunctionalTestCase {
	/**
	 * A mock for the uml class
	 */
	private RefObject mockClassInstance;
	
	/**
	 * A mock for UML Attribute
	 */
	private RefObject mockAttributeInstance;
	
	/**
	 * Action to be tested
	 */
	private DesignElementNameChangedAction nameChangedAction;
	
	@Override
	protected void setUp() throws Exception {		
		super.setUp();
		
		this.mockClassInstance = createNiceMock(RefObject.class);		
		expect(this.mockClassInstance.refMetaObject()).andReturn(
				this.core.lookupElement("Class")).anyTimes();
		
		this.mockAttributeInstance = createNiceMock(RefObject.class);
		expect(this.mockAttributeInstance.refMetaObject()).andReturn(
				this.core.lookupElement("Attribute")).anyTimes();
		
		this.nameChangedAction = new DesignElementNameChangedAction();
		
				
	}

	@Override
	protected void tearDown() throws Exception {	
		super.tearDown();		
	}	
	
	public void testNameChangedAction(){
		
		expect(this.mockClassInstance.refMofId()).andReturn("mofId").anyTimes();		
		replay(this.mockClassInstance);			
		
		PropertyChangeEvent nameChangedEvent = 
			new PropertyChangeEvent(this.mockClassInstance, "name", "person", "People");
		
		//Executing the action
		this.nameChangedAction.execute(nameChangedEvent);
		
		//asserting that decisions of the person subtree was rejected
		Idea personDomainIdea = this.modelRepository.getIdea("person");
		List<Decision> personDecisionList = 
			(List<Decision>)personDomainIdea.getIsConcludedBy();
		Collections.sort(personDecisionList, new DecisionByDateComparator());
		Decision personIsAModelElementDecision = 
			personDecisionList.get(personDecisionList.size()-1);		
		System.out.println("ID: " + personIsAModelElementDecision.getId());
		assertFalse(personIsAModelElementDecision.getIsAccepted());
		Question howModelPersonQuestion = (Question)personDomainIdea.listSuggests().next();
		Idea personDesignIdea = (Idea)howModelPersonQuestion.listIsAddressedBy().next();
		List<Decision> personDesignIdeaDecisionList = 
			(List<Decision>) personDesignIdea.getIsConcludedBy();
		Collections.sort(personDesignIdeaDecisionList, new DecisionByDateComparator());
		Decision modelPersonAsClassDecicion = 
			personDesignIdeaDecisionList.get(personDesignIdeaDecisionList.size() -1);
		System.out.println(personDesignIdeaDecisionList);
		assertFalse(modelPersonAsClassDecicion.getIsAccepted());
		
		//asserting that the People subtree was created and is consistent
		assertFalse(this.modelRepository.getIdeaByText("People").isEmpty());
		Idea peopleDomainIdea = this.modelRepository.getIdeaByText("People").get(0);
		assertNotNull(peopleDomainIdea);
		
		assertTrue(peopleDomainIdea.listIsConcludedBy().hasNext());
		Decision peopleIsAModelElementDecision = 
			(Decision)peopleDomainIdea.listIsConcludedBy().next();
		assertTrue(peopleIsAModelElementDecision.getIsAccepted());			
		
		Question howModelPeopleQuestion = this.modelRepository.getFirstQuestionByText(Question.DOMAIN_QUESTION_TEXT_PREFIX+"People?");
		assertNotNull(howModelPeopleQuestion);
		assertEquals(1, peopleDomainIdea.getSuggests().size());
		assertEquals(howModelPeopleQuestion, peopleDomainIdea.listSuggests().next());
		
		Decision modelPeopleAsClassDecision  = (Decision) howModelPeopleQuestion.getHasDecision().iterator().next();		
		assertEquals("Class", modelPeopleAsClassDecision.getConcludes().getHasText());
		
		assertTrue(howModelPeopleQuestion.listHasDecision().hasNext());
		assertEquals(2, howModelPeopleQuestion.getHasDecision().size());
		assertTrue(modelPeopleAsClassDecision.getIsAccepted());
		assertEquals(modelPeopleAsClassDecision, howModelPeopleQuestion.listHasDecision().next());				
		
	}	
	
	public void testNameChangedWithRelationBetweenKuabaSubtrees(){
		
		expect(this.mockAttributeInstance.refMofId()).andReturn("mofId").anyTimes();		
		replay(this.mockAttributeInstance);
		
		//making email owned by person class
		Idea classDesignIdea = this.modelRepository.getIdea("class");
		classDesignIdea.setCanCopy(true);
		Question ownerQuestion = this.modelRepository.getQuestion("email_owner");
		Decision personOwnerOfemailDecision = this.modelRepository.getModelFactory().createDecision("person_owner_of_email");
		ownerQuestion.addIsAddressedBy(classDesignIdea);
		personOwnerOfemailDecision.setIsAccepted(true);		
		personOwnerOfemailDecision.setHasDate(new GregorianCalendar());
		personOwnerOfemailDecision.setConcludes(classDesignIdea);
		ownerQuestion.addHasDecision(personOwnerOfemailDecision);		
		
		PropertyChangeEvent nameChangedEvent = 
			new PropertyChangeEvent(this.mockAttributeInstance, "name", "email", "telephone");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//executing the action
		this.nameChangedAction.execute(nameChangedEvent);
		
		//asserting that email domain idea was rejected inclusive its subtree.
		Idea emailDomainIdea = this.repo.getIdea("email");
		List<Decision> emailAsModelElementDecisionList = (List<Decision>)emailDomainIdea.getIsConcludedBy();
		assertFalse(emailAsModelElementDecisionList.isEmpty());
		Decision emailIsModelElementDecision = emailAsModelElementDecisionList.get(0);
		
		assertNotNull(emailIsModelElementDecision);
		assertFalse(emailIsModelElementDecision.getIsAccepted());
		List<Decision> classDecisionList = (List<Decision>)classDesignIdea.getIsConcludedBy();
		Collections.sort(classDecisionList, new DecisionByDateComparator());
		Decision newPersonOwnerOfEmailDecision = classDecisionList.get(classDecisionList.size() - 1);
		System.out.println(classDecisionList);
		assertFalse(newPersonOwnerOfEmailDecision.getIsAccepted());
		
		//asserting that the telephone subtree was correctly created
		
		Idea telephoneDomainIdea = (Idea)this.repo.getIdea("telephone");
		assertEquals("telephone", telephoneDomainIdea.getHasText());
		
		Question howModelTelephoneQuestion = (Question)this.modelRepository.getQuestion("how_model_telephone");
		Idea attributeDesignIdea = (Idea)this.repo.getIdea("attribute");
		
		assertTrue(telephoneDomainIdea.listSuggests().hasNext());
		assertEquals(howModelTelephoneQuestion, telephoneDomainIdea.listSuggests().next());
		assertTrue(howModelTelephoneQuestion.listIsAddressedBy().hasNext());
		
		assertFalse(telephoneDomainIdea.getIsConcludedBy().isEmpty());
		Decision telephoneIsModelElementDecision = (Decision)telephoneDomainIdea.listIsConcludedBy().next();
		assertTrue(telephoneIsModelElementDecision.getIsAccepted());
		
		Idea telephoneAttributeDesignIdea = 
			(Idea)howModelTelephoneQuestion.listIsAddressedBy().next();
		assertEquals("Attribute", telephoneAttributeDesignIdea.getHasText());
		assertNotSame(attributeDesignIdea, telephoneAttributeDesignIdea);		
		assertTrue(telephoneAttributeDesignIdea.listSuggests().hasNext());
		
		Decision modelTelephoneAsAttributeDecision = 
			(Decision)telephoneAttributeDesignIdea.listIsConcludedBy().next();
		assertTrue(modelTelephoneAsAttributeDecision.getIsAccepted());
		
		Question telephoneOwnerQuestion = 
			(Question)telephoneAttributeDesignIdea.listSuggests().next();
		assertNotSame(ownerQuestion, telephoneOwnerQuestion);	
		
		assertEquals("Owner?", telephoneOwnerQuestion.getHasText());
		assertTrue(telephoneOwnerQuestion.listIsAddressedBy().hasNext());
		assertEquals(classDesignIdea, telephoneOwnerQuestion.listIsAddressedBy().next());
		
		assertFalse(telephoneOwnerQuestion.getHasDecision().isEmpty());
		System.out.println("Decisions: " + telephoneOwnerQuestion.getHasDecision());
		Decision personOwnerOfTelephoneDecision = 
			(Decision)telephoneOwnerQuestion.listHasDecision().next();
		assertTrue(personOwnerOfTelephoneDecision.getIsAccepted());		
		
	}
	
	public void testNameChangedAndReturned(){
		expect(this.mockAttributeInstance.refMofId()).andReturn("mofId").anyTimes();		
		replay(this.mockAttributeInstance);
		
		//name changed from email to telephone
		PropertyChangeEvent nameChangedEvent = 
			new PropertyChangeEvent(this.mockAttributeInstance, "name", "email", "telephone");
		this.nameChangedAction.execute(nameChangedEvent);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//name returned from telephone to email
		PropertyChangeEvent nameReturnedEvent = 
			new PropertyChangeEvent(this.mockAttributeInstance, "name", "telephone", "email");
		this.nameChangedAction.execute(nameReturnedEvent);
		
		Idea emailDomainIdea = this.modelRepository.getIdea("email");
		List<Decision> emailAsModelElementDecisionList = (List<Decision>)emailDomainIdea.getIsConcludedBy();
		assertEquals(2, emailAsModelElementDecisionList.size());		
		Comparator<Decision> decisionByDateComparator = new Comparator<Decision>(){
			@Override
			public int compare(Decision d1, Decision d2) {
				
				return d1.getHasDate().compareTo(d2.getHasDate());
			}
		};
		
		//asserting that email subtree decisions was correctly made
		
		Collections.sort(emailAsModelElementDecisionList, decisionByDateComparator);
		Decision emailDecisionOne = emailAsModelElementDecisionList.get(0);
		Decision emailDecisionTwo = emailAsModelElementDecisionList.get(1);				
		assertTrue(!emailDecisionOne.getIsAccepted() & emailDecisionTwo.getIsAccepted());
		
		 
		Iterator<Idea> emailDesignIdeaIterator = 
			((Question)emailDomainIdea.listSuggests().next()).listIsAddressedBy();
		assertTrue(emailDesignIdeaIterator.hasNext());
		List<Decision> emailAttributeDecisionList = 
			(List<Decision>)((Idea)emailDesignIdeaIterator.next()).getIsConcludedBy();
		assertEquals(2, emailAttributeDecisionList.size());
		Collections.sort(emailAttributeDecisionList, decisionByDateComparator);
		System.out.println("email decision list: " + emailAttributeDecisionList);
		assertTrue((!emailAttributeDecisionList.get(0).getIsAccepted() && emailAttributeDecisionList.get(1).getIsAccepted()));		
		
		//asserting that telephone subtree decisions was correctly made
		
		Idea telephoneDomainIdea = this.modelRepository.getIdea("telephone");		
		List<Decision> telephoneAsModelElementDecisionList = (List<Decision>)telephoneDomainIdea.getIsConcludedBy();
		assertEquals(2, telephoneAsModelElementDecisionList.size());
		Collections.sort(telephoneAsModelElementDecisionList, decisionByDateComparator);
		Decision telephoneDecisionOne = telephoneAsModelElementDecisionList.get(0);
		Decision telephoneDecisionTwo = telephoneAsModelElementDecisionList.get(1);
		assertTrue(telephoneDecisionOne.getIsAccepted() & !telephoneDecisionTwo.getIsAccepted());
		
		Iterator<Idea> telephoneDesignIdeaIterator = 
			((Question)telephoneDomainIdea.listSuggests().next()).listIsAddressedBy();
		assertTrue(telephoneDesignIdeaIterator.hasNext());
		List<Decision> telephoneAttributeDecisionList = 
			(List<Decision>)((Idea)telephoneDesignIdeaIterator.next()).getIsConcludedBy();
		System.out.println("telephone decision list: " + telephoneAttributeDecisionList);
		assertEquals(2, telephoneAttributeDecisionList.size());
		
		Collections.sort(telephoneAttributeDecisionList, decisionByDateComparator);
		
		assertTrue((!telephoneAttributeDecisionList.get(1).getIsAccepted() & telephoneAttributeDecisionList.get(0).getIsAccepted()));
		
	}

}
