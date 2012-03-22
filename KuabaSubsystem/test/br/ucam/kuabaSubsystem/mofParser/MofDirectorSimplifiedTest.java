package br.ucam.kuabaSubsystem.mofParser;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.ArrayList;

import java.util.List;

import javax.jmi.model.MofClass;
import javax.jmi.model.MofPackage;
import javax.jmi.model.MultiplicityType;
import javax.jmi.model.NameNotFoundException;
import javax.jmi.model.Namespace;
import javax.jmi.model.Reference;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import br.ucam.kuabaSubsystem.kuabaModel.Idea;

import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.mocks.MockCore;

@SuppressWarnings("unchecked")
public class MofDirectorSimplifiedTest {
	
	private MofPackage core;	
	private BaseClassRecognizer mockStrategy;
	private Director director;
	private List testMofElements;
	private ParserBuilder builder;
	
	@Before
	public void setUp() throws Exception {
		this.core = new MockCore();
		this.testMofElements = new ArrayList<MofPackage>();
		this.mockStrategy = createNiceMock(BaseClassRecognizer.class);
		this.director = new UmlClassDirector(this.core);
			
		expect(this.mockStrategy.findBaseClass(this.testMofElements)).
		andReturn((MofClass)this.core.lookupElement("ModelElement"));
		replay(this.mockStrategy);		
		
		
		
	}

	@Test
	public void testBuilderCalls() throws NameNotFoundException {
		this.core.getContents().remove(this.core.lookupElement("Association"));
		this.builder = EasyMock.createMock(ParserBuilder.class);
		
		//creating a reference for umlClass
		Reference asc = createMock(Reference.class);
		Namespace mockNamespace = createMock(Namespace.class);
		MultiplicityType ascMult = createMock(MultiplicityType.class);
		
		expect(ascMult.getUpper())
		.andReturn(1).anyTimes();
		
		expect(asc.getName())
		.andReturn("association").anyTimes();
		
		expect(asc.getMultiplicity())
		.andReturn(ascMult).anyTimes();
		
		expect(asc.getContainer())
		.andReturn(mockNamespace).anyTimes();
		
		expect(mockNamespace.getContainer())
		.andReturn(mockNamespace).anyTimes();
		
		MofClass mockReferenceType = createMock(MofClass.class);
		expect(asc.getType())
		.andReturn(mockReferenceType).anyTimes();
		
		expect(mockReferenceType.getContainer())
		.andReturn(mockNamespace).anyTimes();			
		
		expect(mockReferenceType.isAbstract())
		.andReturn(false).anyTimes();
		
		expect(mockReferenceType.getName())
		.andReturn("AssociationEnd").anyTimes();
		
		expect(mockReferenceType.getContents())
		.andReturn(new ArrayList()).anyTimes();
		
		expect(mockReferenceType.getSupertypes())
		.andReturn(new ArrayList()).anyTimes();
		
		MofClass umlClass =  (MofClass)this.core.lookupElement("Class");		
		umlClass.getContents().add(asc);
		this.builder.createRootQuestion("How Model?");
		
		this.builder.createIdea("Domain Idea");
		this.builder.push();
		this.builder.createQuestion("How Model", Question.ORTYPE);
		this.builder.push();
		this.builder.createIdea("Class");		
		this.builder.push();
		this.builder.createQuestion("isActive", Question.ORTYPE);
		
		this.builder.createQuestion("visibility", Question.ORTYPE);
		this.builder.push();
		this.builder.createIdea("vk_public");
		this.builder.createIdea("vk_protected");
		this.builder.createIdea("vk_private");
		this.builder.createIdea("vk_package");		
		expect(this.builder.pop()).andReturn(createNiceMock(Idea.class));
		
		this.builder.createQuestion("association", Question.XORTYPE);		
		expect(this.builder.pop()).andReturn(createNiceMock(Idea.class));		
		
		this.builder.createIdea("AssociationEnd");
		this.builder.push();
		expect(this.builder.pop()).andReturn(createNiceMock(Idea.class));
		
		replay(this.builder);
		replay(asc);
		replay(mockNamespace);
		replay(mockReferenceType);
		replay(ascMult);
		
		this.testMofElements.add(umlClass);
		this.director.init(this.builder, this.mockStrategy);
		this.director.construct();
		
	}

}
