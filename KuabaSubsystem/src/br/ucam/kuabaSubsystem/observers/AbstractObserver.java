package br.ucam.kuabaSubsystem.observers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jmi.reflect.RefObject;

public abstract class AbstractObserver implements PropertyChangeListener {
	protected static Map<RefObject, PropertyChangeListener> attributeObserversRegistry =new HashMap<RefObject, PropertyChangeListener>();
	protected static Map<RefObject, PropertyChangeListener> referenceObserversRegistry =new HashMap<RefObject, PropertyChangeListener>();
	
}
