package br.ucam.kuabaSubsystem.mofParser.integration;



import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.resetToNice;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.jmi.model.ModelElement;
import javax.jmi.model.MofClass;
import javax.jmi.model.MofPackage;
import javax.jmi.model.NameNotFoundException;


import org.junit.Before;
import org.junit.Test;

import br.ucam.kuabaSubsystem.kuabaModel.FormalModel;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaModelFactory;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.mocks.MockCore;
import br.ucam.kuabaSubsystem.mocks.NiceMockCore;
import br.ucam.kuabaSubsystem.mofParser.BaseClassRecognizer;
import br.ucam.kuabaSubsystem.mofParser.UmlClassDirector;
import br.ucam.kuabaSubsystem.mofParser.KuabaBuilder;
import br.ucam.kuabaSubsystem.mofParser.MOFCompositeIterator;
import br.ucam.kuabaSubsystem.mofParser.Director;
import br.ucam.kuabaSubsystem.mofParser.MOFDirectorImpl;
import br.ucam.kuabaSubsystem.mofParser.MofDirector;
import br.ucam.kuabaSubsystem.mofParser.ParserBuilder;
import br.ucam.kuabaSubsystem.repositories.OwlApiFileGateway;
import br.ucam.kuabaSubsystem.util.Sequence;
import br.ucam.kuabaSubsystem.util.SequenceGenerator;
import br.ucam.kuabaSubsystem.util.SequenceGeneratorImpl;

@SuppressWarnings("unchecked")
public class MofParserIntegrationTest {
	
	private MofPackage core;
	private KuabaModelFactory mockFactory;
	private BaseClassRecognizer mockStrategy;
	private Director director;
	private FormalModel mockFormalModel;
	private Iterator mofIterator;
	private List testMofElements;
	private ParserBuilder builder;	
	
	@Before
	public void setUp() throws Exception{
		this.core = new NiceMockCore();
		this.testMofElements = new ArrayList<MofPackage>();
		this.mockStrategy = createNiceMock(BaseClassRecognizer.class);
		this.mockFactory = createNiceMock(KuabaModelFactory.class);
                
//                this.mockFactory = OwlApiFileGateway.getInstance().load("test/br/ucam/kuabaSubsystem/testBase/testKuabaKnowlegeBase.xml").getModelFactory();
		
		this.mockFormalModel = createNiceMock(FormalModel.class);
		File sequencesFile = new File("test/br/ucam/kuabaSubsystem/testBase/fileOperationsTestBase.txt"); 
		SequenceGenerator generator = new SequenceGeneratorImpl(sequencesFile);
		Sequence testSequence = null;
		if(generator.loadSequence("TestSequence") == null){
			testSequence = generator.createNewSequence("TestSequence", 0, 1);
		}else{
			testSequence = generator.loadSequence("TestSequence");
			testSequence.setCurrentVal(0);
			testSequence.setStep(1);
		}
		this.builder = new KuabaBuilder(this.mockFactory, mockFormalModel, testSequence);
		replay(mockFormalModel);	
		expect(this.mockStrategy.findBaseClass(this.testMofElements)).
		andReturn((MofClass)this.core.lookupElement("ModelElement"));
		replay(this.mockStrategy);
		this.director = new UmlClassDirector(this.core);
	}
	
	@Test
	public void testIteratorDirectorBuilderIntegration() 
						throws NameNotFoundException{
		
		ModelElement umlClass = this.core.lookupElement("Class");		
		
		this.core.getContents().remove(
				this.core.lookupElement("Association"));
		this.testMofElements.add(umlClass);
		//this.testMofElements.add(umlAssociation);
		
		//Iterator<ModelElement> mockIterator = createMock(Iterator.class);		
		this.mofIterator = new MOFCompositeIterator(
				this.testMofElements.listIterator());
		//this.director.init(this.builder,this.mofIterator);
		this.director.init(this.builder, this.mockStrategy);
		
		//expected general behavior		
		Question howModel = createMock(Question.class);
		expect(this.mockFactory.createQuestion("How Model?")).		
		andReturn(howModel);		
		howModel.setIsDefinedBy(this.mockFormalModel);
		howModel.setHasText("How Model?");
		howModel.setHasType(Question.XORTYPE);		
		
		//expected behavior to extract Kuaba from UML Class element
		Idea classIdea = createMock(Idea.class);
		expect(this.mockFactory.createIdea("_1")).
		andReturn(classIdea);
		classIdea.setIsDefinedBy(this.mockFormalModel);
		classIdea.setHasText("Class");
		classIdea.addAddress(howModel);
		
		Question visibility = createMock(Question.class);
		expect(this.mockFactory.createQuestion("_2")).
		andReturn(visibility);
		visibility.setIsDefinedBy(this.mockFormalModel);
		visibility.setHasText("visibility?");
		visibility.setHasType(Question.ORTYPE);
		visibility.addIsSuggestedBy(classIdea);
		
		Idea vk_public = createMock(Idea.class);
		expect(this.mockFactory.createIdea("_3")).
		andReturn(vk_public);
		vk_public.setIsDefinedBy(this.mockFormalModel);
		vk_public.setHasText("vk_public");
		vk_public.addAddress(visibility);
		
		Idea vk_protected = createMock(Idea.class);
		expect(this.mockFactory.createIdea("_4")).
		andReturn(vk_protected);
		vk_protected.setIsDefinedBy(this.mockFormalModel);
		vk_protected.setHasText("vk_protected");
		vk_protected.addAddress(visibility);
		
		Idea vk_private = createMock(Idea.class);
		expect(this.mockFactory.createIdea("_5")).
		andReturn(vk_private);
		vk_private.setIsDefinedBy(this.mockFormalModel);
		vk_private.setHasText("vk_private");		
		vk_private.addAddress(visibility);
		
		Idea vk_package = createMock(Idea.class);
		expect(this.mockFactory.createIdea("_6")).
		andReturn(vk_package);
		vk_package.setIsDefinedBy(this.mockFormalModel);
		vk_package.setHasText("vk_package");
		vk_package.addAddress(visibility);
		
		Question isActive = createMock(Question.class);
		expect(this.mockFactory.createQuestion("_7")).
		andReturn(isActive);
		isActive.setIsDefinedBy(this.mockFormalModel);
		isActive.setHasText("isActive?");
		
		isActive.setHasType(Question.ORTYPE);
		isActive.addIsSuggestedBy(classIdea);
		
		
		
		//expected behavior to extract Kuaba from UML Association element
		/*Idea association = createMock(Idea.class);
		expect(this.mockFactory.createIdea("Association")).
		andReturn(association);
		association.setHasText("Association");
		association.addAddress(howModel);
		
		Question connection = createMock(Question.class);
		expect(this.mockFactory.createQuestion("connection")).
		andReturn(connection);		
		connection.setHasText("connection?");
		connection.setHasType(Arrays.asList(new String[]{Question.ORTYPE}));
		connection.addIsSuggestedBy(association);*/
		
		replay(this.mockFactory);
		replay(howModel);
		
		
		replay(classIdea);
		//replay(association);
		replay(isActive);
		replay(visibility);
		replay(vk_private);
		replay(vk_public);
		replay(vk_protected);
		replay(vk_package);
		//replay(connection);
		//replay(mockIterator);
		this.director.construct();
		
		Question root = this.builder.getResult();
		
		assertNotNull(root);		
		assertEquals(howModel, root);
		
		verify(this.mockFactory);
		verify(howModel);
		
		verify(howModel);
		verify(classIdea);
		//verify(association);
		verify(isActive);
		verify(visibility);
		verify(vk_private);
		verify(vk_public);
		verify(vk_protected);
		verify(vk_package);
		//verify(connection);
	}
	
	@Test
	public void testIntegrationWithAbstractClass() throws Exception {
		
		
	}

}
