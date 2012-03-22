package br.ucam.kuabaSubsystem.kuabaModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.impl.MyFactory;

import com.hp.hpl.jena.util.FileUtils;


public class QuestionTest extends AbstractKuabaTestCase {
	
	@Override
	protected void setUp() throws Exception {		
		super.setUp();
	}
	public void test_add_has_type() throws FileNotFoundException, Exception {
		
		Question howModelGenre = this.factory.createQuestion("how_model_genre");
//		howModelGenre.addHasType(Question.ANDTYPE);
//		howModelGenre.addHasType(Question.ORTYPE);
//		howModelGenre.addHasType(Question.XORTYPE);
                howModelGenre.setHasType(Question.XORTYPE);
		
//		Collection<String> types = howModelGenre.getHasType();
                String type = howModelGenre.getHasType();
                
		assertTrue(howModelGenre.hasHasType());
                assertNotNull(type);
//		assertEquals(3, types.size());
//		assertTrue(types.contains(Question.ANDTYPE));
//		assertTrue(types.contains(Question.ORTYPE));
//		assertTrue(types.contains(Question.XORTYPE));
                assertEquals(type,Question.XORTYPE);
		
		//Saving the Ontology Model on file instances.xml.
                gate.save(repo);
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());		 

		 //Loading the model again to guarantee that the addresses was really saved.
                repo.dispose();
                repo = gate.load(testKuabaKnowlegeBase.getAbsolutePath());
//		 JenaOWLModel otherModel = ProtegeOWL.createJenaOWLModel();
//		 otherModel.getRepositoryManager().addProjectRepository(this.lr);
//		 FileInputStream stream = new FileInputStream(testKuabaKnowlegeBase); 
//		 otherModel.load(stream, "oi");
		 
		 //Creating other Factory with the otherModel.
//		 MyFactory otherFactory = new MyFactory(otherModel);
		 

		 Question retrievedHowModelGenre = repo.getQuestion("how_model_genre");
		 

//		 Collection<String> loadedTypes = retrievedHowModelGenre.getHasType();
                 String loadedType = retrievedHowModelGenre.getHasType();
		 
		 //testing again
		 assertTrue(retrievedHowModelGenre.hasHasType());
                 assertNotNull(loadedType);
                 assertEquals(loadedType, Question.XORTYPE);
//		 assertEquals(3, loadedTypes.size());
//		 assertTrue(loadedTypes.contains(Question.ANDTYPE));
//		 assertTrue(loadedTypes.contains(Question.ORTYPE));
//		 assertTrue(loadedTypes.contains(Question.XORTYPE));		 
//		 otherModel.dispose();
//		 stream.close();
	}
	
	public void test_add_repeated_types(){
		
		Question howModelCd = this.repo.getQuestion("how_model_cd");
		howModelCd.setHasType(Question.ORTYPE);
		howModelCd.setHasType(Question.ANDTYPE);
		howModelCd.setHasType(Question.XORTYPE);
		
		String type = howModelCd.getHasType();
		assertTrue(howModelCd.hasHasType());
		System.out.println(type);
		assertEquals(Question.XORTYPE, type);
		
//		assertTrue(types.contains(Question.XORTYPE));
		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());		
	}
	
	public void test_listLocalizations(){
		
		Question uml = this.repo.getQuestion("how_model_cd");				
		
		String type = uml.getHasType();
		
		assertFalse(type == null);
		assertEquals("XOR", type);		
		
		//The addressIterator have only 3 elements, so, a call for a next element
		//must throw a NoSuchElementException.
		
//		try {
//			typeIterator.next();
//			assertTrue(false);
//		} catch (NoSuchElementException e) {			
//			assertTrue(true);
//		}
	}
	
//	public void test_removeHasLocalization(){
//		
//		Question howModelCd = this.repo.getQuestion("how_model_cd");		
//		
//		assertTrue(howModelCd.hasHasType());
//		assertEquals(1, howModelCd.getHasType().size());
//		assertTrue(howModelCd.getHasType().contains("XOR"));		
//		
//		howModelCd.removeHasType("XOR");		
//		
//		assertTrue(howModelCd.getHasType().isEmpty());
//		
//	}
	
//	public void test_setHasLocalization(){
//		
//		Question howModelCd = this.factory.getQuestion("how_model_cd");
//		
//		List<String> newLocalizations = new ArrayList<String>();
//		newLocalizations.add(Question.ANDTYPE);
//		newLocalizations.add(Question.ORTYPE);
//		
//		howModelCd.setHasType(newLocalizations);
//		
//		Collection<String> types = howModelCd.getHasType();
//		assertTrue(types.contains(Question.ANDTYPE));
//		assertTrue(types.contains(Question.ORTYPE));		
//		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());
//	}
}
