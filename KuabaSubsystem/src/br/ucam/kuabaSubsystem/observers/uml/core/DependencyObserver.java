package br.ucam.kuabaSubsystem.observers.uml.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.jmi.reflect.RefObject;
import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.observers.EventFilter;
import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;
import br.ucam.kuabaSubsystem.util.KuabaHelper;

public class DependencyObserver extends ModelElementObserver {
	
	public DependencyObserver() {
		super();
		this.referencesAvailable = 
			new String[]{"client"};
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
		String childName = 
			(String)((RefObject)evt.getNewValue()).refGetValue("name");		
		
		if(evt.getPropertyName().equals("client")){
			List<RefObject> supplierList = new ArrayList<RefObject>(
					(Collection<RefObject>)source.refGetValue("supplier"));
			RefObject parent = supplierList.get(supplierList.size()-1);
			String xmiId = KuabaSubsystem.resolver.resolveXmiId(parent);
			Idea supplierDomainIdea = KuabaHelper.getDomainIdea(this.modelRepository(),
					xmiId, (String)parent.refGetValue("name"));
			Idea supplierDesignIdea = null;
			if(supplierDomainIdea != null)
				supplierDesignIdea = KuabaHelper.getAcceptedDesignIdea(
						supplierDomainIdea, (String)parent.refMetaObject(
								).refGetValue("name"));			
			String text = "Why does make " + childName + " " +
			"dependent of " + this.getNamePropertyValue(parent);
			Question supplierQuestion = null;						
			Idea addressedIdea = KuabaHelper.getIdeaByText(
					(Collection) question.getIsSuggestedBy(),
					(String)source.refMetaObject().refGetValue("name"));
			supplierQuestion = 
				(Question)KuabaHelper.getReasoningElementInTree(addressedIdea, 
						"supplier?");			
			HashMap<Idea, Question> ideaQuestionMap = new HashMap<Idea, Question>();
			ideaQuestionMap.put(idea, question);
			ideaQuestionMap.put(supplierDesignIdea, supplierQuestion);
			KuabaSubsystem.facade.renderInFavorArgumentView(ideaQuestionMap, text);
	
		}
		
	}
	

}
