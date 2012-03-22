package br.ucam.kuabaSubsystem.util;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.jmi.model.Classifier;
import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofClass;
import javax.jmi.model.MofPackage;
import javax.jmi.model.NameNotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;

import br.ucam.kuabaSubsystem.util.MofHelper;


public class MofHelperTest {
	private MofPackage core;
	private MofPackage commonBehavior;
	
	@Before
	public void setUp() throws Exception {
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
            System.out.println("conteudo:" + resource);
            Collection<MofPackage> col = null;
            col = reader.read(resource.toString(), mofExtent);            
            assertNotNull(col);

            assertNotNull(col);
            MofPackage core = null;
            Iterator<MofPackage> itm = col.iterator();
            while (itm.hasNext()) {
				MofPackage mofPackage = (MofPackage) itm.next();				
				
				if(mofPackage.getName().equals("Core")){				
					this.core = mofPackage;
				}
				if(mofPackage.getName().equals("Common_Behavior")){				
					this.commonBehavior = mofPackage;
				}
			}
         MofHelper.subClassesMap = new HashMap<MofClass, List<MofClass>>();   

	}

	@After
	public void tearDown() throws Exception {
		this.commonBehavior = null;
		this.core = null;
		MofHelper.subClassesMap.clear();
	}

	@Test
	public void testGetBehavioralFeatureSubClasses() throws NameNotFoundException {
		
		Classifier behavioralFeature = (Classifier) this.core.lookupElement("BehavioralFeature");
		Classifier operation = (Classifier) this.core.lookupElement("Operation");
		Classifier method = (Classifier) this.core.lookupElement("Method");
		Classifier reception = (Classifier)this.commonBehavior.lookupElement("Reception");
		long lower = System.currentTimeMillis();
			List<MofClass> subClasses = MofHelper.getConcreteSubClasses(behavioralFeature);
		long upper = System.currentTimeMillis();
		System.out.println("Tempo gasto: "+ (upper - lower));
		
		lower = System.currentTimeMillis();
			subClasses = MofHelper.getSubClasses(behavioralFeature);
		upper = System.currentTimeMillis();
		System.out.println("Tempo gasto na segunda vez: "+ (upper - lower));
		
		assertEquals(3, subClasses.size());
		assertTrue(subClasses.contains(operation));		
		assertTrue(subClasses.contains(method));
		assertTrue(subClasses.contains(reception));
	}
	@Test
	public void testGetStructuralFeatureSubClasses() throws NameNotFoundException {
		
		Classifier structuralFeature = (Classifier) this.core.lookupElement("StructuralFeature");
		Classifier attribute = (Classifier) this.core.lookupElement("Attribute");		
		List<MofClass> subClasses = MofHelper.getSubClasses(structuralFeature);		
		for (MofClass mofClass : subClasses) {
			System.out.println("Teste 2: "+ mofClass.getName());
		}
		assertTrue(subClasses.contains(attribute));		
		
	}
	@Test
	public void testGetFeatureSubClasses() throws NameNotFoundException{
		Classifier feature = (Classifier) this.core.lookupElement("Feature");
		
		/*
		 * BehavioralFeature subclasses
		 */
		Classifier behavioralFeature = (Classifier) this.core.lookupElement("BehavioralFeature");
		Classifier operation = (Classifier) this.core.lookupElement("Operation");
		Classifier method = (Classifier) this.core.lookupElement("Method");
		Classifier reception = (Classifier)this.commonBehavior.lookupElement("Reception");
		
		/*
		 * StructuralFeature subclasses
		 */
		Classifier structuralFeature = (Classifier) this.core.lookupElement("StructuralFeature");
		Classifier attribute = (Classifier) this.core.lookupElement("Attribute");
		
	long lower = System.currentTimeMillis();
		List<MofClass> subClasses = MofHelper.getConcreteSubClasses(feature);
	long upper = System.currentTimeMillis();
	System.out.println("Tempo gasto: "+ (upper - lower));
	
	lower = System.currentTimeMillis();
		subClasses = MofHelper.getConcreteSubClasses(feature);
	upper = System.currentTimeMillis();
	System.out.println("Tempo gasto na segunda vez: "+ (upper - lower));
	
	assertEquals(4, subClasses.size());
	assertTrue(!subClasses.contains(behavioralFeature));		
	assertTrue(!subClasses.contains(structuralFeature));
	
	assertTrue(subClasses.contains(operation));		
	assertTrue(subClasses.contains(method));
	assertTrue(subClasses.contains(reception));
	assertTrue(subClasses.contains(attribute));
	}
}
