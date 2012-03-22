package br.ucam.kuabaSubsystem.observers.uml.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import javax.jmi.reflect.RefObject;

import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.observers.AbstractObserver;
import br.ucam.kuabaSubsystem.observers.EventFilter;
import br.ucam.kuabaSubsystem.observers.NameEventFilter;
import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;

public class AssociationObserver extends ModelElementObserver{
	public static final String[] referencesAvailable = 
		new String[]{"connection"};
	
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
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean makeKuaba(String referenceName) {
		if((Arrays.asList(referencesAvailable).contains(referenceName)
				||(Arrays.asList(referencesAvailable).isEmpty())))
			return true;
		return false;
	}
	

}
