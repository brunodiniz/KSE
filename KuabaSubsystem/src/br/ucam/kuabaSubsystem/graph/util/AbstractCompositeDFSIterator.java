package br.ucam.kuabaSubsystem.graph.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

import javax.jmi.model.ModelElement;

import br.ucam.kuabaSubsystem.kuabaModel.ReasoningElement;

@SuppressWarnings("unchecked")
public abstract class AbstractCompositeDFSIterator implements Iterator {

	private Stack<Iterator> iterationStack;
	protected Iterator target;
	
	public AbstractCompositeDFSIterator(Iterator target){		
		this.iterationStack = new Stack<Iterator>();		
		this.target = target;
		this.iterationStack.push(this.target);		
	}
	public AbstractCompositeDFSIterator(ReasoningElement rootReasoningElement) {
		this.iterationStack = new Stack<Iterator>();
		List<ReasoningElement> reasoningElementList = new ArrayList<ReasoningElement>();
		reasoningElementList.add(rootReasoningElement);
		this.target = reasoningElementList.iterator();
		this.iterationStack.push(this.target);
	}
	@Override
	public Object next() {
		if(hasNext()){
			Iterator peek = this.iterationStack.peek();
			Object element = peek.next();			
			if(this.isComposite(element)){
				this.iterationStack.push(this.getElementIterator(element));
			}			
			return element;
		}
		else
			throw new NoSuchElementException();
	}
	
	protected abstract boolean isComposite(Object nextElement);
	protected abstract Iterator getElementIterator(Object nextElement);
	
	@Override
	public boolean hasNext() {
		if(this.iterationStack.isEmpty()){
			return false;
		}
		else{
			Iterator next = this.iterationStack.peek();
			if(!next.hasNext()){
				this.iterationStack.pop();
				return hasNext();
			}
			else
				return true;
		}
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
	}	

}
