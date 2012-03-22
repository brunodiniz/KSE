package br.ucam.kuabaSubsystem.util.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.ReasoningElement;
import br.ucam.kuabaSubsystem.graph.util.KuabaBFSIterator;

public class KuabaBFSIteratorTest extends AbstractKuabaTestCase {
	
	private Idea classDesignIdea;
	private Idea privateVisibilityIdea;
	private Question visibilityQuestion;
	private Question isAbstractQuestion;
	private Idea trueIdea;
	private Iterator<ReasoningElement> ideaIterator;
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		this.classDesignIdea = this.repo.getIdea("class");
		this.visibilityQuestion = this.factory.createQuestion("visibility");
		this.privateVisibilityIdea = this.factory.createIdea("private");
		this.isAbstractQuestion = this.factory.createQuestion("isAbstract");
		this.trueIdea = this.factory.createIdea("true");
		classDesignIdea.addSuggests(this.isAbstractQuestion);
		this.isAbstractQuestion.addIsAddressedBy(this.trueIdea);
		classDesignIdea.addSuggests(visibilityQuestion);
		visibilityQuestion.addIsAddressedBy(privateVisibilityIdea);		
		
		List<ReasoningElement> ideaList = new ArrayList<ReasoningElement>();
		ideaList.add(this.classDesignIdea);
		this.ideaIterator = ideaList.iterator();	
		
	}
	
	public void testBFSIterator(){
		KuabaBFSIterator kuabaTreeIterator = new KuabaBFSIterator(this.ideaIterator);
		assertTrue(kuabaTreeIterator.hasNext());
		assertEquals(this.classDesignIdea, kuabaTreeIterator.next());
		assertTrue(kuabaTreeIterator.hasNext());
		assertEquals(this.isAbstractQuestion, kuabaTreeIterator.next());
		assertTrue(kuabaTreeIterator.hasNext());
		assertEquals(this.visibilityQuestion, kuabaTreeIterator.next());
		assertTrue(kuabaTreeIterator.hasNext());
		assertEquals(this.trueIdea, kuabaTreeIterator.next());
		assertTrue(kuabaTreeIterator.hasNext());
		assertEquals(this.privateVisibilityIdea, kuabaTreeIterator.next());
		assertFalse(kuabaTreeIterator.hasNext());
		
	}

}
