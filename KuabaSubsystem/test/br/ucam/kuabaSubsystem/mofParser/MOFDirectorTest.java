package br.ucam.kuabaSubsystem.mofParser;


import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.createNiceMock;

import java.io.File;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.jmi.model.AliasType;
import javax.jmi.model.Attribute;
import javax.jmi.model.EnumerationType;
import javax.jmi.model.ModelElement;
import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofClass;
import javax.jmi.model.MofPackage;
import javax.jmi.model.MultiplicityType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.netbeans.api.mdr.CreationFailedException;
import org.netbeans.api.mdr.MDRManager;
import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;

import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaElement;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.mofParser.BaseClassRecognizer;
import br.ucam.kuabaSubsystem.mofParser.Director;
import br.ucam.kuabaSubsystem.mofParser.MOFDirectorImpl;
import br.ucam.kuabaSubsystem.mofParser.ParserBuilder;

@SuppressWarnings("unchecked")

public class MOFDirectorTest{	
	private Director director;	
	private Iterator mockIterator;	
	private ParserBuilder mockBuilder;
	private BaseClassRecognizer mockStrategy;
	private MofPackage core;
	
	@Before
	public void setUp() throws Exception {

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
		String metamodelURL = "src/mof/01-02-15_Diff.xml";
		File file = new File(metamodelURL);		
        URL resource = file.toURI().toURL();
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
		}        
		this.mockStrategy = createNiceMock(BaseClassRecognizer.class);
		expect(this.mockStrategy.findBaseClass(core.getContents())).
		andReturn((MofClass)core.lookupElement("ModelElement"));
		
		replay(this.mockStrategy);
		
		this.director = new MOFDirectorImpl(mockStrategy, core.getContents());
		this.mockIterator = createMock(Iterator.class);
		this.mockBuilder = createMock(ParserBuilder.class);
		
	}
	
	
	

	public void testIteratorCalls() throws Exception {
		
		/*
		 * Creating a mock iterator and specifying the return types and the
		 * number off calls expected in mock iterator.
		 * 
		 *  The director should call hasNext() method three times on mockIterator
		 *  and should call next() method two times.
		 *  
		 *  When the director calls next() in the first time, the iterator should
		 *  return umlClass object. On the second time it should return the isActive
		 *  attribute of the umlClass object. isActive attribute is a mock to.
		 *  
		 *  The director should call getType() on the isActive mock to verify
		 *  whether it has default values (EnumerationType). At last, the director
		 *  calls hasNext() again to verify whether the iterator has a next element.
		 *  This call should return false.   
		 */
		ModelElement umlClass =  this.core.lookupElement("Class");
		Attribute isActive = createMock(Attribute.class);
		MultiplicityType isActiveMultiplicity = createMock(MultiplicityType.class); 
		
		expect(this.mockIterator.hasNext()).andReturn(true);		
		expect(this.mockIterator.next()).andReturn(umlClass);
		expect(this.mockIterator.hasNext()).andReturn(true);
		expect(this.mockIterator.next()).andReturn(isActive);
		expect(isActive.getMultiplicity()).andReturn(isActiveMultiplicity);
		
		expect(isActiveMultiplicity.getUpper()).andReturn(1);
		expect(isActiveMultiplicity.getUpper()).andReturn(1);
		expect(isActive.getType()).andReturn(createMock(AliasType.class));		
		expect(isActive.getName()).andReturn("isActive").times(2);
		expect(this.mockIterator.hasNext()).andReturn(false);
		
		replay(isActive);
		replay( this.mockIterator);
		replay(isActiveMultiplicity);
		ParserBuilder mock = createNiceMock(ParserBuilder.class);		
		replay(mock);
		this.director.init(mock, this.mockStrategy);
		this.director.construct();
		
		verify(this.mockIterator);
		verify(isActive);
	}
	
	
	public void testBuilderCalls() throws Exception {		
		
		this.mockIterator = createNiceMock(Iterator.class);
		ModelElement umlClass =  this.core.lookupElement("Class");
		Attribute isActive = createMock(Attribute.class);
		MultiplicityType isActiveMultiplicity = createMock(MultiplicityType.class);		
		
		/*expect(this.mockIterator.hasNext()).andReturn(true);		
		expect(this.mockIterator.next()).andReturn(umlClass);
		expect(this.mockIterator.hasNext()).andReturn(true);
		expect(this.mockIterator.next()).andReturn(isActive);*/
		expect(isActive.getType()).andReturn(createMock(AliasType.class));
		expect(isActive.getName()).andReturn("isActive").times(2);
		//expect(this.mockIterator.hasNext()).andReturn(false);
		expect(isActive.getMultiplicity()).andReturn(isActiveMultiplicity);		
		expect(isActiveMultiplicity.getUpper()).andReturn(1).anyTimes();
		
		replay(isActive);
		replay(isActiveMultiplicity);
		//replay( this.mockIterator);	
		
		this.mockBuilder.createRootQuestion("What Elements");
		
		this.mockBuilder.createIdea("Domain Idea");
		this.mockBuilder.push();
		this.mockBuilder.createQuestion("How Model", Question.ORTYPE);
		this.mockBuilder.push();
		this.mockBuilder.createIdea("Class");		
		this.mockBuilder.push();
		this.mockBuilder.createQuestion("isActive", Question.XORTYPE);
		expect(this.mockBuilder.pop()).andReturn(createNiceMock(Idea.class));	
		expect(this.mockBuilder.top()).andReturn(createNiceMock(Idea.class));
		
		replay(this.mockBuilder);		
		
		this.director.init(this.mockBuilder, this.mockStrategy);
		this.director.construct();
		verify(this.mockBuilder);		
	}
	
	
	public void testQuestionORTypeSelection() throws Exception {		
		
		/* OR type testing.
		 * 
		 * If upper multiplicity > 1 or upper multiplicity < 0, then the
		 * structural feature (Attribute or Reference) accepts more than
		 * one value. So, the question generated must be OR
		 */
		Attribute isActive = createMock(Attribute.class);
		
		MultiplicityType oneToMany = createMock(MultiplicityType.class);
		expect(oneToMany.getUpper()).andReturn(-1);
		
		MultiplicityType oneToOne = createMock(MultiplicityType.class);
		expect(oneToOne.getUpper()).andReturn(1).times(2);
		
		MultiplicityType oneToTwo = createMock(MultiplicityType.class);
		expect(oneToTwo.getUpper()).andReturn(2).times(2);
		
		expect(isActive.getMultiplicity()).andReturn(oneToMany);
		expect(isActive.getMultiplicity()).andReturn(oneToTwo);
		expect(isActive.getMultiplicity()).andReturn(oneToOne);		
		
		replay(isActive);		
		replay(oneToMany);				
		replay(oneToOne);
		replay(oneToTwo);
		
		this.director.init(this.mockBuilder, this.mockStrategy);
		assertEquals(Question.ORTYPE, ((MOFDirectorImpl)this.director).selectQuestionType(isActive));
		assertEquals(Question.ORTYPE, ((MOFDirectorImpl)this.director).selectQuestionType(isActive));
		assertEquals(Question.XORTYPE, ((MOFDirectorImpl)this.director).selectQuestionType(isActive));
		verify(isActive);		
		verify(oneToMany);			
		
	}
	
	
	public void testCallsWhithEnumerationAttribute() throws Exception{
		
		
		//creating br.ucam.kuabaSubsystem.mocks and getting the "Class" UML meta-class 
		ModelElement umlClass =  this.core.lookupElement("Class");
		Attribute isActive = createMock(Attribute.class);
		EnumerationType mockType = createMock(EnumerationType.class);
		MultiplicityType isActiveMultiplicity = createMock(MultiplicityType.class);
		List<String> mockLabels = createMock(List.class);		
		this.mockIterator = createNiceMock(Iterator.class);
		
		this.mockBuilder.createRootQuestion("What Elements");		
		this.mockBuilder.createIdea("Domain Idea");
		this.mockBuilder.push();
		this.mockBuilder.createQuestion("How Model", Question.ORTYPE);
		this.mockBuilder.push();
		
		expect(this.mockIterator.hasNext()).andReturn(true);		
		expect(this.mockIterator.next()).andReturn(umlClass);
			expect(this.mockBuilder.top()).andReturn(createMock(Question.class));			
			this.mockBuilder.createIdea("Class");
			this.mockBuilder.push();
			
		expect(this.mockIterator.hasNext()).andReturn(true);
		expect(this.mockIterator.next()).andReturn(isActive);
			expect(isActive.getMultiplicity()).andReturn(isActiveMultiplicity);
			
			expect(isActiveMultiplicity.getUpper()).andReturn(1).anyTimes();
			this.mockBuilder.createQuestion("isActive", Question.XORTYPE);
			expect(isActive.getName()).andReturn("isActive").times(2);
			expect(isActive.getType()).andReturn(mockType);
			expect(isActive.getType()).andReturn(mockType);
			expect(mockType.getLabels()).andReturn(mockLabels);
			expect(mockLabels.size()).andReturn(2);
			this.mockBuilder.push();
			
		expect(this.mockIterator.hasNext()).andReturn(true);
		expect(this.mockIterator.next()).andReturn("True");
			this.mockBuilder.createIdea("True");
			
		expect(this.mockIterator.hasNext()).andReturn(true);
		expect(this.mockIterator.next()).andReturn("False");
			this.mockBuilder.createIdea("False");			
			expect(this.mockBuilder.pop()).andReturn(createMock(KuabaElement.class));
			
		expect(this.mockIterator.hasNext()).andReturn(false);
		
		replay(isActive);
		replay(isActiveMultiplicity);
		replay( this.mockIterator);	
		replay(this.mockBuilder);
		replay(mockLabels);
		replay(mockType);
		
		this.director.init(this.mockBuilder, this.mockStrategy);
		this.director.construct();
		verify(this.mockBuilder);
		verify(this.mockIterator);
		verify(mockLabels);
		verify(isActiveMultiplicity);
		verify(mockType);
	}
	
	
	public void testWhenIteratorReturnsAbstractMofClass() throws Exception {
	
		this.mockBuilder.createRootQuestion("What Elements");		
		this.mockBuilder.createIdea("Domain Idea");
		this.mockBuilder.push();
		this.mockBuilder.createQuestion("How Model",Question.ORTYPE);
		this.mockBuilder.push();
		
		MofClass mockMetaClass = createMock(MofClass.class);
		
		expect(this.mockIterator.hasNext()).andReturn(true);
		expect(this.mockIterator.next()).andReturn(mockMetaClass);
		expect(mockMetaClass.allSupertypes()).andReturn(new ArrayList<ModelElement>());
		expect(mockMetaClass.isAbstract()).andReturn(true);
		expect(this.mockIterator.hasNext()).andReturn(false);		
		
		replay( this.mockIterator);	
		replay(this.mockBuilder);
		replay( mockMetaClass);
		
		this.director.init(this.mockBuilder, this.mockStrategy);
		this.director.construct();
		
		verify(this.mockBuilder);
		verify(this.mockIterator);
		verify( mockMetaClass);
	}
	
}
