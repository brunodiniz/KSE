package br.ucam.kuabaSubsystem.abstractTestCase;

import java.io.File;
import java.net.URL;
import java.util.Collection;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;

import org.easymock.EasyMock;
import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;

import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.core.PropertiesConfigurator;
import br.ucam.kuabaSubsystem.kuabaFacades.KuabaFacade;
import br.ucam.kuabaSubsystem.kuabaModel.impl.MyFactory;
import br.ucam.kuabaSubsystem.kuabaModel.impl.OwlApiKuabaModelFactory;
import br.ucam.kuabaSubsystem.observers.KuabaEventPump;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.repositories.OwlApiKuabaRepository;
import org.semanticweb.owlapi.model.OWLOntology;

public class FunctionalTestCase extends AbstractKuabaTestCase {
	protected MofPackage core;
	protected MofPackage useCase;
	protected KuabaFacade facade;
	protected KuabaRepository templatesRepository;
	protected KuabaRepository modelRepository;
	
	@Override
	protected void setUp() throws Exception {		
		super.setUp();
		MDRepository repository = MDRManager.getDefault().getDefaultRepository();
		ModelPackage mofExtent = (ModelPackage) repository.getExtent("MOF Extent");
		if (mofExtent == null) {

            try {
                mofExtent = 
                    (ModelPackage) repository.createExtent("MOF Extent");
            } catch (CreationFailedException e) {
                e.printStackTrace();
            }
		}
		    XMIReader reader = XMIReaderFactory.getDefault().createXMIReader();		       
		    
		        String metamodelURL = "mof/01-02-15_Diff.xml";
		    File file = new File(metamodelURL);
		    
		    assertNotNull(file);
		    assertTrue(file.exists());
		    
            URL resource = file.toURL();//getClass().getResource(metamodelURL);
            
            Collection<MofPackage> col = null;
            col = reader.read(resource.toString(), mofExtent);
            
            for (MofPackage mofPackage : col) {
				if(mofPackage.getName().equals("Core"))
					this.core = mofPackage;
				
				if(mofPackage.getName().equals("Use_Cases"))
					this.useCase = mofPackage;
				
			}
            assertNotNull(this.core);
            assertNotNull(this.useCase);
            
            PropertiesConfigurator manager = new PropertiesConfigurator();
    		manager.addProperty(KuabaSubsystem.REPOSITORY_PATH, this.testKuabaKnowlegeBase.toString());
    		manager.addProperty(KuabaSubsystem.TEMPLATES_REPOSITORY_PATH, TEST_BASE_PACKAGE + "templatesTestBase.xml");	
    		
    		KuabaEventPump mockPump = EasyMock.createNiceMock(KuabaEventPump.class);
    		EasyMock.replay(mockPump);
    		KuabaSubsystem.init(manager, mockPump );
    		this.facade = new KuabaFacade();
    		this.facade.setKuabaFactory(this.factory);
    		this.templatesRepository = KuabaSubsystem.gateway.load(TEST_BASE_PACKAGE + "templatesTestBase.xml");
    		String modelUrl = KuabaSubsystem.getProperty(
    				KuabaSubsystem.REPOSITORY_PATH);
    		this.modelRepository = KuabaSubsystem.gateway.load(modelUrl);
    		
    		this.factory = new OwlApiKuabaModelFactory(modelRepository);
    		this.facade.getMainSequence().setCurrentVal(0);
    	
	}

	@Override
	protected void tearDown() throws Exception {		
		super.tearDown();		
		KuabaSubsystem.eventPump = null;		
		this.templatesRepository.dispose();		
		this.core = null;
		this.facade = null;
		this.useCase = null;
		br.ucam.kuabaSubsystem.kuabaModel.KuabaEventPump.getInstance(
				).reset();
		
		
	}

	@Override
	public void test() {		
		super.test();
	}	

}
