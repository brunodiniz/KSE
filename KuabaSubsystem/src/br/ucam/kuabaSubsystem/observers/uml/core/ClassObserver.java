package br.ucam.kuabaSubsystem.observers.uml.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.jmi.reflect.RefObject;

import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.observers.NameEventFilter;
import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;

public class ClassObserver extends ModelElementObserver {

	public ClassObserver() {
		super();
		this.referencesAvailable = 
			new String[]{"feature"};
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {				
		super.propertyChange(evt);
		if(evt.getPropertyName().equals("association")){
			this.associationAdded((RefObject)evt.getNewValue(), 
					(RefObject)evt.getSource());
//			try {
//				attr.refGetValue("association");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//			List<Reference> refs = MofHelper.referencesOfClass((MofClass)attr.refMetaObject(), true);
//			for (Reference reference : refs) {
//				System.out.println(reference.getName());
//			}
//			List<Attribute> attrs = MofHelper.attributesOfClass((MofClass)attr.refMetaObject(), true);
//			for (Attribute attribute : attrs) {
//				System.out.println(attribute.getName());
//			}
			
		}
			
		
		

	}
	
	public void associationAdded(RefObject ascEnd, RefObject umlClass ){
	/*	String ascEndMofId = ascEnd.refMofId();
		List<Idea> domainIdeas = 
			this.modelRepository.findDomainIdeasWhereIdLike(ascEndMofId);
		Idea ascEndDomainIdea = KuabaHelper.getIdeaByText(domainIdeas,
				(String)ascEnd.refGetValue("name"));
		Idea ascEndDesignIdea = 
			this.modelRepository.findFirstIdeaByText(
				(Question)ascEndDomainIdea.listSuggests().next(),
				"AssociationEnd");
		
		String classMofId = umlClass.refMofId();
		List<Idea> classDomainIdeas = 
			this.modelRepository.findDomainIdeasWhereIdLike(classMofId);
		Idea classDomainIdea = KuabaHelper.getIdeaByText(classDomainIdeas,
				(String)umlClass.refGetValue("name"));
		Question howModel = (Question)classDomainIdea.listSuggests().next();
		Question participant = (Question)KuabaHelper.getReasoningElementInTree(
				howModel, "participant?");
		
		participant.addIsAddressedBy(ascEndDesignIdea);
		this.facade.makeDecision(participant, classDomainIdea, true);*/
		
	}

	@Override
	protected PropertyChangeListener applyFilter(RefObject subject) {		
		return new NameEventFilter(subject, this);
	}

	@Override
	protected void askReferenceChangeArguments(Idea idea, Question question,
			PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}
}
