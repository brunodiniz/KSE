package br.ucam.kuabaSubsystem.observers.uml.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.jmi.model.MofClass;
import javax.jmi.model.Reference;
import javax.jmi.reflect.RefObject;

import br.ucam.kuabaSubsystem.controller.ArgumentController;
import br.ucam.kuabaSubsystem.controller.InFavorArgumentController;
import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Decision;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.observers.NameEventFilter;
import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;
import br.ucam.kuabaSubsystem.repositories.KuabaRepository;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
import br.ucam.kuabaSubsystem.util.MofHelper;

public class AttributeObserver extends ModelElementObserver {

	public AttributeObserver() {
		super();
		this.referencesAvailable = 
			new String[]{"owner"};
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		
		/*System.out.println("O atributo "+attr.refMetaObject().refGetValue("name")+ " foi modificado");
		System.out.println("Nome da propriedade: "+evt.getPropertyName());		
		System.out.println("Novo valor: "+evt.getNewValue());		
		System.out.println("--------------------------AttrObs--------------------");
		*/			
	
	}
	/*public void nameChanged(RefObject source, String newName, String elementMofId) {
		
		System.out.println("new Name: "+ newName);
		KuabaSubsystem.facade.domainIdeaRejected(elementMofId);
		
		//getting the owner of the attribute
		RefObject owner = (RefObject)source.refGetValue("owner");
		//getting the owner name of the attribute
		String ownerName = (String)owner.refGetValue("name");
		
		if(ownerName.equals(""))
			ownerName = owner.refMofId();
		
		//getting the model repository
		KuabaRepository modelRepository = KuabaSubsystem.facade.getModelRepository();
		
		//getting all ideas that have text = newName and is modeled like "Attribute" 
		List<Idea> attrIdeas = modelRepository.findDesignedDomainIdeas(newName, "Attribute");
		System.out.println("Idéias " + newName + "modeladas como atributo");
		for (Idea idea2 : attrIdeas) {
			System.out.println(idea2.getHasText());
		}
		System.out.println();
		Idea domainIdea = null;
		for (Idea idea : attrIdeas) {
			Idea ownerIdea = this.getOwnerIdea(modelRepository, idea);
			if (ownerIdea != null){
				if(ownerName.equals(ownerIdea.getHasText())){
					domainIdea = idea;
					break;
				}
			}
		}
		
		if(domainIdea != null){
			KuabaSubsystem.facade.makeDecision((Question)domainIdea.listAddress().next(), domainIdea, true);
			KuabaSubsystem.facade.saveSession();
		}
		else{
			KuabaSubsystem.facade.domainIdeaAdded(newName, "Attribute", elementMofId);
		}
		
		
		
		
	}
	private Question getOwnerQuestion(KuabaRepository kr, Idea domainIdea){
		String howModelText =  Question.DOMAIN_QUESTION_TEXT_PREFIX + domainIdea.getHasText() + "?";
		System.out.println(howModelText);
		Question howModel = kr.findFirstQuestionByText(domainIdea, howModelText);
		Idea attribute = kr.findFirstIdeaByText(howModel, "Attribute");		
		Question owner = kr.findFirstQuestionByText(attribute, "owner?");
		return owner;
	}
	
	private Idea getOwnerIdea(KuabaRepository kr, Idea domainIdea){
		Question owner = this.getOwnerQuestion(kr, domainIdea);
		List<Idea> ownerIdeas = kr.findAcceptedIdeas(owner);
		if(ownerIdeas.isEmpty())
			return null;
		else
			return ownerIdeas.get(0);
		
	}*/
	
	@Override
	protected PropertyChangeListener applyFilter(RefObject subject) {

		return new NameEventFilter(subject, this);
	}
	@Override
	public void askReferenceChangeArguments(Idea idea,
			Question question, PropertyChangeEvent evt) {
		String targetName = (String)((RefObject)evt.getNewValue()).refGetValue(
				"name");
		String sourceName = (String)((RefObject)evt.getSource()).refGetValue(
		"name");
		String text = "Why does make " + targetName + " " 
		+ evt.getPropertyName()	+ " of " + sourceName + "?";
		
		KuabaSubsystem.facade.renderInFavorArgumentView(question, idea, text);
	}
	

}
