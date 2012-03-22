package br.ucam.kuabaSubsystem.kuabaModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.impl.MyFactory;

import br.ucam.kuabaSubsystem.kuabaModel.impl.OwlApiKuabaModelFactory;
import com.hp.hpl.jena.util.FileUtils;


public class ActicityTest extends AbstractKuabaTestCase {	
	
	/**
	 * Tests the {@link Activity#setHasName(String)} method.
	 */
	public void test_setHasName(){
		
		Activity useCaseDesign = this.factory.createActivity("use case design");
		useCaseDesign.setHasName("use case modelling");
		
		assertEquals("use case modelling", useCaseDesign.getHasName());
		
	}	
	

	/**
	 * Tests the {@link Activity#setHasDescription(String)} method
	 */
	public void test_setHasDescription(){
		
		Activity useCaseDesign = this.factory.createActivity("use case design");
		useCaseDesign.setHasDescription("This activity designs the system use case");
		
		assertEquals("This activity designs the system use case", useCaseDesign.getHasDescription());
//		List<String> errors = new ArrayList<String>();
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
//		
//		assertTrue(errors.isEmpty());
		useCaseDesign = this.repo.getActivity("use_case_design");
		
		assertEquals("This activity designs the system use case", useCaseDesign.getHasDescription());
	}

	/**
	 * Tests the {@link Activity#setHasStartDate(GregorianCalendar)} method.
	 */
	public void test_setHasStartDate() throws FileNotFoundException, Exception{
		Activity useCaseDesign = this.factory.createActivity("use case design");
		
		useCaseDesign.setHasName("use case design");
		GregorianCalendar startDate = new GregorianCalendar();
		startDate.set(2008, 3, 10, 8, 30, 00);
		
		useCaseDesign.setHasStartDate(startDate);
		
		GregorianCalendar resultStartDate = useCaseDesign.getHasStartDate();
		
		assertEquals(2008, resultStartDate.get(GregorianCalendar.YEAR));
		
		assertEquals(3, resultStartDate.get(GregorianCalendar.MONTH));
		
		assertEquals(10, resultStartDate.get(GregorianCalendar.DAY_OF_MONTH));
		
		assertEquals(8, resultStartDate.get(GregorianCalendar.HOUR));
		
		assertEquals(30, resultStartDate.get(GregorianCalendar.MINUTE));
		
		assertEquals(0, resultStartDate.get(GregorianCalendar.SECOND));		
		
		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());
		
		//Loading the model again to guarantee that the start date was really saved.		 		 
//		 JenaOWLModel otherModel = ProtegeOWL.createJenaOWLModel();
//		 otherModel.getRepositoryManager().addProjectRepository(this.lr);
//		 otherModel.load(new FileInputStream(new File("test/br/ucam/kuabaSubsystem/testBase/testKuabaKnowlegeBase.xml")), "oi");
		 
		 //Creating other Factory with the otherModel.
		 KuabaModelFactory otherFactory = new OwlApiKuabaModelFactory(repo);
		 
		 //Loading the Activity "use case design" again
		 Activity newUseCaseDesign = repo.getActivity("use_case_design");
		 
		 //getting the startDate literal		 
		 String retrievedStartDate =  newUseCaseDesign.getHasStartDate().toString();
		 
		 //testing if the start date was really saved in correct format. 
		 assertNotNull(retrievedStartDate);
//		 assertEquals("2008-04-10T08:30:00", retrievedStartDate);
//		 otherModel.dispose();
		 
	}
	
	/**
	 * Tests the {@link Activity#setHasFinishDate(GregorianCalendar)} method.
	 * 
	 * @throws FileNotFoundException: thrown if the br.ucam.kuabaSubsystem.testBase.xml was
	 * not found.
	 * 
	 * @throws Exception: thrown if save method generates an error. 
	 */
	public void test_setHasFinishDate() throws FileNotFoundException, Exception{
		Activity useCaseDesign = this.factory.createActivity("use case design");
		
		GregorianCalendar finishDate = new GregorianCalendar();
		finishDate.set(2008, 4, 12, 8, 30, 20);
		
		useCaseDesign.setHasFinishDate(finishDate);
		
		GregorianCalendar resultFinishDate = useCaseDesign.getHasFinishDate();
		
		assertEquals(2008, resultFinishDate.get(GregorianCalendar.YEAR));
		
		assertEquals(4, resultFinishDate.get(GregorianCalendar.MONTH));
		
		assertEquals(12, resultFinishDate.get(GregorianCalendar.DAY_OF_MONTH));
		
		assertEquals(8, resultFinishDate.get(GregorianCalendar.HOUR));
		
		assertEquals(30, resultFinishDate.get(GregorianCalendar.MINUTE));
		
		assertEquals(20, resultFinishDate.get(GregorianCalendar.SECOND));		
	
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());
		
		//Loading the model again to guarantee that the start date was really saved.		 		 
//		 JenaOWLModel otherModel = ProtegeOWL.createJenaOWLModel();
//		 otherModel.getRepositoryManager().addProjectRepository(this.lr);
//		 otherModel.load(new FileInputStream(new File("test/br/ucam/kuabaSubsystem/testBase/testKuabaKnowlegeBase.xml")), "oi");
		 
		 //Creating other Factory with the otherModel.
//		 MyFactory otherFactory = new MyFactory(otherModel);
		 
		 //Loading the Activity "use case design" again
		 Activity newUseCaseDesign = repo.getActivity("use_case_design");
		 
		 //getting the startDate literal		 
		 String retrievedFinishDate = newUseCaseDesign.getHasFinishDate().toString();
		 
		 //testing if the start date was really saved in correct format. 
		 assertNotNull(retrievedFinishDate);
//		 assertEquals("2008-05-12T08:30:20", retrievedFinishDate);
//		 otherModel.dispose();
	}
	
	/**
	 * Tests the {@link Activity#getHasStartDate(GregorianCalendar)} method.
	 * Tests the {@link Activity#getHasFinishDate(GregorianCalendar)} method.
	 * 
	 * This test guarantee that null is returned when no start
	 * and finish date was set in the activity object.
	 */
	public void test_get_empty_start_and_finish_date(){
		
		Activity testActivity = this.factory.createActivity("test"); 
		
		assertNull(testActivity.getHasStartDate());
		assertNull(testActivity.getHasFinishDate());
	}
	
	/**
	 *  Test {@link Activity#getHasExpectedDuration(ExpectedDuration)} method.
	 *  
	 *  Tests if the right ExpectedDuration object is returned. This relationship
	 *  is defined on activities fixture.
	 *  
	 *  @see br.ucam.kuabaSubsystem.fixtures/activities.xml  
	 */
	public void test_get_has_expected_duration(){
		
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		
		ExpectedDuration twoWeeks = diagramDomainModel.getHasExpectedDuration();
		
		assertEquals(2, twoWeeks.getHasAmount());
		assertEquals(ExpectedDuration.WEEK, twoWeeks.getHasUnitTime().iterator().next());
	}
	
	/**
	 * Tests {@link Activity#setHasExpectedDuration(ExpectedDuration)} method.
	 */
	public void test_set_expected_duration(){
		
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		
		ExpectedDuration oneMonth = this.repo.getExpectedDuration("one_month");
		
		assertEquals("day", oneMonth.getHasUnitTime().iterator().next());
		diagramDomainModel.setHasExpectedDuration(oneMonth);
		
		ArrayList<String> errors = new ArrayList<String>();
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
		
		assertTrue(errors.isEmpty());
		
		diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		oneMonth = diagramDomainModel.getHasExpectedDuration();
		
		assertEquals(30, oneMonth.getHasAmount());
		
		assertEquals(ExpectedDuration.DAY, oneMonth.getHasUnitTime().iterator().next());
	}
	
	/**
	 * Tests {@link Activity#getInvolves()} method.
	 */
	public void test_getInvolves(){
		
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		
		Collection<ReasoningElement> involvesCollection = diagramDomainModel.getInvolves();
		assertFalse(involvesCollection.isEmpty());
		
		assertEquals(5, involvesCollection.size());
		
		Iterator<ReasoningElement> involvesIterator = involvesCollection.iterator();
		
		
		assertEquals("model_athlete_as_class", involvesIterator.next().getId());
                assertEquals("how_model_athlete", involvesIterator.next().getId());
                assertEquals("class", involvesIterator.next().getId());
		assertEquals("athlete", involvesIterator.next().getId());
		assertEquals(Question.ROOT_QUESTION_ID, involvesIterator.next().getId());
		
	}
	
	/**
	 * Tests {@link Activity#hasInvolves()} method.
	 */
	public void test_hasInvolves(){
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		assertTrue(diagramDomainModel.hasInvolves());
	}
	
	/**
	 * Tests {@link Activity#listInvolves()} method.
	 */
	public void test_listInvolves(){
		
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		Iterator<ReasoningElement> involvesIterator = diagramDomainModel.listInvolves();
		
		assertEquals("model_athlete_as_class", involvesIterator.next().getId());
                assertEquals("how_model_athlete", involvesIterator.next().getId());
                assertEquals("class", involvesIterator.next().getId());
		assertEquals("athlete", involvesIterator.next().getId());
		assertEquals(Question.ROOT_QUESTION_ID, involvesIterator.next().getId());
		
		try{
			involvesIterator.next();
			fail();
		}catch(NoSuchElementException ex){
			assertTrue(true);			
		}
	}
	
	/**
	 * Tests {@link Activity#addInvolves(ReasoningElement)} method.
	 * 
	 * @throws FileNotFoundException: thrown if the br.ucam.kuabaSubsystem.testBase.xml was
	 * not found.
	 * 
	 * @throws Exception: thrown if save method generates an error. 
	 */
	public void test_addInvolves() throws FileNotFoundException, Exception{
		
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		ReasoningElement newElement = this.factory.createIdea("test_reasoning_element");
		
		assertEquals(5, diagramDomainModel.getInvolves().size());
		
		diagramDomainModel.addInvolves(newElement);		
				
//		ArrayList<String> errors = new ArrayList<String>();		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);		
//		assertTrue(errors.isEmpty());
		
		Iterator<ReasoningElement> involvesIterator = diagramDomainModel.listInvolves();
		
		assertEquals("model_athlete_as_class", involvesIterator.next().getId());
                assertEquals("how_model_athlete", involvesIterator.next().getId());
                assertEquals("class", involvesIterator.next().getId());
		assertEquals("athlete", involvesIterator.next().getId());
		assertEquals(Question.ROOT_QUESTION_ID, involvesIterator.next().getId());
		
		assertEquals("test_reasoning_element", involvesIterator.next().getId());
	}
	
	/**
	 * Tests {@link Activity#addInvolves(ReasoningElement)} method.
	 * 
	 * The goal of this test is to guarantee that the addInvolves method do not
	 * add the same reasoning element more than one time.
	 */
	public void test_add_repeated_involves(){
		
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		ReasoningElement classIdea = this.repo.getIdea("class");
		
		diagramDomainModel.addInvolves(classIdea);
		diagramDomainModel.addInvolves(classIdea);
		diagramDomainModel.addInvolves(classIdea);
		diagramDomainModel.addInvolves(classIdea);
		
		assertEquals(5, diagramDomainModel.getInvolves().size());
	}
	/**
	 * Tests {@link Activity#removeInvolves(ReasoningElement)} method.
	 */
	public void test_remove_involves(){
		
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		ReasoningElement classIdea = this.repo.getIdea("class");
		
		assertEquals(5, diagramDomainModel.getInvolves().size());
		
		diagramDomainModel.removeInvolves(classIdea);
		
		assertEquals(4, diagramDomainModel.getInvolves().size());
		
		Iterator<ReasoningElement> involvesIterator = diagramDomainModel.listInvolves();		
		
//		assertEquals("athlete", involvesIterator.next().getId());
//		assertEquals("how_model_athlete", involvesIterator.next().getId());
//		assertEquals(Question.ROOT_QUESTION_ID, involvesIterator.next().getId());
//		assertEquals("model_athlete_as_class", involvesIterator.next().getId());
                
                assertEquals("model_athlete_as_class", involvesIterator.next().getId());
                assertEquals("how_model_athlete", involvesIterator.next().getId());
		assertEquals("athlete", involvesIterator.next().getId());
		assertEquals(Question.ROOT_QUESTION_ID, involvesIterator.next().getId());
		
		try{
			involvesIterator.next();
			fail();
		}catch(NoSuchElementException ex){
			assertTrue(true);			
		}
		
//		ArrayList<String> errors = new ArrayList<String>();		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);		
//		assertTrue(errors.isEmpty());
	}
	
	/**
	 * Tests {@link Activity#getIsExecutedBy()} method.
	 */
	public void test_getIsExecutedBY(){
		
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		
		Collection<Person> isExecutedByCollection = diagramDomainModel.getIsExecutedBy();
		assertFalse(isExecutedByCollection.isEmpty());
		
		assertEquals(1, isExecutedByCollection.size());
		
		Iterator<Person> isExecutedByIterator = isExecutedByCollection.iterator();
		
		assertEquals("pedro", isExecutedByIterator.next().getId());			
		
	}
	
	/**
	 * Tests {@link Activity#hasIsExecutedBy()} method.
	 */
	public void test_hasIsExecutedBy(){
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		assertTrue(diagramDomainModel.hasIsExecutedBy());
	}
	
	/**
	 * Tests {@link Activity#listIsExecutedBy()} method.
	 */
	public void test_listIsExecutedBy(){
		
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		Iterator<Person> isExecutedByIterator = diagramDomainModel.listIsExecutedBy();
		
		assertEquals("pedro", isExecutedByIterator.next().getId());
		
		try{
			isExecutedByIterator.next();
			fail();
		}catch(NoSuchElementException ex){
			assertTrue(true);			
		}
	}
	
	/**
	 * Tests {@link Activity#addIsExecutedBy(Person)} method.
	 * 
	 * @throws FileNotFoundException: thrown if the br.ucam.kuabaSubsystem.testBase.xml was
	 * not found.
	 * 
	 * @throws Exception: thrown if save method generates an error. 
	 */
	public void test_addIsExecutedBy() throws FileNotFoundException, Exception{
		
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		Person newElement = this.factory.createPerson("another_person");
		
		assertEquals(1, diagramDomainModel.getIsExecutedBy().size());
		
		diagramDomainModel.addIsExecutedBy(newElement);		
				
//		ArrayList<String> errors = new ArrayList<String>();		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);		
//		assertTrue(errors.isEmpty());
		
		Iterator<Person> IsExecutedByIterator = diagramDomainModel.listIsExecutedBy();
		
				
		assertEquals("another_person", IsExecutedByIterator.next().getId());
                assertEquals("pedro", IsExecutedByIterator.next().getId());
	}
	
	/**
	 * Tests {@link Activity#addIsExecutedBy(Person)} method.
	 * 
	 * The goal of this test is to guarantee that the addIsExecutedBy method do not
	 * add the same reasoning element more than one time.
	 */
	public void test_add_repeated_isExecutedBy(){
		
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		Person pedro = this.repo.getPerson("pedro");
		
		diagramDomainModel.addIsExecutedBy(pedro);
		diagramDomainModel.addIsExecutedBy(pedro);
		diagramDomainModel.addIsExecutedBy(pedro);
		diagramDomainModel.addIsExecutedBy(pedro);
		
		assertEquals(1, diagramDomainModel.getIsExecutedBy().size());
	}
	
	/**
	 * Tests {@link Activity#removeIsExecutedBy(Person)} method.
	 */
	public void test_removeIsExecutedBy(){
		
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		Person classIdea = this.repo.getPerson("pedro");
		
		assertEquals(1, diagramDomainModel.getIsExecutedBy().size());
		
		diagramDomainModel.removeIsExecutedBy(classIdea);
		
		assertTrue(diagramDomainModel.getIsExecutedBy().isEmpty());
		assertEquals(0, diagramDomainModel.getIsExecutedBy().size());
		
		Iterator<Person> isExecutedByIterator = diagramDomainModel.listIsExecutedBy();		
		try{
			isExecutedByIterator.next();
			fail();
		}catch(NoSuchElementException ex){
			assertTrue(true);			
		}
		
//		ArrayList<String> errors = new ArrayList<String>();		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);		
//		assertTrue(errors.isEmpty());
	}
	
	/**
	 * Tests {@link Activity#getRequires()} method.
	 */
	public void test_getRequires(){
		
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		
		Collection<Role> requiresCollection = diagramDomainModel.getRequires();
		assertFalse(requiresCollection.isEmpty());
		
		assertEquals(1, requiresCollection.size());
		
		Iterator<Role> requiresIterator = requiresCollection.iterator();
		
		assertEquals("developer", requiresIterator.next().getId());			
		
	}
	
	/**
	 * Tests {@link Activity#hasRequires()} method.
	 */
	public void test_hasRequires(){
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		assertTrue(diagramDomainModel.hasRequires());
	}
	
	/**
	 * Tests {@link Activity#listRequires()} method.
	 */
	public void test_listRequires(){
		
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		Iterator<Role> requiresIterator = diagramDomainModel.listRequires();
		
		assertEquals("developer", requiresIterator.next().getId());
		
		try{
			requiresIterator.next();
			fail();
		}catch(NoSuchElementException ex){
			assertTrue(true);			
		}
	}
	
	/**
	 * Tests {@link Activity#addRequires(Role)} method.
	 * 
	 * @throws FileNotFoundException: thrown if the br.ucam.kuabaSubsystem.testBase.xml was
	 * not found.
	 * 
	 * @throws Exception: thrown if save method generates an error. 
	 */
	public void test_addRequires() throws FileNotFoundException, Exception{
		
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		Role newElement = this.factory.createRole("another_Role");
		
		assertEquals(1, diagramDomainModel.getRequires().size());
		
		diagramDomainModel.addRequires(newElement);		
				
//		ArrayList<String> errors = new ArrayList<String>();		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);		
//		assertTrue(errors.isEmpty());
		
		Iterator<Role> requiresIterator = diagramDomainModel.listRequires();
		
				
		assertEquals("another_Role", requiresIterator.next().getId());
                assertEquals("developer", requiresIterator.next().getId());
	}
	
	/**
	 * Tests {@link Activity#addRequires(Role)} method.
	 * 
	 * The goal of this test is to guarantee that the addRequires method do not
	 * add the same reasoning element more than one time.
	 */
	public void test_add_repeated_Requires(){
		
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		Role developer = this.repo.getRole("developer");
		
		diagramDomainModel.addRequires(developer);
		diagramDomainModel.addRequires(developer);
		diagramDomainModel.addRequires(developer);
		diagramDomainModel.addRequires(developer);
		
		assertEquals(1, diagramDomainModel.getRequires().size());
	}
	
	/**
	 * Tests {@link Activity#removeRequires(Role)} method.
	 */
	public void test_removeRequires(){
		
		Activity diagramDomainModel = this.repo.getActivity("diagram_domain_model");
		Role developer = this.repo.getRole("developer");
		
		assertEquals(1, diagramDomainModel.getRequires().size());
		
		diagramDomainModel.removeRequires(developer);
		
		assertTrue(diagramDomainModel.getRequires().isEmpty());
		assertEquals(0, diagramDomainModel.getRequires().size());
		
		Iterator<Role> RequiresIterator = diagramDomainModel.listRequires();		
		try{
			RequiresIterator.next();
			fail();
		}catch(NoSuchElementException ex){
			assertTrue(true);			
		}
		
//		ArrayList<String> errors = new ArrayList<String>();		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);		
//		assertTrue(errors.isEmpty());
	}

}
