package br.ucam.kuabaSubsystem.kuabaModel;

import java.util.ArrayList;

import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.Argument;
import br.ucam.kuabaSubsystem.kuabaModel.Justification;

import com.hp.hpl.jena.util.FileUtils;


public class JustificationTest extends AbstractKuabaTestCase {

	public void test_HasText(){
		Justification justification = this.factory.createJustification("test justification");
		
//		assertEquals("", justification.getHasText());
		
		justification.setHasText("testing the setHasText method");
		
		ArrayList<String> errors = new ArrayList<String>();
		
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
		
		assertTrue(errors.isEmpty());
		
		Justification retrievedJustification = this.repo.getJustification("test_justification");
		assertEquals("testing the setHasText method", retrievedJustification.getHasText());
	}
}
