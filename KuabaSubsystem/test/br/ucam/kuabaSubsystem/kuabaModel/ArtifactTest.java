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
import br.ucam.kuabaSubsystem.kuabaModel.Activity;
import br.ucam.kuabaSubsystem.kuabaModel.Artifact;
import br.ucam.kuabaSubsystem.kuabaModel.FormalModel;
import br.ucam.kuabaSubsystem.kuabaModel.Person;
import br.ucam.kuabaSubsystem.kuabaModel.impl.MyFactory;

//import com.hp.hpl.jena.util.FileUtils;

//import edu.stanford.smi.protegex.owl.ProtegeOWL;
//import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
//import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
//import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFIndividual;

public class ArtifactTest extends AbstractKuabaTestCase {
	
	/**
	 * Tests {@link Artifact#getHasCreationDate()} method.
	 */
	public void test_getHasCreationDate(){
		
		Artifact cdClass = this.repo.getArtifact("cd_class");
		GregorianCalendar creationDate = cdClass.getHasCreationDate();
		
		assertEquals(2008, creationDate.get(GregorianCalendar.YEAR));		
		assertEquals(6, creationDate.get(GregorianCalendar.MONTH));		
		assertEquals(21, creationDate.get(GregorianCalendar.DAY_OF_MONTH));		
		assertEquals(20, creationDate.get(GregorianCalendar.HOUR_OF_DAY));		
		assertEquals(22, creationDate.get(GregorianCalendar.MINUTE));		
		assertEquals(0, creationDate.get(GregorianCalendar.SECOND));
		
	}
	
	/**
	 * Tests {@link Artifact#setHasCreationDate(GregorianCalendar)} method.
	 */
	public void test_setHasCreationDate() throws FileNotFoundException, Exception{
		Artifact cd = this.factory.createAtomicArtifact("CD");		
		
		GregorianCalendar creationDate = new GregorianCalendar();
		creationDate.set(2008, 3, 10, 8, 30, 00);
		
		cd.setHasCreationDate(creationDate);
		
		GregorianCalendar resultCreationDate = cd.getHasCreationDate();
		
		assertEquals(2008, resultCreationDate.get(GregorianCalendar.YEAR));
		
		assertEquals(3, resultCreationDate.get(GregorianCalendar.MONTH));
		
		assertEquals(10, resultCreationDate.get(GregorianCalendar.DAY_OF_MONTH));
		
		assertEquals(8, resultCreationDate.get(GregorianCalendar.HOUR));
		
		assertEquals(30, resultCreationDate.get(GregorianCalendar.MINUTE));
		
		assertEquals(0, resultCreationDate.get(GregorianCalendar.SECOND));		
		
		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());
		/*
		//Loading the model again to guarantee that the creation date was really saved.		 		 
		 JenaOWLModel otherModel = this.model;
		 otherModel.getRepositoryManager().addProjectRepository(this.lr);
		 otherModel.load(new FileInputStream(new File("test/br/ucam/kuabaSubsystem/testBase/testKuabaKnowlegeBase.xml")), "oi");
		 
		 //Creating other Factory with the otherModel.
		 MyFactory otherFactory = new MyFactory(otherModel);
		 
		 //Loading the Artifact "CD" again
		 Artifact retrievedArtifact = otherFactory.getArtifact("CD");		 
		 //getting the startDate literal		 
		 RDFSLiteral retrievedCreationDate = (RDFSLiteral) ((DefaultRDFIndividual)retrievedArtifact).getPropertyValue(retrievedArtifact.getHasCreationDateProperty());
		 
		 //testing if the start date was really saved in correct format. 
		 assertNotNull(retrievedCreationDate);
		 assertEquals("2008-04-10T08:30:00", retrievedCreationDate.getString());
		 */
	}
	
	/**
	 * Tests {@link Artifact#getHasDescription()} method.
	 */
	public void test_getHasDescription(){
		
		Artifact cdClass = this.repo.getArtifact("cd_class");
		assertEquals("A CD class", cdClass.getHasDescription());
	}
	
	/**
	 * Tests {@link Artifact#setHasDescription()} method.
	 */
	public void test_setHasDescription(){
		
		Artifact cd = this.factory.createAtomicArtifact("CD");
		cd.setHasDescription("This artifact represents a cd class");
		
		assertEquals("This artifact represents a cd class", cd.getHasDescription());
//		List<String> errors = new ArrayList<String>();
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
		
//		assertTrue(errors.isEmpty());
		cd = this.repo.getArtifact("CD");
		
		assertEquals("This artifact represents a cd class", cd.getHasDescription());
	}
	
	/**
	 * Tests {@link Artifact#getHasUrl()} method.
	 */
	public void test_getHasUrl(){
		
		Artifact cdClass = this.repo.getArtifact("cd_class");
		assertEquals("http://kuaba-project/googlecode/svn", cdClass.getHasUrl());
	}
	
	/**
	 * Tests {@link Artifact#setHasUrl(String)} method.
	 */
	public void test_setHasUrl(){
		
		Artifact cd = this.factory.createAtomicArtifact("CD");
		cd.setHasUrl("http://kuaba-project/googlecode/svn/KuabaSubsystem/");
		
		assertEquals("http://kuaba-project/googlecode/svn/KuabaSubsystem/", cd.getHasUrl());
//		List<String> errors = new ArrayList<String>();
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
//		
//		assertTrue(errors.isEmpty());
		cd = this.repo.getArtifact("CD");
		
		assertEquals("http://kuaba-project/googlecode/svn/KuabaSubsystem/", cd.getHasUrl());
	}
	
	/**
	 * Tests {@link Artifact#getIsCreatedBy()} method.
	 */
	public void test_getIsCreatedBy(){
		
		Artifact cdClass = this.repo.getArtifact("cd_class");
		
		Collection<Person> isCreatedByCollection = cdClass.getIsCreatedBy();
		assertFalse(isCreatedByCollection.isEmpty());
		
		assertEquals(1, isCreatedByCollection.size());
		
		Iterator<Person> isCreatedByIterator = isCreatedByCollection.iterator();
		
		assertEquals("pedro", isCreatedByIterator.next().getId());			
		
	}
	
	/**
	 * Tests {@link Artifact#hasIsCreatedBy()} method.
	 */
	public void test_hasIsCreatedBy(){
		Artifact cdClass = this.repo.getArtifact("cd_class");
		assertTrue(cdClass.hasIsCreatedBy());
	}
	
	/**
	 * Tests {@link Artifact#listIsCreatedBy()} method.
	 */
	public void test_listIsCreatedBy(){
		
		Artifact cdClass = this.repo.getArtifact("cd_class");
		Iterator<Person> IsCreatedByIterator = cdClass.listIsCreatedBy();
		
		assertEquals("pedro", IsCreatedByIterator.next().getId());
		
		try{
			IsCreatedByIterator.next();
			fail();
		}catch(NoSuchElementException ex){
			assertTrue(true);			
		}
	}
	
	/**
	 * Tests {@link Artifact#addIsCreatedBy(Person)} method.
	 * 
	 * @throws FileNotFoundException: thrown if the br.ucam.kuabaSubsystem.testBase.xml was
	 * not found.
	 * 
	 * @throws Exception: thrown if save method generates an error. 
	 */
	public void test_addIsCreatedBy() throws FileNotFoundException, Exception{
		
		Artifact cdClass = this.repo.getArtifact("cd_class");
		Person newElement = this.factory.createPerson("another_person");
		
		assertEquals(1, cdClass.getIsCreatedBy().size());
		
		cdClass.addIsCreatedBy(newElement);		
				
//		ArrayList<String> errors = new ArrayList<String>();		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);		
//		assertTrue(errors.isEmpty());
		
		Iterator<Person> IsCreatedByIterator = cdClass.listIsCreatedBy();
				
		assertEquals("another_person", IsCreatedByIterator.next().getId());
                assertEquals("pedro", IsCreatedByIterator.next().getId());
	}
	
	/**
	 * Tests {@link Artifact#addIsCreatedBy(Person)} method.
	 * 
	 * The goal of this test is to guarantee that the addIsCreatedBy method do not
	 * add the same reasoning element more than one time.
	 */
	public void test_add_repeated_IsCreatedBy(){
		
		Artifact cdClass = this.repo.getArtifact("cd_class");
		Person pedro = this.repo.getPerson("pedro");
		
		cdClass.addIsCreatedBy(pedro);
		cdClass.addIsCreatedBy(pedro);
		cdClass.addIsCreatedBy(pedro);
		cdClass.addIsCreatedBy(pedro);
		
		assertEquals(1, cdClass.getIsCreatedBy().size());
	}
	
	/**
	 * Tests {@link Artifact#removeIsCreatedBy(Person)} method.
	 */
	public void test_removeIsCreatedBy(){
		
		Artifact cdClass = this.repo.getArtifact("cd_class");
		Person pedro = this.repo.getPerson("pedro");
		
		assertEquals(1, cdClass.getIsCreatedBy().size());
		
		cdClass.removeIsCreatedBy(pedro);
		
		assertTrue(cdClass.getIsCreatedBy().isEmpty());
		assertEquals(0, cdClass.getIsCreatedBy().size());
		
		Iterator<Person> IsCreatedByIterator = cdClass.listIsCreatedBy();		
		try{
			IsCreatedByIterator.next();
			fail();
		}catch(NoSuchElementException ex){
			assertTrue(true);			
		}
		
//		ArrayList<String> errors = new ArrayList<String>();		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);		
//		assertTrue(errors.isEmpty());
	}
	
	/**
	 * Tests {@link Artifact#getIsDescribedBy()} method.
	 */
	public void test_getIsDescribedBy(){
		
		Artifact cdClass = this.repo.getArtifact("cd_class");
		
		Collection<FormalModel> isDescribedByCollection = cdClass.getIsDescribedBy();
		assertFalse(isDescribedByCollection.isEmpty());
		
		assertEquals(1, isDescribedByCollection.size());
		
		Iterator<FormalModel> isDescribedByIterator = isDescribedByCollection.iterator();
		
		assertEquals("UML", isDescribedByIterator.next().getId());			
		
	}
	
	/**
	 * Tests {@link Artifact#hasIsDescribedBy()} method.
	 */
	public void test_hasIsDescribedBy(){
		Artifact cdClass = this.repo.getArtifact("cd_class");
		assertTrue(cdClass.hasIsDescribedBy());
	}
	
	/**
	 * Tests {@link Artifact#listIsDescribedBy()} method.
	 */
	public void test_listIsDescribedBy(){
		
		Artifact cdClass = this.repo.getArtifact("cd_class");
		Iterator<FormalModel> IsDescribedByIterator = cdClass.listIsDescribedBy();
		
		assertEquals("UML", IsDescribedByIterator.next().getId());
		
		try{
			IsDescribedByIterator.next();
			fail();
		}catch(NoSuchElementException ex){
			assertTrue(true);			
		}
	}
	
	/**
	 * Tests {@link Artifact#addIsDescribedBy(FormalModel)} method.
	 * 
	 * @throws FileNotFoundException: thrown if the br.ucam.kuabaSubsystem.testBase.xml was
	 * not found.
	 * 
	 * @throws Exception: thrown if save method generates an error. 
	 */
	public void test_addIsDescribedBy() throws FileNotFoundException, Exception{
		
		Artifact cdClass = this.repo.getArtifact("cd_class");
		FormalModel newElement = this.factory.createFormalModel("another_FormalModel");
		
		assertEquals(1, cdClass.getIsDescribedBy().size());
		
		cdClass.addIsDescribedBy(newElement);		
				
//		ArrayList<String> errors = new ArrayList<String>();		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);		
//		assertTrue(errors.isEmpty());
		
		Iterator<FormalModel> IsDescribedByIterator = cdClass.listIsDescribedBy();
		
				
		assertEquals("another_FormalModel", IsDescribedByIterator.next().getId());
                assertEquals("UML", IsDescribedByIterator.next().getId());
	}
	
	/**
	 * Tests {@link Artifact#addIsDescribedBy(FormalModel)} method.
	 * 
	 * The goal of this test is to guarantee that the addIsDescribedBy method do not
	 * add the same reasoning element more than one time.
	 */
	public void test_add_repeated_IsDescribedBy(){
		
		Artifact cdClass = this.repo.getArtifact("cd_class");
		FormalModel UML = this.repo.getFormalModel("UML");
		
		cdClass.addIsDescribedBy(UML);
		cdClass.addIsDescribedBy(UML);
		cdClass.addIsDescribedBy(UML);
		cdClass.addIsDescribedBy(UML);
		
		assertEquals(1, cdClass.getIsDescribedBy().size());
	}
	
	/**
	 * Tests {@link Artifact#removeIsDescribedBy(FormalModel)} method.
	 */
	public void test_removeIsDescribedBy(){
		
		Artifact cdClass = this.repo.getArtifact("cd_class");
		FormalModel UML = this.repo.getFormalModel("UML");
		
		assertEquals(1, cdClass.getIsDescribedBy().size());
		
		cdClass.removeIsDescribedBy(UML);
		
		assertTrue(cdClass.getIsDescribedBy().isEmpty());
		assertEquals(0, cdClass.getIsDescribedBy().size());
		
		Iterator<FormalModel> IsDescribedByIterator = cdClass.listIsDescribedBy();		
		try{
			IsDescribedByIterator.next();
			fail();
		}catch(NoSuchElementException ex){
			assertTrue(true);			
		}
		
//		ArrayList<String> errors = new ArrayList<String>();		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);		
//		assertTrue(errors.isEmpty());
	}
	
}
