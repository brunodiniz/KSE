package br.ucam.kuabaSubsystem.kuabaModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;


import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.FormalModel;
import br.ucam.kuabaSubsystem.kuabaModel.impl.MyFactory;

//import com.hp.hpl.jena.util.FileUtils;
//
//import edu.stanford.smi.protegex.owl.ProtegeOWL;
//import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;

public class FormalModelTest extends AbstractKuabaTestCase {
	
	@Test
	public void test_add_has_localization() throws FileNotFoundException, Exception {
		
		FormalModel model = this.factory.createFormalModel("DER");
		model.addHasLocalization("www.der.org");
		model.addHasLocalization("www.w3c.org");
		model.addHasLocalization("www.omg.org");
		
		Collection<String> localizations = model.getHasLocalization();
		
		assertTrue(model.hasHasLocalization());
		assertEquals(3, localizations.size());
		assertTrue(localizations.contains("www.der.org"));
		assertTrue(localizations.contains("www.w3c.org"));
		assertTrue(localizations.contains("www.omg.org"));
		
		//Saving the Ontology Model on file instances.xml.
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());		 
		 
		 //Loading the model again to guarantee that the addresses was really saved.		 		 
		 
		 //otherModel.getRepositoryManager().addProjectRepository(this.lr);
		 //otherModel.load(new FileInputStream(br.ucam.kuabaSubsystem.testBase), "oi");
//		JenaOWLModel otherModel = this.model;
		 
		 //Creating other Factory with the otherModel.
//		 MyFactory otherFactory = new MyFactory(otherModel);
		 
		 //Loading the Person "thiago" again
		 FormalModel der = repo.getFormalModel("DER");
		 
		 //Loading the address from the Person "thiago"
		 Collection<String> loadedlocalizations = der.getHasLocalization();
		 
		 //testing again
		 assertTrue(der.hasHasLocalization());
		 assertEquals(3, localizations.size());
		 assertTrue(loadedlocalizations.contains("www.der.org"));
		 assertTrue(loadedlocalizations.contains("www.w3c.org"));
		 assertTrue(loadedlocalizations.contains("www.omg.org"));
		 
		
	}
	@Test
	public void test_add_repeated_localizations(){
		
		FormalModel uml = this.repo.getFormalModel("UML");
		uml.addHasLocalization("www.omg.org");
		uml.addHasLocalization("www.omg.org");
		uml.addHasLocalization("www.omg.org");
		
		Collection<String> localizations = uml.getHasLocalization();
		assertTrue(uml.hasHasLocalization());
		assertEquals(1, localizations.size());
		assertTrue(localizations.contains("www.omg.org"));
		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());		
	}
	
	@Test
	public void test_listLocalizations(){
		
		FormalModel uml = this.repo.getFormalModel("UML");				
		
		Iterator<String> localizationIterator = uml.listHasLocalization();
		
		assertFalse(localizationIterator == null);
		assertEquals("www.omg.org", localizationIterator.next());		
		
		//The addressIterator have only 3 elements, so, a call for a next element
		//must throw a NoSuchElementException.
		
		try {
			localizationIterator.next();
			assertTrue(false);
		} catch (NoSuchElementException e) {			
			assertTrue(true);
		}
	}
	@Test
	public void test_removeHasLocalization(){
		
		FormalModel uml = this.repo.getFormalModel("UML");		
		
		assertTrue(uml.hasHasLocalization());
		assertEquals(1, uml.getHasLocalization().size());
		assertTrue(uml.getHasLocalization().contains("www.omg.org"));		
		
		uml.removeHasLocalization("www.omg.org");		
		
		assertTrue(uml.getHasLocalization().isEmpty());
		
	}
	@Test
	public void test_setHasLocalization(){
		
		FormalModel uml = this.repo.getFormalModel("UML");
		
		List<String> newLocalizations = new ArrayList<String>();
		newLocalizations.add("www.w3c.org");
		newLocalizations.add("www.uml.org");
		
		uml.setHasLocalization(newLocalizations);
		
		Collection<String> localizations = uml.getHasLocalization();
		assertTrue(localizations.contains("www.w3c.org"));
		assertTrue(localizations.contains("www.uml.org"));		
		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());
	}
	@Test
	public void test_get_has_name(){
		
		FormalModel uml = this.repo.getFormalModel("UML");		
		assertEquals("Unified Modeling Language", uml.getHasName());		
	}
	@Test
	public void test_set_has_name(){
		FormalModel uml = this.repo.getFormalModel("UML");
		uml.setHasName("uml");
		
		assertEquals("uml", uml.getHasName());
//		List<String> errors = new ArrayList<String>();
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
		
//		assertTrue(errors.isEmpty());
		uml = this.repo.getFormalModel("UML");
		
		assertEquals("uml", uml.getHasName());
	}
}
