package br.ucam.kuabaSubsystem.observers.uml.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.jmi.reflect.RefObject;

import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.observers.EventFilter;
import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;

public class ParameterObserver extends ModelElementObserver {	

	@Override
	public void propertyChange(PropertyChangeEvent evt) {		
		
	}

	@Override
	protected PropertyChangeListener applyFilter(RefObject subject) {		
		return new EventFilter(this);
	}

	@Override
	protected void askReferenceChangeArguments(Idea idea, Question question,
			PropertyChangeEvent evt) {		
	}	
}
