package br.ucam.kuabaSubsystem.observers.uml.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;

import javax.jmi.reflect.RefObject;

import br.ucam.kuabaSubsystem.controller.ArgumentController;
import br.ucam.kuabaSubsystem.controller.InFavorArgumentController;
import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.observers.EventFilter;
import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
import java.util.Collection;


public class GeneralizationObserver extends ModelElementObserver{
	public GeneralizationObserver() {
		super();
		this.referencesAvailable = 
			new String[]{"child"};
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {		
		super.propertyChange(evt);		
	}
	
	@Override
	protected PropertyChangeListener applyFilter(RefObject subject) {
		return new EventFilter(this);
	}

	@Override
	protected void askReferenceChangeArguments(Idea idea, Question question,
			PropertyChangeEvent evt) {
		RefObject source = (RefObject)evt.getSource();
		RefObject parent = (RefObject)source.refGetValue("parent");
		String xmiId = KuabaSubsystem.resolver.resolveXmiId(parent);
		Idea supplierDomainIdea = KuabaHelper.getDomainIdea(this.modelRepository(),
				xmiId, (String)parent.refGetValue("name"));
		Idea supplierDesignIdea = null;
		if(supplierDomainIdea != null)
			supplierDesignIdea = KuabaHelper.getAcceptedDesignIdea(
					supplierDomainIdea, (String)parent.refMetaObject(
							).refGetValue("name"));			
		String text = "";
		if(evt.getPropertyName().equals("child")){			
			String targetName =
				(String)((RefObject)evt.getNewValue()).refGetValue("name");
			text = "Why does make " + targetName + 
			" " + evt.getPropertyName() + " of " + parent.refGetValue("name");
		}
		Question supplierQuestion = null;						
		Idea addressedIdea = KuabaHelper.getIdeaByText(
				(Collection)question.getIsSuggestedBy(),
				(String)source.refMetaObject().refGetValue("name"));
		supplierQuestion = 
			(Question)KuabaHelper.getReasoningElementInTree(addressedIdea, 
					"parent?");			
		HashMap<Idea, Question> ideaQuestionMap = new HashMap<Idea, Question>();
		ideaQuestionMap.put(idea, question);
		ideaQuestionMap.put(supplierDesignIdea, supplierQuestion);
		KuabaSubsystem.facade.renderInFavorArgumentView(ideaQuestionMap, text);
		
	}

}
