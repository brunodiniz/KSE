package br.ucam.kuabaSubsystem.observers.uml.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jmi.model.Attribute;
import javax.jmi.model.BehavioralFeature;
import javax.jmi.model.ModelElement;
import javax.jmi.model.MofClass;
import javax.jmi.model.Reference;
import javax.jmi.model.StructuralFeature;
import javax.jmi.reflect.RefObject;


import org.netbeans.api.mdr.MDRObject;

import br.ucam.kuabaSubsystem.core.KuabaSubsystem;
import br.ucam.kuabaSubsystem.kuabaModel.Idea;
import br.ucam.kuabaSubsystem.kuabaModel.Question;
import br.ucam.kuabaSubsystem.observers.AbstractObserver;
import br.ucam.kuabaSubsystem.observers.EventFilter;
import br.ucam.kuabaSubsystem.observers.NameEventFilter;
import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;
import br.ucam.kuabaSubsystem.util.MofHelper;

public class ModelObserver extends ModelElementObserver{

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getNewValue() != null){
			elementAdded((RefObject)evt.getNewValue());
		}		
	}	
	
	private void elementAdded(RefObject element){
		MofClass newValue = (MofClass)element.refMetaObject();
		if(KuabaSubsystem.eventPump.getObserver(element) != null)
			return;
		if(newValue.refGetValue("name").equals("Association")){			
			PropertyChangeListener ascListener = new EventFilter(new AssociationObserver());
			KuabaSubsystem.eventPump.addModelElementObserver(ascListener, element);
			PropertyChangeEvent nameEvt = new PropertyChangeEvent(
					element, "name", "", element.refGetValue(
							"name"));
			ascListener.propertyChange(nameEvt);
		}
		if(newValue.refGetValue("name").equals("Class")){
			PropertyChangeListener classListener = new NameEventFilter(element, new ClassObserver());
			KuabaSubsystem.eventPump.addModelElementObserver(classListener, element);			
		}
		if(newValue.refGetValue("name").equals("Comment")){
			PropertyChangeListener commentListener = new EventFilter(new CommentObserver());
			KuabaSubsystem.eventPump.addModelElementObserver(commentListener, element);
		}
		if(newValue.refGetValue("name").equals("Generalization")){
			PropertyChangeListener generaListener = new EventFilter(new GeneralizationObserver());
			KuabaSubsystem.eventPump.addModelElementObserver(generaListener, element);

			PropertyChangeEvent nameEvt = new PropertyChangeEvent(
					element, "name", "", element.refGetValue(
							"name"));
			generaListener.propertyChange(nameEvt);
			
			PropertyChangeEvent childEvt = new PropertyChangeEvent(
					element, "child", "", element.refGetValue("child"));
			generaListener.propertyChange(childEvt);
						
		}
		if(newValue.refGetValue("name").equals("Abstraction")){
			PropertyChangeListener generaListener = new EventFilter(new DependencyObserver());
			KuabaSubsystem.eventPump.addModelElementObserver(generaListener, element);

			PropertyChangeEvent nameEvt = new PropertyChangeEvent(
					element, "name", "", element.refGetValue(
							"name"));
			generaListener.propertyChange(nameEvt);
			
			List<RefObject> clientList = new ArrayList<RefObject>(
					(Collection<RefObject>)element.refGetValue("client"));
			int indexOfLast = clientList.size() - 1;
			PropertyChangeEvent childEvt = new PropertyChangeEvent(
					element, "client", "", clientList.get(indexOfLast));
			//generaListener.propertyChange(childEvt);
		}
		if(newValue.refGetValue("name").equals("Interface")){
			PropertyChangeListener classListener = new NameEventFilter(element, new InterfaceObserver());
			KuabaSubsystem.eventPump.addModelElementObserver(classListener, element);			
		}
		if(newValue.refGetValue("name").equals("Usage")){
			PropertyChangeListener generaListener = new EventFilter(new DependencyObserver());
			KuabaSubsystem.eventPump.addModelElementObserver(generaListener, element);

			PropertyChangeEvent nameEvt = new PropertyChangeEvent(
					element, "name", "", element.refGetValue(
							"name"));
			generaListener.propertyChange(nameEvt);
			
			List<RefObject> clientList = new ArrayList<RefObject>(
					(Collection<RefObject>)element.refGetValue("client"));
			int indexOfLast = clientList.size() - 1;
			PropertyChangeEvent childEvt = new PropertyChangeEvent(
					element, "client", "", clientList.get(indexOfLast));
			generaListener.propertyChange(childEvt);

		}
		
	}
	/*public void propertyChange(PropertyChangeEvent evt) {		
		
		if(evt.getOldValue() == null){
			MofClass newValue = (MofClass)((MDRObject)evt.getNewValue()).refMetaObject();
			String modelElementName = (String)((RefObject)evt.getNewValue()).refGetValue("name")+ "";
			if(modelElementName.equals("") || modelElementName.equals("null"))
				modelElementName = ((RefObject)evt.getNewValue()).refMofId();
			
			
			if(!attributeObserversRegistry.containsKey(((RefObject)evt.getNewValue()))){
					
				PropertyChangeListener attributeListener = new EventFilter(
							new MofAttributeObserver());			
				List<Attribute> attributes = MofHelper.attributesOfClass(newValue, true);						
				for (Attribute attribute : attributes) {
					KuabaSubsystem.eventPump.addModelElementObserver(
							attributeListener, evt.getNewValue(), attribute.getName());
				}
				attributeObserversRegistry.put((RefObject)evt.getNewValue(), attributeListener);
			}
			
			if(!referenceObserversRegistry.containsKey((RefObject)evt.getNewValue())){
				PropertyChangeListener referenceListener = new EventFilter(new MofReferenceObserver());
				List<Reference> references = MofHelper.referencesOfClass(newValue, true);
				
				for (Reference reference : references) {

					KuabaSubsystem.eventPump.addModelElementObserver(
							referenceListener, evt.getNewValue(), reference.getName());
				}
				referenceObserversRegistry.put((RefObject)evt.getNewValue(), referenceListener);
			}
			System.out.println();
			//KuabaSubsystem.facade.domainIdeaAdded(modelElementName);
			
			KuabaSubsystem.eventPump.addNameChangedObserver(
					new EventFilter(new NameChangedObserver()) ,evt.getNewValue());
			if(newValue.refGetValue("name").equals("Association")){
				PropertyChangeListener ascListener = new EventFilter(new AssociationObserver());
				KuabaSubsystem.eventPump.addModelElementObserver(ascListener, evt.getNewValue());
			}
			
		}else{
			MofClass oldValue = (MofClass)((MDRObject)evt.getOldValue()).refMetaObject();
			KuabaSubsystem.facade.domainIdeaRemoved(oldValue.getName());
		}

	}*/

	@Override
	public PropertyChangeListener applyFilter(RefObject subject) {		
		return new EventFilter(this);
	}

	@Override
	protected void askReferenceChangeArguments(Idea idea, Question question,
			PropertyChangeEvent evt) {
		
		
	}

}
