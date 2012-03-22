package br.ucam.kuabaSubsystem.kuabaModel;

import java.util.ArrayList;
import java.util.List;

import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.RelatedType;

import com.hp.hpl.jena.util.FileUtils;


public class RelatedTypeTest extends AbstractKuabaTestCase {
	
	public void test_get_has_relation_type(){
		
		RelatedType include = this.factory.getKuabaRepository().getRelatedType("include");		
		assertEquals("include", include.getHasRelationType());		
	}
	
	public void test_set_has_relation_type(){
		RelatedType uml = this.factory.getKuabaRepository().getRelatedType("include");
		uml.setHasRelationType("include two use cases");
		
		assertEquals("include two use cases", uml.getHasRelationType());
		List<String> errors = new ArrayList<String>();
//		this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
                this.gate.save(this.factory.getKuabaRepository());
		
		assertTrue(errors.isEmpty());
		uml = this.factory.getKuabaRepository().getRelatedType("include");
		
		assertEquals("include two use cases", uml.getHasRelationType());
	}
}
