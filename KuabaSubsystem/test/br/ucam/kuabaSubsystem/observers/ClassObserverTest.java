package br.ucam.kuabaSubsystem.observers;


import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.jmi.reflect.RefObject;

import br.ucam.kuabaSubsystem.abstractTestCase.FunctionalTestCase;
import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaFacades.SessionUnitOfWork;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.observers.uml.core.ClassObserver;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
//import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
//import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
//import edu.stanford.smi.protegex.owl.model.RDFIndividual;

public class ClassObserverTest extends FunctionalTestCase {
	private RefObject mockClassInstance;
	private PropertyChangeEvent evtNameChanged;
	private ClassObserver obs;
	
	@Override
	protected void setUp() throws Exception {
		
		super.setUp();
		this.mockClassInstance = createNiceMock(RefObject.class);
		this.obs = new ClassObserver();
		expect(this.mockClassInstance.refMetaObject()).andReturn(
				this.core.lookupElement("Class")).anyTimes();		
		this.evtNameChanged = new PropertyChangeEvent(this.mockClassInstance, "name", "","");		
	}

	@Override
	protected void tearDown() throws Exception {	
		super.tearDown();		
	}
	
	/**
	 * this test creates an name changing event and sending to a classObserver
	 * 
	 * if an user changes a name of a class, the subsystem must create an kuaba
	 * subtree for this element and make a decision. This decision must conclude
	 * the class idea.
	 */
	public void testNameChanged(){
		//creating an name change event of the mock class
		PropertyChangeEvent evtNameChanged = new PropertyChangeEvent(
				this.mockClassInstance, "name", "","validName");
		
		expect(this.mockClassInstance.refMofId()).andReturn("01").anyTimes();		
		replay(this.mockClassInstance);
		
		//sending the event to the class observer
		this.obs.propertyChange(evtNameChanged);
		
		//testing if a domain idea with the refMofId of the class was created 
		Idea domainIdea = this.modelRepository.getDomainIdeasWhereIdLike("01").get(0);
                
//                for (Idea i : this.modelRepository.getDomainIdeasWhereIdLike("01")) System.out.println("IDEA = "+i.getId()+" -- "+i.getHasText());
		
		assertNotNull(domainIdea);
		
		//testing if a howModel question was created

		Question howModel01 = (Question)domainIdea.listSuggests().next();
		assertNotNull(howModel01);
		assertEquals(Question.DOMAIN_QUESTION_TEXT_PREFIX+"validName?", howModel01.getHasText());
		
		//asserting that the two ideas was assigned to How Model question
		Collection<Idea> howModelAdressedIdeaCol = howModel01.getIsAddressedBy();

//		assertEquals(2, howModelAdressedIdeaCol.size());		
//		
		Iterator<Idea> howModelAddressedIt = howModelAdressedIdeaCol.iterator();
		Idea classIdea = KuabaHelper.getAcceptedAddressedIdeas(howModel01).iterator().next();
//		Idea attributeIdea = howModelAddressedIt.next();		
		assertEquals("Class", classIdea.getHasText());
//		assertEquals("Attribute", attributeIdea.getHasText());
		
		Collection<Decision> classDecisionCol = classIdea.getIsConcludedBy();
		assertEquals(1, classDecisionCol.size());
		
		Decision howModelClassDecision = classDecisionCol.iterator().next();
		assertEquals(true, howModelClassDecision.getIsAccepted());
		assertEquals(classIdea, howModelClassDecision.getConcludes());		
		
		
//		OWLNamedClass decisionClass = this.model.getOWLNamedClass("k:Decision");
//		Collection<RDFIndividual> instances = decisionClass.getInstances(true);
//		
//		assertEquals(3, instances.size());
	
	}
	
	/**
	 * if the name of an design element change twice, 
	 * the subsystem must reject the idea to model this domain element with your type of
	 * design element. In this case, on the first time that the name of the test class changes, the subsystem
	 * must create your kuaba sub-tree, with the domain idea according to the name, and create a decision
	 * to make your domain idea modeled as class.
	 * 
	 * 
	 */
	public void testNameChanged2(){
		
		expect(this.mockClassInstance.refMofId()).andReturn("mofId").anyTimes();		
		replay(this.mockClassInstance);
		//sending the first name event
		this.obs.propertyChange(this.evtNameChanged);		
		this.evtNameChanged = new PropertyChangeEvent(this.mockClassInstance, "name", "", "Person");
		
		//sending the second name event
		this.obs.propertyChange(this.evtNameChanged);
		Idea domainIdea = this.modelRepository.getDomainIdeasWhereIdLike("mofId").get(0);
		assertTrue(domainIdea.getIsConcludedBy().isEmpty());
		
		Idea person = this.modelRepository.getIdeaByText("Person").get(0);
		
		assertNotNull(person);
		
		Question howModelPerson = this.modelRepository.getFirstQuestionByText(Question.DOMAIN_QUESTION_TEXT_PREFIX+"Person?");
		assertNotNull(howModelPerson);
		assertEquals(howModelPerson, person.listSuggests().next());
		
		Decision decision  = (Decision) howModelPerson.getHasDecision().iterator().next();		
		assertEquals("Class", decision.getConcludes().getHasText());
		
//		OWLNamedClass decisionClass = this.model.getOWLNamedClass("k:Decision");
//		Collection<RDFIndividual> instances = decisionClass.getInstances(true);
		
//		assertEquals(4, instances.size());
		
		SessionUnitOfWork unitOfWork = KuabaSubsystem.getSession().unitOfWork();
		
		assertEquals(person, unitOfWork.getConsideredIdea(person.getId()));
		assertEquals("", unitOfWork.getRejectedIdeas().get(0).getHasText());
		
		this.evtNameChanged = new PropertyChangeEvent(this.mockClassInstance, "name", "Person", "People");
		this.obs.propertyChange(this.evtNameChanged);
		
		Idea people = this.modelRepository.getIdeaByText("People").get(0);
		assertNotNull(people);
		
		Question howModelPeople = this.modelRepository.getFirstQuestionByText(Question.DOMAIN_QUESTION_TEXT_PREFIX+"People?");
		assertNotNull(howModelPeople);
		assertEquals(1, people.getSuggests().size());
		assertEquals(howModelPeople, people.listSuggests().next());
		
		Decision decision2  = (Decision) howModelPeople.getHasDecision().iterator().next();		
		assertEquals("Class", decision2.getConcludes().getHasText());
		
		assertTrue(unitOfWork.getConsideredIdeas().contains(people));
		assertEquals(2, unitOfWork.getRejectedIdeas().size());
		assertTrue(unitOfWork.getRejectedIdeas().contains(person));		
		
	}
	
	public void testNameChangedAndReturned(){
//		System.out.println("model no teste: "+ this.model.getName());
		expect(this.mockClassInstance.refMofId()).andReturn(
				"mofId").anyTimes();		
		replay(this.mockClassInstance);		
		this.obs.propertyChange(this.evtNameChanged);
		
		this.evtNameChanged = new PropertyChangeEvent(this.mockClassInstance, 
				"name", "", "Person");
		this.obs.propertyChange(this.evtNameChanged);
		
		this.evtNameChanged = new PropertyChangeEvent(this.mockClassInstance,
				"name", "Person", "");
		this.obs.propertyChange(this.evtNameChanged);
		
		Idea mofIdIdea = this.modelRepository.getDomainIdeasWhereIdLike(
				"mofId").get(0);		
		assertNotNull(mofIdIdea);
		System.out.println("Existent domain Idea: " + mofIdIdea.getId());
		Idea person = this.modelRepository.getIdeaByText("Person").get(0);
		
		SessionUnitOfWork unitOfWork = KuabaSubsystem.getSession().unitOfWork();
		assertTrue(unitOfWork.getConsideredIdeas().contains(mofIdIdea));
		assertEquals(1, unitOfWork.getRejectedIdeas().size());
		assertTrue(unitOfWork.getRejectedIdeas().contains(person));
		
		this.evtNameChanged = new PropertyChangeEvent(this.mockClassInstance, 
				"name", "", "Person");
		this.obs.propertyChange(this.evtNameChanged);
		
		assertTrue(unitOfWork.getConsideredIdeas().contains(person));
		assertEquals(1, unitOfWork.getRejectedIdeas().size());
		assertTrue(unitOfWork.getRejectedIdeas().contains(mofIdIdea));
		
	}
	
	
	public void testSaveSession(){
		this.factory.createQuestion("_02").setHasText("What Elements?");
		
		Idea dog  = this.factory.getKuabaRepository().getIdea("dog");
		Idea athlete = this.factory.getKuabaRepository().getIdea("athlete");
		
		KuabaSubsystem.getSession().unitOfWork().addConsideredIdea(dog);
		KuabaSubsystem.getSession().unitOfWork().addRejectedIdea(athlete);
		assertTrue(dog.getIsConcludedBy().isEmpty());
		assertTrue(athlete.getIsConcludedBy().isEmpty());
		
		KuabaSubsystem.facade.saveSession(this.modelRepository, null);
		
		assertFalse(dog.getIsConcludedBy().isEmpty());
		assertFalse(athlete.getIsConcludedBy().isEmpty());
		
		Decision dogDecision = (Decision)dog.getIsConcludedBy().
														iterator().next();
		assertTrue(dogDecision.getIsAccepted());
		
		Decision athleteDecision = (Decision)athlete.getIsConcludedBy().
														iterator().next();
		assertFalse(athleteDecision.getIsAccepted());
		
	}
	
	
}
