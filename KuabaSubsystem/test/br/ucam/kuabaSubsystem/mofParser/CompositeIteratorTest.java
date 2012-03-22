package br.ucam.kuabaSubsystem.mofParser;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.easymock.EasyMock.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.jmi.model.Attribute;
import javax.jmi.model.ModelElement;
import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofClass;
import javax.jmi.model.MofPackage;
import javax.jmi.model.Reference;
import javax.jmi.model.StructuralFeature;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;

import br.ucam.kuabaSubsystem.mofParser.MOFCompositeIterator;

@SuppressWarnings("unchecked")
public class CompositeIteratorTest {	
	
	private List list;
	private MofPackage core;
	private MofPackage commonBehavior;
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Before
	public void setUp() throws Exception {
		this.list = new ArrayList();
		
		/*
		 * loading the UML metamodel from "src/mof/01-02-15_Diff.xml"
		 */
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
        URL resource = file.toURL();
        System.out.println("conteudo:" + resource);
        Collection<MofPackage> col = null;
        col = reader.read(resource.toString(), mofExtent);
        
        //getting the core MofPackage for test
        
        Iterator<MofPackage> itm = col.iterator();
        while (itm.hasNext()) {
			MofPackage mofPackage = (MofPackage) itm.next();			
			if(mofPackage.getName().equals("Core")){			
			core = mofPackage;
			}
			if(mofPackage.getName().equals("Common_Behavior")){			
				this.commonBehavior = mofPackage;
			}
			
		}                
		
	}
	@After
	public void tearDown(){
		
		
	}
	@Test
	public void testHasNext() throws Exception {
		
		this.list.add(this.core.lookupElement("Class"));
		MOFCompositeIterator mofClassIterator = new MOFCompositeIterator(list.listIterator());		
		
		assertTrue(mofClassIterator.hasNext());
		
		assertEquals("Class", ((MofClass)mofClassIterator.next()).getName());
		mofClassIterator.next();
		assertEquals("visibility", ((Attribute)mofClassIterator.next()).getName());		

		for (int i = 0; i < 22; i++) {
			
			System.out.println(mofClassIterator.next());			
		}
		
		assertFalse(mofClassIterator.hasNext());	
		
		
	}
	
	@Test
	public void testHasNext2() throws Exception {
		
		MOFCompositeIterator mofClassIterator = new MOFCompositeIterator(list.listIterator());		
		assertFalse(mofClassIterator.hasNext());		
	}
	
	@Test
	public void testNextException() throws Exception {		
		
		MOFCompositeIterator mofClassIterator = new MOFCompositeIterator(list.listIterator());		
		try{
			mofClassIterator.next();
			fail();
		}catch (NoSuchElementException e) {
			assertTrue(true);
		}		
	}
	
	@Test
	public void testNext() throws Exception {
		this.list.add(this.core.lookupElement("Operation"));
		MOFCompositeIterator mofClassIterator = new MOFCompositeIterator(this.list.listIterator());
		assertTrue(mofClassIterator.hasNext());
		assertEquals("Operation", ((MofClass)mofClassIterator.next()).getName());
		
		assertEquals("name", ((Attribute)mofClassIterator.next()).getName());
		assertEquals("visibility", ((Attribute)mofClassIterator.next()).getName());
			assertEquals("vk_public", (String)mofClassIterator.next());
			assertEquals("vk_protected", (String)mofClassIterator.next());
			assertEquals("vk_private", (String)mofClassIterator.next());
			assertEquals("vk_package", (String)mofClassIterator.next());			
		assertEquals("isSpecification", ((Attribute)mofClassIterator.next()).getName());
		assertEquals("ownerScope", ((Attribute)mofClassIterator.next()).getName());
			assertEquals("sk_instance", (String)mofClassIterator.next());
			assertEquals("sk_classifier", (String)mofClassIterator.next());
		assertEquals("isQuery", ((Attribute)mofClassIterator.next()).getName());
		assertEquals("concurrency", ((Attribute)mofClassIterator.next()).getName());
			assertEquals("cck_sequential", (String)mofClassIterator.next());
			assertEquals("cck_guarded", (String)mofClassIterator.next());
			assertEquals("cck_concurrent", (String)mofClassIterator.next());
		assertEquals("isRoot", ((Attribute)mofClassIterator.next()).getName());
		assertEquals("isLeaf", ((Attribute)mofClassIterator.next()).getName());
		assertEquals("isAbstract", ((Attribute)mofClassIterator.next()).getName());
		assertEquals("specification", ((Attribute)mofClassIterator.next()).getName());
		
		assertEquals("namespace", ((Reference)mofClassIterator.next()).getName());
		assertEquals("clientDependency", ((Reference)mofClassIterator.next()).getName());	
		
		
		for(int i = 0; i < 9; i++)
			mofClassIterator.next();
		assertFalse(mofClassIterator.hasNext());
	}	
	
	@Test
	public void testIteratorWithTwoClasses() throws Exception {
		this.list.add(this.core.lookupElement("Operation"));
		this.list.add(this.core.lookupElement("Class"));
		MOFCompositeIterator mofClassIterator = new MOFCompositeIterator(this.list.listIterator());
		assertTrue(mofClassIterator.hasNext());
		
		//next class
		assertEquals("Operation", ((MofClass)mofClassIterator.next()).getName());		

		assertEquals("name", ((Attribute)mofClassIterator.next()).getName());
		assertEquals("visibility", ((Attribute)mofClassIterator.next()).getName());
			assertEquals("vk_public", (String)mofClassIterator.next());
			assertEquals("vk_protected", (String)mofClassIterator.next());
			assertEquals("vk_private", (String)mofClassIterator.next());
			assertEquals("vk_package", (String)mofClassIterator.next());			
		assertEquals("isSpecification", ((Attribute)mofClassIterator.next()).getName());
		assertEquals("ownerScope", ((Attribute)mofClassIterator.next()).getName());
			assertEquals("sk_instance", (String)mofClassIterator.next());
			assertEquals("sk_classifier", (String)mofClassIterator.next());
		assertEquals("isQuery", ((Attribute)mofClassIterator.next()).getName());
		assertEquals("concurrency", ((Attribute)mofClassIterator.next()).getName());
			assertEquals("cck_sequential", (String)mofClassIterator.next());
			assertEquals("cck_guarded", (String)mofClassIterator.next());
			assertEquals("cck_concurrent", (String)mofClassIterator.next());
		assertEquals("isRoot", ((Attribute)mofClassIterator.next()).getName());
		assertEquals("isLeaf", ((Attribute)mofClassIterator.next()).getName());
		assertEquals("isAbstract", ((Attribute)mofClassIterator.next()).getName());
		assertEquals("specification", ((Attribute)mofClassIterator.next()).getName());
		
		assertEquals("namespace", ((Reference)mofClassIterator.next()).getName());
		assertEquals("clientDependency", ((Reference)mofClassIterator.next()).getName());	
		
		
		for(int i = 0; i < 9; i++)
			mofClassIterator.next();
		
		//next concrete class: "Class"
		assertEquals("Class", ((MofClass)mofClassIterator.next()).getName());				
		//next Attribute of the above class
		mofClassIterator.next();
		assertEquals("visibility", ((Attribute)mofClassIterator.next()).getName());		

		for (int i = 0; i < 22; i++) {
			
			System.out.println(mofClassIterator.next());			
		}
		
		assertFalse(mofClassIterator.hasNext());
	}
	
	@Test
	public void testIterateAll() throws Exception {
		
		//this.list.add(this.core.lookupElement("Operation"));
		this.list = new ArrayList(this.core.getContents());
		
		MOFCompositeIterator mofClassIterator = new MOFCompositeIterator(this.list.listIterator());
		long initTime = System.currentTimeMillis();
		while (mofClassIterator.hasNext()) {
			 mofClassIterator.next();
			
		}
		long finishTime = System.currentTimeMillis();
		System.out.println("Iteration Time: " + (finishTime-initTime));
	}
	@Test
	public void testIterateWithAbstractClass() throws Exception {
		
		MofClass link = (MofClass) this.commonBehavior.lookupElement("Link");
		this.list.add(link);
		MOFCompositeIterator iterator = new MOFCompositeIterator(this.list.listIterator());
		assertEquals(link, iterator.next());
		
		//ModelElement inherited attributes	
		assertEquals("name", ((Attribute)iterator.next()).getName());		
		assertEquals("visibility", ((Attribute)iterator.next()).getName());
		assertEquals("vk_public", (String)iterator.next());
		assertEquals("vk_protected", (String)iterator.next());
		assertEquals("vk_private", (String)iterator.next());
		assertEquals("vk_package", (String)iterator.next());
		assertEquals("isSpecification", ((Attribute)iterator.next()).getName());
		assertEquals("namespace", ((Reference)iterator.next()).getName());
		assertEquals("clientDependency", ((Reference)iterator.next()).getName());
		for(int i = 0; i < 7; i++)
			iterator.next();
		//and of ModelElement inherited attributes
		
		//Link attributes
		assertEquals("association", ((Reference)iterator.next()).getName());
		assertEquals("connection", ((Reference)iterator.next()).getName());
		//end of Link attributes
		
		//Association Class imported from the Core package.
		assertEquals(this.core.lookupElement("Association"), iterator.next());
		
		for (int i = 0; i < 21; i++){
			Object modelElement = iterator.next();
			if (modelElement instanceof ModelElement) {
				ModelElement new_name = (ModelElement) modelElement;
				System.out.println(new_name.getName());	
			}
			
		}
		assertFalse(iterator.hasNext());
			
		
		
		
	}

}
