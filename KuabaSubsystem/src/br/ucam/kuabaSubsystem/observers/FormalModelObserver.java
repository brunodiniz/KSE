package br.ucam.kuabaSubsystem.observers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import br.ucam.kuabaSubsystem.core.KuabaSubsystem;

public class FormalModelObserver implements PropertyChangeListener {
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		KuabaSubsystem.facade.formalModelAdded((String)evt.getNewValue());

	}

}
