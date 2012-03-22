package br.ucam.kuabaSubsystem.action;


import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Iterator;

import javax.jmi.reflect.RefObject;
import br.ucam.kuabaSubsystem.abstractTestCase.FunctionalTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;

public class DesignElementNameAddedActionTest extends FunctionalTestCase {
	/**
	 * A mock for the uml class
	 */
	private RefObject mockClassInstance;	
	
	@Override
	protected void setUp() throws Exception {
		
		super.setUp();
		this.mockClassInstance = createNiceMock(RefObject.class);		
		expect(this.mockClassInstance.refMetaObject()).andReturn(
				this.core.lookupElement("Class")).anyTimes();		
				
	}

	@Override
	protected void tearDown() throws Exception {	
		super.tearDown();		
	}
	
	/**
	 * 
	 * if an user adds a name to a class, the subsystem must create a kuaba
	 * subtree for this element and make a decision. This decision must conclude
	 * the class design idea.
	 */
//	public void testDesignElementValidNameEntered(){
//		//creating an name change event of the mock class
//		PropertyChangeEvent evtNameChanged = new PropertyChangeEvent(
//				this.mockClassInstance, "name", "","validName");
//		
//		expect(this.mockClassInstance.refMofId()).andReturn("01").anyTimes();		
//		replay(this.mockClassInstance);
//		
//		//executing the action
//		DesignAction nameChangedAction = new DesignElementNameAddedAction();
//		nameChangedAction.execute(evtNameChanged);
//		
//		//testing if a domain idea was created 
//		Idea validNameDomainIdea = this.factory.getIdea("validName");
//		
//		assertNotNull(validNameDomainIdea);
//		
//		//testing if a howModel question was created
//		Question howModelValidNameQuestion = (Question)validNameDomainIdea.listSuggests().next();
//		assertNotNull(howModelValidNameQuestion);
//		assertEquals("How Model validName?", howModelValidNameQuestion.getHasText());
//		
//		//asserting that the two ideas was assigned to How Model question
//		Collection<Idea> adressedIdeaColection = howModelValidNameQuestion.getIsAddressedBy();
//		assertEquals(2, adressedIdeaColection.size());		
//		
//		Iterator<Idea> addressedIdeaIterator = adressedIdeaColection.iterator();
//		Idea classDesignIdea = addressedIdeaIterator.next();
//		Idea attributeDesignIdea = addressedIdeaIterator.next();		
//		assertEquals("Class", classDesignIdea.getHasText());
//		assertEquals("Attribute", attributeDesignIdea.getHasText());
//		
//		Collection<Decision> classDecisionCol = classDesignIdea.getIsConcludedBy();
//		assertEquals(1, classDecisionCol.size());
//		
//		Decision howModelClassDecision = classDecisionCol.iterator().next();
//		assertEquals(true, howModelClassDecision.getIsAccepted());
//		assertEquals(classDesignIdea, howModelClassDecision.getConcludes());		
//		
//		
//		OWLNamedClass decisionClass = this.model.getOWLNamedClass("k:Decision");
//		Collection<RDFIndividual> instances = decisionClass.getInstances(true);
//		
//		assertEquals(3, instances.size());
//	
//	}
	public void testDesignElementValidNameEntered(){
		//creating an name change event of the mock class
		PropertyChangeEvent evtNameChanged = new PropertyChangeEvent(
				this.mockClassInstance, "name", "","validName");
		
		expect(this.mockClassInstance.refMofId()).andReturn("01").anyTimes();		
		replay(this.mockClassInstance);
		
		//executing the action
		DesignAction nameChangedAction = new DesignElementNameAddedAction();
		nameChangedAction.execute(evtNameChanged);
		
		//testing if a domain idea was created 
		Idea validNameDomainIdea = this.repo.getIdea("validName");
		
		assertNotNull(validNameDomainIdea);
		
		//testing if a howModel question was created
		Question howModelValidNameQuestion = (Question)validNameDomainIdea.listSuggests().next();
		assertNotNull(howModelValidNameQuestion);
		assertEquals(Question.DOMAIN_QUESTION_TEXT_PREFIX+"validName?", howModelValidNameQuestion.getHasText());
		
		//asserting that the two ideas was assigned to How Model question
		Collection<Idea> adressedIdeaColection = howModelValidNameQuestion.getIsAddressedBy();
		assertEquals(1, adressedIdeaColection.size());		
		
		Iterator<Idea> addressedIdeaIterator = adressedIdeaColection.iterator();
		Idea classDesignIdea = addressedIdeaIterator.next();
				
		assertEquals("Class", classDesignIdea.getHasText());
		
		
		Collection<Decision> classDecisionCol = classDesignIdea.getIsConcludedBy();
		assertEquals(1, classDecisionCol.size());
		
		Decision howModelClassDecision = classDecisionCol.iterator().next();
		assertEquals(true, howModelClassDecision.getIsAccepted());
		assertEquals(classDesignIdea, howModelClassDecision.getConcludes());	
	
	}

	
}
