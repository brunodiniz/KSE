package br.ucam.kuabaSubsystem.kuabaModel;



import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.jmi.model.AliasType;
import javax.jmi.model.Association;
import javax.jmi.model.Attribute;
import javax.jmi.model.BehavioralFeatureClass;
import javax.jmi.model.Classifier;
import javax.jmi.model.EnumerationType;
import javax.jmi.model.ModelElement;
import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofClass;
import javax.jmi.model.MofClassClass;
import javax.jmi.model.MofPackage;
import javax.jmi.model.NameNotFoundException;
import javax.jmi.model.NameNotResolvedException;
import javax.jmi.model.Reference;
import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;
import javax.jmi.xmi.MalformedXMIException;
import javax.jmi.xmi.XmiReader;

import org.apache.log4j.PropertyConfigurator;

import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIInputConfig;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;

import junit.framework.TestCase;

public class XmiReaderTest extends TestCase {

	public void testReadUMLMetamodel() throws IOException, MalformedXMIException {			
        
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
            try {
            col = reader.read(resource.toString(), mofExtent);                
           Iterator<MofPackage> it = col.iterator();
           while (it.hasNext()) {
              MofPackage object = it.next();           
 
              
              System.out.println(object.getName());

                  
              }
              
              
           }
            catch (IOException e) {
           e.printStackTrace();
            }
            assertNotNull(col);
            MofPackage core = null;
            Iterator<MofPackage> itm = col.iterator();
            while (itm.hasNext()) {
				MofPackage mofPackage = (MofPackage) itm.next();				
				System.out.println(mofPackage.getName());
				if(mofPackage.getName().equals("Core")){
				System.out.println("----------------------");
				System.out.println(mofPackage.getName());
				System.out.println("----------------------");
				core = mofPackage;
				}
			}
            
            assertNotNull(core);
            assertTrue(core.nameIsValid(""));
            Iterator<ModelElement> modelEls = core.getRequiredElements().iterator();
           
            while (modelEls.hasNext()) {
				ModelElement modelElement = (ModelElement) modelEls.next();
				
				//System.out.println(((MofClass)modelElement.refMetaObject()).getName());
				//System.out.println(modelElement.getName());
				if (modelElement instanceof MofClass) {
					Classifier new_name = (Classifier) modelElement;					
					Iterator<ModelElement> contents =  new_name.getContents().iterator();
					System.out.println();
					System.out.println(new_name.getContainer().getName());
					System.out.println(new_name.getName());
					while (contents.hasNext()) {
						ModelElement modelElement2 = (ModelElement) contents.next();
						System.out.println("------------------" + modelElement2.getClass());
						System.out.println("------------------" + modelElement2.getName());
						//System.out.println("------------------"+((MofClass)modelElement2.refMetaObject()).getContents().isEmpty());
					}
				}
				
								
			}
            try {            	
				ModelElement element = core.lookupElement("Class");
				System.out.println("------------Operation elements---------");
				Iterator mCol = ((MofClass)element).getContents().iterator();
				RefPackage pack = element.refOutermostPackage();
				System.out.println(((MofClass)element).getContents().isEmpty());
				/*List<ModelElement> mElements = core.findElementsByType((MofClass)element.refClass(), true);
				System.out.println("--------Elementos do tipo " + element.getName());
				Iterator<ModelElement> itElements = mElements.iterator();
				while (itElements.hasNext()) {
					ModelElement modelElement = (ModelElement) itElements.next();
					System.out.println(modelElement.getName());
				}*/
			
				while (mCol.hasNext()) {
					ModelElement modelElement = (ModelElement) mCol.next();
					if (modelElement instanceof Reference){
						Reference ref = (Reference)modelElement;
						System.out.println("Reference: " + ref.getName());						
						System.out.println("Tipo: " + ref.getType().getName());	
						System.out.println("Multiplicity: " + ref.getMultiplicity().getLower()
								+ ", " + ref.getMultiplicity().getUpper());
						System.out.println();
					}
					if (modelElement instanceof Attribute){
						Attribute new_name = (Attribute) modelElement;						
					    //Association new_name = (Association) modelElement;
						System.out.println();
						System.out.println(new_name.isRequiredBecause(element, new String[]{ModelElement.REFERENCEDENDSDEP}));
						System.out.println(new_name.getType().getName());
						System.out.println(new_name.getName());
						
						//assertFalse(((EnumerationType)new_name.getType()).getLabels().isEmpty());
						Iterator<ModelElement> it = null;
							if (new_name.getType() instanceof EnumerationType) {
								it = ((EnumerationType)new_name.getType()).getLabels().iterator();
								//System.out.println(((EnumerationType)new_name.getType()).get);
								while (it.hasNext()) {
									//ModelElement modelElement2 = (ModelElement) it
										//	.next();
									System.out.println("     "+it.next());
									
									
								}
							}							
					}					
				}
			assertTrue(true);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail();
			}         
		}
}
