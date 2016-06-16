package br.ucam.kuabaSubsystem.observers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.jmi.reflect.RefObject;

import org.netbeans.api.mdr.MDRObject;

public class EventFilter implements PropertyChangeListener {
	private PropertyChangeListener observer;
	public static final int REMOVE = 0;
	public static final int ADD = 1;
	public static final int UNKNOW = 2;
	private HashMap<String, HashMap<Integer, PropertyChangeEvent>> lastEventsHash =
		new HashMap<String, HashMap<Integer, PropertyChangeEvent>>();	
	
	public EventFilter(PropertyChangeListener observer) {
		super();
		this.observer = observer;
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		HashMap<Integer, PropertyChangeEvent> evtHash = 
			this.lastEventsHash.get(arg0.getPropertyName());
		if(!(evtHash == null)){
			Collection<PropertyChangeEvent> lastEvts = evtHash.values();
			for (PropertyChangeEvent lastEvt : lastEvts)
				if(areEquals(arg0, lastEvt))
					return;
                                //Else inserido para tratar quando se modifica um nó de uma associacao
                                //nao estava disparando evendo addAssociaation
                                else
                                {
                                    this.observer.propertyChange(arg0);
                                    this.putEventOnLastEventsHash(arg0);
                                    return ;
                                }
		}
		this.observer.propertyChange(arg0);
		this.putEventOnLastEventsHash(arg0);
	}
	
	public void putEventOnLastEventsHash(PropertyChangeEvent evt){
		HashMap<Integer, PropertyChangeEvent> evtHash = this.lastEventsHash.get(evt.getPropertyName()); 
		if(evtHash == null){
			evtHash = new HashMap<Integer, PropertyChangeEvent>();
			this.lastEventsHash.put(evt.getPropertyName(), evtHash);
		}

		if((evt.getNewValue() == null) && (evt.getOldValue() != null))						
			evtHash.put(REMOVE, evt);				
		
		else if((evt.getNewValue() != null) && (evt.getOldValue() == null))
				evtHash.put(ADD, evt);
		else
			evtHash.put(UNKNOW, evt);
			
	}
	
	public static boolean areEquals(PropertyChangeEvent evt1,
			PropertyChangeEvent evt2){
		Object newValue = evt1.getNewValue();
		Object oldValue = evt1.getOldValue();		
		String propName = evt1.getPropertyName();
		Object source = evt1.getSource();
		
		if(evt2 != null)
			if  ((areEquals(newValue, evt2.getNewValue())) &&
				(areEquals(oldValue, evt2.getOldValue())) &&
				(areEquals(propName, evt2.getPropertyName())) &&
				(areEquals(source, evt2.getSource())))
					return true;	

		return false;
	}
	
	public static boolean areEquals(Object obj1, Object obj2){
		if("".equals(obj1))
			obj1 = null;
		if("".equals(obj2))
			obj2 = null;
		if(obj1 == null){ 
			if(obj1 == obj2)
				return true;
			else
				return false;
		}
		else
			if (obj1.equals(obj2)) 
				return true;
			else
				return false;
		
	}
	
	public PropertyChangeListener getObserver() {
		return observer;
	}

	@Override
	public String toString() {

		return this.observer.toString();
	}
	

}
