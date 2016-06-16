package br.ucam.kuabaSubsystem.observers.uml.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.jmi.reflect.RefObject;

import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.observers.EventFilter;
import br.ucam.kuabaSubsystem.observers.NameEventFilter;
import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;

public class InterfaceObserver extends ModelElementObserver {
	
	public InterfaceObserver() {
		super();
		this.referencesAvailable = 
			new String[]{"feature"};
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {		
		super.propertyChange(evt);
	}
	
	@Override
	public PropertyChangeListener applyFilter(RefObject subject) {
		return new NameEventFilter(subject, this);
	}

	@Override
	protected void askReferenceChangeArguments(Idea idea, Question question,
			PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}

}
