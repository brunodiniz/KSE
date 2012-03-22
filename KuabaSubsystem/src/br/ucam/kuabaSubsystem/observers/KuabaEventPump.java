package br.ucam.kuabaSubsystem.observers;

import java.beans.PropertyChangeListener;

import javax.jmi.model.ModelElement;

import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;

public interface KuabaEventPump {
	
	//public void addFormalModelObserver(PropertyChangeListener listener);
	
	//public void addModelObserver(PropertyChangeListener modelListener);
	public void removeObservers(Object element);
	
	//public void addModelElementObserver(
		//	PropertyChangeListener modelElementListener, Object element, String field);
	public void addModelElementObserver(
			PropertyChangeListener modelElementListener, Object element);
	
	//public void addNameChangedObserver(
		//	PropertyChangeListener listener, Object element);
	public PropertyChangeListener getObserver(Object element);
	public void flush();
}
