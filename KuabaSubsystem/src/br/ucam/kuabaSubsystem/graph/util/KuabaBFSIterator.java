package br.ucam.kuabaSubsystem.graph.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Stack;

import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.ReasoningElement;

public class KuabaBFSIterator implements Iterator {
	private Queue<Iterator> elementsQueue = new LinkedList<Iterator>();
	protected Iterator<ReasoningElement> target;
	
	public KuabaBFSIterator(Iterator<ReasoningElement> ideaIterator) {
				
		this.target = ideaIterator;
		this.elementsQueue.offer(this.target);		
	}
	@Override
	public boolean hasNext() {
		if(this.elementsQueue.isEmpty()){
			return false;
		}
		else{
			Iterator next = this.elementsQueue.peek();
			if(!next.hasNext()){
				this.elementsQueue.poll();
				return hasNext();
			}
			else
				return true;
		}
	}
	private boolean isComposite(Object nextElement){
		return true;
	}
	private Iterator getElementIterator(Object nextElement){
		if (nextElement instanceof Question) {
			Question question = (Question) nextElement;
			return question.listIsAddressedBy();
		}
		if (nextElement instanceof Idea) {
			Idea idea = (Idea) nextElement;
			return idea.listSuggests();
			
		}
		return null;
	}
	@Override
	public Object next() {
		if(hasNext()){
			Iterator peek = this.elementsQueue.peek();
			Object element = peek.next();
			if(this.isComposite(element)){
				this.elementsQueue.offer(this.getElementIterator(element));
			}			
			return element;
		}
		else
			throw new NoSuchElementException();
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

}
