package org.argouml.module.kuabaModule;



import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jmi.model.MofClass;
import javax.jmi.model.Reference;
import javax.jmi.reflect.RefObject;
import javax.swing.Action;






import org.argouml.model.Model;
import org.argouml.model.ModelEventPump;
import org.argouml.ui.ArgoJMenu;
import org.argouml.uml.diagram.ui.ActionFindDesignRationale;

import br.ucam.kuabaSubsystem.observers.KuabaEventPump;
import br.ucam.kuabaSubsystem.observers.uml.ModelElementObserver;
import br.ucam.kuabaSubsystem.util.MofHelper;
import java.util.*;




 
/**
 *
 * @author Thiago
 */
public class ArgoUMLEventPumpAdapter implements KuabaEventPump, PropertyChangeListener {
	
	private ModelEventPump eventPump;
	private List<RefObject> visitedRefObjects = new ArrayList<RefObject>();
	private Object argoModel;
	private Map<Object, List<PropertyChangeListener>> observersMap = 
	    new HashMap<Object, List<PropertyChangeListener>>();
	
        private boolean stopped = false;
        
	/**
	 * @param argoModel
	 */
	public ArgoUMLEventPumpAdapter(Object argoModel) {
		super();
		this.argoModel = argoModel;
		this.eventPump = Model.getPump();
		loadObservers();
	}
	
	public Object getArgoModel() {
        return argoModel;
    }

    public void setArgoModel(Object argoModel) {
        this.argoModel = argoModel;
    }

    public void loadObservers(){	    
	    load((RefObject)argoModel);
	}
	
	private void load(RefObject source){
	    visitedRefObjects.add(source);	                    
	        addObserver(source);
	        MofClass ownerMofClass = (MofClass)source.refMetaObject();        
	        List<Reference> references = MofHelper.referencesOfClass(
	                ownerMofClass, true);	        
	        for (Reference reference : references) {	                        
	            if((source.refGetValue(reference.getName()) 
	                    instanceof List) || ((source.refGetValue(reference.getName()) 
	                            instanceof Collection))){                
	                Collection<RefObject> refObjects = 
	                    (Collection<RefObject>)source.refGetValue(
	                            reference.getName());
	                for (RefObject refValue : refObjects) {	                    
	                    if(!visitedRefObjects.contains(refValue)){                        
	                        load(refValue);
	                        addObserver(refValue);	                        
	                    }	                             
	                }               
	            }                
	            if(source.refGetValue(reference.getName())
	                    instanceof RefObject){
	                RefObject refValue = (RefObject)source.refGetValue(
	                        reference.getName()); 
//	                Action action = new ActionFindDesignRationale(refValue);
//	                ArgoJMenu menu = null;
	                if(!visitedRefObjects.contains(refValue)){                    
	                    load(refValue);
                            addObserver(refValue);  
	                }
	                //else
	                  //  designRationaleMenu.add(action);                
	            }
	        }	        
	    
	}
	
	private void addObserver(RefObject object){
	    MofClass ownerMofClass = (MofClass)object.refMetaObject();
	    String mofClassName = ownerMofClass.getName();
	    PropertyChangeListener listener = null;
	    String observerName = ModelElementObserver.observersPack + "." 
	    + mofClassName + "Observer";
	    try {
            listener = (PropertyChangeListener) Class.forName(observerName
                        ).newInstance();
            } catch (InstantiationException e) {               
               e.printStackTrace();
            } catch (IllegalAccessException e) {               
               e.printStackTrace();
            } catch (ClassNotFoundException e) {              
               System.err.println("Observer "+ observerName +  
                       " does not exists");
            }	    
	    if(listener != null)
	        addModelElementObserver(listener, object);
	}

    public void startPumpingEvents() {
//        Set keySet = observersMap.keySet();
//        
//        for (Object element : keySet) {
//            List<PropertyChangeListener> list = observersMap.get(element);
//            for (PropertyChangeListener pcl : list) {
//                this.eventPump.addModelEventListener(pcl, element);
//            }
////            this.eventPump.addModelEventListener(this, element);
//        }
        stopped = false;
    }

    public void stopPumpingEvents() {
//        Set keySet = observersMap.keySet();
//        
//        for (Object element : keySet) {
//            List<PropertyChangeListener> list = observersMap.get(element);
//            for (PropertyChangeListener pcl : list) {
//                this.eventPump.removeModelEventListener(pcl, element);
//            }
////            this.eventPump.removeModelEventListener(this, element);
//        }
        stopped = true;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if(stopped) return;
        Object element = evt.getSource();
        
        List<PropertyChangeListener> list = observersMap.get(element);
        for (PropertyChangeListener pcl : list) {
            pcl.propertyChange(evt);
        }
    }
	
	
	class ClassNameAddedListenerAdapter implements PropertyChangeListener{

		public void propertyChange(PropertyChangeEvent evt) {
			/*System.out.println("Evento recebido!");
			System.out.println("ID: " + evt.getPropagationId());
			System.out.println("Nome propriedade: " + evt.getPropertyName());
			System.out.println("Novo Valor: " + evt.getNewValue());
			System.out.println("Antigo Valor: " + evt.getOldValue());
			System.out.println("--------------------------------------");
			System.out.println();
			
			try {
				eventPump.addModelEventListener(new ClassNameAddedListenerAdapter(), evt.getNewValue(),
				"name");
			} catch (ClassCastException e) {

				e.printStackTrace();
			}*/
			
		}
		
	}

    
    public void flush() {
        this.eventPump.flushModelEvents();
        
    }
    
    public void addModelElementObserver(
            PropertyChangeListener modelElementListener, Object element) {
       // modelElementListener = new ObserverAdapter(modelElementListener);
        if (this.observersMap.containsKey(element)) {
            Collection<PropertyChangeListener> listeners = this.observersMap.get(element);
            if(listeners.contains(modelElementListener)) return;
            this.observersMap.get(element).add(modelElementListener);
        }else{
            List<PropertyChangeListener> observers = new ArrayList<PropertyChangeListener>();
            observers.add(modelElementListener);
            this.observersMap.put(element, observers);
      
        }
        
//        this.eventPump.addModelEventListener(modelElementListener, element);
        this.eventPump.addModelEventListener(this, element);
    }

    public PropertyChangeListener getObserver(Object element){
        List<PropertyChangeListener> listeners = this.observersMap.get(element);
        if(listeners == null)
            return null;
        if(!listeners.isEmpty())            
                return listeners.get(0);
        return null;
    }
    
    public void removeObservers(Object element) {
//        List<PropertyChangeListener> observers = this.observersMap.get(element);
//        for (PropertyChangeListener observer : observers) {
//            this.eventPump.removeModelEventListener(observer, element);
            this.eventPump.removeModelEventListener(this, element);
//        }
        this.observersMap.remove(element);
    }   
    
	
}
