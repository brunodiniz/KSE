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
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.observers.EventFilter;
import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;
import br.ucam.kuabaSubsystem.util.KuabaHelper;
import br.ucam.kuabaSubsystem.util.MofHelper;

public class AssociationEndObserver extends ModelElementObserver{
	
	public AssociationEndObserver() {
		super();
		this.referencesAvailable = 
			new String[]{"participant"};
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
		RefObject participant = (RefObject)evt.getNewValue();
		String text = ""; 
		if(evt.getPropertyName().equals("participant")){
			text = "Why does make " + participant.refGetValue("name") + 
			" participant of this association?";
			KuabaSubsystem.facade.renderInFavorArgumentView(question, idea, text);
		}		
	}	
	
}
