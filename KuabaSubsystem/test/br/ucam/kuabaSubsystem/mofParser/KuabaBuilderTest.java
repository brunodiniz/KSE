/**
 * 
 */
package br.ucam.kuabaSubsystem.mofParser;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.List;


import org.junit.Before;
import org.junit.Test;

import br.ucam.kuabaSubsystem.kuabaModel.FormalModel;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaElement;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaModelFactory;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.mofParser.KuabaBuilder;
import br.ucam.kuabaSubsystem.mofParser.ParserBuilder;
import br.ucam.kuabaSubsystem.util.Sequence;
import br.ucam.kuabaSubsystem.util.SequenceGenerator;
import br.ucam.kuabaSubsystem.util.SequenceGeneratorImpl;
import static org.easymock.EasyMock.*;

/**
 * @author Thiago
 *
 */
public class KuabaBuilderTest {
	
	private KuabaModelFactory mockFactory;
	private ParserBuilder builder;
	private FormalModel mockFormalModel;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.mockFactory = createMock(KuabaModelFactory.class);
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
	}	

	/**
	 * Test method for {@link br.ucam.KuabaSubsystem.mofParser.KuabaBuilder#clearStack()}.
	 */
	@Test
	public void testClearStack() {
		Question mockQuestion = createMock(Question.class);
		replay(mockQuestion);
		((KuabaBuilder)this.builder).getBuildStack().push(mockQuestion);
		
		assertEquals(mockQuestion, ((KuabaBuilder)this.builder).getBuildStack().peek());
		
		this.builder.clearStack();
		
		assertTrue(((KuabaBuilder)this.builder).getBuildStack().isEmpty());		
	}

	/**
	 * Test method for {@link br.ucam.KuabaSubsystem.mofParser.KuabaBuilder#createIdea(java.lang.String)}.
	 */
	@Test
	public void testCreateIdeaEmptyStackException() {
		Question mockIdea = createMock(Question.class);		
		
		replay(mockIdea);
		replay(this.mockFactory);
		
		try{
			this.builder.createIdea("Test Idea");
			fail("EmptyStackException must be thrown");
		}catch (EmptyStackException e) {
			assertTrue(true);
		}	
		
		KuabaElement lastcreatedElement = ((KuabaBuilder)this.builder).getLastCreatedElement();
		assertNull(lastcreatedElement);
		
		verify(this.mockFactory);
		verify(mockIdea);
	}
	
	/**
	 * Test method for {@link br.ucam.KuabaSubsystem.mofParser.KuabaBuilder#createIdea(java.lang.String)}.	 * 
	 */
	@Test
	public void testCreateIdeaIllegalStateException() {
		Idea mockIdea = createNiceMock(Idea.class);		
		Question mockQuestion = createNiceMock(Question.class);
		expect(this.mockFactory.createIdea("_1")).andReturn(mockIdea);
		
		replay(mockIdea);
		replay(mockQuestion);
		replay(this.mockFactory);
		
		((KuabaBuilder)this.builder).getBuildStack().push(mockIdea);
		try{
			this.builder.createIdea("Test Idea");
			fail("IllegalStateException must be thrown");
		}catch (IllegalStateException e) {
			e.printStackTrace();
			assertTrue(true);
		}	
		
		KuabaElement lastcreatedElement = ((KuabaBuilder)this.builder).getLastCreatedElement();
		assertNull(lastcreatedElement);
		
		((KuabaBuilder)this.builder).getBuildStack().push(mockQuestion);
		try{
			this.builder.createIdea("Test Idea");
			assertTrue(true);
		}catch (IllegalStateException e) {
			e.printStackTrace();
			fail("IllegalStateException must not be thrown");
			
		}
		
		lastcreatedElement = ((KuabaBuilder)this.builder).getLastCreatedElement();
		assertEquals(lastcreatedElement, mockIdea);
		
		verify(this.mockFactory);
		verify(mockIdea);
	}
	
	/**
	 * Test method for {@link br.ucam.KuabaSubsystem.mofParser.KuabaBuilder#createIdea(java.lang.String)}.	 * 
	*/
	@Test
	public void testCreateIdeaAndRelatedWithPeekQuestion() {
		Idea mockIdea = createMock(Idea.class);		
		Question mockQuestion = createMock(Question.class);
		
		
		expect(this.mockFactory.createIdea("_1")).andReturn(mockIdea);
		
		mockIdea.setHasText("Test Idea");
		mockIdea.setIsDefinedBy(this.mockFormalModel);
		mockIdea.addAddress(mockQuestion);
		
		replay(mockIdea);
		replay(mockQuestion);
		replay(this.mockFactory);		
		
		KuabaElement lastcreatedElement = ((KuabaBuilder)this.builder).getLastCreatedElement();
		assertNull(lastcreatedElement);
		
		((KuabaBuilder)this.builder).getBuildStack().push(mockQuestion);
		
		this.builder.createIdea("Test Idea");
		assertTrue(true);
		
		lastcreatedElement = ((KuabaBuilder)this.builder).getLastCreatedElement();
		assertEquals(lastcreatedElement, mockIdea);
		
		verify(this.mockFactory);
		verify(mockIdea);
	}
	
	/**
	 * Test method for {@link br.ucam.KuabaSubsystem.mofParser.KuabaBuilder#createQuestion(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateQuestionEmptyStackException() {
	Question mockQuestion = createMock(Question.class);		
		
		replay(mockQuestion);
		replay(this.mockFactory);
		
		try{
			this.builder.createQuestion("Test Question", Question.ORTYPE);
			fail("EmptyStackException must be thrown");
		}catch (EmptyStackException e) {
			assertTrue(true);
		}	
		
		KuabaElement lastcreatedElement = ((KuabaBuilder)this.builder).getLastCreatedElement();
		assertNull(lastcreatedElement);
		
		verify(this.mockFactory);
		verify(mockQuestion);
	}
	
	/**
	 * Test method for {@link br.ucam.KuabaSubsystem.mofParser.KuabaBuilder#createQuestion(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateQuestionIllegalStateException() {
		Idea mockIdea = createNiceMock(Idea.class);		
		Question mockQuestion = createNiceMock(Question.class);
		expect(this.mockFactory.createQuestion("_1")).andReturn(mockQuestion);
		
		replay(mockIdea);
		replay(mockQuestion);
		replay(this.mockFactory);
		
		((KuabaBuilder)this.builder).getBuildStack().push(mockQuestion);
		try{
			this.builder.createQuestion("Test Question", Question.ORTYPE);
			fail("IllegalStateException must be thrown");
		}catch (IllegalStateException e) {
			e.printStackTrace();
			assertTrue(true);
		}	
		
		KuabaElement lastcreatedElement = ((KuabaBuilder)this.builder).getLastCreatedElement();
		assertNull(lastcreatedElement);
		
		((KuabaBuilder)this.builder).getBuildStack().push(mockIdea);
		try{
			this.builder.createQuestion("Test Question", Question.ORTYPE);
			assertTrue(true);
		}catch (IllegalStateException e) {
			e.printStackTrace();
			fail("IllegalStateException must not be thrown");
			
		}
		
		lastcreatedElement = ((KuabaBuilder)this.builder).getLastCreatedElement();
		assertEquals(lastcreatedElement, mockQuestion);
		
		verify(this.mockFactory);
		verify(mockIdea);
	}
	
	/**
	 * Test method for {@link br.ucam.KuabaSubsystem.mofParser.KuabaBuilder#createQuestion(java.lang.String)}.	 * 
	*/
	@Test
	public void testCreateQuestionAndRelatedWithPeekIdea() {
		Idea mockIdea = createMock(Idea.class);		
		Question mockQuestion = createMock(Question.class);		
		
		expect(this.mockFactory.createQuestion("_1")).andReturn(mockQuestion);
		mockQuestion.setHasText("Test Question?");
		mockQuestion.setIsDefinedBy(this.mockFormalModel);
		mockQuestion.setHasType(Question.ORTYPE);
		mockQuestion.addIsSuggestedBy(mockIdea);
		
		replay(mockIdea);
		replay(mockQuestion);
		replay(this.mockFactory);		
		
		KuabaElement lastcreatedElement = ((KuabaBuilder)this.builder).getLastCreatedElement();
		assertNull(lastcreatedElement);
		
		((KuabaBuilder)this.builder).getBuildStack().push(mockIdea);
		
		this.builder.createQuestion("Test Question", Question.ORTYPE);
		assertTrue(true);
		
		lastcreatedElement = ((KuabaBuilder)this.builder).getLastCreatedElement();
		assertEquals(lastcreatedElement, mockQuestion);
		
		verify(this.mockFactory);
		verify(mockQuestion);
		verify(mockIdea);
	}
	
	/**
	 * Test method for {@link br.ucam.KuabaSubsystem.mofParser.KuabaBuilder#pop()}.
	 */
	@Test
	public void testPop() {
		Idea mockIdea = createMock(Idea.class);
		Idea mockIdea2 = createMock(Idea.class);
		
		replay(mockIdea);
		replay(mockIdea2);
		
		((KuabaBuilder)this.builder).getBuildStack().push(mockIdea);
		((KuabaBuilder)this.builder).getBuildStack().push(mockIdea2);
		
		assertEquals(mockIdea2, this.builder.pop());
		assertEquals(mockIdea, ((KuabaBuilder)this.builder).getBuildStack().peek());
		
		assertEquals(mockIdea, this.builder.pop());
		
		assertTrue(((KuabaBuilder)this.builder).getBuildStack().isEmpty());
	}

	/**
	 * Test method for {@link br.ucam.KuabaSubsystem.mofParser.KuabaBuilder#push()}.
	 */
	@Test
	public void testPush() {
		Idea mockIdea = createMock(Idea.class);
		((KuabaBuilder)this.builder).setLastCreatedElement(mockIdea);
		
		this.builder.push();
		assertEquals(mockIdea, ((KuabaBuilder)this.builder).getBuildStack().peek());		
	}
	
	/**
	 * Test method for {@link br.ucam.KuabaSubsystem.mofParser.KuabaBuilder#createRootQuestion(String)}.
	 */
	@Test
	public void testCreateRootQuestion(){
//		Question mockRootQuestion = createMock(Question.class);
//		expect(this.mockFactory.createQuestion("_1")).andReturn(mockRootQuestion);
                
//		mockRootQuestion.setIsDefinedBy(this.mockFormalModel);
                
		Question mockRootQuestion2 = createMock(Question.class);
                mockRootQuestion2.setHasText("Test Question");
		mockRootQuestion2.setHasType(Question.XORTYPE);
                mockRootQuestion2.setIsDefinedBy(this.mockFormalModel);
                
		expect(this.mockFactory.createQuestion("Test Question")).andReturn(mockRootQuestion2);
                
//		replay(mockRootQuestion);
                replay(mockRootQuestion2);
		replay(this.mockFactory);
                
                

                
		this.builder.createRootQuestion("Test Question");
		
		assertEquals(mockRootQuestion2, ((KuabaBuilder)this.builder).
				getBuildStack().peek());
		assertEquals(mockRootQuestion2, this.builder.getResult());
		
		verify(mockRootQuestion2);
		verify(this.mockFactory);
	}	
	

}
