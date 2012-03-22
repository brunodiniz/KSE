package br.ucam.kuabaSubsystem.kuabaModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.NoSuchElementException;


import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.Activity;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.ExpectedDuration;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.ReasoningElement;
import br.ucam.kuabaSubsystem.kuabaModel.Role;
import br.ucam.kuabaSubsystem.kuabaModel.impl.MyFactory;

//import com.hp.hpl.jena.util.FileUtils;
//
//import edu.stanford.smi.protegex.owl.ProtegeOWL;
//import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
//import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
//import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFIndividual;

public class ArgumentTest extends AbstractKuabaTestCase {
	
	/**
	 * Tests {@link ReasoningElement#setHasCreationDate(GregorianCalendar)}
	 * 
	 * @throws FileNotFoundException: thrown if the br.ucam.kuabaSubsystem.testBase.xml was
	 * not found.
	 * 
	 * @throws Exception: thrown if save method generates an error. 
	 */
	public void test_setHasCreationDate() throws FileNotFoundException, Exception{
		Argument modelAsClass = this.factory.createArgument("model_as_class");		
		
		GregorianCalendar creationDate = new GregorianCalendar();
		creationDate.set(2008, 3, 10, 8, 30, 00);
		
		modelAsClass.setHasCreationDate(creationDate);
		
		GregorianCalendar resultCreationDate = modelAsClass.getHasCreationDate();
		
		assertEquals(2008, resultCreationDate.get(GregorianCalendar.YEAR));
		
		assertEquals(3, resultCreationDate.get(GregorianCalendar.MONTH));
		
		assertEquals(10, resultCreationDate.get(GregorianCalendar.DAY_OF_MONTH));
		
		assertEquals(8, resultCreationDate.get(GregorianCalendar.HOUR));
		
		assertEquals(30, resultCreationDate.get(GregorianCalendar.MINUTE));
		
		assertEquals(0, resultCreationDate.get(GregorianCalendar.SECOND));		
		
		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());
		
		//Loading the model again to guarantee that the start date was really saved.		 		 
//		 JenaOWLModel otherModel = ProtegeOWL.createJenaOWLModel();
//		 otherModel.getRepositoryManager().addProjectRepository(this.lr);
//		 otherModel.load(new FileInputStream(new File("test/br/ucam/kuabaSubsystem/testBase/testKuabaKnowlegeBase.xml")), "oi");
		 
		 //Creating other Factory with the otherModel.
//		 MyFactory otherFactory = new MyFactory(otherModel);
		 
		 //Loading the Activity "use case design" again
		 Argument retrievedArgument = repo.getArgument("model_as_class");
		 
		 //getting the startDate literal		 
		 GregorianCalendar retrievedCreationDate = retrievedArgument.getHasCreationDate();
		 
		 //testing if the start date was really saved in correct format. 
		 assertNotNull(retrievedCreationDate);
//		 assertEquals("2008-04-10T08:30:00", retrievedCreationDate.getString());
////		 otherModel.dispose();
	}	
	
	/**
	 * Tests {@link ReasoningElement#getHasCreationDate()}
	 */
        
        //não faz sentido não deixar a factory setar a creation date automaticamente
//	public void test_get_empty_creation_date(){
//		
//		Argument testArgument = this.factory.createArgument("test"); 
//		
//		assertNull(testArgument.getHasCreationDate());
//		
//		testArgument.setHasCreationDate(new GregorianCalendar());
//		assertNotNull(testArgument.getHasCreationDate());
//	}
	
	/**
	 * Tests {@link ReasoningElement#getHasText()} and
	 * {@link ReasoningElement#setHasText(String)}.
	 *  
	 */
	public void test_HasText(){
		Argument testArgument = this.factory.createArgument("testArgument");
		
//		assertEquals("", testArgument.getHasText());
		
		testArgument.setHasText("testing the setHasText method");
		
//		ArrayList<String> errors = new ArrayList<String>();
//		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
//		
//		assertTrue(errors.isEmpty());
		
		Argument retrievedArgument = this.repo.getArgument("testArgument");
		assertEquals("testing the setHasText method", retrievedArgument.getHasText());
	}
	
	/**
	 *  Test {@link Argument#getConsiders()} method.
	 *  
	 *  Tests if the right Question object is returned. This relationship
	 *  is defined on arguments fixture.
	 *  
	 *  @see br.ucam.kuabaSubsystem.fixtures/arguments.xml  
	 */
	public void test_getConsiders(){
		
		Argument modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		
		Question whatElements = modelAthleteAsClass.getConsiders();
		
		assertEquals(Question.ROOT_QUESTION_ID, whatElements.getId());
		assertEquals("OR", whatElements.getHasType());
		assertEquals("What Elements?", whatElements.getHasText());
	}
	
	/**
	 * Tests {@link Argument#setConsiders(Question)} method.
	 */
	public void test_setConsiders(){
		
		Argument modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		
		Question howModelCd = this.repo.getQuestion("how_model_cd");		
		
		assertEquals(Question.ROOT_QUESTION_ID, modelAthleteAsClass.getConsiders().getId());
		
		modelAthleteAsClass.setConsiders(howModelCd);
		
//		ArrayList<String> errors = new ArrayList<String>();
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
//		
//		assertTrue(errors.isEmpty());
		
		modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		howModelCd = modelAthleteAsClass.getConsiders();
		
		assertEquals("how_model_cd", howModelCd.getId());
		
		
	}
	
	/**
	 * Tests {@link Argument#getInFavorOf()} method.
	 */
	public void test_getInFavorOf(){
		
		Argument modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		
		Collection<Idea> inFavorOfCollection = modelAthleteAsClass.getInFavorOf();
		assertFalse(inFavorOfCollection.isEmpty());
		
		assertEquals(1, inFavorOfCollection.size());
		
		Iterator<Idea> inFavorOfIterator = inFavorOfCollection.iterator();
		
		assertEquals("athlete", inFavorOfIterator.next().getId());
		
	}
	
	/**
	 * Tests {@link Argument#hasInFavorOf()} method.
	 */
	public void test_hasInFavorOf(){
		Argument modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		assertTrue(modelAthleteAsClass.hasInFavorOf());
	}
	
	/**
	 * Tests {@link Argument#listInFavorOf()} method.
	 */
	public void test_listInFavorOf(){
		
		Argument modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		Iterator<Idea> InFavorOfIterator = modelAthleteAsClass.listInFavorOf();
		
		assertEquals("athlete", InFavorOfIterator.next().getId());
		
		try{
			InFavorOfIterator.next();
			fail();
		}catch(NoSuchElementException ex){
			assertTrue(true);			
		}
	}
	
	/**
	 * Tests {@link Argument#addInFavorOf(Idea)} method.
	 * 
	 * @throws FileNotFoundException: thrown if the br.ucam.kuabaSubsystem.testBase.xml was
	 * not found.
	 * 
	 * @throws Exception: thrown if save method generates an error. 
	 */
	public void test_addInFavorOf() throws FileNotFoundException, Exception{
		
		Argument modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		Idea newElement = this.factory.createIdea("another_Idea");
		
		assertEquals(1, modelAthleteAsClass.getInFavorOf().size());
		
		modelAthleteAsClass.addInFavorOf(newElement);		
				
//		ArrayList<String> errors = new ArrayList<String>();		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);		
//		assertTrue(errors.isEmpty());
		
		assertEquals(2, modelAthleteAsClass.getInFavorOf().size());
		Iterator<Idea> InFavorOfIterator = modelAthleteAsClass.listInFavorOf();
		
                assertEquals("another_Idea", InFavorOfIterator.next().getId());
		assertEquals("athlete", InFavorOfIterator.next().getId());		
	}
	
	/**
	 * Tests {@link Argument#addInFavorOf(Idea)} method.
	 * 
	 * Here we testing that the same Idea can't be present in inFavorof collection
	 * and in objectsTo collection.
	 * 
	 *  If an Idea is added to InFavorOf collection and this Idea is present in 
	 *  objectsTo collection, it must be removed from the objectsTo collection before
	 *  that can be added to inFavorOf collection.
	 *  
	 *  @see Argument#addInFavorOf(Idea) contract.
	 */
	public void test_add_same_idea_in_inFavorOf_collection(){
		
		Argument modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		Idea attribute = this.repo.getIdea("attribute");
		
		modelAthleteAsClass.addInFavorOf(attribute);
		
//		ArrayList<String> errors = new ArrayList<String>();		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);		
//		assertTrue(errors.isEmpty());
		
		modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		
		assertTrue(modelAthleteAsClass.hasInFavorOf());
		assertFalse(modelAthleteAsClass.getInFavorOf().isEmpty());
		assertEquals(2, modelAthleteAsClass.getInFavorOf().size());
		
		Collection<Idea> inFavorOfCollection = modelAthleteAsClass.getInFavorOf();
		
		assertTrue(inFavorOfCollection.contains(repo.getIdea("athlete")));
		assertTrue(inFavorOfCollection.contains(attribute));		
		
		assertTrue(modelAthleteAsClass.getObjectsTo().isEmpty());
		assertFalse(modelAthleteAsClass.hasObjectsTo());		
				
	}
	/**
	 * Tests {@link Argument#addInFavorOf(Idea)} method.
	 * 
	 * The goal of this test is to guarantee that the addInFavorOf method do not
	 * add the same reasoning element more than one time.
	 */
	public void test_add_repeated_InFavorOf(){
		
		Argument modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		Idea athlete = this.repo.getIdea("athlete");
		
		modelAthleteAsClass.addInFavorOf(athlete);
		modelAthleteAsClass.addInFavorOf(athlete);
		modelAthleteAsClass.addInFavorOf(athlete);
		modelAthleteAsClass.addInFavorOf(athlete);
		
		assertEquals(1, modelAthleteAsClass.getInFavorOf().size());
	}
	
	/**
	 * Tests {@link Argument#removeInFavorOf(Idea)} method.
	 */
	public void test_removeInFavorOf(){
		
		Argument modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		Idea athlete = this.repo.getIdea("athlete");
		
		assertEquals(1, modelAthleteAsClass.getInFavorOf().size());
		
		modelAthleteAsClass.removeInFavorOf(athlete);
		
		assertTrue(modelAthleteAsClass.getInFavorOf().isEmpty());
		assertEquals(0, modelAthleteAsClass.getInFavorOf().size());
		
		Iterator<Idea> InFavorOfIterator = modelAthleteAsClass.listInFavorOf();		
		try{
			InFavorOfIterator.next();
			fail();
		}catch(NoSuchElementException ex){
			assertTrue(true);			
		}
		
//		ArrayList<String> errors = new ArrayList<String>();		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);		
//		assertTrue(errors.isEmpty());
	}
	
	/**
	 * Tests {@link Argument#getObjectsTo()} method.
	 */
	public void test_getObjectsTo(){
		
		Argument modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		
		Collection<Idea> ObjectsToCollection = modelAthleteAsClass.getObjectsTo();
		assertFalse(ObjectsToCollection.isEmpty());
		
		assertEquals(1, ObjectsToCollection.size());
		
		Iterator<Idea> ObjectsToIterator = ObjectsToCollection.iterator();
		
		assertEquals("attribute", ObjectsToIterator.next().getId());			
		
	}
	
	/**
	 * Tests {@link Argument#hasObjectsTo()} method.
	 */
	public void test_hasObjectsTo(){
		Argument modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		assertTrue(modelAthleteAsClass.hasObjectsTo());
	}
	
	/**
	 * Tests {@link Argument#listObjectsTo()} method.
	 */
	public void test_listObjectsTo(){
		
		Argument modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		Iterator<Idea> ObjectsToIterator = modelAthleteAsClass.listObjectsTo();
		
		assertEquals("attribute", ObjectsToIterator.next().getId());
		
		try{
			ObjectsToIterator.next();
			fail();
		}catch(NoSuchElementException ex){
			assertTrue(true);			
		}
	}
	
	/**
	 * Tests {@link Argument#addObjectsTo(Idea)} method.
	 * 
	 * @throws FileNotFoundException: thrown if the br.ucam.kuabaSubsystem.testBase.xml was
	 * not found.
	 * 
	 * @throws Exception: thrown if save method generates an error. 
	 */
	public void test_addObjectsTo() throws FileNotFoundException, Exception{
		
		Argument modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		Idea newElement = this.factory.createIdea("another_Idea");
		
		assertEquals(1, modelAthleteAsClass.getObjectsTo().size());
		
		modelAthleteAsClass.addObjectsTo(newElement);		
				
//		ArrayList<String> errors = new ArrayList<String>();		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);		
//		assertTrue(errors.isEmpty());
		
		assertEquals(2, modelAthleteAsClass.getObjectsTo().size());
		Iterator<Idea> ObjectsToIterator = modelAthleteAsClass.listObjectsTo();
		
		assertEquals("attribute", ObjectsToIterator.next().getId());		
		assertEquals("another_Idea", ObjectsToIterator.next().getId());
	}
	
	/**
	 * Tests {@link Argument#addInFavorOf(Idea)} method.
	 * 
	 * Here we testing that the same Idea can't be present in inFavorof collection
	 * and in objectsTo collection.
	 * 
	 *  If an Idea is added to InFavorOf collection and this Idea is present in 
	 *  objectsTo collection, it must be removed from the objectsTo collection before
	 *  that can be added to inFavorOf collection.
	 *  
	 *  @see Argument#addInFavorOf(Idea) contract.
	 */
	public void test_add_same_idea_in_objectsTo_collection(){
		
		Argument modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		Idea athlete = this.repo.getIdea("athlete");
		
		modelAthleteAsClass.addObjectsTo(athlete);
		
//		ArrayList<String> errors = new ArrayList<String>();		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);		
//		assertTrue(errors.isEmpty());
		
		modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		
		assertFalse(modelAthleteAsClass.hasInFavorOf());
		assertTrue(modelAthleteAsClass.getInFavorOf().isEmpty());
		
		assertFalse(modelAthleteAsClass.getObjectsTo().isEmpty());
		assertTrue(modelAthleteAsClass.hasObjectsTo());
		assertEquals(2, modelAthleteAsClass.getObjectsTo().size());
		
		Iterator<Idea> objectsToIterator = modelAthleteAsClass.listObjectsTo();
				
		assertEquals("attribute", objectsToIterator.next().getId());
		assertEquals("athlete", objectsToIterator.next().getId());		
	}
	
	/**
	 * Tests {@link Argument#addObjectsTo(Idea)} method.
	 * 
	 * The goal of this test is to guarantee that the addObjectsTo method do not
	 * add the same reasoning element more than one time.
	 */
	public void test_add_repeated_ObjectsTo(){
		
		Argument modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		Idea attribute = this.repo.getIdea("attribute");
		
		modelAthleteAsClass.addObjectsTo(attribute);
		modelAthleteAsClass.addObjectsTo(attribute);
		modelAthleteAsClass.addObjectsTo(attribute);
		modelAthleteAsClass.addObjectsTo(attribute);
		
		assertEquals(1, modelAthleteAsClass.getObjectsTo().size());
	}
	
	/**
	 * Tests {@link Argument#removeObjectsTo(Idea)} method.
	 */
	public void test_removeObjectsTo(){
		
		Argument modelAthleteAsClass = this.repo.getArgument("model_athlete_as_class");
		Idea attribute = this.repo.getIdea("attribute");
		
		assertEquals(1, modelAthleteAsClass.getObjectsTo().size());
		
		modelAthleteAsClass.removeObjectsTo(attribute);
		
		assertTrue(modelAthleteAsClass.getObjectsTo().isEmpty());
		assertEquals(0, modelAthleteAsClass.getObjectsTo().size());
		
		Iterator<Idea> ObjectsToIterator = modelAthleteAsClass.listObjectsTo();		
		try{
			ObjectsToIterator.next();
			fail();
		}catch(NoSuchElementException ex){
			assertTrue(true);			
		}
		
//		ArrayList<String> errors = new ArrayList<String>();		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);		
//		assertTrue(errors.isEmpty());
	}
}
