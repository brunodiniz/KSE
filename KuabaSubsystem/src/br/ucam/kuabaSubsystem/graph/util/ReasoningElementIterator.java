package br.ucam.kuabaSubsystem.graph.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.kuabaModel.ReasoningElement;

public class ReasoningElementIterator extends AbstractCompositeDFSIterator{

	public ReasoningElementIterator(Iterator<ReasoningElement> iterator) {
		super(iterator);
		// TODO Auto-generated constructor stub
	}
	public ReasoningElementIterator(ReasoningElement rootReasoningElement) {		
		super(rootReasoningElement);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected Iterator getElementIterator(Object nextElement) {
//                System.out.println(((ReasoningElement)nextElement).getId());
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
	protected boolean isComposite(Object nextElement) {
		
		return true;
	}
	
}
