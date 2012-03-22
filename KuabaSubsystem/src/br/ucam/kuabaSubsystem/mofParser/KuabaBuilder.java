package br.ucam.kuabaSubsystem.mofParser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Stack;

import br.ucam.kuabaSubsystem.kuabaExceptions.SequenceNameAlreadyExistsException;
import br.ucam.kuabaSubsystem.kuabaModel.FormalModel;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaElement;
import br.ucam.kuabaSubsystem.kuabaModel.KuabaModelFactory;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.util.Sequence;
import br.ucam.kuabaSubsystem.util.SequenceGenerator;
import br.ucam.kuabaSubsystem.util.SequenceGeneratorImpl;

import com.hp.hpl.jena.graph.BulkUpdateHandler;


public class KuabaBuilder implements ParserBuilder {
	
	private Stack<KuabaElement> buildStack;
	private KuabaElement lastCreatedElement;
	private FormalModel formalModel;
	private Question result;
	private Sequence seq;
	private KuabaModelFactory factory;
	
	public KuabaBuilder(KuabaModelFactory factory, FormalModel formalModel, Sequence seq) {
		this.factory = factory;
		this.buildStack = new Stack<KuabaElement>();
		this.seq = seq;
		this.formalModel = formalModel;
	}
	
	@Override
	public void clearStack() {
		this.buildStack = new Stack<KuabaElement>();
	}
	
	@Override
	public void createRootQuestion(String questionText) {
		Question root = null;		
		root = this.factory.createQuestion(questionText);		
		root.setHasText(questionText);
		root.setHasType(Question.XORTYPE);		
		root.setIsDefinedBy(this.formalModel);
		this.buildStack.push(root);
		this.result = root;
	}

	@Override
	public void createIdea(String ideaText) {
		Question peek = null;
		if (buildStack.peek() instanceof Question) {
			peek = (Question) buildStack.peek();		
		}
		else
			throw new IllegalStateException("createIdea(String) method requires a Question " +
					"on the peek of the internal stack and the peek is a " + 
					this.buildStack.peek().getClass().getName() + "class");
		
		Idea idea = null;

		idea = this.factory.createIdea("_"+seq.nextVal());
		
		idea.setHasText(ideaText);
		idea.addAddress(peek);
		idea.setIsDefinedBy(this.formalModel);
		this.lastCreatedElement = idea;
	}

	@Override
	public void createQuestion(String questionText, String questionType) {
		Idea peek = null;
		if (buildStack.peek() instanceof Idea) {
			peek = (Idea) buildStack.peek();			
		}
		else
			throw new IllegalStateException("createQuestion(String) method requires an Idea " +
					"on the peek of the internal stack and the peek is a " + 
					this.buildStack.peek().getClass().getName() + "class");		
		
		Question question = null;		
		question = this.factory.createQuestion("_"+seq.nextVal());		
		System.out.println("Questão: " + questionText);
		question.setHasText(questionText + "?");		
		question.setHasType(questionType);		
		question.addIsSuggestedBy(peek);
		question.setIsDefinedBy(this.formalModel);
		if(this.result == null)
			this.result = question;
		
		this.lastCreatedElement = question;
	}
	
	@Override
	public KuabaElement pop() {		
		return this.buildStack.pop();
	}

	@Override
	public void push() {
		this.buildStack.push(this.lastCreatedElement);
	}

	@Override
	public KuabaElement top() {
		return this.buildStack.peek();
	}
	
	public Stack<KuabaElement> getBuildStack() {
		return buildStack;
	}	
	
	public void setLastCreatedElement(KuabaElement lastCreatedElement) {
		this.lastCreatedElement = lastCreatedElement;
	}

	public KuabaElement getLastCreatedElement() {
		return lastCreatedElement;
	}

	@Override
	public Question getResult() {
		return this.result;
	}
	

}
