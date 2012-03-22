package br.ucam.kuabaSubsystem.observers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.jmi.reflect.RefObject;

public class NameEventFilter extends EventFilter {
	private boolean nameEventAlreadyFired = false;	 
	private List<PropertyChangeEvent> eventQueue = 
		new ArrayList<PropertyChangeEvent>();
	
	public NameEventFilter(RefObject subject, PropertyChangeListener observer) {
		super(observer);
		if(!((subject.refGetValue("name") == null)||(subject.refGetValue(
				"name").equals("")) || (subject.refGetValue("name").equals(
						"newAttr"))|| (subject.refGetValue("name").equals(
						"newOperation")))){
			this.nameEventAlreadyFired = true;
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if((this.nameEventAlreadyFired)){			
			super.propertyChange(arg0);
		}			
		else{		
			if((arg0.getPropertyName().equals("name"))&& 
					(!((arg0.getNewValue() == null)||
							(arg0.getNewValue().equals("")) ||
							(arg0.getNewValue().equals("newAttr"))||
							(arg0.getNewValue().equals("newOperation"))))){
				this.nameEventAlreadyFired = true;
				super.propertyChange(arg0);
				for (PropertyChangeEvent event : this.eventQueue)
					super.propertyChange(event);			
			}
			else
				if(!arg0.getPropertyName().equals("name"))					
					this.eventQueue.add(arg0);
				else
					super.propertyChange(arg0);
					
		}		
	}
	
	

}
