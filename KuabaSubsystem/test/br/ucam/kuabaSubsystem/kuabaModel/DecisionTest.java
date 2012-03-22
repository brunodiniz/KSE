package br.ucam.kuabaSubsystem.kuabaModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.Activity;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.impl.MyFactory;

//import com.hp.hpl.jena.util.FileUtils;
//
//import edu.stanford.smi.protegex.owl.ProtegeOWL;
//import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
//import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
//import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFIndividual;


public class DecisionTest extends AbstractKuabaTestCase{

	public void test_get_has_date(){
		
		Decision notModelCdAsClass = this.repo.getDecision("model_cd_as_class");
		
		assertTrue(notModelCdAsClass.hasHasDate());
		
//		GregorianCalendar expectedDecisionDate = new GregorianCalendar();
//		expectedDecisionDate.set(2008, 05, 21, 20, 22, 03);
		
		GregorianCalendar decisionDate = notModelCdAsClass.getHasDate();
		
		assertEquals(2008, decisionDate.get(GregorianCalendar.YEAR));
		
		assertEquals(6, decisionDate.get(GregorianCalendar.MONTH));
		
		assertEquals(21, decisionDate.get(GregorianCalendar.DAY_OF_MONTH));
		
		assertEquals(20, decisionDate.get(GregorianCalendar.HOUR_OF_DAY));
		
		assertEquals(22, decisionDate.get(GregorianCalendar.MINUTE));
		
		assertEquals(03, decisionDate.get(GregorianCalendar.SECOND));
	}
	
	public void test_set_has_date() throws FileNotFoundException, Exception{
		
		Decision notModelCdAsClass = this.repo.getDecision("model_cd_as_class");
		
		GregorianCalendar newDecisionDate = new GregorianCalendar();
		newDecisionDate.set(2007, 06, 9, 20, 00, 00);
		notModelCdAsClass.setHasDate(newDecisionDate);
		
		GregorianCalendar decisionDate = notModelCdAsClass.getHasDate();
		assertEquals(2007, decisionDate.get(GregorianCalendar.YEAR));
		
		assertEquals(6, decisionDate.get(GregorianCalendar.MONTH));
		
		assertEquals(9, decisionDate.get(GregorianCalendar.DAY_OF_MONTH));
		
		assertEquals(20, decisionDate.get(GregorianCalendar.HOUR_OF_DAY));
		
		assertEquals(00, decisionDate.get(GregorianCalendar.MINUTE));
		
		assertEquals(00, decisionDate.get(GregorianCalendar.SECOND));
		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());
		
		//Loading the model again to guarantee that the start date was really saved.		 		 
//		 JenaOWLModel otherModel = ProtegeOWL.createJenaOWLModel();
//		 otherModel.getRepositoryManager().addProjectRepository(this.lr);
//		 otherModel.load(new FileInputStream(new File("test/br/ucam/kuabaSubsystem/testBase/testKuabaKnowlegeBase.xml")), "oi");
		 
		 //Creating other Factory with the otherModel.
//		 MyFactory otherFactory = new MyFactory(otherModel);
		 
		 //Loading the Decision "model_cd_as_class" again
		 Decision retrievedDecision = repo.getDecision("model_cd_as_class");
		 
		 //getting the Date literal		 
//		 RDFSLiteral retrievedDate = (RDFSLiteral) ((DefaultRDFIndividual)retrievedDecision).getPropertyValue(retrievedDecision.getHasDateProperty());
		 GregorianCalendar retrievedDate = retrievedDecision.getHasDate();

                 
		 //testing if the date was really saved in correct format. 
		 assertNotNull(retrievedDate);
                 
		
		assertEquals(2007, retrievedDate.get(GregorianCalendar.YEAR));
		
		assertEquals(6, retrievedDate.get(GregorianCalendar.MONTH));
		
		assertEquals(9, retrievedDate.get(GregorianCalendar.DAY_OF_MONTH));
		
		assertEquals(20, retrievedDate.get(GregorianCalendar.HOUR_OF_DAY));
		
		assertEquals(0, retrievedDate.get(GregorianCalendar.MINUTE));
		
		assertEquals(0, retrievedDate.get(GregorianCalendar.SECOND));
                 
//		 assertEquals("2007-07-09T20:00:00", retrievedDate);
//		 otherModel.dispose();
	}
	
	public void test_get_is_accepted(){
		
		Decision notModelCdAsClass = this.repo.getDecision("model_cd_as_class");
		assertFalse(notModelCdAsClass.getIsAccepted());
	}
	
	public void test_set_is_accepted(){
		
		Decision notModelCdAsClass = this.repo.getDecision("model_cd_as_class");
		assertFalse(notModelCdAsClass.getIsAccepted());
		
		notModelCdAsClass.setIsAccepted(true);
		assertTrue(notModelCdAsClass.getIsAccepted());
		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());
		
		Decision modelCdAsClass = this.repo.getDecision("model_cd_as_class");
		
		assertTrue(modelCdAsClass.getIsAccepted());
	}
	
	/**
	 *  Test {@link Decision#getConcludes()} method.
	 *  
	 *  Tests if the right Idea object is returned. This relationship
	 *  is defined on decisions fixture.
	 *  
	 *  @see br.ucam.kuabaSubsystem.fixtures/decisions.xml  
	 */
//	public void test_getConcludes(){
//		
//		Decision modelCdAsAttribute = this.repo.getDecision("model_cd_as_attribute");		
//		Idea attribute = modelCdAsAttribute.getConcludes();		
//		assertEquals("attribute", ((DefaultRDFIndividual)attribute).getName());		
//		
//	}
	
	/**
	 * Tests {@link Decision#setConcludes(Idea)} method.
	 */
	public void test_setConcludes(){
		
		Decision modelCdAsAttribute = this.repo.getDecision("model_cd_as_attribute");
		
		Idea athlete = this.repo.getIdea("athlete");		
		
//		assertEquals("attribute", ((DefaultRDFIndividual)modelCdAsAttribute.getConcludes()).getName());
		
		modelCdAsAttribute.setConcludes(athlete);
		
//		ArrayList<String> errors = new ArrayList<String>();
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
//		
//		assertTrue(errors.isEmpty());
		
//		modelCdAsAttribute = this.repo.getDecision("model_cd_as_attribute");
		athlete = modelCdAsAttribute.getConcludes();
		
		assertEquals("athlete", athlete.getId());
		
		
	}
}
