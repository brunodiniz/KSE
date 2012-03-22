package br.ucam.kuabaSubsystem.repositories;


import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntology;
import static org.junit.Assert.*;

public class OwlFileGatewayTest {
	
	private RepositoryGateway gateway;
	
	@Before
	public void setUp() throws Exception {
		this.gateway = OwlApiFileGateway.getInstance(); 
	}
	
	@Test
	public void testLoad_testKuabaKnowlegeBase_xml(){
		KuabaRepository r;		
                try {
                    r = this.gateway.load("test/br/ucam/kuabaSubsystem/testBase/testKuabaKnowlegeBase.xml");
                } catch (RepositoryLoadException ex) {
                    Logger.getLogger(OwlFileGatewayTest.class.getName()).log(Level.SEVERE, null, ex);
                    fail();
                    return;
                }
		Object model = r.getModel();
		assertNotNull(model);
		OWLOntology owlModel = null;
		try{
			owlModel = (OWLOntology)model;
		}catch (ClassCastException e) {
			fail();
		}
		
		assertEquals("http://www.tecweb.inf.puc-rio.br/DesignRationale/KuabaOntologyGeneratedTemplate.owl", owlModel.getOntologyID().getOntologyIRI().toString());
//		assertEquals(owlModel, ((AbstractRepositoryGateway)this.gateway).getRepositoryMap().get("test/br/ucam/kuabaSubsystem/testBase/testKuabaKnowlegeBase.xml").getModel());
	}
	
	@Test
	public void testLoad_formalModelsTemplate_xml(){
		KuabaRepository r;
                try {
                    r = this.gateway.load("templates/formalModelsTemplate.xml");
                } catch (RepositoryLoadException ex) {
                    Logger.getLogger(OwlFileGatewayTest.class.getName()).log(Level.SEVERE, null, ex);
                    fail();
                    return;
                }
		Object model = r.getModel();
		assertNotNull(model);
		OWLOntology owlModel = null;
		try{
			owlModel = (OWLOntology)model;
		}catch (ClassCastException e) {
			fail();
		}
		
		assertEquals("http://www.tecweb.inf.puc-rio.br/DesignRationale/KuabaOntologyTemplates.owl", owlModel.getOntologyID().getOntologyIRI().toString());
//		assertEquals(owlModel, ((AbstractRepositoryGateway)this.gateway).getRepositoryMap().get("templates/formalModelsTemplate.xml").getModel());
	}
	


}
