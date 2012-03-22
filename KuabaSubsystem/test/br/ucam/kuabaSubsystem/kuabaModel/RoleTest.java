package br.ucam.kuabaSubsystem.kuabaModel;

import java.util.ArrayList;
import java.util.List;

import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.Role;

import com.hp.hpl.jena.util.FileUtils;


public class RoleTest extends AbstractKuabaTestCase {

	public void test_get_has_name(){
		
		Role developer = this.factory.getKuabaRepository().getRole("developer");		
		assertEquals("developer", developer.getHasName());		
	}
	
	public void test_set_has_name(){
		Role developer = this.factory.getKuabaRepository().getRole("developer");
		developer.setHasName("Use Case Developer");
		
		assertEquals("Use Case Developer", developer.getHasName());
		List<String> errors = new ArrayList<String>();
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
		
		assertTrue(errors.isEmpty());
		developer = this.factory.getKuabaRepository().getRole("developer");
		
		assertEquals("Use Case Developer", developer.getHasName());
	}
}
